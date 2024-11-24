# 24-25J-030-wq-server

This repository contains the backend implementation of the Water Quality Prediction and Management System, a collaborative project developed as part of the 4th-year university project in partnership with the Sri Lanka Water Board and JRDC. This backend serves as the backbone for user management, notifications, predictive modeling, sensor management, and system customization.


## Features
+ The backend system includes the following key features:

### 1. User Management:

+ User authentication and authorization.
+ Role-based access control for different system stakeholders.


### 2. Notification System:

+ Real-time alerts based on water quality predictions.
+ Configurable notification settings for various user roles.


### 3. Water Quality Prediction Model:

+ Integrated with Python-based predictive models.
+ Uses historical and real-time sensor data to predict key water quality parameters.


### 4. Flow Customization:

+ Modular configuration for customizing system workflows and thresholds.


### 5. Sensor Management:

+ Plug-and-play support for adding or managing IoT sensors.
+ Data ingestion and processing pipelines.


### Chemical Consumption Prediction Model:

+ Predictive analysis for estimating chemical requirements for water treatment.



## Tech Stack

### Backend Frameworks and Languages:
+ Spring Boot: Backend API development using a modular architecture.
+ Python: For predictive models (e.g., water quality and chemical consumption).

### Database:
+ MySQL: To store and manage system data.

### Architecture:
+ Multi-Module Architecture: Enables separation of concerns and easy scalability.

### Deployment:
+ Hosted on Hostinger with Dockerized microservices.


## Getting Started

### Prerequisites:
+ Java Development Kit (JDK 17+)
+ Python 3.8+
+ MySQL Database installed and configured.
+ Maven for building the Spring Boot project.
+ Docker for containerized deployment.


## Installation:

### 1. Clone the Repository:
```bash
git clone <repo-url>
cd <repo-name>
```

### 2. Set up MySQL Database:

Create a database named water_quality.
Run the SQL scripts located in /db-scripts to set up the schema and initial data.

### 3. Install Python Dependencies:

Navigate to the /python-models directory and install dependencies:

```bash
pip install -r requirements.txt
```

### 4. Configure Environment:

Update the application.properties file in /config with your database credentials and other required configurations.


### 5. Build and Run the Spring Boot Application:

```bash
mvn clean install
mvn spring-boot:run 
```
