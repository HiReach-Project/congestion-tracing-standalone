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
### How to run the API in a docker container
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