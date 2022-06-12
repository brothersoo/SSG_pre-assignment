-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ssg_shopping_cart
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ssg_shopping_cart
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssg_shopping_cart` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ssg_shopping_cart` ;

-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_user` (
  `ssg_user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(64) NULL DEFAULT NULL,
  `username` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`ssg_user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_product_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_product_group` (
  `ssg_product_group_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ssg_product_group_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_product` (
  `ssg_product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` INT NOT NULL,
  `stock` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ssg_product_group_id` BIGINT NOT NULL,
  PRIMARY KEY (`ssg_product_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `fk_product_group_idx` (`ssg_product_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_product_group`
    FOREIGN KEY (`ssg_product_group_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_product_group` (`ssg_product_group_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_cart_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_cart_product` (
  `ssg_cart_product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `quantity` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ssg_user_id` BIGINT NOT NULL,
  `ssg_product_id` BIGINT NOT NULL,
  PRIMARY KEY (`ssg_cart_product_id`),
  INDEX `fk_cart_user1_idx` (`ssg_user_id` ASC) VISIBLE,
  INDEX `fk_ssg_cart_product_ssg_product1_idx` (`ssg_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_user1`
    FOREIGN KEY (`ssg_user_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_user` (`ssg_user_id`),
  CONSTRAINT `fk_ssg_cart_product_ssg_product1`
    FOREIGN KEY (`ssg_product_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_product` (`ssg_product_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 123
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_order` (
  `ssg_order_id` BIGINT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ssg_user_id` BIGINT NOT NULL,
  PRIMARY KEY (`ssg_order_id`),
  INDEX `fk_order_user1_idx` (`ssg_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_user1`
    FOREIGN KEY (`ssg_user_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_user` (`ssg_user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_order_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_order_product` (
  `ssg_order_product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `price` INT NOT NULL,
  `quantity` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ssg_order_id` BIGINT NOT NULL,
  `ssg_product_id` BIGINT NOT NULL,
  PRIMARY KEY (`ssg_order_product_id`),
  INDEX `fk_order_product_order1_idx` (`ssg_order_id` ASC) VISIBLE,
  INDEX `fk_ssg_order_product_ssg_product1_idx` (`ssg_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_product_order1`
    FOREIGN KEY (`ssg_order_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_order` (`ssg_order_id`),
  CONSTRAINT `fk_ssg_order_product_ssg_product1`
    FOREIGN KEY (`ssg_product_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_product` (`ssg_product_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_privilege`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_privilege` (
  `ssg_privilege_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`ssg_privilege_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_role` (
  `ssg_role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`ssg_role_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_role_privilege`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_role_privilege` (
  `ssg_role_privilege_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ssg_role_id` BIGINT NOT NULL,
  `ssg_privilege_id` BIGINT NOT NULL,
  PRIMARY KEY (`ssg_role_privilege_id`),
  INDEX `fk_ssg_role_privilege_ssg_role1_idx` (`ssg_role_id` ASC) VISIBLE,
  INDEX `fk_ssg_role_privilege_ssg_privilege1_idx` (`ssg_privilege_id` ASC) VISIBLE,
  CONSTRAINT `fk_ssg_role_privilege_ssg_privilege1`
    FOREIGN KEY (`ssg_privilege_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_privilege` (`ssg_privilege_id`),
  CONSTRAINT `fk_ssg_role_privilege_ssg_role1`
    FOREIGN KEY (`ssg_role_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_role` (`ssg_role_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssg_shopping_cart`.`ssg_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssg_shopping_cart`.`ssg_user_role` (
  `ssg_user_role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ssg_user_id` BIGINT NOT NULL,
  `ssg_role_id` BIGINT NOT NULL,
  PRIMARY KEY (`ssg_user_role_id`),
  INDEX `fk_ssg_user_role_ssg_user1_idx` (`ssg_user_id` ASC) VISIBLE,
  INDEX `fk_ssg_user_role_ssg_role1_idx` (`ssg_role_id` ASC) VISIBLE,
  CONSTRAINT `fk_ssg_user_role_ssg_role1`
    FOREIGN KEY (`ssg_role_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_role` (`ssg_role_id`),
  CONSTRAINT `fk_ssg_user_role_ssg_user1`
    FOREIGN KEY (`ssg_user_id`)
    REFERENCES `ssg_shopping_cart`.`ssg_user` (`ssg_user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
