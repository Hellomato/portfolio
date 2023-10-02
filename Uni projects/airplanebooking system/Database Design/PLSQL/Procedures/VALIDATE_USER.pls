create or replace PROCEDURE validate_user
(p_username  IN  VARCHAR2,
 p_password  IN  VARCHAR2,
 r_value OUT VARCHAR2)

IS

BEGIN

    /* Compare the first two characters of the username input against the string 'CU', the prefix for a customer account */
    IF UPPER(SUBSTR(p_username, 1, 2)) = 'CU' THEN
        SELECT '1' INTO r_value
        FROM   customers
        WHERE  username = UPPER(p_username)
        AND    password = STAFF_USER_SECURITY.get_hash(p_username, p_password);
        
    /* If account is not a customer account, search the staff_users table */
    ELSE
        SELECT '1' INTO r_value
        FROM   staff_users
        WHERE  username = UPPER(p_username)
        AND    password = STAFF_USER_SECURITY.get_hash(p_username, p_password);
    END IF;

EXCEPTION

    /* If no data is found, return r_value = 0 */
    WHEN NO_DATA_FOUND THEN
      r_value := '0';

END;