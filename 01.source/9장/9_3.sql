# drop user mdguest@localhost;
# drop user mdguest2@localhost;

use mysql;

CREATE USER mdguest2@localhost  IDENTIFIED BY 'mdguest2';
   # mdguest2 사용자 생성은 User 테이블을 확인해본다.
SELECT * FROM User WHERE User LIKE 'mdguest2';
