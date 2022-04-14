/* week 6-1 */

/*
[중첩질의]
SELECT ...
FROM ...
WHERE
속성 > (부속질의...)
속성 > ALL (부속질의...)
속성 IN (부속질의...)
EXISTS (부속질의...)
*/

/* 4-15 평균 주문금액 이하의 주문에 대해서 주문번호와 금액을 보이시오. */
SELECT orderid, bookname, saleprice
FROM Orders, Book
WHERE Orders.bookid = Book.bookㅁid and saleprice <= (SELECT AVG(saleprice)
					FROM Orders);
                    
/* 4-16 각 고객의 평균 주문금액보다 큰 금액의 주문 내역에 대해서 주문번호, 고객번호, 금액을 보이시오 */

SELECT custid, avg(saleprice) FROM Orders GROUP BY custid;

SELECT orderid, custid, saleprice
FROM Orders od
WHERE saleprice > (SELECT AVG(saleprice)
				   FROM Orders so
                   WHERE od.custid = so.custid);
                   
/* ALL, ANY(SOME) */

SELECT saleprice FROM Orders WHERE custid = '3';

SELECT orderid, saleprice
FROM Orders
WHERE saleprice > ALL (SELECT saleprice FROM Orders WHERE custid = '3');
/* = */
SELECT orderid, saleprice
FROM Orders
WHERE saleprice > (SELECT max(saleprice) FROM Orders WHERE custid = '3');

SELECT orderid, saleprice
FROM Orders
WHERE saleprice > SOME (SELECT saleprice FROM Orders WHERE custid = '3');
/* = */
SELECT orderid, saleprice
FROM Orders
WHERE saleprice > (SELECT min(saleprice) FROM Orders WHERE custid = '3');
              
              
/* IN */
SELECT SUM(saleprice) 'total'
FROM Orders
WHERE custid IN (SELECT custid FROM Customer WHERE address LIKE '%대한민국%');

SELECT SUM(saleprice)
FROM Orders od, Customer cs
WHERE od.custid = cs.custid and address LIKE '%대한민국%';

/* NOT IN -> 안 되는 경우가 있으니 주의 */




/* VIEW */
CREATE VIEW vw_Book
AS SELECT *
FROM Book
WHERE bookname LIKE '%축구%';

SELECT * FROM vw_book;

