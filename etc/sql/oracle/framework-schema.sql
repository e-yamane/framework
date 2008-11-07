
-----------------------------------------------------------------------------
-- UNIT
-----------------------------------------------------------------------------
DROP TABLE UNIT CASCADE CONSTRAINTS PURGE;

CREATE TABLE UNIT
(
  ID NUMBER (20, 0) NOT NULL,
  NAME VARCHAR2 (32) NOT NULL,
  DESCRIPTION VARCHAR2 (64),
  BASE_UNIT_ID NUMBER (20, 0) NOT NULL,
  RATE VARCHAR2 (16) NOT NULL,
  SCALE NUMBER NOT NULL,
  VERSION NUMBER (20, 0) NOT NULL,
    CONSTRAINT unq_UNIT_1 UNIQUE (NAME)
);
ALTER TABLE UNIT
    ADD CONSTRAINT UNIT_PK
PRIMARY KEY (ID);



-----------------------------------------------------------------------------
-- numbering
-----------------------------------------------------------------------------
DROP TABLE numbering CASCADE CONSTRAINTS PURGE;

CREATE TABLE numbering
(
  id VARCHAR2 (128) NOT NULL,
  next_number NUMBER (20, 0) NOT NULL
);
ALTER TABLE numbering
    ADD CONSTRAINT numbering_PK
PRIMARY KEY (id);


