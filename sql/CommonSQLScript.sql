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
DROP DATABASE IF EXISTS `repairagency`;
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
  `password` VARCHAR(70) NOT NULL,
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
    ON DELETE CASCADE
    ON UPDATE CASCADE)
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
-- Table `repairagency`.`devices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`devices` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` ENUM('COMPUTER', 'LAPTOP', 'SMARTPHONE', 'TABLET', 'E_READER') NOT NULL,
  `description` TEXT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `repairagency`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `repairagency`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_by_client_id` INT NOT NULL,
  `device_id` INT NOT NULL,
  `description` VARCHAR(1000) NOT NULL,
  `master_id` INT NULL,
  `manager_id` INT NULL,
  `cost` DECIMAL(10,2) NULL,
  `status` ENUM('NEW', 'WAIT_FOR_PAY', 'PAYED', 'CANCELED', 'WORKING', 'MADE') NOT NULL,
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_created_by_client_id_idx` (`created_by_client_id` ASC) VISIBLE,
  INDEX `fk_gadget_id_idx` (`device_id` ASC) VISIBLE,
  INDEX `fk_master_id_idx` (`master_id` ASC) VISIBLE,
  INDEX `fk_manager_id_idx` (`manager_id` ASC) VISIBLE,
  CONSTRAINT `fk_created_by_client_id`
    FOREIGN KEY (`created_by_client_id`)
    REFERENCES `repairagency`.`clients` (`parent`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_device_id`
    FOREIGN KEY (`device_id`)
    REFERENCES `repairagency`.`devices` (`id`)
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

-- -----------------------------------------------------
-- Fill roles table --
-- -----------------------------------------------------
INSERT INTO roles(value, description) 
				values ('DIRECTOR', 'Can create (register / unregister) an administrator'),
						('CLIENT', 'Can register yourself, shange its own registration ddata, 
										make order on repair its gadjet, pay the order from its count, leave a review'),
						('ADMINISTRATOR', 'Can create (register / unregister) managers and masters'),
						('MANAGER', 'Can change client count, determine the cost of work,
											appoint a master for carying out an order, change oarder status to "WHAIT_FOR_PAY", "PAYED" and "CANCELED"'),
						('MASTER', 'Can choose an order, change order status to "WORKING" and "MADE"');
                        
-- -----------------------------------------------------
-- Fill locales table --
-- -----------------------------------------------------
INSERT INTO locales(value, description) 
					VALUES ('UA', 'Ukrainian'), ('US', 'USA'), ('EN', 'British'), ('RU', 'Russian');
                    
-- -----------------------------------------------------
-- appoint Director --
-- -----------------------------------------------------
INSERT INTO users (`email`, `password`, `person_name`, `role_id`, `photo_path`, `locale_id`)
					VALUES ('boss@gmail.com', 'EA423EB03AFEF7313E584FCECBB629836117EFF53C7364AB1517A3341EC6A337', 'Александр Васильевич', '1', 'img/users/boss.jpg', '1');
                    
-- -----------------------------------------------------
-- add Clients --
-- -----------------------------------------------------
INSERT INTO users (`email`, `password`, `person_name`, `role_id`, `photo_path`, `locale_id`)
					VALUES ('arni@gmail.com', '9ED4EB9FFBE2277A3F7EB84CDA2C05567FE027431D9D4780C0D3C3E9A49B5197', 'Arnold Schwarzenegger', '2', 'img/users/Arnold.jpg', '2'),
							('bond007@gmail.com', '36304D11D4CB51CD1E3CDCE4085A7ACF03DD369415B0C9942C96643AECC9AF4D', 'James Bond', '2', 'img/users/JamesBond.jpg', '3'),
                            ('jolie@gmail.com', 'C225D13E678A4E801F42D8CC2FBA73ABC3E54FFD3A10547DAC2634E9F04D9ED2', 'Angelina Jolie', '2', 'img/users/Angelina-Jolie.jpg', '2');
INSERT INTO clients VALUES (2, 0), (3, 0), (4, 0);

-- -----------------------------------------------------
-- add devices --
-- -----------------------------------------------------
INSERT INTO devices (value, description) 
				VALUES ('COMPUTER', 'Most people associate a personal computer (PC) with the phrase computer.  A PC is a small and relatively inexpensive computer designed for an individual use.'),
						('LAPTOP', 'A notebook computer is a battery- or AC-powered personal computer generally smaller than a briefcase that can easily be transported and conveniently used in temporary spaces such as on airplanes, in libraries, temporary offices, and at meetings.'),
                        ('SMARTPHONE', 'A smartphone is a mobile phone that includes advanced functionality beyond making phone calls and sending text messages. Most smartphones have the capability to display photos, play videos, check and send e-mail, and surf the Web.'),
                        ('TABLET', 'A tablet computer, commonly shortened to tablet, is a mobile device, typically with a mobile operating system and touchscreen display processing circuitry, and a rechargeable battery in a single, thin and flat package.'),
                        ('E_READER', 'An e-reader is a device designed as a convenient way to read e-books. It is similar in form factor to a tablet computer, but features electronic paper rather than an LCD screen. ... An e-reader may also download e-books from a computer or read them from a memory card.');
					                  
-- -----------------------------------------------------
-- add managers --
-- -----------------------------------------------------
INSERT INTO users (`email`, `password`, `person_name`, `role_id`, `photo_path`, `locale_id`)
					VALUES ('manager1@gmail.com', '38B7A50805A37780D3D6FEF6A6F3838A21D7017B3237E8390066CB6D84DC3AB4', 'Джордан Белфорт', '4', 'img/users/manager1.jpg', '1'),
							('manager2@gmail.com', '38B7A50805A37780D3D6FEF6A6F3838A21D7017B3237E8390066CB6D84DC3AB4', 'Катерина Трамелл', '4', 'img/users/manager2.jpg', '2');
					                  
-- -----------------------------------------------------
-- add masters --
-- -----------------------------------------------------
INSERT INTO users (`email`, `password`, `person_name`, `role_id`, `photo_path`, `locale_id`)
					VALUES ('master1@gmail.com', '193BAB4B88B17F961DB12D2431E1C1819599EAC0DCF73FAC7A2E22E4A250020E', 'Фред Уилсон', '5', 'img/users/master1.jpg', '1'),
							('master2@gmail.com', '193BAB4B88B17F961DB12D2431E1C1819599EAC0DCF73FAC7A2E22E4A250020E', 'Энтони Джонсон', '5', 'img/users/master2.jpg', '2'),
                            ('master3@gmail.com', '193BAB4B88B17F961DB12D2431E1C1819599EAC0DCF73FAC7A2E22E4A250020E', 'Фёдор Петров', '5', 'img/users/master3.jpg', '3');
                            
-- -----------------------------------------------------
-- add orders --
-- -----------------------------------------------------
INSERT INTO orders (`created_by_client_id`, `device_id`, `description`, `master_id`, `manager_id`, `cost`, `status`, `created_date`)
					VALUES (2, 1, 'Половина монитора черная', 7, 5, 700, 'NEW', '2020-03-1'),
							(2, 1, 'Не работает кнопка enter на клавиатуре', 7, 5, 700, 'NEW', '2020-03-1'),
                            (3, 3, 'Сенсор внизу перестал работать', 9, 5, 400, 'PAYED', '2020-03-25'),
                            (4, 4, 'Очень быстро греется', 8, 6, 200, 'CANCELED', '2020-03-30'),
                            (2, 5, 'Скачанная книга не отображается', 7, 5, 200, 'WORKING', '2020-04-15'),
                            (3, 1, 'Не работает звук', 9, 6, 500, 'MADE', '2020-04-20'),
                            (2, 2, 'Девайс упал, и экран разбился', 9, 5, 1000, 'NEW', '2020-04-25'),
                            (4, 3, 'Камера перестала работать', 8, 6, 700, 'WAIT_FOR_PAY', '2020-04-22'),
                            (3, 4, 'Часто виснет', 7, 5, 300, 'PAYED', '2020-05-6'),
                            (4, 5, 'Невозможно переключаться между страницами', 8, 6, 250, 'CANCELED', '2020-05-15'),
                            (2, 1, 'Не работает мышка', 8, 5, 350, 'WORKING', '2020-05-20'),
                            (3, 2, 'Шумит и медленно работает', 7, 6, 300, 'MADE', '2020-05-24'),
                            (4, 3, 'Памяти еще много, а новые приложения невозможно установить', 9, 5, 250, 'NEW', '2020-05-31'),
                            (2, 4, 'Пропало изображение экрана', 7, 6, 100, 'WAIT_FOR_PAY', '2020-06-2');

INSERT INTO orders (`created_by_client_id`, `device_id`, `description`, `master_id`, `manager_id`, `status`, `created_date`)
					VALUES  (3, 5, 'Книга сама листает страницы и меняет шрифт', 7, 5, 'PAYED', '2020-06-9'),
                            (4, 1, 'Системный блок беспрерывно пищит', 8, 6, 'CANCELED', '2020-06-19'),
                            (2, 2, 'Гнездо питания ноутбука шатается', 8, 5, 'WORKING', '2020-06-28'),
                            (3, 3, 'Не видит SIM карты', 9, 6, 'MADE', '2020-07-5');

INSERT INTO orders (`created_by_client_id`, `device_id`, `description`, `manager_id`, `status`, `created_date`)
					VALUES  (4, 4, 'Девайс упал в воду, теперь не включается', 5, 'NEW', '2020-07-11'),
                            (2, 5, 'Книга сама перезагружается', 6, 'WAIT_FOR_PAY', '2020-07-19'),
                            (3, 1, 'Не включается системный блок, не загорается индикатор "Power"', 5, 'PAYED', '2020-07-29'),
                            (4, 2, 'Ноутбук не читает диски', 6, 'CANCELED', '2020-08-7'),
                            (2, 3, 'Очень быстро разряжается', 5, 'WORKING', '2020-08-13'),(2, 5, 'Книга сама перезагружается', 6, 'WAIT_FOR_PAY', '2020-07-19'),
                            (3, 4, 'Перестал заряжаться', 6, 'MADE', '2020-08-21'),
                            (4, 5, 'При перелистывании страницы на следующую пауза 5 секунд', 5, 'NEW', '2020-08-30');
                            
INSERT INTO orders (`created_by_client_id`, `device_id`, `description`, `status`, `created_date`)
					VALUES  (2, 1, 'Компьютер невозможно перезагрузить', 'WAIT_FOR_PAY', '2020-09-8'),
                            (3, 2, 'Матрица стала гореть тускло', 'PAYED', '2020-09-16'),
                            (4, 3, 'Не работают динамики', 'CANCELED', '2020-09-27'),
                            (2, 4, 'Не работает микрофон', 'NEW', '2020-10-15');