CREATE TABLE STATIONS
(
    station_id NUMBER,
    station_name VARCHAR(255) NOT NULL,
    post_code VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    platform_count NUMBER NOT NULL,
    station_status VARCHAR(255) NOT NULL,
    
    CONSTRAINT stations_pk PRIMARY KEY (station_id)
);