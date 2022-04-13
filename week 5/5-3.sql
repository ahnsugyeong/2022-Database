/* week 5-2 */

/* 부속 질의 (SUBQUERY) */

SELECT * FROM book;

/* 3-28 가장 비싼 도서의 이름을 보이시오. */
SELECT bookname, price
FROM Book
WHERE price = (SELECT MAX(price) FROM Book);

/* 3-29 도서를 구매한 적이 있는 고객의 이름을 검색하시오. */
SELECT name
FROM Customer
WHERE custid IN (SELECT custid FROM Orders);

/* 대한미디어에서 출판한 도서를 구매한 고객의 이름읇 보이시오. */

SELECT name, bookname
FROM Customer c, Book b, Orders o
WHERE c.custid = o.custid and b.bookid = o.bookid and b.publisher = '대한미디어';

SELECT name, bookname	/* 부속 질의 */
FROM Customer c, Book b, Orders o
WHERE c.custid = o.custid and b.bookid = o.bookid and b.publisher = '대한미디어' and
	  c.custid IN (SELECT custid FROM Orders WHERE bookid IN (SELECT bookid FROM Book WHERE publisher='대한미디어'));
      

/* 3-31 출판사별로 출판사의 평균 도서 가격보다 비싼 도서를 구하시오 */

SELECT b1.publisher, b1.bookname
FROM Book b1
WHERE b1.price > (SELECT avg(b2.price)
				  FROM Book b2
                  WHERE b2.publisher = b1.publisher);
                  
/* 합집합 */
SELECT name
FROM Customer
WHERE address LIKE '대한민국%'
UNION
SELECT name
FROM Customer
WHERE custid IN (SELECT custid FROM Orders);



