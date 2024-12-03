
# pip install torch-geometric

import sys
sys.path.append('/content/drive/MyDrive')

import torch
import torch.nn as nn
import torch.optim as optim
import torch_geometric.nn as pyg_nn
from torch_geometric.data import Data
from tensorflow.keras.models import load_model
import numpy as np
import pandas as pd
import joblib
from preprocessing import preprocess_dataframe, scale_and_split_data
import matplotlib.pyplot as plt
from sklearn.metrics import mean_squared_error, r2_score

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

# 3. Load Pretrained LSTM Model and Scalers
lstm_model = load_model('/content/drive/MyDrive/lstm_model.h5')
scaler_X = joblib.load('/content/drive/MyDrive/scaler_X.pkl')
scaler_y = joblib.load('/content/drive/MyDrive/scaler_y.pkl')

center_data_paths = {
    'center_1': '/content/drive/MyDrive/combined_with_weather.csv',
    'center_2': '/content/drive/MyDrive/combined_with_weather_MW2.csv',
    'center_3': '/content/drive/MyDrive/combined_with_weather_MW3.csv',
    'center_4': '/content/drive/MyDrive/combined_with_weather_MW4.csv'
}

center_features = {}

for center_name, data_path in center_data_paths.items():
    wq_df = pd.read_csv(data_path)
    wq_df = preprocess_dataframe(wq_df)
    target_columns = ['pH', 'Conductivity', 'Turbidity']

    X_train, X_val, X_test, y_train, y_val, y_test = scale_and_split_data(wq_df, target_columns)

    lstm_output = lstm_model.predict(X_train)
    center_features[center_name] = lstm_output.flatten()

lengths = [len(value) for value in center_features.values()]
print(lengths)

min_len = 720
truncated_features = [value[:min_len] for value in center_features.values()]
node_features = torch.tensor(truncated_features, dtype=torch.float)

print("Node features shape:", node_features.shape)

# Define Graph Structure
edge_index = torch.tensor([[0, 1, 2, 3],
                           [1, 2, 3, 0]],
                          dtype=torch.long)

num_nodes = len(node_features)
print(f"Number of nodes: {num_nodes}")

print(f"Edge indices: {edge_index}")

# Initialize the GNN Model
gnn_model = GNNLSTMHybrid(lstm_output_dim=node_features.shape[1], gnn_hidden_dim=64, num_centers=len(center_features))

# Training Loop
optimizer = optim.Adam(gnn_model.parameters(), lr=0.001)
criterion = nn.MSELoss()

true_labels = torch.tensor(y_train[:4], dtype=torch.float)
print("True labels shape:", true_labels.shape)

# Loss tracking
losses = []

gnn_model.train()
for epoch in range(100):
    optimizer.zero_grad()

    predictions = gnn_model(node_features, edge_index)
    print(f"Predictions shape: {predictions.shape}")

    loss = criterion(predictions, true_labels)
    loss.backward()
    optimizer.step()
    losses.append(loss.item())

    print(f"Epoch {epoch+1}, Loss: {loss.item()}")

# Plotting the loss curve
plt.plot(range(1, 101), losses)
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.title('Training Loss Curve')
plt.show()

gnn_model.eval()
with torch.no_grad():
    predictions = gnn_model(node_features, edge_index)

y_true = true_labels.cpu().detach().numpy()
y_pred = predictions.cpu().detach().numpy()

mse = mean_squared_error(y_true, y_pred)
r2 = r2_score(y_true, y_pred)

print(f"MSE: {mse}, R² Score: {r2}")

# Save the Trained GNN Model
torch.save(gnn_model.state_dict(), 'gnn_lstm_hybrid.pth')
print("GNN model saved!")