CREATE TABLE PRCS251J.customers
    (customer_id int,
        CONSTRAINT customer_id_pk
        PRIMARY KEY (customer_id),
    customer_forename VARCHAR2(50)
        CONSTRAINT customer_forename_nn NOT NULL ENABLE,
            Check(customer_forename = INITCAP(customer_forename)),
	customer_surname VARCHAR2(50)
        CONSTRAINT customer_surname_nn NOT NULL ENABLE,
            Check(customer_surname = INITCAP(customer_surname)),
    username VARCHAR2(16)
        CONSTRAINT username UNIQUE 
        CONSTRAINT username_nn NOT NULl ENABLE
            Check(username = LOWER(username))
); 
