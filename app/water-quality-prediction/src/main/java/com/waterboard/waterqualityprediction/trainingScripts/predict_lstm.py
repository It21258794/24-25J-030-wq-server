import numpy as np
import joblib
from tensorflow.keras.models import load_model

# Load the trained LSTM model
model = load_model("lstm_model.h5")

# Load scalers
scaler_X = joblib.load("scaler_X.pkl")
scaler_y = joblib.load("scaler_y.pkl")

def predict_future(model, scaler_X, scaler_y, last_data, steps, features):
    predictions = []
    last_data_scaled = scaler_X.transform(last_data)
    input_sequence = last_data_scaled[-30:]

    for _ in range(steps):
        input_sequence_reshaped = input_sequence.reshape(1, 30, features)
        pred = model.predict(input_sequence_reshaped)
        pred_rescaled = scaler_y.inverse_transform(pred)
        predictions.append(pred_rescaled[0])
        input_sequence = np.append(input_sequence[1:], pred, axis=0)

    return np.array(predictions)

# Example
last_data = np.random.rand(30, 3)
weekly_predictions = predict_future(model, scaler_X, scaler_y, last_data, steps=7, features=3)
monthly_predictions = predict_future(model, scaler_X, scaler_y, last_data, steps=30, features=3)

print("Weekly Predictions:", weekly_predictions)
print("Monthly Predictions:", monthly_predictions)