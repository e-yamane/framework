
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


