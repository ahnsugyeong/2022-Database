
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
START TRANSACTION;
USE madang;
SELECT SUM(price) 총액
FROM   Book;


SELECT SUM(price) 총액
FROM   Book;

/* 앞의 결과와 같음 */
 
COMMIT;

