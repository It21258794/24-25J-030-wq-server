#imports
import joblib
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import requests
import time
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dropout, Dense
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.callbacks import EarlyStopping
from sklearn.model_selection import train_test_split
from tensorflow.keras.models import load_model

# List of Excel sheet file paths
excel_files = [
    '/content/drive/MyDrive/pH.xlsx',
    '/content/drive/MyDrive/Water Temperature (°C).xlsx',
    '/content/drive/MyDrive/Turbidity.xlsx',
    '/content/drive/MyDrive/Conductivity.xlsx',
    '/content/drive/MyDrive/Flow.xlsx'
]

sheet_keys = ['pH', 'Temperature', 'Turbidity', 'Conductivity', 'Flow']

# Read and merge each Excel sheet
data_frames = []
for file, key in zip(excel_files, sheet_keys):
    df = pd.read_excel(file, index_col=None)

    if 'date' not in df.columns:
        raise ValueError(f"File {file} does not have a 'date' column")
    df['date'] = pd.to_datetime(df['date'])
    df = df.set_index('date')

    df = df.loc[:, ~df.columns.str.contains('^Unnamed')]

    df.columns = [f"{key}" if col.lower() == key.lower() or key in col else f"{key}_{col}" for col in df.columns]
    data_frames.append(df)

# Merge all DataFrames
merged_df = pd.concat(data_frames, axis=1, join='outer')

# Save to CSV file
merged_df.reset_index().to_csv('/content/drive/MyDrive/combined_data.csv', index=False, na_rep='null')

print("Combined CSV file created successfully!");

data_path = '/content/drive/MyDrive/combined_data.csv'
combined_df = pd.read_csv(data_path)

combined_df.info()

combined_df['date'] = pd.to_datetime(combined_df['date'])

start_date = combined_df['date'].min()
end_date = combined_df['date'].max()

def date_to_unix(date):
    return int(time.mktime(date.timetuple()))

API_KEY = '747a2d3255663c2a031e99b189dcfb22'
LAT = 22.2661
LON = 113.9936

def fetch_weather_data(date):
    unix_time = date_to_unix(date)
    url = f'https://api.openweathermap.org/data/3.0/onecall/timemachine?lat={LAT}&lon={LON}&dt={unix_time}&appid={API_KEY}'
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Failed to fetch data for {date}. Error: {response.text}")
        return None

def parse_weather_data(weather_json):
    if not weather_json:
        return None
    try:
        data = weather_json.get('data', [])[0]
        return {
            'temp': data.get('temp', None),
            'humidity': data.get('humidity', None),
            'wind_speed': data.get('wind_speed', None),
            'clouds': data.get('clouds', None),
            'feels_like': data.get('feels_like', None),
            'pressure': data.get('pressure', None),
            'dew_point': data.get('dew_point', None),
            'visibility': data.get('visibility', None),
            'wind_deg': data.get('wind_deg', None)
        }
    except (IndexError, AttributeError) as e:
        print(f"Error parsing weather data: {e}")
        return None

weather_data = []
for date in combined_df['date']:
    weather_json = fetch_weather_data(date)
    weather_info = parse_weather_data(weather_json)
    weather_data.append(weather_info)

# Create a DataFrame for weather data
weather_df = pd.DataFrame(weather_data, index=combined_df.index)

# Merge
merged_with_weather = pd.concat([combined_df, weather_df], axis=1)

# Save
merged_with_weather.to_csv('/content/drive/MyDrive/combined_with_weather.csv', index=False, na_rep='null')
print("Weather data integrated successfully!")

data_path = '/content/drive/MyDrive/combined_with_weather.csv'
wq_df = pd.read_csv(data_path)

# fill the missing values with mean
wq_df['date'] = pd.to_datetime(wq_df['date'])
wq_df.fillna(wq_df.mean(), inplace=True)
wq_df.head()

def show_correlation(df):
    numeric_df = df.select_dtypes(include=['number'])
    correlation = numeric_df.corr()

    # Plotting the correlation
    plt.figure(figsize=(10, 8))
    sns.heatmap(correlation, annot=True, cmap='coolwarm', fmt='.2f')
    plt.title('Correlation Matrix')
    plt.show()

show_correlation(wq_df)

sns.pairplot(wq_df, vars=['pH', 'Turbidity', 'Conductivity'], hue='humidity')

sns.pairplot(wq_df, vars=['pH', 'Turbidity', 'Conductivity'], hue='Flow')

# check the data types of the columns
wq_df.info()

