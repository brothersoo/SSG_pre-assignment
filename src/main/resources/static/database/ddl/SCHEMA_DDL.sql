-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ecommerce
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ecommerce
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ecommerce` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ecommerce` ;

-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_product_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_product_group` (
  `ecommerce_product_group_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ecommerce_product_group_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_product` (
  `ecommerce_product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` INT NOT NULL,
  `stock` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ecommerce_product_group_id` BIGINT NOT NULL,
  PRIMARY KEY (`ecommerce_product_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `fk_ecommerce_product_ecommerce_product_group1_idx` (`ecommerce_product_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_ecommerce_product_ecommerce_product_group1`
    FOREIGN KEY (`ecommerce_product_group_id`)
    REFERENCES `ecommerce`.`ecommerce_product_group` (`ecommerce_product_group_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 47
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_user` (
  `ecommerce_user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`ecommerce_user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_cart_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_cart_product` (
  `ecommerce_cart_product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `quantity` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ecommerce_product_id` BIGINT NOT NULL,
  `ecommerce_user_id` BIGINT NOT NULL,
  PRIMARY KEY (`ecommerce_cart_product_id`),
  INDEX `fk_ecommerce_cart_product_ecommerce_product1_idx` (`ecommerce_product_id` ASC) VISIBLE,
  INDEX `fk_ecommerce_cart_product_ecommerce_user1_idx` (`ecommerce_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_ecommerce_cart_product_ecommerce_product1`
    FOREIGN KEY (`ecommerce_product_id`)
    REFERENCES `ecommerce`.`ecommerce_product` (`ecommerce_product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ecommerce_cart_product_ecommerce_user1`
    FOREIGN KEY (`ecommerce_user_id`)
    REFERENCES `ecommerce`.`ecommerce_user` (`ecommerce_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 163
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_order` (
  `ecommerce_order_id` BIGINT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ecommerce_user_id` BIGINT NOT NULL,
  PRIMARY KEY (`ecommerce_order_id`),
  INDEX `fk_order_user1_idx` (`ecommerce_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_user1`
    FOREIGN KEY (`ecommerce_user_id`)
    REFERENCES `ecommerce`.`ecommerce_user` (`ecommerce_user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 27
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_order_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_order_product` (
  `ecommerce_order_product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `price` INT NOT NULL,
  `quantity` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ecommerce_order_id` BIGINT NOT NULL,
  `ecommerce_product_id` BIGINT NOT NULL,
  PRIMARY KEY (`ecommerce_order_product_id`),
  INDEX `fk_ecommerce_order_product_ecommerce_order1_idx` (`ecommerce_order_id` ASC) VISIBLE,
  INDEX `fk_ecommerce_order_product_ecommerce_product1_idx` (`ecommerce_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_ecommerce_order_product_ecommerce_order1`
    FOREIGN KEY (`ecommerce_order_id`)
    REFERENCES `ecommerce`.`ecommerce_order` (`ecommerce_order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ecommerce_order_product_ecommerce_product1`
    FOREIGN KEY (`ecommerce_product_id`)
    REFERENCES `ecommerce`.`ecommerce_product` (`ecommerce_product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 41
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_privilege`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_privilege` (
  `ecommerce_privilege_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`ecommerce_privilege_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_role` (
  `ecommerce_role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`ecommerce_role_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_role_privilege`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_role_privilege` (
  `ecommerce_role_privilege_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ecommerce_role_id` BIGINT NOT NULL,
  `ecommerce_privilege_id` BIGINT NOT NULL,
  PRIMARY KEY (`ecommerce_role_privilege_id`),
  INDEX `fk_ecommerce_role_privilege_ecommerce_role1_idx` (`ecommerce_role_id` ASC) VISIBLE,
  INDEX `fk_ecommerce_role_privilege_ecommerce_privilege1_idx` (`ecommerce_privilege_id` ASC) VISIBLE,
  CONSTRAINT `fk_ecommerce_role_privilege_ecommerce_role1`
    FOREIGN KEY (`ecommerce_role_id`)
    REFERENCES `ecommerce`.`ecommerce_role` (`ecommerce_role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ecommerce_role_privilege_ecommerce_privilege1`
    FOREIGN KEY (`ecommerce_privilege_id`)
    REFERENCES `ecommerce`.`ecommerce_privilege` (`ecommerce_privilege_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce`.`ecommerce_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`ecommerce_user_role` (
  `ecommerce_user_role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `ecommerce_user_id` BIGINT NOT NULL,
  `ecommerce_role_id` BIGINT NOT NULL,
  PRIMARY KEY (`ecommerce_user_role_id`),
  INDEX `fk_ecommerce_user_role_ecommerce_user1_idx` (`ecommerce_user_id` ASC) VISIBLE,
  INDEX `fk_ecommerce_user_role_ecommerce_role1_idx` (`ecommerce_role_id` ASC) VISIBLE,
  CONSTRAINT `fk_ecommerce_user_role_ecommerce_user1`
    FOREIGN KEY (`ecommerce_user_id`)
    REFERENCES `ecommerce`.`ecommerce_user` (`ecommerce_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ecommerce_user_role_ecommerce_role1`
    FOREIGN KEY (`ecommerce_role_id`)
    REFERENCES `ecommerce`.`ecommerce_role` (`ecommerce_role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
