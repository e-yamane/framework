
-----------------------------------------------------------------------------
-- UNIT
-----------------------------------------------------------------------------
DROP TABLE UNIT;


CREATE TABLE UNIT
(
    ID int8 NOT NULL,
    NAME varchar (32) NOT NULL,
    DESCRIPTION varchar (64),
      -- REFERENCES UNIT (ID)
    BASE_UNIT_ID int8 NOT NULL,
    RATE varchar (16) NOT NULL,
    SCALE integer NOT NULL,
    VERSION int8 NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT unq_UNIT_1 UNIQUE (NAME)
);


-----------------------------------------------------------------------------
-- numbering
-----------------------------------------------------------------------------
DROP TABLE numbering;


CREATE TABLE numbering
(
    id varchar (128) NOT NULL,
    next_number int8 NOT NULL,
    PRIMARY KEY (id)
);

