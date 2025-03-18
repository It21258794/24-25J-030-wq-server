from fastapi import FastAPI
import torch
from torch_geometric.data import Data
from torch_geometric.nn import GCNConv
from tensorflow import keras
import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from datetime import datetime, timedelta
from collections import deque

app = FastAPI()

def preprocess_data(df, targets=['PH', 'Conductivity', 'Turbidity'], sequence_length=5):
    dropped_columns = ['visibility', 'humidity', 'wind_speed', 'clouds', 'feels_like', 'pressure', 'dew_point', 'wind_deg']
    df = df.drop(columns=dropped_columns, errors='ignore')

    if 'rainfall' in df.columns:
        df['rainfall_binary'] = (df['rainfall'] > 0).astype(int)
        df = df.drop(columns=['rainfall'])

    df['datetime'] = pd.to_datetime(df['datetime'])
    df['Year'] = df['datetime'].dt.year
    df['Month'] = df['datetime'].dt.month
    df['Day'] = df['datetime'].dt.day
    df['DayOfWeek'] = df['datetime'].dt.dayofweek
    df['Hour'] = df['datetime'].dt.hour
    df['Minute'] = df['datetime'].dt.minute
    df = df.drop(columns=['datetime'])

    df = df.fillna(df.mean())

    if 'PH' in df.columns:
        df.loc[df['PH'] > 14, 'PH'] = df['PH'] / 10

    def remove_outliers_iqr(df, column, lower_percentile=0.25, upper_percentile=0.75):
        if column in df.columns:
            Q1, Q3 = df[column].quantile([lower_percentile, upper_percentile])
            IQR = Q3 - Q1
            lower_bound, upper_bound = Q1 - 1.5 * IQR, Q3 + 1.5 * IQR
            df = df[(df[column] >= lower_bound) & (df[column] <= upper_bound)]
        return df

    df = remove_outliers_iqr(df, 'Turbidity', lower_percentile=0.05, upper_percentile=0.95)

    n_lags = 3
    for target in targets:
        for lag in range(1, n_lags + 1):
            df[f'{target}_lag{lag}'] = df[target].shift(lag)

    df.dropna(inplace=True)

    feature_columns = [col for col in df.columns if col not in targets]
    feature_scaler = MinMaxScaler()
    target_scaler = MinMaxScaler()

    df_scaled_features = pd.DataFrame(feature_scaler.fit_transform(df[feature_columns]), columns=feature_columns)
    df_scaled_targets = pd.DataFrame(target_scaler.fit_transform(df[targets]), columns=targets)

    df_scaled = pd.concat([df_scaled_features, df_scaled_targets], axis=1)

    def create_sequences(data, target_columns, sequence_length=5):
        X, y = [], []
        for i in range(len(data) - sequence_length):
            X.append(data.iloc[i:i+sequence_length].values)
            y.append(data.iloc[i+sequence_length][target_columns].values)
        return np.array(X), np.array(y)

    X, y = create_sequences(df_scaled, targets, sequence_length)

    train_size = int(len(X) * 0.8)
    X_train, X_test = X[:train_size], X[train_size:]
    y_train, y_test = y[:train_size], y[train_size:]

    return X_test, y_test, target_scaler

lstm_model = keras.models.load_model('/Users/kaveesha/Documents/wq_pred/lag_lstm_water_quality.keras')  # Change to the correct path

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

class GNNModel(torch.nn.Module):
    def __init__(self, in_channels, hidden_channels, out_channels):
        super(GNNModel, self).__init__()
        self.conv1 = GCNConv(in_channels, hidden_channels)
        self.conv2 = GCNConv(hidden_channels, out_channels)

    def forward(self, x, edge_index):
        x = self.conv1(x, edge_index)
        x = torch.relu(x)
        x = self.conv2(x, edge_index)
        return x

gnn_model = GNNModel(in_channels=3, hidden_channels=16, out_channels=3).to(device)
gnn_model.load_state_dict(torch.load("/Users/kaveesha/Documents/wq_pred/final_gnn_water_quality.pt"))  # Change to the correct path
gnn_model.eval()

