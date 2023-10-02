CREATE OR REPLACE TRIGGER PRCS251J.TRG_staff BEFORE INSERT OR 
    UPDATE OF staff_role, staff_forename, staff_surname ,address_line, 
		post_code, username ON staff_account FOR EACH ROW
    
BEGIN
    IF INSERTING THEN
        IF :NEW.staff_id IS NULL THEN
            SELECT staff_id_seq.nextval
            INTO :NEW.staff_id
            FROM sys.dual;
        END IF;
        
        :NEW.staff_forename := INITCAP(:NEW.staff_forename);
        :NEW.staff_surname := INITCAP(:NEW.staff_surname);
        
        IF (:NEW.date_of_birth + NUMTOYMINTERVAL(16,'YEAR') > SYSDATE) 
        THEN
            RAISE_APPLICATION_ERROR(-20000, 'Employees have to be 16 or older to work here');
        END IF;
    END IF;
 END;  