REST-SERVER

Работаем с БД:


IN SYSTEM:
CREATE USER RestTest IDENTIFIED BY root;
GRANT CONNECT, RESOURCE TO RestTest;


IN RESTTEST:
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



Для проверки мы будем выводить следующую информацию:
select IN_ID "IN_ID", IN_DATA "IN_DATA", MO_ID "M_ID", DATEOFREG "M_DATE" from MODEL M join INNER_OBJ I on M.INNER_ID = I.IN_ID where M.MO_ID = 1
Можно проверить что она выводит в SqlDeveloper

Запускаем таску RUN FOREST RUN
Ждём появления надписи типа:
2017-04-09 15:20:52.035  INFO 6756 --- [main] projectpackage.Application: Started Application in 36.142 seconds (JVM running for 51.925)

Пиздуем в браузере на
http://localhost:8080/fuck/gimme
И получаем свой JSON.

Или пиздуем на:
http://localhost:8080/fuck/gimme?model_id=1
И опять получаем свой JSON.

Добавляем новые модели  и иннер обджект и  получаем их из базы меняя model_id в последней ссылке.