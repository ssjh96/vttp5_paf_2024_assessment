-- Write your Task 1 answers in this file

-- drop the database if exists
drop database if exists bedandbreakfast;

-- create the database
-- create database if not exists bedandbreakfast; -- already have drop database if exists
create database bedandbreakfast;

-- select the database
use bedandbreakfast;

-- create one or more tables
-- select "Creating RSVP table..." as msg; // cmdline
SELECT "CREATING USERS...";
create table users (
    email varchar(128), -- this is the PK
    name varchar(128),

    constraint pk_email primary key(email)
);

SELECT "CREATING BOOKINGS...";
create table bookings (
    booking_id char(8), -- this is the PK
    listing_id varchar(20),
    duration int,
    email varchar(128), -- fk

    constraint pk_booking_id primary key (booking_id),
    constraint fk_email foreign key(email) references users(email)
);

SELECT "CREATING REVIEWS...";
create table reviews (
    id int auto_increment, -- this is the PK
    date timestamp DEFAULT CURRENT_TIMESTAMP,
    listing_id varchar(20), 
    reviewer_name varchar(64),
    comments text, 

    constraint pk_id primary key (id)
);

-- Insert data from users.csv
SELECT "INSERTING INTO USERS...";
INSERT INTO USERS(email, name)
    VALUES 
        ("fred@gmail.com", "Fred Flintstone"),
        ("barney@gmail.com", "Barney Rubble"),
        ("fry@planetexpress.com", "Philip J Fry"),
        ("hlmer@gmail.com", "Homer Simpson");


-- Grant fred access to the DB
SELECT "GRANTING ALL PRIVILEGES TO FRED..";
GRANT ALL PRIVILEGES ON bedandbreakfast.* TO 'fred'@'%';

-- Apply changes to privileges
FLUSH PRIVILEGES;





