create table company
(
    id int primary key,
    name varchar not null,
    access_key varchar not null
);
create sequence company_seq;

create table device_location
(
    device_id varchar(50) primary key,
    company_id int references company(id),
    updated_at timestamp default timezone('utc', now()),
    location_point geography(POINT, 4326) not null
);

create table device_location_history
(
    id bigint primary key,
    company_id int references company(id),
    device_id varchar(50),
    updated_at timestamp default timezone('utc', now()),
    location_point geography(POINT, 4326) not null
);
create sequence device_location_history_seq;



