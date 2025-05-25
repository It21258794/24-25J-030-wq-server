# 24-25J-030-wq-server

This pilot project is a collaborative effort between the Water Board Sri Lanka and JRDC to develop and deploy a comprehensive water quality management system for the Meewathura Water Treatment Plant. The system integrates machine learning and deep learning models for predicting key water quality parameters and chemical consumption, and also forecasts step-wise treated water parameters. It supports real-time monitoring and visualization of sensor data through integration with the existing SCADA system. The goal is to improve water treatment efficiency and ensure proactive quality management of water sourced from the Mahaweli River.

## Team Members

- **Shiraz M.S** - `IT21277054`  
  [![Email](https://img.shields.io/badge/Gmail-D14836?style=flat&logo=gmail&logoColor=white)](mailto:oshadhianjana@gmail.com)
  [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/shamry-shiraz-0b80b21a9/)

- **Karunasena H.G.M.K.K.L** - `IT21258794`  
  [![Email](https://img.shields.io/badge/Gmail-D14836?style=flat&logo=gmail&logoColor=white)](mailto:kaveeshakarunasena@gmail.com)
  [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/kaveesha-karunasena/)

- **Hansani K.J.M** - `IT21222672`  
  [![Email](https://img.shields.io/badge/Gmail-D14836?style=flat&logo=gmail&logoColor=white)](mailto:hansanimu00@gmail.com)
  [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/hansani-mudelige-2a7776275)

- **Kumarasinghe O.A.** - `IT21174308`  
  [![Email](https://img.shields.io/badge/Gmail-D14836?style=flat&logo=gmail&logoColor=white)](mailto:oshadhianjana@gmail.com)
  [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/oshadhi-anjana-kumarasinghe-a784aa213/)


## Architecture diagram

<img width="9428" alt="Water Quality Prediction - Conceptual Diagram (5)" src="https://github.com/user-attachments/assets/f3b49588-e581-4d94-acf9-8c139f9bae8b" />


## 1.  FEATUERS
The backend system includes the following key features:

### 1. User Management:

+ User authentication and authorization.
+ Role-based access control for different system stakeholders. (Admin/User)


### 2. Notification System:

+ Real-time alerts based on water quality predictions.
+ Configurable notification settings for various user roles.
+ SMTP Gmail service
+ Twillio message service


### 3. Water Quality Prediction Model:

+ Integrated with Python-based predictive models.
+ Hybrid DL model using LSTM and GNN
+ Uses historical and real-time sensor data to predict key water quality parameters.
+ Historical data provides by JRDC
+ Thsi will provise max 30 days predictions for PH, Turbidity, Conductivity
+ External Weather Api use.


### 4. Flow Customization and Step wise Treated water quality prediction:

+ Modular configuration for customizing system workflows and thresholds.
+ Step wise treated water quality parameter prediction through out the treatement process


### 5. Sensor Management:

+ Plug-and-play support for adding or managing IoT sensors.
+ Data ingestion and processing pipelines.
+ Loacl SCADA system integration through a python script


### 6. Chemical Consumption Prediction Model:

+ Predictive analysis for estimating chemical requirements for water treatment process.
+ Use treated water historical data provides by JRDC
+ External Weather Api use.

## 2.  MULTI-MODULE ARCHITECTURE
The backend application was structured using Spring Boot’s multi-module setup, which included:


+ Main Application: Application Entrypoint with all initializations.
+ Core Module: Contains shared business logic and utilities.
+ Common Module: Exposes REST endpoints for frontend and external integrations.
+ Analytics Module : Handle all the statistcis iin the project 
+	Chlorine Cunsumption Module: Logic for communicating with respective ML/DL model container.
+	Water Quality Prediction Module : Logic for communicating with respective ML/DL model container.
+	Flow Customization Module: Handles Logic for communicating with respective ML/DL model container and treatment process configuration.
+	Sensor Management Module : : Handles real-time data intake from SCADA systems.
+	Notification Module: Manages integration with Twilio (SMS) and Gmail SMTP (Email).
+	Security Module: Handles JWT-based authentication and authorization.
+	User Management Module : Hnadles uses within the system


## 3. SOFTWARE SPECIFICATIONS

| Component               | Technology Used            | Purpose                                         |
|-------------------------|----------------------------|-------------------------------------------------|
| Frontend                | React.js                   | Dashboard and visualization                     |
| Backend                 | Spring Boot (Multi-module) | User management, ML model integration, notifications |
| Machine Learning Services | Python (FastAPI / FlaskAPI) | Predictions                                  |
| Database 1              | MongoDB Atlas              | Sensor and prediction data storage              |
| Database 2              | MySQL                      | Structured transactional data                   |
| Authentication          | JWT                        | Secure access control                           |
| Notification - SMS      | Twilio                     | Sending SMS alerts                              |
| Notification - Email    | Gmail SMTP                 | Sending email alerts                            |
| Email Templating        | Thymeleaf (HTML)           | Designing dynamic email templates               |
| Weather API             | OpenWeather                | API external input for predictions              |
| Message Broker          | RabbitMQ (CloudAMQP)       | Async communication for tasks                   |



## 4. TOOLS AND VERSIONS

| Tool/Service              | Version / Type           | Purpose                                                |
|---------------------------|---------------------------|--------------------------------------------------------|
| Java (OpenJDK)            | 17                        | Backend runtime environment for Spring Boot application|
| Spring Boot               | 3.4.0                     | Backend framework                                      |
| Maven                     | 3.8.1                     | Build automation and dependency management tool        |
| RestAssured               | -                         | Automated API testing                                  |
| MongoDB Atlas             | 8.0.9                     | NoSQL cloud DB                                         |
| MySQL                     | 9.0.1                     | SQL data storage                                       |
| Docker                    | 4.37.1                    | Containerization                                       |
| AWS EC2                   | t2.medium (us-west-2)     | Application hosting                                    |
| AWS S3                    | -                         | Store secured config files                             |
| AWS Secrets Manager       | -                         | Manage secrets/credentials                             |
| CloudWatch                | -                         | Logging and monitoring                                 |
| RabbitMQ (CloudAMQP)      | -                         | Queue service                                          |
| Thymeleaf                 | 3.4.0                     | Create dynamic HTML email templates             |



## 5.TECH STACK

### Backend Frameworks and Languages:
+ Spring Boot: Backend API development using a modular architecture.
+ Java : 17
+ Thymeleaf : For email template design

### Database:
+ MySQL: To store and manage user related data.
+ MongoDB : to store prediction data

### Deployment:
The system is hosted on AWS EC2 instances in the us-west-2 region, configured with:
EC2 Instance Configuration
+	Region: us-west-2
+	Instance Type: t2.medium
+	OS: Amazon Linux 2
+	VPC/Subnet: Configured with route tables
+	Security Groups: Controlled access on ports 80, 443, 22, 8000
+	IAM Roles: Limited access using least privilege
+	S3 Access: Configured via IAM policies for reading env files

+	AWS ARCHITECTURE DIAGRAM

  <img width="10385" alt="Untitled (1)" src="https://github.com/user-attachments/assets/edb6ad37-32c4-4daa-afe5-5c539dde260a" />



## 6. LINKED GIT REPOSITORIES
+ Frontend : https://github.com/It21258794/It21258794-24-25J-030-wq-web
+ Water Quality Prediction Model (FastApi) - https://github.com/It21258794/water-quality-prediction-service
+ Step wise prediction (FlaskApi) - https://github.com/IT21174308/24-25J-030-wq-model.git
+ Chemical consumption prediction model (FlaskApi) - https://github.com/it21222672/chemical_deply.git
+ SCADA integration script (Python) - https://github.com/IT21277054/RP-Water-Quality-Management-Scripts


## Getting Started

### Prerequisites:
+ Java Development Kit (JDK 17+)
+ MySQL and MongoDB Database installed and configured.
+ Maven for building the Spring Boot project.
+ Docker for containerized deployment.
+ RabbitMQ


## INSTALLATION:

### 1. Clone the Repository:
```bash
git clone <repo-url>
cd <repo-name>
```

### 2. Set up MySQL Database:

Create a database named water_quality.

Run the SQL scripts located in /db-scripts to set up the schema and initial data.

### 3. Set up MySQL Database:

run rabbitMQ locally

### 4. Configure Environment:

Update the application.properties file in /config with your database credentials and other required configurations.


### 5. Build and Run the Spring Boot Application:

```bash
mvn clean install
mvn spring-boot:run 
```

## PULL REQUESTS

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

> [!TIP]
> https://github.com/It21258794/24-25J-030-wq-server/pull/28
> 
> feat(flow-customization): implemented flow customization funcionality
>- add jar test configuration flow
>- add chemical mangement part for Test
>- Implement a ml modle to predict step wise treated water quality




- **[#37](https://github.com/It21258794/24-25J-030-wq-server/pull/37)**: fix(sensor-management): updated modbus IP
  - Description: - Updated MODBUS_IP 
