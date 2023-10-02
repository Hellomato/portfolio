CREATE OR REPLACE TRIGGER PRCS251J.TRG_customer BEFORE INSERT OR 
    UPDATE OF customer_forename, customer_surname , username ON customers FOR EACH ROW
    
BEGIN
    IF INSERTING THEN
        IF :NEW.customer_id IS NULL OR :NEW.customer_id = 0 THEN
            SELECT customer_id_seq.nextval
            INTO :NEW.customer_id
            FROM sys.dual;
        END IF;
        
        :NEW.customer_forename := INITCAP(:NEW.customer_forename);
        :NEW.customer_surname := INITCAP(:NEW.customer_surname);
        END IF;
 END;  