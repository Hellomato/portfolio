CREATE TABLE PRCS251J.staff_account
    (staff_id int,
        CONSTRAINT staff_id_pk
        PRIMARY KEY (staff_id),
    staff_role VARCHAR2(50)
        CONSTRAINT staff_role_nn NOT NULL ENABLE
            Check(staff_role = INITCAP(staff_role)),
    staff_forename VARCHAR2(50)
        CONSTRAINT staff_forename_nn NOT NULL ENABLE,
            Check(staff_forename = INITCAP(staff_forename)),
	staff_surname VARCHAR2(50)
        CONSTRAINT staff_surname_nn NOT NULL ENABLE,
            Check(staff_surname = INITCAP(staff_surname)),
    date_of_birth date
        CONSTRAINT date_of_birth_nn NOT NULL ENABLE,
    address_line Varchar2(50)
        CONSTRAINT address_line_nn NOT NULL ENABLE,
    post_code Varchar2(50)
        CONSTRAINT post_code_nn NOT NULl ENABLE
            Check(post_code = UPPER(post_code)),
    username VARCHAR2(16)
        CONSTRAINT username UNIQUE 
        CONSTRAINT username_nn NOT NULl ENABLE
            Check(username = LOWER(username))
);