def get_future_dates(start_date, days_ahead):
    future_dates = [start_date + timedelta(days=i) for i in range(1, days_ahead + 1)]
    return [date.strftime('%Y-%m-%d') for date in future_dates]


import numpy as np
from collections import deque
from fastapi import FastAPI
import torch
import pandas as pd
from datetime import datetime

def convert_numpy_types(obj):
    if isinstance(obj, np.generic):
        return obj.item()
    elif isinstance(obj, list):
        return [convert_numpy_types(item) for item in obj]
    elif isinstance(obj, dict):
        return {key: convert_numpy_types(value) for key, value in obj.items()}
    else:
        return obj

app = FastAPI()

@app.post("/predict")
async def predict(data: dict, prediction_period: str):
    # Load data from input
    df_center1 = pd.DataFrame(data['center1'])
    df_center2 = pd.DataFrame(data['center2'])
    df_center3 = pd.DataFrame(data['center3'])

    X_test1, y_test1, scaler1 = preprocess_data(df_center1)
    X_test2, y_test2, scaler2 = preprocess_data(df_center2)
    X_test3, y_test3, scaler3 = preprocess_data(df_center3)

    current_input1 = X_test1[-1].copy()
    current_input2 = X_test2[-1].copy()
    current_input3 = X_test3[-1].copy()

    predictions = []
    prediction_periods = {'7 days': 7, 'next month': 30, 'next 2 months': 60}
    days_ahead = prediction_periods.get(prediction_period)
    if not days_ahead:
        return {"error": "Invalid prediction period"}

    future_dates = get_future_dates(datetime.now(), days_ahead)

    for date in future_dates:
        daily_preds = {'date': date, 'avg_ph': 0, 'avg_conduct': 0, 'avg_turbidity': 0}
        daily_ph, daily_conduct, daily_turbidity = [], [], []

        for step in range(12):
            pred1 = lstm_model.predict(current_input1.reshape(1, -1, current_input1.shape[1]))
            pred2 = lstm_model.predict(current_input2.reshape(1, -1, current_input2.shape[1]))
            pred3 = lstm_model.predict(current_input3.reshape(1, -1, current_input3.shape[1]))

            pred1 = scaler1.inverse_transform(pred1).flatten()
            pred2 = scaler2.inverse_transform(pred2).flatten()
            pred3 = scaler3.inverse_transform(pred3).flatten()
            print(f"LSTM predictions (actual scale):\nCenter 1: {pred1}\nCenter 2: {pred2}\nCenter 3: {pred3}")

            X_nodes = torch.tensor(np.vstack([pred1, pred2, pred3]), dtype=torch.float).to(device)

            edge_index = torch.tensor([
                [0, 1, 0, 2, 1, 2],
                [1, 0, 2, 0, 2, 1]
            ], dtype=torch.long).to(device)

            with torch.no_grad():
                gnn_output = gnn_model(X_nodes, edge_index)

            gnn_output = gnn_output.cpu().detach().numpy()
            gnn_pred1, gnn_pred2, gnn_pred3 = gnn_output[0], gnn_output[1], gnn_output[2]

            daily_ph.append(gnn_pred1[0])
            daily_conduct.append(gnn_pred1[1])
            daily_turbidity.append(gnn_pred1[2])

            current_input1 = np.roll(current_input1, shift=-1, axis=0)
            current_input2 = np.roll(current_input2, shift=-1, axis=0)
            current_input3 = np.roll(current_input3, shift=-1, axis=0)

            current_input1[-1, :3] = gnn_pred1
            current_input2[-1, :3] = gnn_pred2
            current_input3[-1, :3] = gnn_pred3

        daily_preds['avg_ph'] = float(np.mean(daily_ph))
        daily_preds['avg_conduct'] = float(np.mean(daily_conduct))
        daily_preds['avg_turbidity'] = float(np.mean(daily_turbidity))

        predictions.append(daily_preds)

    return {"predictions": predictions}

