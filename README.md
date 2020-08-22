# Congestion API  
This is a REST API built to track congestion spots and crowded areas.
For the API to work it needs to get real-time location data from mobile devices.
##### Use case:
You have a mobility mobile app with a large user base. 
The mobile app, installed on a user device, will send location data (latitude, longitude) to the API.
Using this data the API calculates live congestion in different spots requested by you.
The API can also make future predictions based on history data using a Machine Learning algorithm.  

By using this API you can:
 - show on a map crowded areas
 - plan a route avoiding crowded buss stations
 - notify your users when approaching crowded areas
 - predict future congestion
## Installation
### Postgres database
Tested with: PostgreSQL 12.2 and PostGIS 3.0.0 extension.

Create a database:
```bash
sudo -u postgres psql
create database "database_name" owner "database_owner";
```
Connect to the created database:
```bash
\c congestion_tracing
```
Create PostGIS extension:
```sql
CREATE EXTENSION postgis;
```
### Run the API in a docker container
Package the API in a JAR:
```bash
./mvnw package
```
Build docker container:
```bash
docker build -t congestion_tracing .
```
Run container:
```bash
docker run --network=host congestion_tracing
```
### Machine Learning prediction service
The machine learning part is build as a separate service/API. The `/prediction` endpoint of this API will not work if the prediction service is not running.
To run the prediction service see: https://github.com/cod-r/ml-congestion-prediction

# API specification
## Authorization
All API requests require the use of an API key.
To authenticate an API request, you should append your API key as a GET parameter.
```http
GET /api/congestion/?key=1234567890
```
## Endpoints
### Get congestion
```http
GET /api/congestion/?key=1234567890&lat=44.348732&lon=26.104334&radius=10
```
*  **URL Params**

   **Required:**   
   `key=[integer]` - Authorization key.  
   `lat=[numeric]` - Latitude. Constraints: `-90.0 < lat < 90.0`  
   `lon=[numeric]` - Longitude. Constraints: `-180.0 < lat < 180.0`  
   `radius=[numeric]` - Distance in meters representing how big the perimeter to be when querying for active devices. Must be a positive number. 
   
    **Optional:**  
    
   `seconds_ago=[integer]` - Devices will post their location ideally every few seconds. This parameter controls how long to go back in time when querying for congestion. Default is 30 seconds.

    **Sample call:**
    ```shell script
    curl --request GET http://localhost:8080/api/congestion/?key=1234567890&lat=44.348732&lon=26.104334&radius=12.5&seconds_ago=45
    ```
   **Success Response:**
        
    **Code:** 200 OK  
    **Content:** `3`  
    The response is an integer representing the total number of devices (users) being inside the perimeter of a circle
    of radius `12.5` meters and the center at `44.348732, 26.104334` which posted their location data (`lat`, `lon`) in the past 45 seconds.

### Get prediction
```http
GET /api/predict?key=1234567890&lat=44.4133671&lon=26.1630280&prediction_date=2020-08-19T17:07:33.4782Z
```
*  **URL Params**

   **Required:**   
   `key=[integer]` - Authorization key.  
   `lat=[numeric]` - Latitude. Constraints: `-90.0 < lat < 90.0`  
   `lon=[numeric]` - Longitude. Constraints: `-180.0 < lat < 180.0`  
   `prediction_date=[timestamp]` - A future timestamp for which you want the congestion prediction to be made. The timestamp must use the ISO 8601 standard.
   
    **Optional:**  
    
   `radius=[numeric]` - Distance in meters representing how big the perimeter to be for the prediction. Must be a positive number. Default is 11 meters.   

    **Sample call:**
    ```shell script
    curl --request GET http://localhost:8080/api/predict/?key=1234567890&lat=44.4133671&lon=26.1630280&prediction_date=2020-08-19T17:07:33.4782Z&radius=11 
    ```
   **Success Response:**
        
    **Code:** 200 OK  
    **Content:**  
    ```json
        {
            "predicted_value": "17.041208956969186",
            "predicted_date": "2020-08-19T17:07:33.478200Z"
        }
    ```  
    `predicted_value` - represents the predicted number of devices (users) to be inside the perimeter of a circle
    of radius `11` meters and the center at `44.4133671, 26.1630280` at date `2020-08-19T17:07:33.478200Z`  
    `predicted_date` - represents the future date for which the prediction has been made.


### Post device location
```http
POST /api/location/?key=1234567890&lat=44.348732&lon=26.104334&device_id=6601ebc9-1c74-4ee0-9240-bec88dbcdea7
```
*  **URL Params**

   **Required:**   
   `key=[integer]` - Authorization key.  
   `lat=[numeric]` - Latitude. Constraints: `-90.0 < lat < 90.0`  
   `lon=[numeric]` - Longitude. Constraints: `-180.0 < lat < 180.0`  
   `device_id=[string]` - Every device which posts its location to the API must be uniquely identified. We recommend using a Version 4 UUID generator.
   To correctly update the location of one device the `device_id` must not be changed between requests.  
   
    **Sample call:**
    ```shell script
    curl --request POST http://localhost:8080/api/location/?key=1234567890&lat=44.348732&lon=26.104334&device_id=6601ebc9-1c74-4ee0-9240-bec88dbcdea7
    ```
   **Success Response:**
        
    **Code:** 200 OK  
    **Content:**   


## Error Responses  
**Code:** 403 FORBIDDEN <br />
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 403,
    "error": "Forbidden",
    "message": "",
    "path": "/api/location"
}
```
**Code:** 429 TOO MANY REQUESTS <br />
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 429,
    "error": "Too Many Requests",
    "message": "You have exhausted the API Request Quota.",
    "path": "/api/location"
}
```
**Code:** 400 BAD REQUEST <br />
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 400,
    "error": "Bad Request",
    "message": "lon should be of type double",
    "path": "/api/location"
}
```
**Code:** 500 INTERNAL SERVER ERROR <br />
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Oops! Something went wrong on our side.",
    "path": "/api/location"
}
```