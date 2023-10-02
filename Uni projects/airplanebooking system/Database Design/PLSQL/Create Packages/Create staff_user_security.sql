create or replace PACKAGE BODY staff_user_security AS

  FUNCTION get_hash (p_username  IN  VARCHAR2,
                     p_password  IN  VARCHAR2)
    RETURN VARCHAR2 AS
    l_salt VARCHAR2(30) := 'PutYourSaltHere';
    BEGIN
        RETURN DBMS_OBFUSCATION_TOOLKIT.MD5(
            input_string => UPPER(p_username) || l_salt || UPPER(p_password));
    END;

  PROCEDURE add_staff_user  (p_username  IN  VARCHAR2,        
                             p_password  IN  VARCHAR2) AS
  BEGIN
    INSERT INTO staff_users (
      staff_id,
      username,
      password
    )
    VALUES (
      STAFF_USERS_ID_SEQ.NEXTVAL,
      UPPER(p_username),
      get_hash(p_username, p_password)
    );

    COMMIT;
  END;

  PROCEDURE change_password (p_username      IN  VARCHAR2,
                             p_old_password  IN  VARCHAR2,
                             p_new_password  IN  VARCHAR2) AS
    v_rowid  ROWID;
  BEGIN
    SELECT rowid
    INTO   v_rowid
    FROM   staff_users
    WHERE  username = UPPER(p_username)
    AND    password = get_hash(p_username, p_old_password)
    FOR UPDATE;

    UPDATE staff_users
    SET    password = get_hash(p_username, p_new_password)
    WHERE  rowid    = v_rowid;

    COMMIT;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_APPLICATION_ERROR(-20000, 'Invalid username/password.');
  END;

  PROCEDURE valid_user (p_username  IN  VARCHAR2,
                        p_password  IN  VARCHAR2) AS
    v_dummy  VARCHAR2(1);
  BEGIN
    SELECT '1'
    INTO   v_dummy
    FROM   staff_users
    WHERE  username = UPPER(p_username)
    AND    password = get_hash(p_username, p_password);
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_APPLICATION_ERROR(-20000, 'Invalid username/password.');
  END;

  FUNCTION valid_user (p_username  IN  VARCHAR2,
                       p_password  IN  VARCHAR2) 
                       
    RETURN BOOLEAN AS
  BEGIN
    valid_user(p_username, p_password);
    RETURN TRUE;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN FALSE;
  END;

END;