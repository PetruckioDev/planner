create table participants (
    id uuid default RANDOM_UUID() primary key,
    participant_name varchar(255) not null,
    participant_email varchar(255) not null,
    is_confirmed boolean not null,
    trip_id uuid,
    foreign key (trip_id) references trips(id)
);