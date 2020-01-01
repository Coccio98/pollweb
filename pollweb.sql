DROP DATABASE IF EXISTS `pollweb`;
CREATE DATABASE `pollweb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `pollweb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: pollweb
-- ------------------------------------------------------
-- Server version	5.7.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Table structure for table `domanda`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(100)NOT NULL UNIQUE,
  `text` varchar(200) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  `position` smallint(5) NOT NULL,
  `mandatory` char(2) NOT NULL DEFAULT true,
  `type` varchar(45) NOT NULL,
  `min_date` date DEFAULT NULL,
  `max_date` date DEFAULT NULL,
  `min_value` varchar(45) DEFAULT NULL,
  `max_value` varchar(45) DEFAULT NULL,
  `reg_expr` varchar(45) DEFAULT NULL,
  `id_survey` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_survey` (`id_survey`),
  CONSTRAINT `survey` FOREIGN KEY (`id_survey`) REFERENCES `surveys` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domanda`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `opzione`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(45) NOT NULL,
  `position` smallint(5) NOT NULL,
  `id_question` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_question` (`id_question`),
  CONSTRAINT `option_ibfk_1` FOREIGN KEY (`id_question`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opzione`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participants`
--

DROP TABLE IF EXISTS `participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participants`(
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(100) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `compiled` char(2) NOT NULL DEFAULT false,
  `id_survey` int(11) NOT NULL,
  KEY `id_survey` (`id_survey`),
  CONSTRAINT `survey_fk` FOREIGN KEY (`id_survey`) REFERENCES `surveys` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participants`
--

LOCK TABLES `participants` WRITE;
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey_responses`
--

DROP TABLE IF EXISTS `survey_responses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `survey_responses`(
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `submission_date` datetime NOT NULL,
  `id_survey` int(11) NOT NULL,
  `id_participant` int(11) NULL,
  CONSTRAINT `survey_response_fk` FOREIGN KEY (`id_survey`) REFERENCES `surveys` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `participant_response_fk` FOREIGN KEY (`id_participant`) REFERENCES `participants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participants`
--

LOCK TABLES `survey_responses` WRITE;
/*!40000 ALTER TABLE `survey_responses` DISABLE KEYS */;
/*!40000 ALTER TABLE `survey_responses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `risposta`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(10000) NOT NULL,
  `type` varchar(45) NOT NULL,
  `id_question` int(11) NOT NULL,
  `id_survey_response` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_question` (`id_question`),
  CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`id_question`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `answer_ibfk_2` FOREIGN KEY (`id_survey_response`) REFERENCES `survey_responses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `risposta`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sondaggio`
--

DROP TABLE IF EXISTS `surveys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surveys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `opening_text` varchar(45) NOT NULL,
  `closing_text` varchar(45) NOT NULL,
  `reserved` char(2) DEFAULT false,
  `open` char(2) DEFAULT false,
  `id_manager` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `manager` (`id_manager`),
  CONSTRAINT `manager` FOREIGN KEY (`id_manager`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sondaggio`
--

LOCK TABLES `surveys` WRITE;
/*!40000 ALTER TABLE `surveys` DISABLE KEYS */;
INSERT INTO `surveys` (`id`, `title`, `opening_text`, `closing_text`, `reserved`, `open`, `id_manager`) VALUES
(1, 'Vacanze Romane', 'Cosa visitare a Roma', 'Grazie per aver risposto!!', '0', '1', 1);
/*!40000 ALTER TABLE `surveys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenteregistrato`
--



DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` varchar(15) NOT NULL DEFAULT 'responsible',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


INSERT INTO `questions` (`id`, `code`, `text`, `note`, `position`, `mandatory`, `type`, `min_date`, `max_date`, `min_value`, `max_value`, `reg_expr`, `id_survey`) VALUES
(1, '1c,1,0', 'Hai mai visitato Roma?', 'Rispondi una delle due opzioni', 0, '1', 'choice', NULL, NULL, 1, 1, NULL, 1),
(2, '1c,2,1', 'Quali posti hai visitato?', 'Puoi selezionare nessuna o tutte le opzioni', 1, '1', 'choice', NULL, NULL, 0, 2, NULL, 1),
(3, '1n,3,2', 'Quanti giorni?', '', 2, '1', 'number', NULL, NULL, '1', '10', NULL, 1),
(4, '1l,4,3', 'Consiglieresti Roma?', 'Spiega anche perchè', 3, '1', 'long text', NULL, NULL, '10', '1000', NULL, 1),
(5, '1s,5,4', 'La metro è un posto affidabile?', 'Sii breve. Hai 100 caratteri a disposizione', 4, '1', 'short text', NULL, NULL, '0', '100', '.', 1),
(6, '1d,6,5', 'Quand\'è il giorno dei Patroni di Roma nel 202', 'Scrivi la data nel formato AAAA-MM-GG entro i', 5, '1', 'date', '2020-06-01', '2020-06-30', NULL, NULL, NULL, 1);


INSERT INTO `options` (`id`, `text`, `position`, `id_question`) VALUES
(1, 'Si', 0, 1),
(2, 'No', 1, 1),
(3, 'Colosseo', 0, 2),
(4, 'Pantheon', 1, 2),
(5, 'Fontana di Trevi', 2, 2),
(6, 'Piazza Navona', 3, 2),
(7, 'Basilica di San Pietro', 4, 2);


--
-- Dumping data for table `utenteregistrato`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`,`name`,`surname`,`email`,`password`,`role`) VALUES (1,'admin','admin','admin@pollweb.com','21232f297a57a5a743894a0e4a801fc3','administrator');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-19 23:22:15

CREATE USER pollweb@localhost IDENTIFIED BY 'pollweb';
GRANT ALL ON pollweb.* TO pollweb@localhost;
