-- MySQL Script generated by MySQL Workbench
-- Fri Sep 25 22:15:25 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema repairagency
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema repairagency
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `repairagency` DEFAULT CHARACTER SET utf8 ;
USE `repairagency` ;

-- -----------------------------------------------------
-- Table `repairagency`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`clients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `client_name` VARCHAR(100) NOT NULL,
  `wallet_count` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `contact_phone` VARCHAR(45) NULL,
  `locale` ENUM('en', 'ua', 'ru') NOT NULL,
  `registration_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`managers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`managers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `person_name` VARCHAR(100) NOT NULL,
  `contact_phone` VARCHAR(45) NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `displayed_name_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`wallets_story`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`wallets_story` (
  `entry_id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `entry_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `debet` DECIMAL(10,2) NULL,
  `credit` DECIMAL(10,2) NULL,
  `changed_by_manager_id` INT NOT NULL,
  PRIMARY KEY (`entry_id`),
  INDEX `fk_manager_id_idx` (`changed_by_manager_id` ASC) VISIBLE,
  INDEX `fk_client_id_idx` (`client_id` ASC) VISIBLE,
  CONSTRAINT `fk_changed_by_manager_id`
    FOREIGN KEY (`changed_by_manager_id`)
    REFERENCES `repairagency`.`managers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `repairagency`.`clients` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`masters`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`masters` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `person_name` VARCHAR(100) NOT NULL,
  `contact_phone` VARCHAR(45) NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `device` VARCHAR(45) NOT NULL,
  `description` VARCHAR(1000) NOT NULL,
  `master_id` INT NOT NULL,
  `manager_id` INT NOT NULL,
  `cost` DECIMAL(10,2) NOT NULL,
  `status` ENUM('NEW', 'WAIT_FOR_PAY', 'PAYED', 'CANSELED', 'WORKING', 'MADE') NOT NULL,
  `created_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_manager_id_idx` (`manager_id` ASC) VISIBLE,
  INDEX `fk_master_id_idx` (`master_id` ASC) VISIBLE,
  CONSTRAINT `fk_master_id`
    FOREIGN KEY (`master_id`)
    REFERENCES `repairagency`.`masters` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_manager_id`
    FOREIGN KEY (`manager_id`)
    REFERENCES `repairagency`.`managers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`reviews` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `posted_by_client_id` INT NOT NULL,
  `about_master_id` INT NOT NULL,
  `content` VARCHAR(1000) NOT NULL,
  `created_data` DATE NOT NULL,
  `about_order_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_created_by_client_id_idx` (`posted_by_client_id` ASC) VISIBLE,
  INDEX `fk_about_order_id_idx` (`about_order_id` ASC) VISIBLE,
  INDEX `fk_about_master_id_idx` (`about_master_id` ASC) VISIBLE,
  CONSTRAINT `fk_created_by_client_id`
    FOREIGN KEY (`posted_by_client_id`)
    REFERENCES `repairagency`.`clients` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_about_master_id`
    FOREIGN KEY (`about_master_id`)
    REFERENCES `repairagency`.`masters` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_about_order_id`
    FOREIGN KEY (`about_order_id`)
    REFERENCES `repairagency`.`orders` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`administration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`administration` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `person_name` VARCHAR(100) NOT NULL,
  `contact_phone` VARCHAR(45) NOT NULL,
  `role` ENUM('DIRECTOR', 'ADMINISTRATOR') NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;