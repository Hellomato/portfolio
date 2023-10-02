CREATE TABLE JOURNEYS
(
    journey_id NUMBER,
    start_location_id NUMBER NOT NULL,
    end_location_id NUMBER NOT NULL,
    departure_date_time TIMESTAMP NOT NULL,
    arrival_date_time TIMESTAMP NOT NULL,
    journey_comments VARCHAR(255),
	journey_price NUMBER NOT NULL,
    
    CONSTRAINT journey_pk PRIMARY KEY (journey_id),
    CONSTRAINT fk_departure_station
    FOREIGN KEY (start_location_id)
    REFERENCES STATIONS(station_id) ON DELETE CASCADE,
    CONSTRAINT fk_arrival_station
    FOREIGN KEY (end_location_id)
    REFERENCES STATIONS(station_id) ON DELETE CASCADE
);
