# models.py
import torch.nn as nn
import torch_geometric.nn as pyg_nn

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
