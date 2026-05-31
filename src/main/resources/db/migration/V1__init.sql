-- user
CREATE TABLE USERS (
	id varchar(255) PRIMARY KEY,
	username varchar(255) not null UNIQUE
);

INSERT INTO USERS (id, username) VALUES ('81150016-8501-4b97-9168-01113e21d8a5', 'User 001');
INSERT INTO USERS (id, username) VALUES ('d891323f-a3ad-4a95-b340-2e1c8aa8d1bd', 'User 002');

-- favorite_location
CREATE TABLE FAVORITE_LOCATIONS (
	id varchar(255) PRIMARY KEY,
	user_id varchar(255) not null,
	given_name varchar(255) not null,
	given_location varchar(255) not null,
	longitude double not null,
	latitude double not null,
	elevation float not null,
	nearest_airport varchar(255) not null,
	nearest_airport_longitude double not null,
	nearest_airport_latitude double not null,
	nearest_airport_elevation float not null
);

INSERT INTO FAVORITE_LOCATIONS (id, user_id, given_name, given_location, longitude, latitude, elevation, nearest_airport, nearest_airport_longitude, nearest_airport_latitude, nearest_airport_elevation) VALUES ('c5b38625-7eed-4705-858d-c685f18ed47d', '81150016-8501-4b97-9168-01113e21d8a5', 'Linz', 'Vienna, Austria', 16.37208, 48.20849, 171.0, 'LOWW', 16.5697, 48.110298, 183.0);
