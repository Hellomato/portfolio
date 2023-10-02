-- Creating customer_security Package
CREATE OR REPLACE PACKAGE customer_security AS

  FUNCTION get_hash (p_username  IN  VARCHAR2,
                     p_password  IN  VARCHAR2)
    RETURN VARCHAR2;
    
  PROCEDURE add_customer (p_username  IN  VARCHAR2,
                      	p_password  IN  VARCHAR2,
			p_forename IN VARCHAR2,
			p_surname IN VARCHAR2);

  PROCEDURE change_password (p_username      IN  VARCHAR2,
                             p_old_password  IN  VARCHAR2,
                             p_new_password  IN  VARCHAR2);
END;
/
-- Creating package body to define actual operations
CREATE OR REPLACE PACKAGE BODY customer_security AS

  FUNCTION get_hash (p_username  IN  VARCHAR2,
                     p_password  IN  VARCHAR2)
    RETURN VARCHAR2 AS
    l_salt VARCHAR2(30) := 'SaltySaltness';
  BEGIN
    RETURN DBMS_OBFUSCATION_TOOLKIT.MD5(
    	input_string => UPPER(p_username) || l_salt || UPPER(p_password));
  END;

  PROCEDURE add_customer (p_username  IN  VARCHAR2,
                      p_password  IN  VARCHAR2,
			p_forename IN VARCHAR2,
			p_surname IN VARCHAR2) AS
  BEGIN
    IF UPPER(SUBSTR(p_username, 1, 2)) = 'CU' THEN
      INSERT INTO CUSTOMERS (
        customer_id,
        username,
        password,
	customer_forename,
	customer_surname
      )
      VALUES (
        CUSTOMER_ID_SEQ.NEXTVAL,
        UPPER(p_username),
        get_hash(p_username, p_password),
	UPPER(p_forename ),
	UPPER(p_surname)
      );
    ELSE
	RAISE_APPLICATION_ERROR(-20000, 'Invalid username/password.');
    END IF;
    COMMIT;
  END;
   
  PROCEDURE change_password (p_username      IN  VARCHAR2,
                             p_old_password  IN  VARCHAR2,
                             p_new_password  IN  VARCHAR2) AS
    v_rowid  ROWID;
  BEGIN
    SELECT rowid
    INTO   v_rowid
    FROM   customers
    WHERE  username = UPPER(p_username)
    AND    password = get_hash(p_username, p_old_password)
    FOR UPDATE;
    
    UPDATE customers
    SET    password = get_hash(p_username, p_new_password)
    WHERE  rowid    = v_rowid;
    
    COMMIT;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_APPLICATION_ERROR(-20000, 'Invalid username/password.');
  END;
END;
