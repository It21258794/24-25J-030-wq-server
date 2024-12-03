import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler, LabelEncoder
from sklearn.model_selection import train_test_split

# Initialize global scalers
scaler_X = MinMaxScaler()
scaler_y = MinMaxScaler()

def preprocess_dataframe(wq_df):
    """
    Preprocess the water quality DataFrame for training.
    - Handles missing values, date feature extraction, and scaling.
    """
    # Fill missing values with the mean
    wq_df['date'] = pd.to_datetime(wq_df['date'])
    wq_df.fillna(wq_df.mean(), inplace=True)

    # Extract date features
    wq_df = extract_date_features(wq_df, 'date')

    # Drop unused columns
    columns_to_drop = ['date']
    wq_df = wq_df.drop(columns=columns_to_drop)

    # Encode categorical columns
    list_of_labels = []
    wq_df = clean_labels_encoder(list_of_labels, wq_df)

    return wq_df


def extract_date_features(df, date_column):
    """
    Extracts date-related features from a datetime column.
    """
    df['Year'] = df[date_column].dt.year
    df['Month'] = df[date_column].dt.month
    df['Day'] = df[date_column].dt.day
    df['DayOfWeek'] = df[date_column].dt.dayofweek
    df['Hour'] = df[date_column].dt.hour
    df['Minute'] = df[date_column].dt.minute
    df['Second'] = df[date_column].dt.second
    return df


def clean_labels_encoder(list_of_labels, df):
    """
    Encodes categorical columns in the DataFrame.
    """
    le = LabelEncoder()
    for label in list_of_labels:
        df[label] = le.fit_transform(df[label])
    return df


def scale_and_split_data(df, target_columns):
    """
    Scales the dataset and splits it into training, validation, and test sets.
    """
    global scaler_X, scaler_y

    # Define features and targets
    X = df.drop(columns=target_columns)
    y = df[target_columns]

    # First split: Training and Temp (Validation + Test)
    X_train, X_temp, y_train, y_temp = train_test_split(X, y, test_size=0.4, random_state=42, shuffle=False)

    # Second split: Validation and Test
    X_val, X_test, y_val, y_test = train_test_split(X_temp, y_temp, test_size=0.5, random_state=42, shuffle=False)

    # Scale X and y
    X_train = scaler_X.fit_transform(X_train)
    X_val = scaler_X.transform(X_val)
    X_test = scaler_X.transform(X_test)
    y_train = scaler_y.fit_transform(y_train)
    y_val = scaler_y.transform(y_val)
    y_test = scaler_y.transform(y_test)

    # Reshape for LSTM input
    X_train = X_train.reshape((X_train.shape[0], 1, X_train.shape[1]))
    X_val = X_val.reshape((X_val.shape[0], 1, X_val.shape[1]))
    X_test = X_test.reshape((X_test.shape[0], 1, X_test.shape[1]))

    return X_train, X_val, X_test, y_train, y_val, y_test
