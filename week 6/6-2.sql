/* week 6-2 */

SELECT custid '고객번호', ROUND(SUM(saleprice)/COUNT(*), -2) '평균금액'
FROM Orders
GROUP BY custid;

/* 실제로 바뀌는 게 아니라 display 할 때만 바뀜 */
SELECT bookid, REPLACE(bookname, '야구', '메롱') bookname, publisher, price
FROM Book;

SELECT * FROM Book;

SELECT SYSDATE(), DATE_FORMAT(SYSDATE(), '%Y/%m/%d %M %h:%s') 'SYSDATE_1';

/* 집계 함수 계산 시 NULL은 집계에서 빠짐 */

SELECT *
FROM Customer
WHERE phone IS NOT NULL;

SELECT name '이름', IFNULL(phone, '연락처 없음') '전화번호'
FROM Customer;

SET @seq := 0;
SELECT (@seq := @seq + 1) '순번', custid, name, phone
FROM Customer
WHERE @seq < 2;


/* 스칼라 부속 질의 */
SELECT custid, (SELECT name
				FROM Customer cs
                WHERE cs.custid = od.custid) 'name', SUM(saleprice) 'total'
FROM Orders od
GROUP BY od.custid;

SELECT od.custid, sum(saleprice)
FROM Orders od
GROUP BY od.custid;

/* join 버전 */
SELECT od.custid, cs.name, sum(saleprice)
FROM Orders od, Customer cs
WHERE cs.custid = od.custid
GROUP BY od.custid;


/* inline view */
SELECT cs.name, SUM(od.saleprice) 'total'
FROM (SELECT custid, name	/* 5 + 2 * 10 = 25 -> 더 효율적. but 조심해서 다루기 !! */
	  FROM Customer
      WHERE custid <= 2) cs, Orders od
WHERE cs.custid = od.custid
GROUP BY cs.name;

/* join 버전 */
SELECT cs.name, SUM(od.saleprice)
FROM Customer cs, Orders od		/* 5 * 10 = 50 */
WHERE cs.custid = od.custid and cs.custid <= 2
GROUP BY cs.name;
