create table links (
    id uuid default RANDOM_UUID() primary key,
    title varchar(255) not null,
    url varchar(255) not null,
    trip_id uuid,
    foreign key(trip_id) references trips(id) on delete cascade
);