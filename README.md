# 24-25J-030-wq-server

This repository contains the backend implementation of the Water Quality Prediction and Management System, a collaborative project developed as part of the 4th-year university project in partnership with the Sri Lanka Water Board and JRDC. This backend serves as the backbone for user management, notifications, predictive modeling, sensor management, and system customization.

## Architecture diagram

![wq-architecture](https://github.com/user-attachments/assets/5312f2c2-bf94-4422-b6da-4f80c98c3614)



## Features
The backend system includes the following key features:

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


### 6. Chemical Consumption Prediction Model:

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

## Pull Requests

> [!TIP]
>https://github.com/It21258794/24-25J-030-wq-server/pull/1
> 
>feat(notification):notification module implemented with mail and sms services using smtp, twllio - 17 November 2024 at 00:28:16 GMT+5:30
> 
>- For emil used smpt and created gmail to send notification via email
>- used twilio to send sms alerts for now free version of twilio used
>- two end point created for each altering services

> [!TIP]
>https://github.com/It21258794/24-25J-030-wq-server/pull/17
> 
>feat: added initial LSTM module for water-quality-module - 28 November 2024 at 22:23:12 GMT+5:30
>
>- trained a LSTM model for water quality prediction for a one centre
>- change the folder structure little bit for multi modular architecture
>- change the docker file for main application instead having separate file for each module

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/20
>
> feat: added model for daily chemical prediction
>
>- Implemented a model to predict daily chemical consumption
>

> [!TIP]
>https://github.com/It21258794/24-25J-030-wq-server/pull/21
> 
> feat(water-quality-prediction):initial gnn model training and prediction part
>
>- trained a gnn model to use previous lstm model for each node as centres to get better prediction
>- implemented a prediction model but need water board data to evaluate predictions
>- trained gnn model saved for use in future
>
> fix(notification system): updated sample email template using a simple layout

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/22
>
> fix(water-quality-prediction): separated file code for gnn model and gnn predict

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/23
>
> feat(user- management): security filters and security configurations implemented
>- user login, creation implemented
>- reset password flow implemented
>- spring version updated
>- new modules called common and core added for better moduleraization
>- fix(pom): build fail issue fixed, pom.xml files updated

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/25
>
> feat(user-management): create user flow implemented with sending notifications
>- user create flow implemented with notification triggering
>- new Exception add with hash code
>- email templates add for newly created user
>- 

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/26
> 
> feat(water-quality-prediction): fast api integration with a get request to get prediction
>- scada data stored in db implemented a cron to store predictions
>- fix(user-management): search user api implemented with pagination
create user , user status change, change current password apis implemented
test cases added
>- fix(notification-module): new email templates added for user activation flow
>- updated git file

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/27
> 
> feat(chemical-consumption): add ChemicalController class and required dependencies
>- adjusted security configuration and did minor changes in chemical controller
>- Added ChemicalController.java
>- Implemented WebClient for HTTP requests
>- Enabled CORS for frontend communication




