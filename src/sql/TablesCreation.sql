CREATE TABLE INNER_OBJ(
    IN_ID NUMBER NOT NULL PRIMARY KEY,
    IN_DATA VARCHAR(20)
    );
CREATE TABLE MODEL
    (MO_ID NUMBER PRIMARY KEY,
    DATEOFREG DATE,
    INNER_ID NUMBER,
    CONSTRAINT INNER_OBJECT FOREIGN KEY (INNER_ID) REFERENCES INNER_OBJ (IN_ID) ON DELETE CASCADE);
INSERT INTO INNER_OBJ VALUES (1,'StringData');
INSERT INTO MODEL VALUES (1, SYSDATE, 1);