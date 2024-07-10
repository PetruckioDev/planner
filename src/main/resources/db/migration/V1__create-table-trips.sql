create table trips (
    id UUID default RANDOM_UUID() primary key,
    destination varchar(255) not null,
    starts_at timestamp not null,
    ends_at timestamp not null,
    is_confirmed boolean not null,
    owner_name varchar(255) not null,
    owner_email varchar(255) not null
);