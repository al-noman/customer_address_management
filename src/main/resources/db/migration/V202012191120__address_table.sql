CREATE TABLE address
(
    id char(36) NOT NULL,
    version int,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    email varchar(100) NOT NULL,
    date_of_birth date,
    street varchar(100),
    zip_code int,
    country varchar(100),
    CONSTRAINT address_pkey PRIMARY KEY (id),
    CONSTRAINT _____ERR_UNIQUE_EMAIL_EXISTS_____ UNIQUE (email)
)