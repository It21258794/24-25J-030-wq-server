import pandas as pd
from tensorflow.keras.models import load_model
from preprocessing import preprocess_dataframe, scale_and_split_data

model_path = 'gnn_lstm_hybrid.pth'
scaler_y_path = '/content/drive/MyDrive/scaler_y.pkl'

center_data_paths = {
    'center_1': '/content/drive/MyDrive/combined_with_weather.csv',
    'center_2': '/content/drive/MyDrive/combined_with_weather_MW2.csv',
    'center_3': '/content/drive/MyDrive/combined_with_weather_MW3.csv',
    'center_4': '/content/drive/MyDrive/combined_with_weather_MW4.csv'
}

# Load the LSTM model
lstm_model = load_model('/content/drive/MyDrive/lstm_model.h5')

# Collect center features
center_features = {}
for center_name, data_path in center_data_paths.items():
    wq_df = pd.read_csv(data_path)
    wq_df = preprocess_dataframe(wq_df)
    target_columns = ['pH', 'Conductivity', 'Turbidity']

    X_train, _, _, _, _, _ = scale_and_split_data(wq_df, target_columns)
    lstm_output = lstm_model.predict(X_train)
    center_features[center_name] = lstm_output.flatten()

# Prepare node features and graph structure
min_len = 720
truncated_features = [value[:min_len] for value in center_features.values()]
node_features = torch.tensor(truncated_features, dtype=torch.float)

edge_index = torch.tensor([[0, 1, 2, 3],
                           [1, 2, 3, 0]],
                          dtype=torch.long)

# Predict
predictions_rescaled = predict_gnn(node_features, edge_index, model_path, scaler_y_path)

for i, center_name in enumerate(center_data_paths.keys()):
    print(f"Predictions for {center_name}: pH={predictions_rescaled[i, 0]}, "
          f"Conductivity={predictions_rescaled[i, 1]}, Turbidity={predictions_rescaled[i, 2]}")
