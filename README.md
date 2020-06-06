# congestion-tracing
Create postgres database:
```bash
sudo -u postgres psql
create database "database_name" owner "database_owner";
```
Connect to the created database:
```bash
\c congestion_tracing
```
Create PostGIS extension:
```bash
CREATE EXTENSION postgis;
```
