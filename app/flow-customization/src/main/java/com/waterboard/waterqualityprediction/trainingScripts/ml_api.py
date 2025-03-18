import joblib
import pandas as pd
import numpy as np
from flask import Flask, request, jsonify
import os
# Get the absolute path dynamically
base_dir = os.path.dirname(os.path.abspath(__file__))  # Gets the directory of ml_api.py
model_path = os.path.join(base_dir, "../mlModels/stage_prediction_model.pkl")
scaler_path = os.path.join(base_dir, "../mlModels/scaler.pkl")

# Load the model and scaler
model = joblib.load(model_path)
scaler = joblib.load(scaler_path)
# Define the function before using it in Flask
def predict_treated_water(raw_turbidity, raw_ph, raw_conductivity):
    input_data = pd.DataFrame([[raw_turbidity, raw_ph, raw_conductivity]],
                              columns=['Raw_Water_Turbidity', 'Raw_Water_PH', 'Raw_Water_Conductivity'])
    input_scaled = scaler.transform(input_data)
    prediction = model.predict(input_scaled)
    return prediction[0]  # Ensure it returns a single array of 3 values

# Initialize Flask app
app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        raw_turbidity = data.get('raw_turbidity')
        raw_ph = data.get('raw_ph')
        raw_conductivity = data.get('raw_conductivity')

        # Ensure all inputs are provided
        if None in [raw_turbidity, raw_ph, raw_conductivity]:
            return jsonify({'error': 'Missing input values'}), 400

        # Call the function (now defined above)
        prediction = predict_treated_water(raw_turbidity, raw_ph, raw_conductivity)

        # Convert to list to avoid index errors
        prediction_list = prediction.tolist() if isinstance(prediction, np.ndarray) else prediction

        # Ensure it's the correct format
        if len(prediction_list) != 3:
            return jsonify({'error': f'Expected 3 values, got {len(prediction_list)}'}), 500

        return jsonify({
            'treated_turbidity': float(prediction_list[0]),
            'treated_ph': float(prediction_list[1]),
            'treated_conductivity': float(prediction_list[2])
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
