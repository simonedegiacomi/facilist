-- MySQL dump 10.16  Distrib 10.3.10-MariaDB, for osx10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: web
-- ------------------------------------------------------
-- Server version	10.3.10-MariaDB

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
-- Table structure for table `change_email_request`
--

DROP TABLE IF EXISTS `change_email_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `change_email_request` (
  `id` bigint(20) NOT NULL,
  `new_email` varchar(255) DEFAULT NULL,
  `verification_token_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1em2ti2y6obkjyt193ufxfshp` (`verification_token_id`),
  CONSTRAINT `FK1em2ti2y6obkjyt193ufxfshp` FOREIGN KEY (`verification_token_id`) REFERENCES `verification_token` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `change_email_request`
--

LOCK TABLES `change_email_request` WRITE;
/*!40000 ALTER TABLE `change_email_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `change_email_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_subscription`
--

DROP TABLE IF EXISTS `chat_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_subscription` (
  `id` bigint(20) NOT NULL,
  `message` longtext DEFAULT NULL,
  `sent_at` datetime DEFAULT NULL,
  `shopping_list_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbedvqr4em7ptxbnv0iga4ebr6` (`shopping_list_id`),
  KEY `FKb2gyp9upiyr7kcm55vswhmrvo` (`user_id`),
  CONSTRAINT `FKb2gyp9upiyr7kcm55vswhmrvo` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKbedvqr4em7ptxbnv0iga4ebr6` FOREIGN KEY (`shopping_list_id`) REFERENCES `shopping_list` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_subscription`
--

LOCK TABLES `chat_subscription` WRITE;
/*!40000 ALTER TABLE `chat_subscription` DISABLE KEYS */;
INSERT INTO `chat_subscription` (`id`, `message`, `sent_at`, `shopping_list_id`, `user_id`) VALUES (338,'Ciao a tutti, qualcuno riesce a fare la spesa questo pomeriggio?','2019-01-15 10:03:40',329,3),(339,'Riesco a passare io ?','2019-01-15 10:03:40',329,4);
/*!40000 ALTER TABLE `chat_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` (`next_val`) VALUES (340),(340),(340),(340),(340),(340),(340),(340),(340),(340),(340),(340),(340),(340);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invite_to_collaborate`
--

DROP TABLE IF EXISTS `invite_to_collaborate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invite_to_collaborate` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `inviter_id` bigint(20) DEFAULT NULL,
  `shopping_list_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkwo9ktv2v6fhruq7451aajsln` (`inviter_id`),
  KEY `FK5d5nge3jkn1ve6d8sc9vcykbr` (`shopping_list_id`),
  CONSTRAINT `FK5d5nge3jkn1ve6d8sc9vcykbr` FOREIGN KEY (`shopping_list_id`) REFERENCES `shopping_list` (`id`),
  CONSTRAINT `FKkwo9ktv2v6fhruq7451aajsln` FOREIGN KEY (`inviter_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invite_to_collaborate`
--

LOCK TABLES `invite_to_collaborate` WRITE;
/*!40000 ALTER TABLE `invite_to_collaborate` DISABLE KEYS */;
/*!40000 ALTER TABLE `invite_to_collaborate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `seen_at` datetime DEFAULT NULL,
  `sent_at` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `target_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKopmlfyllvr1wncw4u4mi2kln9` (`target_user_id`),
  CONSTRAINT `FKopmlfyllvr1wncw4u4mi2kln9` FOREIGN KEY (`target_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `product_category_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcwclrqu392y86y0pmyrsi649r` (`product_category_id`),
  KEY `FK8uicnk5t935tcfkoayo9smhuu` (`creator_id`),
  CONSTRAINT `FK8uicnk5t935tcfkoayo9smhuu` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKcwclrqu392y86y0pmyrsi649r` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`id`, `icon`, `name`, `product_category_id`, `creator_id`) VALUES (35,'69b80c55-af37-435e-8917-292ecd781b2e','Arancia',14,NULL),(36,'6f8f23be-e5c6-4ade-800a-ed27dad3b231','Mela',14,NULL),(37,'9e72888c-22d1-4257-b7b9-dab088856b1e','Pera',14,NULL),(38,'c9704c8e-10f4-4719-b1fd-d61ae030806c','Limone',14,NULL),(39,'c1d9d0cb-096b-4a74-b447-62264a354c23','Fragole',14,NULL),(40,'42db83a4-5765-4747-86b3-0d5125f6e7a7','Lampone',14,NULL),(41,'90e71a90-759c-44b6-b415-b6310b1b98e5','Patata',15,NULL),(42,'3e43bd87-9c38-4995-9a8c-148815606920','Merendina',16,NULL),(43,'3e43bd87-9c38-4995-9a8c-148815606920','Schiacciatina',16,NULL),(44,'82039108-96cc-4ebb-9f59-0e863cd412ad','Cellulare',17,NULL),(45,'e599dbce-6f42-4659-8e0a-f4cfd60f246e','Lampadina',18,NULL),(46,'f57475a8-1a38-4780-8f97-d87fddf0bb32','Orologio da muro',18,NULL),(47,'0a0aed5c-b8f9-4a46-971d-4346e8ded168','Tende',18,NULL),(48,'73ab3bcd-e98b-4b27-a5d8-fd1f028cd263','Tappeto',18,NULL),(49,'572e3f4d-5639-4de4-9b0d-d71ccc12986c','Bicchieri',18,NULL),(50,'4b200079-90a7-4e2b-acf9-93f8edda821c','Piatti',18,NULL),(51,'78b55a07-894b-4e38-b175-5975f404e2ce','Posate',18,NULL),(52,'700f9362-e225-447b-9039-a754977aab59','Aspirapolvere',18,NULL),(53,'ef66f811-3bb4-47b0-8d26-4b399345a476','Sedia',18,NULL),(54,'6f74195b-b3fa-414e-aaa5-950ca92d0e4b','Lavatrice',18,NULL),(55,'c8f650b7-2812-49c1-83d7-d917c0c634e0','Fiammifero',18,NULL),(56,'592f1289-d164-4231-a71e-13f9e60cac44','Caricabatterie',17,NULL),(57,'d94a14d8-01b3-44b6-9460-26c7f0a077fc','Computer',17,NULL),(58,'c128a384-fe53-49e4-85bc-8b256683fc76','Televisione',17,NULL),(59,'2e91418d-0ba5-49b2-b931-93e7dbed57cc','Macchina fotografica',17,NULL),(60,'b41f9949-e2f6-424e-8856-d3ad49ba7f2b','Stampante',17,NULL),(61,'1ffbf6ff-664c-4fab-b00e-66e73635e87c','Raspberry pi',17,NULL),(62,'59a219a4-1c66-4a5f-8336-8653f63564a2','Mouse',17,NULL),(63,'a87c9bf8-3c33-4c65-95d2-a0c430bc88d2','Tastiera',17,NULL),(64,'665a294e-07ba-4fe2-b7d6-fb570f47d940','Scanner',17,NULL),(65,'29f687d3-3c29-4e03-8624-776cb9106f97','Webcam',17,NULL),(66,'162c2a29-2605-4a3c-8e7a-1493edb3453c','Hub usb',17,NULL),(67,'d0ec46fa-b97c-4e65-9f54-cf00c6949986','Access Point WI-FI',17,NULL),(68,'227142b3-39d0-4ea1-922f-64e74635b303','Router di rete',17,NULL),(69,'f627f685-210d-493a-9e76-dafd00fa4512','Switch di rete',17,NULL),(70,'24658aa4-4276-4d03-980e-3e7a19a4016d','SSD',17,NULL),(71,'abf22987-052b-414a-a07b-fb99f5a7032c','Hard Disk meccanico',17,NULL),(72,'3e43bd87-9c38-4995-9a8c-148815606920','Piadina',16,NULL),(73,'0e9c4e07-cfe1-4dcd-9f04-ec5979801d0c','Struccante',19,NULL),(74,'eda15831-6a3c-4423-8245-3c5bffbc3479','Caramelle',16,NULL),(75,'79d54a12-2a78-4d24-af6d-d8aa518aac6e','Banana',14,NULL),(76,'2e5c11b4-4b14-48cb-a25f-dc8702ce654a','Mirtillo',14,NULL),(77,'7a98b2b5-205f-465f-a93d-56dd6e4e047d','Kiwi',14,NULL),(78,'be9fc0a4-ca0e-4bbc-9098-07630322c241','Cocco',14,NULL),(79,'dc5b6962-b3ec-4b24-868a-6c1875e21a20','Uova',16,NULL),(80,'f777aab5-5513-46ff-93e2-d9d22bb1c32b','Latte',24,NULL),(81,'c32a38f2-2a59-4fb6-8b05-e0130a7d82a5','Panna',16,NULL),(82,'8452ac43-43dd-4b0e-87db-7f95baf1ea93','Marmellata',16,NULL),(83,'3e43bd87-9c38-4995-9a8c-148815606920','Nutella',16,NULL),(84,'89d07e45-aafc-4692-9322-416f7297c8f1','Cioccolato',16,NULL),(85,'841fefcc-c065-4393-b5d7-3dc0f87a8b62','Pasta',16,NULL),(86,'3da56365-8baf-4048-8350-aaba55afed2b','Pane',16,NULL),(87,'4b959578-68a9-4774-b4a1-ee973f22ee7b','Acqua',24,NULL),(88,'054f9bfa-f483-411d-af39-9e6143cef20c','Cocacola',24,NULL),(89,'48d5c9a0-c699-4c59-a7f2-d337e08bf7d1','Fanta',24,NULL),(90,'d21b6e0a-24b9-4a1f-b683-c6b6c3371418','Thè',24,NULL),(91,'d21b6e0a-24b9-4a1f-b683-c6b6c3371418','Caffè',24,NULL),(92,'0fd52943-5539-4c1b-a795-3e4bf2bb1f6d','Capuccino',24,NULL),(93,'00558c04-ed23-4745-a7ef-38496d6c8849','Cioccolata calda',24,NULL),(94,'f203d2eb-2d8e-48e2-ad98-d837998d6419','Vino',24,NULL),(95,'191a42bf-21d4-4e60-bb59-e0d8f9ea43ac','Birra',24,NULL),(96,'989b75cd-95a8-48b4-a998-afe6332af71e','Redbull',24,NULL),(97,'d21b6e0a-24b9-4a1f-b683-c6b6c3371418','Cedrata',24,NULL),(98,'09abc534-a32c-4a9f-92cf-334f36539bbd','Carote',15,NULL),(99,'10454085-1012-4efe-8fb0-7710e7e4cecd','Insalata',15,NULL),(100,'ef3bece8-38e0-4dd3-bea4-505fce22940f','Cetriolo',15,NULL),(101,'8ee59115-7b6c-44bf-84d6-4ca110d6757d','Zucchina',15,NULL),(102,'cb004633-deed-4725-8ad9-1a1b6cca8e7b','Peperone',15,NULL),(103,'58afc7ed-ca89-4714-b228-19c0684c5cbf','Peperoncino',15,NULL),(104,'58ab3df9-469a-4b7e-983a-a33c61bf7d4a','Zucca',15,NULL),(105,'c1b3666d-036c-4a1f-a2ff-d72c12eece4f','Cavolfiore',15,NULL),(106,'d493f037-a6eb-4ef6-b28d-4a1ba1d880d6','Petto di pollo',20,NULL),(107,'e24cc4d7-a076-4e50-b063-897f72989b6f','Affettato di tacchino',20,NULL),(108,'231d1ac3-c35f-4b9d-831b-2b5dac6b9296','Manzo',20,NULL),(109,'4f8a058c-02e0-4639-8e4c-af1c9d8c2680','Mortadella',20,NULL),(110,'e326813e-de8d-4105-b10f-90a7974f4ac1','Hamburger',20,NULL),(111,'4f8a058c-02e0-4639-8e4c-af1c9d8c2680','Wusterl di pollo',20,NULL),(112,'4f8a058c-02e0-4639-8e4c-af1c9d8c2680','Bresaola',20,NULL),(113,'4777bf8c-fd0a-46d4-a77c-1540e9abaabb','Costine',20,NULL),(114,'04a3b9f3-2f1a-4d41-9504-680e3e838da1','Salamina',20,NULL),(115,'dc718f0b-213b-4c97-8b5a-6dfa0d88551d','Salame',20,NULL),(116,'89b96561-ed33-40e7-9e98-2c9d83f772ae','Prosciutto crudo',20,NULL),(117,'235b09f0-e757-482b-a916-4370019e6855','Prosciutto cotto',20,NULL),(118,'b04c9a7c-0f83-40a7-8c70-27cfc61a5565','Zucchero',21,NULL),(119,'dcf8f4df-c666-4636-b0d3-321ada829f84','Salsa barbecue',21,NULL),(120,'68139646-4e29-4a88-a770-665954a73df6','Sale',21,NULL),(121,'ad058284-74f1-48ae-af24-e6913d8d8ec8','Pepe nero',21,NULL),(122,'dfa96578-ac86-4fdb-a0d0-635bbddaa8da','Olio di oliva',21,NULL),(123,'60fb754c-0991-477c-bd36-b5c5c9816a59','Aceto',21,NULL),(124,'e5bd5b15-6456-46e5-8256-3d17a6e6997a','Aceto balsamico',21,NULL),(125,'3fabc207-442e-4a9d-bc3c-8bfe614a982f','Peperoncino',21,NULL),(126,'433fa51c-f8b9-4490-94e7-ce6b17ecc57c','Ketchup',21,NULL),(127,'7210da35-6e8f-4e43-b95b-b20be331cef8','Maionese',21,NULL),(128,'ec9017f1-84e4-48a2-a21d-4b0fa6fdea82','Basilico',15,NULL),(129,'1f14d77f-5c70-4430-828c-1decf89d9c70','Rosmarino',15,NULL),(130,'c38252c0-efa5-47e5-ae8c-1cfbfd6caac0','Melanzana',15,NULL),(131,'10454085-1012-4efe-8fb0-7710e7e4cecd','Cavolo',15,NULL),(132,'d29a60de-b178-4fe4-8aef-69b6480cd1ca','Dentifricio',19,NULL),(133,'28afd391-2f4d-4765-8bb0-41c12a33800b','Spazzolino',19,NULL),(134,'fac4a197-bbb9-4dd7-8ee1-4e8fa732c6ed','Carta igenica',19,NULL),(135,'064f4f7a-7117-4361-8ba1-4dd0cdf6f3c1','Spugna',19,NULL),(136,'32894c7e-6c09-4e3d-a475-dda3f85ce404','Spazzola per capelli',22,NULL),(137,'555de0a7-a23a-4def-8399-9bdace1ad5b2','Rasoio',22,NULL),(138,'af5e5341-2c0c-4ce2-ab7f-d8b081443356','Schiuma da barba',22,NULL),(139,'c8729687-f250-4af5-b652-b3fed16e9e7c','Crema solare',22,NULL),(140,'ea60b15d-e051-47ad-9251-08807bac6509','Crema depilatoria',22,NULL),(141,'d0cbfa8b-8003-45d3-b632-2ebabe3e6ba0','Tachipirina',22,NULL),(142,'3f1e6373-5455-4b86-8ddf-f82bebebf6bf','Collutorio',19,NULL),(143,'23157d11-6b98-4423-9227-029ab5f6eba0','Sbiancante',19,NULL),(144,'0b4e1d63-b744-416c-8e97-0ff9c0f09064','Filo interdentale',22,NULL),(145,'fd647591-83ef-4d1e-9323-11879fcc7a08','Integratore alimentare',22,NULL),(146,'22a708af-7250-4991-a474-992294b3dc6e','Crema mani',22,NULL),(147,'23d3bb83-bdf7-490a-ada8-610a59d2c514','Crema viso',22,NULL),(148,'417dc619-48f3-492c-964c-c4629a6dd1d3','Cotone',22,NULL),(149,'bf0cb66a-dd96-4c9b-ba35-e0bac56aaaf9','Disinfettante',22,NULL),(150,'ec827314-b9c5-49e7-b0ff-fadc10dc7ce0','MomentDol',22,NULL),(151,'c6fe3612-3700-4fe8-a5ad-4977d04d01c2','MomentAct',22,NULL),(152,'0261c536-da58-4cb5-8415-dd3e6914d030','Vitamine',22,NULL),(153,'305fe57f-3a76-42ff-a37d-d1e7755e153f','Goccie oculari',22,NULL),(154,'a2b4caeb-8331-441e-aa2a-fdbf69c78abc','Lenti morbide ',22,NULL),(155,'9a196b20-c683-4dbc-b948-b4ae5e5a3b87','Acqua per lenti',22,NULL),(156,'1f872146-7ac4-473c-a99e-cfb9e7f1d3bd','Gocce orecchio',22,NULL),(157,'07655723-53f9-49d1-b795-4a433fe59c29','Diuretici ',22,NULL),(158,'5e5fe51c-e272-43f9-9f14-914a5a289b07','Nurofen',22,NULL),(159,'5ba240f8-5987-4f10-b422-b5a7e8dad16c','Cioccolatini',25,NULL),(160,'7d11e996-d7b3-4984-8068-1325ef3b2f3e','Fiore',25,NULL),(161,'9a4134df-9169-4418-8da6-ddf454f97ecc','Bigliettino auguri',25,NULL),(162,'0a02ccf3-6865-440a-817e-28d4d0475f89','Palloncini',25,NULL),(163,'e3e8e293-3eb6-4b13-8c12-5e2c2177c51e','Cappellini festa',25,NULL),(164,'0a02ccf3-6865-440a-817e-28d4d0475f89','Decorazione scritta auguri',25,NULL),(165,'7613a56b-0d13-45fd-bfd3-1c9688cac9ad','Camicia unisex',23,NULL),(166,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Calzini unisex',23,NULL),(167,'438d9ea4-c245-4b9e-bfdd-b7133a5539b7','Scarpe unisex',23,NULL),(168,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Ciabatte unisex',23,NULL),(169,'a5dc1b91-70f2-4140-a6e4-199b9ec44dd5','Costume mare uomo',23,NULL),(170,'6dccd117-ec38-4f9f-b670-411119464c46','Costume halloween uomo',23,NULL),(171,'b89ddff7-8805-4b62-b166-74bd960beb9a','Costume carnevale uomo',23,NULL),(172,'79eda83b-945e-4963-b6ec-30dd823aed79','Vestito matrimonio uomo',23,NULL),(173,'96183f8c-f213-4ff4-8b60-6d66187fbb57','Vestito cerimonia uomo',23,NULL),(174,'227d1e2d-6059-47d8-aa12-c9d35408066e','Costume mare donna',23,NULL),(175,'d54dfbed-00d7-40ad-9781-fa0cccc0bfe3','Costume halloween donna',23,NULL),(176,'4a91395c-914c-4e8a-a659-88546307c83f','Costume carnevale donna',23,NULL),(177,'3244b58a-285c-4d46-b719-2b9118a97ffb','Vestito matrimonio donna',23,NULL),(178,'c846391f-0f29-40f0-b68a-cffa1db3282a','Vestito cerimonia donna',23,NULL),(179,'2c506eea-13a3-40d5-bae8-36511b65a9f5','Costume mare bimbo',23,NULL),(180,'3884d638-f8cb-4e54-9cde-cb9dee3d2f21','Costume halloween bimbo',23,NULL),(181,'b671df7f-4bce-4625-b9d9-f5d74b7d37b8','Costume carnevale bimbo',23,NULL),(182,'eca5c355-e857-4db8-b021-bb35525c967b','Vestito matrimonio bimbo',23,NULL),(183,'e604e216-4d48-45e4-bf58-e9a0579f118c','Vestito cerimonia bimbo',23,NULL),(184,'e1d13253-4362-4584-9ca2-8b6e79655379','Costume mare bimba',23,NULL),(185,'3d9c2b26-4e7b-4027-9151-1fbb3fd1f70e','Costume halloween bimba',23,NULL),(186,'9ff1a63a-e5eb-44eb-80fa-6c1b0f8cb51c','Costume carnevale bimba',23,NULL),(187,'b6eb99c3-ccbb-4848-b84f-2a0b1cc00b18','Vestito matrimonio bimba',23,NULL),(188,'d39d2184-a63c-4492-88a1-b7065014743a','Vestito cerimonia bimba',23,NULL),(189,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Vestito neonato',23,NULL),(190,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Vestito neonata',23,NULL),(191,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Ballerine donna',23,NULL),(192,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Ballerine bambina',23,NULL),(193,'e888f0ca-2362-43fc-86c3-f7ab4a410e34','Sciarpa invernale donna',23,NULL),(194,'8668f5f6-c2bc-4615-8c0b-fc8417a59a00','Sciarpa invernale uomo',23,NULL),(195,'c87d913d-0243-4bd4-beef-e8dac2944a77','Sciarpa estiva donna',23,NULL),(196,'7fca8900-3e6e-4cfa-ae4e-152d6e757244','Sciarpa primaverile donna',23,NULL),(197,'8734ed32-65f2-4662-b4b6-3839625939af','Calze',23,NULL),(198,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Collant',23,NULL),(199,'817299af-b643-40e9-abb7-5a4e3f4d787f','Pantaloni lunghi lavoro donna',23,NULL),(200,'abd0c378-f999-4d19-9541-71fd8a232994','Pantaloni corti lavoro donna',23,NULL),(201,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Gonna lavoro donna',23,NULL),(202,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Pantaloni lunghi lavoro uomo',23,NULL),(203,'c4d81105-31da-4fff-9d06-f43d2c7f75f3','Pantaloni corti lavoro uomo',23,NULL),(204,'a227a1ba-b9fe-46d0-88f5-0aa41041d520','Pantaloni lunghi unisex',23,NULL),(205,'88979a1a-8f1b-436a-b8c2-218e7e9b4e23','Cappello mare uomo',23,NULL),(206,'b42baca6-da7b-4da5-9b29-7083ae1aed5f','Cappello mare donna',23,NULL),(207,'42c85edb-106c-4856-9f2a-8be16fe297aa','Cappello bimbo',23,NULL),(208,'f81f8007-243d-450a-85e8-6965e5c9364d','Cappello bimba',23,NULL),(209,'dcbe8899-15a1-4333-9c96-6e06d6c28c57','Occhiali da sole unisex',23,NULL),(210,'c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Cintura unisex',23,NULL),(211,'28750c47-651a-486c-83eb-afe1d8fe0617','Orecchini donna',25,NULL),(212,'b7b4f538-5496-42a0-abb7-e634186b3050','Profumo donna',25,NULL),(213,'c4238a31-7993-4d4a-aa76-1408f5009341','Profumo uomo',25,NULL),(214,'5c248784-9b48-42f6-9c84-3e09b92856da','Deodorante donna',22,NULL),(215,'fa87b0cb-a3c5-4ee9-a4d5-09e73b8f38c3','Deodorante uomo ',22,NULL),(216,'034c7868-f9a4-437b-a905-afaa3e292e71','Shampoo',19,NULL),(217,'00a79dd4-447a-453b-831f-f58481f2e914','Sapone',19,NULL),(218,'b366389f-eb93-460b-8bd9-b0d83b47929d','Bagnoschiuma',19,NULL),(219,'c8a8c6d2-f6f7-44c6-9c77-1e7f45d3cde0','Balsamo',22,NULL),(220,'abc03441-802b-4c8e-afd5-9d6493526eb9','Acetone',22,NULL),(221,'20e3f6f5-258f-4051-95ca-6650e8386b21','Cotonfioc',22,NULL),(222,'b39cae6a-e4e4-49a8-abe2-b9d7e1013911','Anelli',13,NULL),(223,'7b35dceb-4cea-4a6b-83fd-807d1494ed5f','Bolide',13,NULL),(224,'c5f8d051-dac4-4122-8535-b526fb0df442','Pluffa',13,NULL),(225,'eb3728b0-aa43-45e9-a560-e9d0f7d5c63c','Boccino',13,NULL),(226,'ba681032-9a13-4fe8-858b-7d0434afad33','Divisa boccino',13,NULL),(227,'8993d1f3-aeac-48a7-aeeb-3c04437fb7cc','Fascia cercatore',13,NULL),(228,'31d76417-59d4-4bd6-a290-1ffe1023af99','Fascia battitore',13,NULL),(229,'e85098cf-6b70-4499-b2d3-af592e50e422','Fascia portiere',13,NULL),(230,'d2717c7b-3cc4-46fb-864f-c3b7cff2658c','Fascia cercatore',13,NULL),(231,'2a884c19-2ed2-4113-a1f6-1f0854045975','Guanti',13,NULL),(232,'dfeb502c-628a-4a01-86cd-b67ceea6c381','Scopa',13,NULL),(233,'2b4623cb-32e0-46c2-a0d6-b970071cf43e','Paradenti',13,NULL),(234,'ac744f59-f82d-4c96-83fe-ed365d2840ab','Cacciavite a croce',26,NULL),(235,'bf331ab7-da38-4457-b864-87330bb59793','Chiave inglese',26,NULL),(236,'ac10fe5f-2483-47b4-ae7d-f18dc7da49a9','Martello',26,NULL),(237,'4ed81868-793b-44d5-937b-0648d301cce7','Torcia',26,NULL),(238,'6ba23364-7fe3-4790-a6d5-0cf1b99a3a09','Cacciavite piatto',26,NULL),(239,'61e52720-dd6a-4d79-8aaf-0571d1f6494b','Trapano',26,NULL),(240,'628a30e8-5ba1-4d32-a7c8-455e8cdd0d32','Avvitatore',26,NULL),(241,'faab29b0-c0bc-4a58-96af-3bdeb1bbd03e','Trapano a batteria',26,NULL),(242,'ce7022c0-959b-43e9-8bbf-eae4278ba4ec','Kit di chiavi',26,NULL),(243,'85ec4740-a0c6-4bd3-ae60-69fbb94aa93d','Sega',26,NULL),(244,'16c7c30a-ebd6-4c1e-b663-7c03bc762ead','Asse in legno',27,NULL),(245,'0b54dec9-6c11-4792-87cf-0dc4558b159c','Ruota in legno',27,NULL),(246,'259d2c50-a18c-483e-9baa-a8279ee14b85','Mensola',27,NULL),(247,'0b54dec9-6c11-4792-87cf-0dc4558b159c','Elemento di fissaggio',27,NULL),(248,'f314fcb8-4e96-499e-ad45-6096b63ad9e4','Porta',27,NULL),(249,'0b54dec9-6c11-4792-87cf-0dc4558b159c','Spigolo in legno',27,NULL),(250,'0b54dec9-6c11-4792-87cf-0dc4558b159c','Fissaggio finestra',27,NULL),(251,'16cafd4d-a0c1-44d7-a10a-224c140ffbb5','Maniglia',27,NULL),(252,'fab69b14-066e-444d-8907-52a7075742ac','Sedia',27,NULL),(253,'8018c237-808e-4131-bb1a-23ddb8ed3131','Tavolo',27,NULL),(254,'e66da5b1-0c0b-4980-8cfe-489945260d56','Viti a croce',28,NULL),(255,'81810ce2-d6f4-4ba8-b27a-6ba7d1a99edd','Viti piatte',28,NULL),(256,'9b48b804-cb93-43f2-a775-ec850cf05752','Viti autofilettanti',28,NULL),(257,'063c9112-6c45-40c7-8749-151e32376772','Chiodi per legno',28,NULL),(258,'a73b3bd1-2c5c-43dd-9979-a94dcfcd4b4c','Puntina',28,NULL),(259,'640a3475-8d1f-472b-998e-985027176b03','Chiodi per muro',28,NULL),(260,'bf6eaf30-d7fe-4749-8c06-f4568614a5f4','Viti per legno',28,NULL),(261,'9e1cbacf-cb0b-4edb-af0e-14f1c8250ef6','Viti con fisher',28,NULL),(262,'115ef684-5827-4632-b018-9777db903b6a','Vite esagonale',28,NULL),(263,'61ab7cb5-2c51-4576-904c-312c3e747f7a','Chiodi per metallo',28,NULL),(264,'3c6820dd-cb4f-45f5-a887-60f5f5c6c18f','Chiodo generico',28,NULL),(265,'e523e214-fe83-40fa-b411-fb3e14fa5178','Canditi',29,NULL),(266,'e523e214-fe83-40fa-b411-fb3e14fa5178','Fichi secchi',29,NULL),(267,'391ce11e-0c63-4f46-ae35-f9d1a6efbbe1','Mandorle',29,NULL),(268,'e523e214-fe83-40fa-b411-fb3e14fa5178','Uvetta',29,NULL),(269,'f45f6d2d-d13a-4b9a-b91a-9e28286b2eb7','Noci',29,NULL),(270,'4969678e-4533-4741-bff7-62e76ca109e8','Pinoli',29,NULL),(271,'5f205f23-4cb0-4bc4-a767-716c1210349f','Pistacchio',29,NULL),(272,'267d81d2-7278-4185-ae0b-1a764b222d03','Ghiande',29,NULL),(273,'e523e214-fe83-40fa-b411-fb3e14fa5178','Tamarindo',29,NULL),(274,'ad7b370b-2dc8-4865-b6bf-5bf1d9cb38f3','Nocciola',29,NULL),(275,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata di albicocche',30,NULL),(276,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata di castagne',30,NULL),(277,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata di mirtilli',30,NULL),(278,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata di pesca',30,NULL),(279,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata di fragole',30,NULL),(280,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata mista',30,NULL),(281,'b954c74b-c5d3-4afa-83b4-72597ba189fe','Marmellata di frutti di bosco',30,NULL),(282,'ec6c71eb-8137-437f-81fa-a934ad0d37cd','Miele di Acacia',30,NULL),(283,'180fb5b5-c8d7-4a74-847d-313813572790','Miele misto',30,NULL),(284,'e3eb97d2-d133-4eef-8bf0-86ed9fd36337','Miele di pino',30,NULL),(285,'9506e6a1-3f6f-408f-a486-5ac5ac563008','Miele di Eukalipto',30,NULL),(286,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di asparagi',31,NULL),(287,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di zucca',31,NULL),(288,'961c9afa-fde2-44fb-8ae0-742d8a7965c4','Minestrone di verdura',31,NULL),(289,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di funghi',31,NULL),(290,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di patate',31,NULL),(291,'aa66ad27-2a23-407c-906c-3cf771e47b60','Purè',31,NULL),(292,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di piselli',31,NULL),(293,'11132397-fb78-4e4b-ade0-dd0977bea23c','Minestrone di legumi',31,NULL),(294,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di finochi',31,NULL),(295,'e291c0f3-b16b-491e-9028-e67271776c8c','Vellutata di carote',31,NULL),(296,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Cerniera per porta',32,NULL),(297,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Cerniera per finestra',32,NULL),(298,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Supporto per mensola',32,NULL),(299,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Supporto per armadio',32,NULL),(300,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Reti per materasso',32,NULL),(301,'9b376567-ddee-49c5-85f8-ccbb3b4f6370','Cavalletto',32,NULL),(302,'e8d48df2-d7d4-4f83-adc2-33b12aa89ee1','Portachiavi da muro',32,NULL),(303,'0be343a7-93db-4914-9bca-e581ac06d427','Porta abiti',32,NULL),(304,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Sostegno per tubi',32,NULL),(305,'22e9f0a0-464f-4d3c-bb58-0794eec17674','Sostegno per antenna',32,NULL),(306,'7aa79c65-206c-4e0c-be5a-5ddcf1104619','Vetro',33,NULL),(307,'54f49a08-d923-4320-89e0-5de9551a4e34','Specchio',33,NULL),(308,'822ff541-0f08-488e-81f3-784d62d0fbb9','Vetro infrangibile',33,NULL),(309,'097abd23-c3bd-4e42-a7b3-3458f688ee21','Foglio di alluminio',33,NULL),(310,'0d3fa903-79c7-4908-97c6-669534b4948c','Plexiglass',33,NULL),(311,'097abd23-c3bd-4e42-a7b3-3458f688ee21','Foglio di accaio inox',33,NULL),(312,'5bd5831f-1df0-4207-ad04-ddb75e9043bd','Tubo',33,NULL),(313,'f0dcb0f9-3584-4340-a916-db1f508a21b5','Tubo flessibile',33,NULL),(314,'097abd23-c3bd-4e42-a7b3-3458f688ee21','Tegole',33,NULL),(315,'097abd23-c3bd-4e42-a7b3-3458f688ee21','Fogli in catrame',33,NULL),(316,'22b60b73-3c7d-4d0b-985b-1c34bf01c60d','DVD',34,NULL),(317,'95b162ed-dd70-4165-ad6a-8ecc504c5a82','Blu-ray disk',34,NULL),(318,'1bf369e7-2ece-4211-8634-6865cc9df328','VHS',34,NULL),(319,'29d466e3-0a5e-4074-afc8-1572956ddce7','Abbonamento Netflix',34,NULL),(320,'34b46c71-6afa-4589-8029-62b2cb4628d2','Pellicola',34,NULL),(321,'93417310-9f49-4497-87c5-37c70e028d6e','Laser disc',34,NULL),(322,'552ae5f3-f297-4dee-a97b-f0098d92e6f6','Biglietto cinema',34,NULL),(323,'716eb4dc-0cb9-4969-95a1-7cfa1823e623','Abbonamento Amazon Video',34,NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_category` (
  `id` bigint(20) NOT NULL,
  `description` longtext DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_category_unique_name_constraint` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` (`id`, `description`, `icon`, `name`) VALUES (13,'Materiali per allenamento di Quidditch.','d8d5baa4-dcbe-4278-86fe-0d4d0901e74b','Quidditch'),(14,'Prodotti freschi che si trovano dal fruttivendolo o al sueprmercato','c44081df-c35d-4116-889a-473b3cddd39d','Frutta'),(15,'Prodotti freschi da fruttivendolo, supermercato o coltivatore locale','10454085-1012-4efe-8fb0-7710e7e4cecd','Verdura'),(16,'Il panificio è il singolo laboratorio dedicato alla produzione completa del pane, ed alla sua vendita diretta al pubblico.','3e43bd87-9c38-4995-9a8c-148815606920','Panificio'),(17,'Il termine tecnologia è una parola composta derivante dal greco tͩkhne-log, cioè letteralmente \'trattato sistematico su un\'arte\'. Nell\'Etica nicomachea Aristotele distinse due forme di azione, pr͢xis e tͩchn: mentre la prima ha il proprio scopo in se stessa, la seconda è sempre al servizio di altro, come un mezzo. In questo senso la \'tecnica\' (termine usato spesso come sinonimo), non era diversa ne dall\'arte, ne dalla scienza, ne da qualsiasi procedimento o operazione atto a raggiungere un effetto qualsiasi, e il suo campo si estendeva su tutte le attività umane.','82039108-96cc-4ebb-9f59-0e863cd412ad','Tecnologia'),(18,'Per casa si intende una qualunque struttura utilizzata da uomini e donne per ripararsi dagli agenti atmosferici. Essa generalmente ospita uno o più nuclei familiari e talvolta anche animali domestici.','f8fd7c78-9450-423b-83bb-22b034cb6fbf','Domestico'),(19,'Una moderna sala da bagno La stanza da bagno o semplicemente bagno o anche toilette è un locale adibito alla sede di apparecchi igienici, diffuso in gran parte del mondo.','8a0ba3f1-b784-464d-b778-50a4f6c1e9c6','Bagno'),(20,'Carne è il termine usato comunemente per intendere le parti commestibili degli animali omeotermi, e può comprendere perciò anche gli organi interni','4f8a058c-02e0-4639-8e4c-af1c9d8c2680','Carne'),(21,'Ketchup, uno dei condimenti più diffusi Vari condimenti al mercato di Sangha, Mali 1992 Un condimento è una sostanza utilizzata in cucina per insaporire i generi alimentari o i preparati culinari, come le salse.','1a18e8e0-b58a-4d56-9ce7-b5387f5644ad','Condimenti'),(22,'Un medico ausculta il cuore di un paziente di tre anni con uno stetoscopio. Con il termine salute si intende una condizione di elevato benessere psicofisico e sociale. Viene spesso indicata come il contrario dello stato di malattia.','f008781b-a911-456d-8f8a-7d2aec2c75f5','Salute'),(23,'Ciascun articolo d\'abbigliamento ha un significato culturale e sociale. In esso si condensano alcune funzioni tramandate o evolutesi nel tempo: quella pratica legata alla vestibilità, quella estetica legata al gusto dell\'epoca e a canoni specifici delle diverse comunità, quella simbolica grazie alla quale l\'abito può definire l\'appartenenza ad una particolare comunità e nello specifico identificare lo status sociale, civile e religioso.','c0f5a2a1-a70c-4535-b95a-9bb90996fc30','Abbigliamento'),(24,'Una bevanda è un liquido adatto al consumo umano. Anche se la bevanda per eccellenza è sicuramente l\'acqua, elemento indispensabile per la vita più del cibo, il termine molto spesso si riferisce per antonomasia alle bevande non alcoliche, fredde o calde, e a quelle alcoliche.','d21b6e0a-24b9-4a1f-b683-c6b6c3371418','Bevanda'),(25,'Per regalo si intende il passaggio di proprietà di un bene da un soggetto ad un altro senza una compensazione diretta che deriverebbe dallo scambio commerciale con un altro bene o servizio dotato di valore economicamente valutabile','0a02ccf3-6865-440a-817e-28d4d0475f89','Regalo'),(26,'Strumenti da lavoro per sistemare la casa o per progetti DIY','880dcc4f-55d0-410d-b90f-70e9ce519d60','Utensili'),(27,'Oggetti in legno per realizzare progetti o per abbellire la casa','0b54dec9-6c11-4792-87cf-0dc4558b159c','Legno'),(28,'Oggetti essenziali per assemblare pezzi in metallo e in legno','d1ac842f-2a63-485d-b217-8429f4d6f2f1','Chodi & viti'),(29,'Frutta secca da sgranocchiare dopo il pranzo o nel tempo libero','e523e214-fe83-40fa-b411-fb3e14fa5178','Frutta secca'),(30,'Creme, zuppe e minestroni basati su verdure fresche','b954c74b-c5d3-4afa-83b4-72597ba189fe','Creme di frutta'),(31,'Creme dolci come marmellate e vari tipi di miele','e291c0f3-b16b-491e-9028-e67271776c8c','Creme di verdura'),(32,'Supporti per mobili e altri oggetti della casa e di uso comune','22e9f0a0-464f-4d3c-bb58-0794eec17674','Sostegni'),(33,'Assi in legno e altri materiali primari per realizzare progetti','097abd23-c3bd-4e42-a7b3-3458f688ee21','Materiali'),(34,'Biglietti del cinema, abbonamenti e supporti multimediali per guardare un film in compagnia','b0148345-8fe9-4bbb-9096-8332664357fd','Film');
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `push_subscription`
--

DROP TABLE IF EXISTS `push_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `push_subscription` (
  `id` bigint(20) NOT NULL,
  `base64auth` varchar(255) DEFAULT NULL,
  `base64public_key` varchar(255) DEFAULT NULL,
  `endpoint` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK59avwrc3dqa9qrr19b9bbvjoy` (`user_id`),
  CONSTRAINT `FK59avwrc3dqa9qrr19b9bbvjoy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `push_subscription`
--

LOCK TABLES `push_subscription` WRITE;
/*!40000 ALTER TABLE `push_subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `push_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list`
--

DROP TABLE IF EXISTS `shopping_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list` (
  `id` bigint(20) NOT NULL,
  `description` longtext DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `shopping_list_category_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK51jsy2w71rdbd0e33a3ls2dgv` (`shopping_list_category_id`),
  KEY `FKli0aopg4pqmvi4sx14nrqvj39` (`creator_id`),
  CONSTRAINT `FK51jsy2w71rdbd0e33a3ls2dgv` FOREIGN KEY (`shopping_list_category_id`) REFERENCES `shopping_list_category` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKli0aopg4pqmvi4sx14nrqvj39` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list`
--

LOCK TABLES `shopping_list` WRITE;
/*!40000 ALTER TABLE `shopping_list` DISABLE KEYS */;
INSERT INTO `shopping_list` (`id`, `description`, `icon`, `name`, `shopping_list_category_id`, `creator_id`) VALUES (329,'Cose che ci mangiamo tra un corso e l\'altro','23987a93-54c8-4493-809d-10a5e8679006','Pranzo in università',326,1);
/*!40000 ALTER TABLE `shopping_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list__product`
--

DROP TABLE IF EXISTS `shopping_list__product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list__product` (
  `id` bigint(20) NOT NULL,
  `bought` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `shopping_list_id` bigint(20) DEFAULT NULL,
  `shopping_list_product_updates_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopping_list_product_unique_constraint` (`product_id`,`shopping_list_id`),
  KEY `FKntsi22pxsv228lvxkcky1inkf` (`shopping_list_id`),
  KEY `FKh8yc81db8q8jk8q81mp6vcj66` (`shopping_list_product_updates_group_id`),
  CONSTRAINT `FKb87hygfx47nnfe95ibdw40sq` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKh8yc81db8q8jk8q81mp6vcj66` FOREIGN KEY (`shopping_list_product_updates_group_id`) REFERENCES `shopping_list_product_updates_group` (`id`),
  CONSTRAINT `FKntsi22pxsv228lvxkcky1inkf` FOREIGN KEY (`shopping_list_id`) REFERENCES `shopping_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list__product`
--

LOCK TABLES `shopping_list__product` WRITE;
/*!40000 ALTER TABLE `shopping_list__product` DISABLE KEYS */;
INSERT INTO `shopping_list__product` (`id`, `bought`, `image`, `note`, `quantity`, `product_id`, `shopping_list_id`, `shopping_list_product_updates_group_id`) VALUES (333,'','2e5c11b4-4b14-48cb-a25f-dc8702ce654a',NULL,1,76,329,NULL),(334,'\0','79d54a12-2a78-4d24-af6d-d8aa518aac6e',NULL,1,75,329,NULL),(335,'','3da56365-8baf-4048-8350-aaba55afed2b',NULL,1,86,329,NULL),(336,'\0','841fefcc-c065-4393-b5d7-3dc0f87a8b62',NULL,1,85,329,NULL),(337,'','4b959578-68a9-4774-b4a1-ee973f22ee7b',NULL,1,87,329,NULL);
/*!40000 ALTER TABLE `shopping_list__product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list__user`
--

DROP TABLE IF EXISTS `shopping_list__user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list__user` (
  `id` bigint(20) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `shopping_list_id` bigint(20) DEFAULT NULL,
  `recent_shopping_list_products_update_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopping_list_collaboration_unique_constraint` (`user_id`,`shopping_list_id`),
  KEY `FKjqd3elsebko2x3xexbua7pjgg` (`shopping_list_id`),
  KEY `FKnfaun9kori1vsxvcbj6ennrnh` (`recent_shopping_list_products_update_id`),
  CONSTRAINT `FKjqd3elsebko2x3xexbua7pjgg` FOREIGN KEY (`shopping_list_id`) REFERENCES `shopping_list` (`id`),
  CONSTRAINT `FKnfaun9kori1vsxvcbj6ennrnh` FOREIGN KEY (`recent_shopping_list_products_update_id`) REFERENCES `shopping_list_product_updates_group` (`id`),
  CONSTRAINT `FKqv3ppvjaf2pbchegb2qg6rfwg` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list__user`
--

LOCK TABLES `shopping_list__user` WRITE;
/*!40000 ALTER TABLE `shopping_list__user` DISABLE KEYS */;
INSERT INTO `shopping_list__user` (`id`, `role`, `shopping_list_id`, `recent_shopping_list_products_update_id`, `user_id`) VALUES (330,'ADMIN',329,NULL,2),(331,'BASIC',329,NULL,3),(332,'SOCIAL',329,NULL,4);
/*!40000 ALTER TABLE `shopping_list__user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list_category`
--

DROP TABLE IF EXISTS `shopping_list_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list_category` (
  `id` bigint(20) NOT NULL,
  `description` longtext DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopping_list_category_unique_name_constraint` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list_category`
--

LOCK TABLES `shopping_list_category` WRITE;
/*!40000 ALTER TABLE `shopping_list_category` DISABLE KEYS */;
INSERT INTO `shopping_list_category` (`id`, `description`, `icon`, `name`) VALUES (324,'Contiene prodotti le cui categorie riguardano i fruttivendoli, e che quindi conviene comprare quando si passa vicino ad un negozio di questo tipo.','67c1ab6d-170a-49ea-8133-364d50368407','Lista del Fruttivendolo'),(325,'Mobili, elettrodomestici e oggetti casalinghi','3d3ad310-1eea-4543-9cbc-2c33e8916076','Lista della casa'),(326,'Prodotti soliti per la puasa pranzo','23987a93-54c8-4493-809d-10a5e8679006','Pausa pranzo'),(327,'Oggetti e accessori che permettono di passare il proprio tempo libero','1b0bf828-63be-41ff-b002-f446ba947d53','Tempo libero'),(328,'Utensili e oggetti di consumo per sistemare la casa e progetti DIY','9e0ffde7-19cf-418c-b19a-d93a7ef97faa','Ferramenta');
/*!40000 ALTER TABLE `shopping_list_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list_category__foursquare_id`
--

DROP TABLE IF EXISTS `shopping_list_category__foursquare_id`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list_category__foursquare_id` (
  `shopping_list_category_id` bigint(20) NOT NULL,
  `foursquare_category_ids` varchar(255) DEFAULT NULL,
  KEY `FKhxj7x5qgh8ri2keekfkd11whx` (`shopping_list_category_id`),
  CONSTRAINT `FKhxj7x5qgh8ri2keekfkd11whx` FOREIGN KEY (`shopping_list_category_id`) REFERENCES `shopping_list_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list_category__foursquare_id`
--

LOCK TABLES `shopping_list_category__foursquare_id` WRITE;
/*!40000 ALTER TABLE `shopping_list_category__foursquare_id` DISABLE KEYS */;
INSERT INTO `shopping_list_category__foursquare_id` (`shopping_list_category_id`, `foursquare_category_ids`) VALUES (324,'4bf58dd8d48988d1f9941735'),(325,'4bf58dd8d48988d1f9941735'),(326,'4bf58dd8d48988d1f9941735'),(327,'4bf58dd8d48988d1f0941735'),(327,'4bf58dd8d48988d1f2941735'),(328,'');
/*!40000 ALTER TABLE `shopping_list_category__foursquare_id` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list_category__product_category`
--

DROP TABLE IF EXISTS `shopping_list_category__product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list_category__product_category` (
  `shopping_list_category_id` bigint(20) NOT NULL,
  `product_category_id` bigint(20) NOT NULL,
  KEY `FKle8xmee0mpohgs4j1ai7e9yd4` (`product_category_id`),
  KEY `FKt2q9qxgngnh93xbfxda707kp7` (`shopping_list_category_id`),
  CONSTRAINT `FKle8xmee0mpohgs4j1ai7e9yd4` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`id`),
  CONSTRAINT `FKt2q9qxgngnh93xbfxda707kp7` FOREIGN KEY (`shopping_list_category_id`) REFERENCES `shopping_list_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list_category__product_category`
--

LOCK TABLES `shopping_list_category__product_category` WRITE;
/*!40000 ALTER TABLE `shopping_list_category__product_category` DISABLE KEYS */;
INSERT INTO `shopping_list_category__product_category` (`shopping_list_category_id`, `product_category_id`) VALUES (324,14),(324,15),(324,29),(324,30),(324,31),(325,19),(325,18),(325,17),(325,34),(326,14),(326,15),(326,20),(326,21),(326,24),(326,16),(327,13),(327,34),(327,17),(327,25),(327,23),(328,26),(328,27),(328,28),(328,32),(328,33);
/*!40000 ALTER TABLE `shopping_list_category__product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list_product_updates_group`
--

DROP TABLE IF EXISTS `shopping_list_product_updates_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list_product_updates_group` (
  `id` bigint(20) NOT NULL,
  `last_edit_at` datetime DEFAULT NULL,
  `shopping_list_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg7vhn7asb8iheswqujcksoiil` (`shopping_list_id`),
  CONSTRAINT `FKg7vhn7asb8iheswqujcksoiil` FOREIGN KEY (`shopping_list_id`) REFERENCES `shopping_list` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list_product_updates_group`
--

LOCK TABLES `shopping_list_product_updates_group` WRITE;
/*!40000 ALTER TABLE `shopping_list_product_updates_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `shopping_list_product_updates_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list_product_updates_group__user`
--

DROP TABLE IF EXISTS `shopping_list_product_updates_group__user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_list_product_updates_group__user` (
  `shopping_list_product_groups_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`shopping_list_product_groups_id`,`user_id`),
  KEY `FKpb42mnlwtgesrkll98uf77m9d` (`user_id`),
  CONSTRAINT `FKpb42mnlwtgesrkll98uf77m9d` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKst4t7pbiw7vx3cux263d0127v` FOREIGN KEY (`shopping_list_product_groups_id`) REFERENCES `shopping_list_product_updates_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list_product_updates_group__user`
--

LOCK TABLES `shopping_list_product_updates_group__user` WRITE;
/*!40000 ALTER TABLE `shopping_list_product_updates_group__user` DISABLE KEYS */;
/*!40000 ALTER TABLE `shopping_list_product_updates_group__user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `locale` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_email_unique_name_constraint` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `email`, `enabled`, `first_name`, `last_name`, `locale`, `password`, `photo`, `role`) VALUES (1,'alessio.faieta@studenti.unitn.it','','Alessio','Faieta','it-IT','$2a$10$PPMQjXtG8eMEy93.GNZIR.VDi2YGuBO/.hfyL9hy4Qxw.F18HyZ8S','default-user-photo','ROLE_USER'),(2,'gentiana.berisha@studenti.unitn.it','','Gentiana','Berisha','it-IT','$2a$10$RIJgEIlvQHVdj2reFwshVOoYhXmkDC.TOHpdmpNwGtIxOHuZVoMP6','2f396daf-4ceb-417d-817d-cadb398304ab','ROLE_USER'),(3,'simone.degiacomi@studenti.unitn.it','','Simone','Degiacomi','it-IT','$2a$10$JfG3ZBLLpOMZ9jxkaJ4laeZPoLmW8zLmUGCNRvTSqHKduTaPFljGe','c8de5c30-54b9-4def-b8e8-242dbafbef01','ROLE_USER'),(4,'slavarublev@gmail.com','','Slava','Rublev','it-IT','$2a$10$QnK83rT45DULsmowlBB6SeoqKvtp2IaobWL.Aoo3DLYrJHSeQ1Lr6','8fe9933f-3b20-4598-856d-d351d715004f','ROLE_USER'),(5,'mario@rossi.it','','Mario','Rossi','it-IT','$2a$10$hwlZtNK9ZesLVZjtpa4Aqu.B5PlR5p5yaD8RPqRiOAbFbGXhbHIIK','default-user-photo','ROLE_USER'),(6,'mario@blue.it','','Mario','Blue','it-IT','$2a$10$CR1vY/8LQ78mawqBqtrbo.JzN61v7ikKm5UjR4BdAZtGZ33Kk/dJO','default-user-photo','ROLE_USER'),(7,'pinco@pallino.it','','Pinco','Pallino','it-IT','$2a$10$k2HS4PqC/34veaB18a6RR.idM6zcFfJ9FQkzBjft.gfk.dR86PqO2','default-user-photo','ROLE_USER'),(8,'alessio@shopping.com','','Alessio Admin','Faieta','it-IT','$2a$10$IzSkD5EY6UWBr7S8SJbYPOOUuYjZqHnfXDsP7yJ96SRVEINf9ZX32','36afe7dd-84f1-4937-913d-8e3c7dc29f93','ROLE_ADMIN'),(9,'gentiana@shopping.com','','Gentiana Admin','Berisha','it-IT','$2a$10$2JAOM.oF492nP79StUtRL./0ztoDvJ32vzVwiostVQbcYFrOKgHuO','cd707d18-5c3b-4603-98b7-d739ad41ce8f','ROLE_ADMIN'),(10,'simone@shopping.com','','Simone Admin','Degiacomi','it-IT','$2a$10$lzRzvKRKFFzn7F9FGshhQO.CDaYHMXj5JXNm22ZRvb.0HbFlO69e.','5bfcd139-ec71-4d5e-9794-0180bf8a6aa2','ROLE_ADMIN'),(11,'slava@shopping.com','','Slava Admin','Rublev','it-IT','$2a$10$hDD9YdORwcaImCqOaz8Imewrg0jGtrYG/0q1lPdJaMsiMqWaqXSAS','6f851057-d487-4b1c-8289-6687453bcc7e','ROLE_ADMIN'),(12,'admin@shopping.com','','Admin','Master','it-IT','$2a$10$c9uLg/Pljt8ASQguh7Z2Lunu1HxUE634GgJy9Ub3.yDH7VAEFHpbm','39a8dc25-d0e8-4315-bc73-55092b477f22','ROLE_ADMIN');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_token`
--

DROP TABLE IF EXISTS `verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verification_token` (
  `id` bigint(20) NOT NULL,
  `creation_time` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrdn0mss276m9jdobfhhn2qogw` (`user_id`),
  CONSTRAINT `FKrdn0mss276m9jdobfhhn2qogw` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_token`
--

LOCK TABLES `verification_token` WRITE;
/*!40000 ALTER TABLE `verification_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `verification_token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-15 10:04:17
