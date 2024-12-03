import torch
import pandas as pd
import numpy as np
from tensorflow.keras.models import load_model
import joblib
import torch.nn as nn
import torch_geometric.nn as pyg_nn
from preprocessing import preprocess_dataframe, scale_and_split_data

# Define the GNN Layer
class GNNLayer(pyg_nn.MessagePassing):
    def __init__(self, in_channels, out_channels):
        super(GNNLayer, self).__init__(aggr='mean')  # Mean aggregation
        self.fc = nn.Linear(in_channels, out_channels)

    def forward(self, x, edge_index):
        return self.propagate(edge_index, x=x)

    def message(self, x_j):
        return x_j

    def update(self, aggr_out):
        return self.fc(aggr_out)

# Define the Hybrid GNN-LSTM Model
class GNNLSTMHybrid(nn.Module):
    def __init__(self, lstm_output_dim, gnn_hidden_dim, num_centers):
        super(GNNLSTMHybrid, self).__init__()
        self.gnn = GNNLayer(lstm_output_dim, gnn_hidden_dim)
        self.fc = nn.Linear(gnn_hidden_dim, 3)

    def forward(self, x, edge_index):
        gnn_output = self.gnn(x, edge_index)
        return self.fc(gnn_output)

# Load the Trained GNN Model
def load_gnn_model(model_path, lstm_output_dim, gnn_hidden_dim, num_centers):
    model = GNNLSTMHybrid(lstm_output_dim, gnn_hidden_dim, num_centers)
    model.load_state_dict(torch.load(model_path))
    model.eval()
    return model

def predict_gnn(center_data_paths, gnn_model_path, lstm_model_path, scaler_X_path, scaler_y_path, min_len=720):
    # Load scalers and LSTM model
    lstm_model = load_model(lstm_model_path)
    scaler_X = joblib.load(scaler_X_path)
    scaler_y = joblib.load(scaler_y_path)

    # Extract features from each center using the LSTM model
    center_features = {}
    for center_name, data_path in center_data_paths.items():
        wq_df = pd.read_csv(data_path)
        wq_df = preprocess_dataframe(wq_df)
        target_columns = ['pH', 'Conductivity', 'Turbidity']

        X_train, X_val, X_test, y_train, y_val, y_test = scale_and_split_data(wq_df, target_columns)
        lstm_output = lstm_model.predict(X_test)
        center_features[center_name] = lstm_output.flatten()

    # Truncate features to minimum length across centers
    min_len = min(len(features) for features in center_features.values())
    truncated_features = [value[:min_len] for value in center_features.values()]
    node_features = torch.tensor(truncated_features, dtype=torch.float)

    # Check the shape of node_features
    print(f"Node Features Shape: {node_features.shape}")

    # Define the graph structure (example)
    edge_index = torch.tensor([[0, 1, 2, 3], [1, 2, 3, 0]], dtype=torch.long)

    # Load the GNN model with the correct lstm_output_dim (720, as in the trained model)
    gnn_model = load_gnn_model(
        gnn_model_path,
        lstm_output_dim=720,  # Ensure this matches the training configuration (720 features)
        gnn_hidden_dim=64,
        num_centers=len(center_features)
    )

    # Predict using the GNN model
    gnn_model.eval()
    with torch.no_grad():
        predictions = gnn_model(node_features, edge_index)

    # Rescale predictions to the original scale
    predictions_rescaled = scaler_y.inverse_transform(predictions.numpy())
    return predictions_rescaled

center_data_paths = {
    'center_1': '/content/drive/MyDrive/combined_with_weather.csv',
    'center_2': '/content/drive/MyDrive/combined_with_weather_MW2.csv',
    'center_3': '/content/drive/MyDrive/combined_with_weather_MW3.csv',
    'center_4': '/content/drive/MyDrive/combined_with_weather_MW4.csv'
}
gnn_model_path = '/content/drive/MyDrive/gnn_lstm_hybrid.pth'
lstm_model_path = '/content/drive/MyDrive/lstm_model.h5'
scaler_X_path = '/content/drive/MyDrive/scaler_X.pkl'
scaler_y_path = '/content/drive/MyDrive/scaler_y.pkl'


# Make Predictions
predictions = predict_gnn(
    center_data_paths, gnn_model_path, lstm_model_path, scaler_X_path, scaler_y_path
)

# Display Predictions
for i, center_name in enumerate(center_data_paths.keys()):
    print(f"Predictions for {center_name}: pH={predictions[i, 0]}, Conductivity={predictions[i, 1]}, Turbidity={predictions[i, 2]}")