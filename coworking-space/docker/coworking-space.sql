-- MySQL Script generated by MySQL Workbench
-- Sun Sep 18 20:52:09 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema coworking_space
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `coworking_space` ;

-- -----------------------------------------------------
-- Schema coworking_space
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `coworking_space` DEFAULT CHARACTER SET utf8 ;
USE `coworking_space` ;

-- -----------------------------------------------------
-- Table `coworking_space`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `coworking_space`.`user` ;

CREATE TABLE IF NOT EXISTS `coworking_space`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(2000) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `coworking_space`.`place`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `coworking_space`.`place` ;

CREATE TABLE IF NOT EXISTS `coworking_space`.`place` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  `nr` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idplace_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `coworking_space`.`reservation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `coworking_space`.`reservation` ;

CREATE TABLE IF NOT EXISTS `coworking_space`.`reservation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATETIME NOT NULL,
  `end_date` DATETIME NOT NULL,
  `place_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `accepted` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_reservation_place_idx` (`place_id` ASC) VISIBLE,
  INDEX `fk_reservation_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_reservation_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `coworking_space`.`place` (`id`),
  CONSTRAINT `fk_reservation_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `coworking_space`.`user` (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `coworking_space`.`place`(`description`, `nr`) VALUES ('First seat','17'), ('second seat','17');
INSERT INTO `coworking_space`.`user` (`firstname`, `lastname`, `email`, `password`, `role`) VALUES ('first', 'lastname', 'email@gmail.com', '$2y$10$ApI2i2/hizoQwO5F9f20cejlfq7/5cpjTx7Cc31y2j/lHHl7Sbc5O', 'admin');
