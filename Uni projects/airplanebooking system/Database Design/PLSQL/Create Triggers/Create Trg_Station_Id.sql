create or replace TRIGGER TRG_STATION_ID
    BEFORE INSERT OR UPDATE OF 
    STATION_ID
    ON STATIONS FOR EACH ROW
    BEGIN
    IF INSERTING THEN
        SELECT STATION_ID_seq.nextval
        INTO :NEW.STATION_ID
        FROM sys.dual;
    END IF;
 END;  