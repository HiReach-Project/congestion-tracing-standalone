create table trace
(
    id bigint primary key,
    company_access_key varchar not null,
    device_id varchar not null,
    updated_at timestamp default timezone('utc', now()),
    location geography(POINT, 4326) not null
);
create sequence trace_seq;

create table company
(
    id int primary key,
    name varchar not null,
    access_key varchar not null
);
create sequence company_seq;

