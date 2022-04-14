/* week 6-1 */

SELECT b1.bookname
FROM Book b1
WHERE b1.price > (SELECT avg(b2.price)
				  FROM Book b2
                  WHERE b2.publisher = b1.publisher);
                  
select  * from book;

/* 주문 내역이 있는 고객의 이름과 주소 */
SELECT name, address
FROM Customer cs
WHERE EXISTS (SELECT *
			  FROM Orders od
              WHERE cs.custid = od.custid);

/* create table */

CREATE TABLE NewOrders(
orderid INTEGER,
custid INTEGER NOT NULL,
bookid INTEGER NOT NULL,
saleprice INTEGER,
orderdate DATE,
PRIMARY KEY (orderid)
/*FOREIGN KEY (custid) REFERENCES NewCustomer(custid) ON DELETE CASCADE*/
);

SELECT * from NewOrders;

DROP TABLE NewOrders;

/*
DROP -> TABLE의 구조&data 모두 삭제하는 명령
DELETE -> TABLE의 data만 삭제하는 명령
 */
 
 
/* 대량 삽입 */
SELECT * FROM imported_book;
 
INSERT INTO Book(bookid, bookname, price, publisher)
SELECT bookid, bookname, price, publisher
FROM Imported_book;

SELECT * FROM Book;

/* UPDATE 문 */
UPDATE Customer
SET address = '대한민국 부산'
WHERE custid = 5;

SELECT * FROM Customer;

/* DELETE 문 */
DELETE FROM Orders;

/* 내장 함수 */
SELECT ABS(-78), ABS(+78)
FROM DUAL;