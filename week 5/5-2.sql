/* week 5-2 */

SELECT * FROM Orders;

SELECT custid, COUNT(*) AS 도서수량
FROM Orders
WHERE saleprice >= 8000
GROUP BY custid
HAVING count(*) >= 2;	/* HAVING은 GROUP BY 따라다님. HAVING이 먼저 나오면 x. */

SELECT custid, bookid, SUM(saleprice), count(*)
FROM Orders
GROUP BY custid;

SELECT *
FROM Customer, Orders;	/* 곱집합 */

SELECT *
FROM Customer, Orders
WHERE Customer.custid = Orders.custid;	/* 동등조인 */

SELECT name, saleprice
FROM Customer, Orders
WHERE Customer.custid = Orders.custid;

SELECT name, SUM(saleprice), count(*)
FROM Customer, Orders
WHERE Customer.custid = Orders.custid
GROUP BY Customer.name
ORDER BY Customer.name;

SELECT * FROM Book;
SELECT * FROM Orders;

SELECT name, bookname, Book.price
FROM Customer, Orders, Book
WHERE Customer.custid = Orders.custid AND Orders.bookid = Book.bookid;

/* 외부조인 (-> 주문하지 않았던 박세리의 주문내역도 나옴)*/
SELECT Customer.name, saleprice
FROM CUstomer LEFT OUTER JOIN Orders
ON Customer.custid = Orders.custid;

SELECT Customer.name, saleprice
FROM CUstomer RIGHT OUTER JOIN Orders
ON Customer.custid = Orders.custid;

/* 동등조인 */
SELECT Customer.name, saleprice
FROM Customer, Orders
WHERE Customer.custid = Orders.custid;

SELECT Customer.name, saleprice
FROM Customer JOIN Orders
ON Customer.custid = Orders.custid;