
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

