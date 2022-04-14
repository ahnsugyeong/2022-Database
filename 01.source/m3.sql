-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `Book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Book` ;

CREATE TABLE IF NOT EXISTS `Book` (
  `bookid` INT NOT NULL,
  `bookname` VARCHAR(45) NULL,
  `publisher` VARCHAR(45) NULL,
  `price` INT NULL,
  PRIMARY KEY (`bookid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `customer` ;

CREATE TABLE IF NOT EXISTS `customer` (
  `custid` INT NOT NULL,
  `bookid` INT NULL,
  `saleprice` INT NOT NULL,
  `orderdate` DATE NULL,
  PRIMARY KEY (`custid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `orders` ;

CREATE TABLE IF NOT EXISTS `orders` (
  `orderid` INT NOT NULL,
  `custid` INT NULL,
  `bookid` INT NULL,
  `saleprice` INT NULL,
  `orderdate` DATE NULL,
  `customer_custid` INT NOT NULL,
  `Book_bookid` INT NOT NULL,
  PRIMARY KEY (`orderid`),
  INDEX `fk_orders_customer_idx` (`customer_custid` ASC) VISIBLE,
  INDEX `fk_orders_Book1_idx` (`Book_bookid` ASC) VISIBLE,
  CONSTRAINT `fk_orders_customer`
    FOREIGN KEY (`customer_custid`)
    REFERENCES `customer` (`custid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_Book1`
    FOREIGN KEY (`Book_bookid`)
    REFERENCES `Book` (`bookid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
