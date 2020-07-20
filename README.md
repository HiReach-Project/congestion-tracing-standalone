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
 
   `key=[integer]`  
   `lat=[numeric]` constraints: `-90.0 < lat < 90.0`  
   `lon=[numeric]` constraints: `-180.0 < lat < 180.0`  
   `radius=[integer]`

