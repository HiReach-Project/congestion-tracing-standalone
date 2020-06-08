create table device_location
(
    device_id varchar(50) primary key,
    company_access_key varchar(50) not null,
    updated_at timestamp default timezone('utc', now()),
    location_point geography(POINT, 4326) not null
);

create table company
(
    id int primary key,
    name varchar not null,
    access_key varchar not null
);
create sequence company_seq;