def extract_date_features(df, date_column):

    if date_column not in df.columns:
        raise ValueError(f"The column '{date_column}' does not exist in the DataFrame.")
    if not pd.api.types.is_datetime64_any_dtype(df[date_column]):
        raise TypeError(f"The column '{date_column}' is not of type datetime64[ns].")

    # Extract date-related features
    df['Year'] = df[date_column].dt.year
    df['Month'] = df[date_column].dt.month
    df['Day'] = df[date_column].dt.day
    df['DayOfWeek'] = df[date_column].dt.dayofweek
    df['Hour'] = df[date_column].dt.hour
    df['Minute'] = df[date_column].dt.minute
    df['Second'] = df[date_column].dt.second

    return df

wq_df = extract_date_features(wq_df, 'date')
print(wq_df)

wq_df.info()

columns_to_drop = ['date']
wq_df = wq_df.drop(columns=columns_to_drop)

#function for encode categorical data
le = LabelEncoder()

def clean_labels_encoder(list_of_labels, df):
    for label in list_of_labels:
        df[label] = le.fit_transform(df[label])
    return df

list_of_labels = []
wq_df = clean_labels_encoder(list_of_labels, wq_df)

wq_df.head()

# Define features and targets
X = wq_df.drop(columns=['pH', 'Conductivity', 'Turbidity'])  # Features
y = wq_df[['pH', 'Conductivity', 'Turbidity']]

# First split: Training and Temp (Validation + Test)
X_train, X_temp, y_train, y_temp = train_test_split(X, y, test_size=0.4, random_state=42, shuffle=False)

# Second split: Validation and Test
X_val, X_test, y_val, y_test = train_test_split(X_temp, y_temp, test_size=0.5, random_state=42, shuffle=False)

print(f"Training set: {X_train.shape}, {y_train.shape}")
print(f"Validation set: {X_val.shape}, {y_val.shape}")
print(f"Test set: {X_test.shape}, {y_test.shape}")

# Initialize scalers
scaler_X = MinMaxScaler()
scaler_y = MinMaxScaler()

# Scale X_train, X_val, and X_test
X_train = scaler_X.fit_transform(X_train)
X_val = scaler_X.transform(X_val)
X_test = scaler_X.transform(X_test)

# Scale y_train, y_val, and y_test (for multiple targets)
y_train = scaler_y.fit_transform(y_train)
y_val = scaler_y.transform(y_val)
y_test = scaler_y.transform(y_test)

# Reshape X_train, X_val, and X_test for LSTM input
X_train = X_train.reshape((X_train.shape[0], 1, X_train.shape[1]))  # 3D shape
X_val = X_val.reshape((X_val.shape[0], 1, X_val.shape[1]))  # 3D shape
X_test = X_test.reshape((X_test.shape[0], 1, X_test.shape[1]))  # 3D shape

print(f"X_train shape: {X_train.shape}, y_train shape: {y_train.shape}")
print(f"X_val shape: {X_val.shape}, y_val shape: {y_val.shape}")
print(f"X_test shape: {X_test.shape}, y_test shape: {y_test.shape}")

# Define the model architecture
model = Sequential()
model.add(LSTM(128, activation='relu', input_shape=(X_train.shape[1], X_train.shape[2]), return_sequences=True))
model.add(Dropout(0.3))
model.add(LSTM(64,return_sequences=True))
model.add(Dropout(0.3))
model.add(LSTM(64))
model.add(Dense(3))

# Compile the model
optimizer = Adam(learning_rate=0.0001)
model.compile(loss='mean_squared_error', optimizer=optimizer, metrics=['mae'])

print(f"X_train shape: {X_train.shape}")
print(f"y_train shape: {y_train.shape}")

# Train the model
early_stopping = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)
history = model.fit(X_train, y_train, epochs=150, batch_size=32, validation_data=(X_val, y_val),
          verbose=2)

# Evaluate the model on the test set
score = model.evaluate(X_test, y_test, verbose=0)
print('Test loss (MSE):', score[0])
print('Test MAE:', score[1])

#saving files
joblib.dump(scaler_X, 'scaler_X.pkl')
joblib.dump(scaler_y, 'scaler_y.pkl')
model.save('lstm_model.h5')

# Plot training & validation loss values
plt.figure(figsize=(12, 6))
plt.plot(history.history['loss'], label='Training Loss')
plt.plot(history.history['val_loss'], label='Validation Loss')
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.title('Training and Validation Loss Over Epochs')
plt.legend()
plt.grid(True)
plt.show()

# Load the trained LSTM model
model = load_model('/content/lstm_model.h5')

# Load the scaler for input (X) and output (y)
scaler_X = joblib.load('scaler_X.pkl')
scaler_y = joblib.load('scaler_y.pkl')

# Prepare input data and predict future values
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

last_data = np.random.rand(30, 3)
weekly_predictions = predict_future(model, scaler_X, scaler_y, last_data, steps=7, features=3)
monthly_predictions = predict_future(model, scaler_X, scaler_y, last_data, steps=30, features=3)

print("Weekly Predictions:", weekly_predictions)
print("Monthly Predictions:", monthly_predictions)