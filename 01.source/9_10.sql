
GRANT SELECT ON madang.Book TO mdguest@localhost; 

GRANT SELECT, UPDATE ON madang.Customer TO mdguest@localhost WITH GRANT OPTION;

-- mdgest@'%' 에 대한 revoke 이므로 오류 발생
REVOKE SELECT ON madang.Book FROM mdguest;

-- 정상 
REVOKE SELECT ON madang.Book FROM mdguest@localhost;

REVOKE SELECT ON madang.Customer FROM mdguest@localhost;

REVOKE UPDATE ON madang.Customer FROM mdguest@localhost;

REVOKE SELECT, UPDATE ON madang.Customer FROM mdguest@localhost;

-- mdguest 에 대한 모든 권한을 회수
revoke all PRIVILEGES, grant option from mdguest@localhost;

-- mdguest2 에 대한 모든 권한을 회수
revoke all PRIVILEGES, grant option from mdguest2@localhost;