-- MySQL Script generated by MySQL Workbench
-- Fri Oct  9 10:59:40 2020
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
-- Table `repairagency`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` ENUM('DIRECTOR', 'CLIENT', 'ADMINISTRATOR', 'MANAGER', 'MASTER') NOT NULL,
  `description` VARCHAR(1000) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`locales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`locales` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` ENUM('UA', 'US', 'EN', 'RU') NOT NULL,
  `description` VARCHAR(1000) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(130) NOT NULL,
  `password` VARCHAR(25) NOT NULL,
  `person_name` VARCHAR(45) NOT NULL,
  `role_id` INT NOT NULL,
  `photo_path` VARCHAR(45) NOT NULL DEFAULT 'img/users/userIcon.png',
  `contact_phone` VARCHAR(45) NULL,
  `locale_id` INT NOT NULL,
  `registration_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_role_id_idx` (`role_id` ASC) VISIBLE,
  INDEX `fk_locale_id_idx` (`locale_id` ASC) VISIBLE,
  CONSTRAINT `fk_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `repairagency`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_locale_id`
    FOREIGN KEY (`locale_id`)
    REFERENCES `repairagency`.`locales` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`clients` (
  `parent` INT NOT NULL,
  `wallet_count` DECIMAL(10,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`parent`),
  CONSTRAINT `fk_parent`
    FOREIGN KEY (`parent`)
    REFERENCES `repairagency`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`wallets_story`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`wallets_story` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `entry_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `debet` DECIMAL(10,2) NULL,
  `credit` DECIMAL(10,2) NULL,
  `changed_by_manager_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_client_id_idx` (`client_id` ASC) VISIBLE,
  INDEX `fk_changed_by_manager_id_idx` (`changed_by_manager_id` ASC) VISIBLE,
  CONSTRAINT `fk_client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `repairagency`.`clients` (`parent`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_changed_by_manager_id`
    FOREIGN KEY (`changed_by_manager_id`)
    REFERENCES `repairagency`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`gadgets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`gadgets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` ENUM('COMPUTER', 'NOTEBOOK', 'SMARTPHONE', 'LAPTOP', 'E-BOOK') NOT NULL,
  `description` VARCHAR(1000) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_by_client_id` INT NOT NULL,
  `gadget_id` INT NOT NULL,
  `description` VARCHAR(1000) NOT NULL,
  `master_id` INT NULL,
  `manager_id` INT NULL,
  `cost` DECIMAL(10,2) NULL,
  `status` ENUM('NEW', 'WAIT_FOR_PAY', 'PAYED', 'CANCELED', 'WORKING', 'MADE') NOT NULL,
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_created_by_client_id_idx` (`created_by_client_id` ASC) VISIBLE,
  INDEX `fk_gadget_id_idx` (`gadget_id` ASC) VISIBLE,
  INDEX `fk_master_id_idx` (`master_id` ASC) VISIBLE,
  INDEX `fk_manager_id_idx` (`manager_id` ASC) VISIBLE,
  CONSTRAINT `fk_created_by_client_id`
    FOREIGN KEY (`created_by_client_id`)
    REFERENCES `repairagency`.`clients` (`parent`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_gadget_id`
    FOREIGN KEY (`gadget_id`)
    REFERENCES `repairagency`.`gadgets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_master_id`
    FOREIGN KEY (`master_id`)
    REFERENCES `repairagency`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manager_id`
    FOREIGN KEY (`manager_id`)
    REFERENCES `repairagency`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
  PRIMARY KEY (`id`, `about_order_id`),
  INDEX `fk_about_order_id_idx` (`about_order_id` ASC) VISIBLE,
  INDEX `fk_posted_by_client_id_idx` (`posted_by_client_id` ASC) VISIBLE,
  INDEX `fk_about_master_id_idx` (`about_master_id` ASC) VISIBLE,
  CONSTRAINT `fk_about_order_id`
    FOREIGN KEY (`about_order_id`)
    REFERENCES `repairagency`.`orders` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_posted_by_client_id`
    FOREIGN KEY (`posted_by_client_id`)
    REFERENCES `repairagency`.`clients` (`parent`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_about_master_id`
    FOREIGN KEY (`about_master_id`)
    REFERENCES `repairagency`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
