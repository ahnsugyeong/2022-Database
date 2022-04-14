
DROP DATABASE IF EXISTS  mydb;
create database mydb;
grant all privileges on mydb.* to madang@localhost with grant option;
commit;