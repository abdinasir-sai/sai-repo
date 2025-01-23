
DROP TABLE IF EXISTS `AIRPORT_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AIRPORT_DETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `AIRPORT_NAME` varchar(100) NOT NULL,
  `COUNTRY` varchar(100) NOT NULL,
  `CITY` varchar(100) NOT NULL,
  `ITA_CODE` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7698 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `userid_pk` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `middlename` varchar(50) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=138373 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `DIVISION`
--

DROP TABLE IF EXISTS `DIVISION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIVISION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKryunt4ywp525bi3f53lkfdqjam` (`CREATOR`),
  KEY `FKhfqj4fkf32oyx6srsdfcnsyf` (`LAST_MODIFIER`),
  CONSTRAINT `FKhfqj4fkf32oyx6srsdfcnsyf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKryunt4ywp525bi3f53lkfdqjam` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `DEPARTMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DEPARTMENT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `DEPARTMENT_CODE` varchar(50) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `MAIL_ALIAS` varchar(100) DEFAULT NULL,
  `DEPARTMENT_LEAD` int DEFAULT NULL,
  `PARENT_DEPARTMENT` int DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `BUSINESS_UNIT` varchar(100) DEFAULT NULL,
  `DIVISION_ID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6crtr3h3316388isj5tlx0jvt` (`CREATOR`),
  KEY `FKjpp210jhgbwtd2fnmr29th44p` (`LAST_MODIFIER`),
  KEY `FK_PARENT_DEPARTMENT` (`PARENT_DEPARTMENT`),
  KEY `DIVISION_ID` (`DIVISION_ID`),
  CONSTRAINT `department_ibfk_2` FOREIGN KEY (`DIVISION_ID`) REFERENCES `DIVISION` (`ID`),
  CONSTRAINT `FK6crtr3h3316388isj5tlx0jvt` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK_PARENT_DEPARTMENT` FOREIGN KEY (`PARENT_DEPARTMENT`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `FKjpp210jhgbwtd2fnmr29th44p` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `APPLICANT`
--

DROP TABLE IF EXISTS `APPLICANT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPLICANT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `WORKSPACE_ID` int DEFAULT '1',
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `EMAIL_ID` varchar(50) DEFAULT NULL,
  `MOBILE` varchar(20) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `FAX` varchar(20) DEFAULT NULL,
  `WEBSITE` varchar(255) DEFAULT NULL,
  `SECONDARY_EMAIL` varchar(255) DEFAULT NULL,
  `PRESENT_ADDRESS` varchar(255) DEFAULT NULL,
  `STREET` varchar(255) DEFAULT NULL,
  `CITY` varchar(255) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `POSTAL_CODE` varchar(20) DEFAULT NULL,
  `COUNTRY` varchar(255) DEFAULT NULL,
  `EXPERIENCE_IN_YEARS` int DEFAULT NULL,
  `HIGHEST_QUALIFICATION` varchar(255) DEFAULT NULL,
  `CURRENT_JOB_TITLE` varchar(255) DEFAULT NULL,
  `CURRENT_EMPLOYER` varchar(255) DEFAULT NULL,
  `EXPECTED_SALARY` int DEFAULT NULL,
  `CURRENT_SALARY` int DEFAULT NULL,
  `SKILL_SET` varchar(255) DEFAULT NULL,
  `APPLICANT_UNIQUE_ID` varchar(255) DEFAULT NULL,
  `REFERED_BY` varchar(255) DEFAULT NULL,
  `APPLICANT_STATUS` enum('New','In review','Qualified','Unqualified','Reviewed','Converted-employee','Converted-temporary','Forward to onboarding') DEFAULT NULL,
  `APPLICANT_SOURCE` enum('Internal Referral','Job Boards','Career Sites','Recruitment Agencies','Social Media','Direct Applications','Campus Recruitment','Employee Referral Programs') DEFAULT NULL,
  `RECRUITER` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `TIME_TO_HIRE` int DEFAULT NULL,
  `APPLICANT_PHOTO` varchar(100) DEFAULT NULL,
  `CITIZENSHIP` varchar(50) DEFAULT NULL,
  `GENDER` enum('Male','Female','Other') DEFAULT NULL,
  `TEXT1` varchar(250) DEFAULT NULL,
  `TEXT2` varchar(250) DEFAULT NULL,
  `TEXT3` varchar(250) DEFAULT NULL,
  `TEXT4` varchar(250) DEFAULT NULL,
  `TEXT5` varchar(250) DEFAULT NULL,
  `TEXT6` varchar(250) DEFAULT NULL,
  `TEXT7` varchar(250) DEFAULT NULL,
  `TEXT8` varchar(250) DEFAULT NULL,
  `TEXT9` varchar(250) DEFAULT NULL,
  `TEXT10` varchar(250) DEFAULT NULL,
  `APPLICANT_RESUME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uc_email_id` (`EMAIL_ID`),
  KEY `FKi4xxlc71wrx0napidqx39djkm` (`CREATOR`),
  KEY `FKs5o2353n4qg1420dcxjicpt71` (`LAST_MODIFIER`),
  CONSTRAINT `FKi4xxlc71wrx0napidqx39djkm` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKs5o2353n4qg1420dcxjicpt71` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=872 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `APPLICANT_RESUME`
--

DROP TABLE IF EXISTS `APPLICANT_RESUME`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPLICANT_RESUME` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(255) DEFAULT NULL,
  `COVER_LETTER` varchar(255) DEFAULT NULL,
  `RESUME_ATTACHMENT` varchar(255) DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `TEXT2` varchar(255) DEFAULT NULL,
  `APPLICANT_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `APPLICANT_ID` (`APPLICANT_ID`),
  KEY `FKojd5f82xy5gu0s6jg47hh` (`CREATOR`),
  KEY `FKbqhj95x8rcq569gu1s12end1` (`LAST_MODIFIER`),
  CONSTRAINT `APPLICANT_RESUME_ibfk_1` FOREIGN KEY (`APPLICANT_ID`) REFERENCES `APPLICANT` (`ID`),
  CONSTRAINT `FKbqhj95x8rcq569gu1s12end1` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f82xy5gu0s6jg47hh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SKILL`
--

DROP TABLE IF EXISTS `SKILL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SKILL` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(100) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKlyust233r8kg27qnd5vw61od4` (`CREATOR`),
  KEY `FKfnsvht93nh88sta8url5ne02e` (`LAST_MODIFIER`),
  CONSTRAINT `FKfnsvht93nh88sta8url5ne02e` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKlyust233r8kg27qnd5vw61od4` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `APPLICANT_SKILL_MAPPING`
--

DROP TABLE IF EXISTS `APPLICANT_SKILL_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPLICANT_SKILL_MAPPING` (
  `APPLICANT_ID_FK` int NOT NULL,
  `SKILLS_ID_FK` int NOT NULL,
  PRIMARY KEY (`APPLICANT_ID_FK`,`SKILLS_ID_FK`),
  KEY `APPLICANT_ID_FK` (`APPLICANT_ID_FK`),
  KEY `SKILLS_ID_FK` (`SKILLS_ID_FK`),
  CONSTRAINT `applicant_skill_mapping_ibfk_1` FOREIGN KEY (`APPLICANT_ID_FK`) REFERENCES `APPLICANT` (`ID`),
  CONSTRAINT `applicatn_skill_mapping_ibfk_2` FOREIGN KEY (`SKILLS_ID_FK`) REFERENCES `SKILL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `APPRAISAL_CYCLES`
--

DROP TABLE IF EXISTS `APPRAISAL_CYCLES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPRAISAL_CYCLES` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_ID` int DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `STATUS` enum('Start','Started') DEFAULT NULL,
  `REVIEW_PERIOD_TYPE` enum('Monthly','Quarterly','Half-Yearly','Yearly') DEFAULT NULL,
  `REVIEW_PERIOD_START_DATE` datetime DEFAULT NULL,
  `REVIEW_PERIOD_END_DATE` datetime DEFAULT NULL,
  `COMMENTS` varchar(200) DEFAULT NULL,
  `RATING` enum('Needs Improvement','Outstanding','Excellent','Satisfactory','Unsatisfactory') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKit6w5v6hv55cxp7tq9dpbpna9` (`CREATOR`),
  KEY `FKtc3oo1b4rby0asx7iys45rfb9` (`LAST_MODIFIER`),
  CONSTRAINT `appraisal_cycles_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`),
  CONSTRAINT `FKit6w5v6hv55cxp7tq9dpbpna9` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKtc3oo1b4rby0asx7iys45rfb9` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `APPRAISAL_TEMPLATES`
--

DROP TABLE IF EXISTS `APPRAISAL_TEMPLATES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPRAISAL_TEMPLATES` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `DEPARTMENT_FK` int DEFAULT NULL,
  `APPLICABLE_FROM_DATE` datetime DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEPARTMENT_FK` (`DEPARTMENT_FK`),
  KEY `FKssur5s35sw5hfcbhfe35cxavc` (`CREATOR`),
  KEY `FKcwy0ptxed1tiqk51l2ic8hntn` (`LAST_MODIFIER`),
  CONSTRAINT `appraisal_templates_ibfk_1` FOREIGN KEY (`DEPARTMENT_FK`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `FKcwy0ptxed1tiqk51l2ic8hntn` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKssur5s35sw5hfcbhfe35cxavc` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ASSET`
--

DROP TABLE IF EXISTS `ASSET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ASSET` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `ASSET_NUMBER` varchar(200) DEFAULT NULL,
  `TYPE` varchar(200) DEFAULT NULL,
  `DEVICE_NAME` varchar(30) DEFAULT NULL,
  `DEVICE_SERIAL_NUMBER` varchar(255) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `TEXT2` varchar(255) DEFAULT NULL,
  `TEXT3` varchar(255) DEFAULT NULL,
  `TEXT4` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKddxmryrlglw9ntnqjws6j6k0` (`CREATOR`),
  KEY `FK3gmsg428eu4l5b4nd13veqwgn` (`LAST_MODIFIER`),
  CONSTRAINT `asset_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FK3gmsg428eu4l5b4nd13veqwgn` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKddxmryrlglw9ntnqjws6j6k0` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ATTENDANCE_GROUPS`
--

DROP TABLE IF EXISTS `ATTENDANCE_GROUPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTENDANCE_GROUPS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `GROUP_NAME` varchar(30) DEFAULT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `REASON` varchar(40) DEFAULT NULL,
  `EMPLOYEEID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEEID` (`EMPLOYEEID`),
  KEY `FKojd5f8wf2xy5gu0s6jg47hh` (`CREATOR`),
  KEY `FKbqhj95x8rcq569gu1s71end1` (`LAST_MODIFIER`),
  CONSTRAINT `Attendance_groups_ibfk_1` FOREIGN KEY (`EMPLOYEEID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKbqhj95x8rcq569gu1s71end1` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8wf2xy5gu0s6jg47hh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ATTENDANCE_REGULARIZATION`
--

DROP TABLE IF EXISTS `ATTENDANCE_REGULARIZATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTENDANCE_REGULARIZATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `APPROVAL_NAME` varchar(30) DEFAULT NULL,
  `DESCRIPTION` varchar(70) DEFAULT NULL,
  `REPORTING_TO` varchar(40) DEFAULT NULL,
  `APPROVER_EMAIL` varchar(35) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `DATE` datetime(6) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FK8ll5vt43l90in9ijkf46271uk` (`CREATOR`),
  KEY `FK23de0seunc2b836u9sfisihoh` (`LAST_MODIFIER`),
  CONSTRAINT `ATTENDANCE_REGULARIZATION_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FK23de0seunc2b836u9sfisihoh` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK8ll5vt43l90in9ijkf46271uk` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BREAKDETAILS`
--

DROP TABLE IF EXISTS `BREAKDETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BREAKDETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `BREAK_NAME` varchar(30) DEFAULT NULL,
  `BREAK_ICON` varchar(255) DEFAULT NULL,
  `PAY_TYPE` enum('Paid','Unpaid') DEFAULT NULL,
  `BREAK_MODE` enum('Automatic','Manual') DEFAULT NULL,
  `START_TIME` datetime(6) DEFAULT NULL,
  `END_TIME` datetime(6) DEFAULT NULL,
  `AVAILABLE_SHIFTS` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8d2jds69oh98h55g5ne1o0fmc` (`CREATOR`),
  KEY `FK4v7573h5b926s2iohetaxvxwy` (`LAST_MODIFIER`),
  CONSTRAINT `FK4v7573h5b926s2iohetaxvxwy` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK8d2jds69oh98h55g5ne1o0fmc` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BUSINESS_PLAN`
--

DROP TABLE IF EXISTS `BUSINESS_PLAN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BUSINESS_PLAN` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `ROLE_ID` int DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `TITLE` varchar(200) DEFAULT NULL,
  `OFFICE_LOCATION` varchar(200) DEFAULT NULL,
  `HIRING_TYPE` varchar(200) DEFAULT NULL,
  `JOB_GRADE_CODE` varchar(200) DEFAULT NULL,
  `PLAN_COUNT` int DEFAULT NULL,
  `ACTUAL_COUNT` int DEFAULT NULL,
  `WORK_MODE` enum('On-Site','Remote') DEFAULT NULL,
  `PLAN_DATE` datetime(6) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FKb494fgxslqpf3gjbgwm2kkvo1` (`CREATOR`),
  KEY `FKgdf83vor7ln1l504p116v80x3` (`LAST_MODIFIER`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `business_plan_ibfk_1` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `business_plan_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `FKb494fgxslqpf3gjbgwm2kkvo1` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKgdf83vor7ln1l504p116v80x3` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BUSINESS_TRIP`
--

DROP TABLE IF EXISTS `BUSINESS_TRIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BUSINESS_TRIP` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `REQUEST_ID` varchar(255) DEFAULT NULL,
  `TRAVEL_START_ARRIVAL_CITY` varchar(255) DEFAULT NULL,
  `TRAVEL_END_DATE` datetime(6) DEFAULT NULL,
  `TRAVEL_END_DEPARTURE_AIRPORT` varchar(255) DEFAULT NULL,
  `TRAVEL_END_DEPARTURE_CITY` varchar(255) DEFAULT NULL,
  `TRAVEL_END_ARRIVAL_AIRPORT` varchar(255) DEFAULT NULL,
  `TRAVEL_END_ARRIVAL_CITY` varchar(255) DEFAULT NULL,
  `BUSINESS_START_DATE` datetime(6) DEFAULT NULL,
  `BUSINESS_END_DATE` datetime(6) DEFAULT NULL,
  `TOTAL_BUSINESS_DAYS` int DEFAULT NULL,
  `TOTAL_LEAVES` int DEFAULT NULL,
  `PAY_VALUE` double DEFAULT NULL,
  `ATTACHMENT` varchar(255) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `REMARK` varchar(255) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `WORKSPACE_ID` int DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `TRAVEL_START_DATE` datetime(6) DEFAULT NULL,
  `TRAVEL_START_DEPARTURE_AIRPORT` varchar(255) DEFAULT NULL,
  `TRAVEL_START_DEPARTURE_CITY` varchar(255) DEFAULT NULL,
  `TRAVEL_START_ARRIVAL_AIRPORT` varchar(255) DEFAULT NULL,
  `TRAVEL_FROM_COUNTRY` varchar(100) DEFAULT NULL,
  `TRAVEL_TO_COUNTRY` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `REQUEST_ID` (`REQUEST_ID`),
  KEY `FKwrt8765rthgfdfghgfdasd46v` (`EMPLOYEE_ID`),
  KEY `FKi6us55shaoddnosb0djcfm6no` (`CREATOR`),
  KEY `FKghksaiu9qd6o4whx2dpcg4jos` (`LAST_MODIFIER`),
  CONSTRAINT `FKghksaiu9qd6o4whx2dpcg4jos` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKi6us55shaoddnosb0djcfm6no` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKwrt8765rthgfdfghgfdasd46v` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CALENDAR_DEFINITION`
--

DROP TABLE IF EXISTS `CALENDAR_DEFINITION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CALENDAR_DEFINITION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `WEEK_STARTS_ON` enum('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday') DEFAULT NULL,
  `WORK_WEEK_STARTS_ON` enum('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday') DEFAULT NULL,
  `WORK_WEEK_ENDS_ON` enum('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday') DEFAULT NULL,
  `HALF_WORKING_DAY_AND_HALF_WEEKEND` bit(1) DEFAULT b'0',
  `CURRENT_YEAR` int DEFAULT NULL,
  `YEAR_STARTS_FROM` datetime(6) DEFAULT NULL,
  `YEAR_ENDS_ON` datetime(6) DEFAULT NULL,
  `STATUTORY_WEEKEND` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKl342xmj1rl6vkpf8l4mk3bjje` (`CREATOR`),
  KEY `FK4chx7bhtbititrcon8skykm7s` (`LAST_MODIFIER`),
  CONSTRAINT `FK4chx7bhtbititrcon8skykm7s` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKl342xmj1rl6vkpf8l4mk3bjje` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CALENDAR_DETAILS`
--

DROP TABLE IF EXISTS `CALENDAR_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CALENDAR_DETAILS` (
  `ID` int NOT NULL,
  `DAY` enum('Saturday','Sunday','Monday','Tuesday','Wednesday','Thursday','Friday') DEFAULT NULL,
  `WEEK_NO_1` bit(1) DEFAULT b'0',
  `WEEK_NO_2` bit(1) DEFAULT b'0',
  `WEEK_NO_3` bit(1) DEFAULT b'0',
  `WEEK_NO_4` bit(1) DEFAULT b'0',
  `WEEK_NO_5` bit(1) DEFAULT b'0',
  `CALENDAR_DEFINITION_FK` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CALENDAR_DEFINITION_FK` (`CALENDAR_DEFINITION_FK`),
  KEY `FKojd5f8wf2xy5gu0s6j7hhud` (`CREATOR`),
  KEY `FKbqhj95x8rcq569gu1s71d1h2d` (`LAST_MODIFIER`),
  CONSTRAINT `CALENDAR_DETAILS_ibfk_1` FOREIGN KEY (`CALENDAR_DEFINITION_FK`) REFERENCES `CALENDAR_DEFINITION` (`ID`),
  CONSTRAINT `FKbqhj95x8rcq569gu1s71d1h2d` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8wf2xy5gu0s6j7hhud` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CANDIDATE`
--

DROP TABLE IF EXISTS `CANDIDATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CANDIDATE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `WORKSPACE_ID` int DEFAULT '1',
  `ADDRESS_LINE1` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE2` varchar(100) DEFAULT NULL,
  `CITY` varchar(50) DEFAULT NULL,
  `STATE` varchar(50) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `POSTAL_CODE` int DEFAULT NULL,
  `ADDRESS_LINE11` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE21` varchar(100) DEFAULT NULL,
  `CITY1` varchar(50) DEFAULT NULL,
  `STATE1` varchar(50) DEFAULT NULL,
  `COUNTRY1` varchar(50) DEFAULT NULL,
  `POSTAL_CODE1` int DEFAULT NULL,
  `PRESENT_ADDRESS` longtext,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `PERMANENT_ADDRESS` longtext,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `EMAIL_ID` varchar(100) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `EXPERIENCE` int DEFAULT NULL,
  `UAN_NUMBER` varchar(20) DEFAULT NULL,
  `AADHAAR_CARD_NUMBER` varchar(20) DEFAULT NULL,
  `PAN_CARD_NUMBER` varchar(20) DEFAULT NULL,
  `SOURCE_OF_HIRE` varchar(100) DEFAULT NULL,
  `OFFICIAL_EMAIL` varchar(100) DEFAULT NULL,
  `SKILL_SET` varchar(100) DEFAULT NULL,
  `HIGHEST_QUALIFICATION` varchar(50) DEFAULT NULL,
  `LOCATION_ID` int DEFAULT NULL,
  `TITLE` varchar(50) DEFAULT NULL,
  `CURRENT_SALARY` double DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `ADDITIONAL_INFORMATION` longtext,
  `TENTATIVE_JOINING_DATE` datetime(6) DEFAULT NULL,
  `ONBOARDING_STATUS` enum('Pending','In-progress','Completed','Probationary-Period','Extended-Onboarding','On-Hold','Exited-During-Onboarding') DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CANDIDATE_ID` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `TIME_TO_ONBOARD` int DEFAULT NULL,
  `CANDIDATE_PHOTO` varchar(100) DEFAULT NULL,
  `ATTACHMENT1` varchar(100) DEFAULT NULL,
  `ATTACHMENT2` varchar(100) DEFAULT NULL,
  `CITIZENSHIP` varchar(50) DEFAULT NULL,
  `TEXT1` varchar(250) DEFAULT NULL,
  `TEXT2` varchar(250) DEFAULT NULL,
  `TEXT3` varchar(250) DEFAULT NULL,
  `TEXT4` varchar(250) DEFAULT NULL,
  `TEXT5` varchar(250) DEFAULT NULL,
  `TEXT6` varchar(250) DEFAULT NULL,
  `TEXT7` varchar(250) DEFAULT NULL,
  `TEXT8` varchar(250) DEFAULT NULL,
  `TEXT9` varchar(250) DEFAULT NULL,
  `TEXT10` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uc_email_id` (`EMAIL_ID`),
  KEY `LOCATION_ID` (`LOCATION_ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FKo355oii0a5lxxbkw3dtxihcg8` (`CREATOR`),
  KEY `FKb3vuafdaqe2t6rdxm93wr5ojv` (`LAST_MODIFIER`),
  CONSTRAINT `CANDIDATE_ibfk_1` FOREIGN KEY (`LOCATION_ID`) REFERENCES `LOCATION` (`ID`),
  CONSTRAINT `CANDIDATE_ibfk_2` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `FKb3vuafdaqe2t6rdxm93wr5ojv` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKo355oii0a5lxxbkw3dtxihcg8` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CANDIDATE_EDUCATION`
--

DROP TABLE IF EXISTS `CANDIDATE_EDUCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CANDIDATE_EDUCATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `SCHOOL_NAME` varchar(100) DEFAULT NULL,
  `DEGREE_DIPLOMA` varchar(50) DEFAULT NULL,
  `FIELD_OF_STUDY` varchar(100) DEFAULT NULL,
  `DATE_OF_COMPLETION` datetime(6) DEFAULT NULL,
  `ADDITIONAL_NOTES` longtext,
  `CANDIDATE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CANDIDATE_ID` (`CANDIDATE_ID`),
  KEY `FKm0sc9fj9gtwpgok1wyq34c7fs` (`CREATOR`),
  KEY `FKrwhd4u70b8j6s84nl96ugotha` (`LAST_MODIFIER`),
  CONSTRAINT `CANDIDATE_EDUCATION_ibfk_1` FOREIGN KEY (`CANDIDATE_ID`) REFERENCES `CANDIDATE` (`ID`),
  CONSTRAINT `FKm0sc9fj9gtwpgok1wyq34c7fs` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKrwhd4u70b8j6s84nl96ugotha` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CANDIDATE_EXPERIENCE`
--

DROP TABLE IF EXISTS `CANDIDATE_EXPERIENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CANDIDATE_EXPERIENCE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `OCCUPATION` varchar(50) DEFAULT NULL,
  `COMPANY` varchar(100) DEFAULT NULL,
  `SUMMARY` longtext,
  `DURATION` varchar(50) DEFAULT NULL,
  `CURRENTLY_WORK_HERE` bit(1) DEFAULT b'0',
  `ROWID` int DEFAULT NULL,
  `CANDIDATE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CANDIDATE_ID` (`CANDIDATE_ID`),
  KEY `FK14r9c6f43p9n8j0auwgt0bobh` (`CREATOR`),
  KEY `FKscv1fuhdck7ex54phr88v5ayu` (`LAST_MODIFIER`),
  CONSTRAINT `CANDIDATE_EXPERIENCE_ibfk_1` FOREIGN KEY (`CANDIDATE_ID`) REFERENCES `CANDIDATE` (`ID`),
  CONSTRAINT `FK14r9c6f43p9n8j0auwgt0bobh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKscv1fuhdck7ex54phr88v5ayu` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CASE_CATEGORY`
--

DROP TABLE IF EXISTS `CASE_CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CASE_CATEGORY` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `description` longtext,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CATEGORY_NAME` varchar(255) DEFAULT NULL,
  `TEXT1` varchar(200) DEFAULT NULL,
  `TEXT2` varchar(200) DEFAULT NULL,
  `TEXT3` varchar(200) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3irf16q9ucgv3foo0ok4sty89` (`CREATOR`),
  KEY `FKpcf2tbxvpdco9oiewgrvpok16` (`LAST_MODIFIER`),
  CONSTRAINT `FK3irf16q9ucgv3foo0ok4sty89` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKpcf2tbxvpdco9oiewgrvpok16` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CASES`
--

DROP TABLE IF EXISTS `CASES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CASES` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `CASE_ID` varchar(100) DEFAULT NULL,
  `CASE_CATEGORY_FK` int DEFAULT NULL,
  `SUBJECT` varchar(255) DEFAULT NULL,
  `AGENT` int DEFAULT NULL,
  `AGENT_DETAILS` varchar(255) DEFAULT NULL,
  `PRIORITY` enum('Low','Medium','High') DEFAULT NULL,
  `STATUS` enum('Open','In Progress','Pending','On Hold','Closed') DEFAULT NULL,
  `COMPLETED_BY` varchar(255) DEFAULT NULL,
  `COMPLETED_TIME` datetime(6) DEFAULT NULL,
  `SOURCE_OF_REQUEST` enum('Web','In Person','Email','Phone') DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `description` longtext,
  `UPLOAD` varchar(255) DEFAULT NULL,
  `ASSIGNED_TO` varchar(255) DEFAULT NULL,
  `GROUP_NAME` varchar(255) DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `TEXT1` varchar(200) DEFAULT NULL,
  `TEXT2` varchar(200) DEFAULT NULL,
  `TEXT3` varchar(200) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CASE_CATEGORY_FK` (`CASE_CATEGORY_FK`),
  KEY `AGENT` (`AGENT`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FK2cfy06fxnvkj6xsunn2t9i2e7` (`CREATOR`),
  KEY `FK5ej4vbxdq7tf3btv9se4fw7dp` (`LAST_MODIFIER`),
  CONSTRAINT `cases_ibfk_1` FOREIGN KEY (`CASE_CATEGORY_FK`) REFERENCES `CASE_CATEGORY` (`ID`),
  CONSTRAINT `cases_ibfk_2` FOREIGN KEY (`AGENT`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `cases_ibfk_3` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `FK2cfy06fxnvkj6xsunn2t9i2e7` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK5ej4vbxdq7tf3btv9se4fw7dp` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CITY`
--

DROP TABLE IF EXISTS `CITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CITY` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `STATE_ID` int DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `DISPLAY_NAME` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `STATE_ID` (`STATE_ID`),
  KEY `FKc75418g32i3gqjy3cnbsmkwd3` (`CREATOR`),
  KEY `FKku7fvaxvmi809tyq2ebm2kj8l` (`LAST_MODIFIER`),
  CONSTRAINT `CITY_ibfk_1` FOREIGN KEY (`STATE_ID`) REFERENCES `STATE` (`ID`),
  CONSTRAINT `FKc75418g32i3gqjy3cnbsmkwd3` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKku7fvaxvmi809tyq2ebm2kj8l` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CLIENTS`
--

DROP TABLE IF EXISTS `CLIENTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENTS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `CLIENT_NAME` varchar(50) DEFAULT NULL,
  `CURRENCY` varchar(50) DEFAULT NULL,
  `BILLING_METHOD` enum('Hourly Job Rate','Hourly User Rate','Hourly User Rate-Jobs','Hourly User Rate-Projects') DEFAULT NULL,
  `EMAIL_ID` varchar(100) DEFAULT NULL,
  `FIRST_NAME` varchar(100) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `MOBILE` varchar(20) DEFAULT NULL,
  `FAX` varchar(20) DEFAULT NULL,
  `STREET_ADDRESS` varchar(100) DEFAULT NULL,
  `CITY` varchar(50) DEFAULT NULL,
  `STATE_PROVINCE` varchar(50) DEFAULT NULL,
  `ZIPCODE` int DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `INDUSTRY` varchar(100) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `COMPANY_SIZE` int DEFAULT NULL,
  `DESCRIPTION` longtext,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uc_email_id` (`EMAIL_ID`),
  KEY `FK3qx1uow1mwpg54hm4wa9gag1w` (`CREATOR`),
  KEY `FKme2qgfc6qh2gg3ukk0l1n30k1` (`LAST_MODIFIER`),
  CONSTRAINT `FK3qx1uow1mwpg54hm4wa9gag1w` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKme2qgfc6qh2gg3ukk0l1n30k1` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COMPANY_DETAILS`
--

DROP TABLE IF EXISTS `COMPANY_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPANY_DETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `LOGO_UPLOAD` varchar(50) DEFAULT NULL,
  `COMPANY_NAME` varchar(100) DEFAULT NULL,
  `WEBSITE` varchar(100) DEFAULT NULL,
  `CONTACT_PERSON` varchar(50) DEFAULT NULL,
  `MOBILE_NUMBER` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE1` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE2` varchar(100) DEFAULT NULL,
  `CITY` varchar(50) DEFAULT NULL,
  `STATE` varchar(50) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `PINCODE` int DEFAULT NULL,
  `INDUSTRY` enum('Information Technology','Healthcare','Finance','Manufacturing','Retail','Hospitality and Tourism','Telecommunications','Education','Transportation and Logistics','Energy') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uc_email` (`EMAIL`),
  KEY `FKmm13bp7kbtcta1f5ssq0sit13` (`CREATOR`),
  KEY `FKbo171kv6f03w8fcm3t4ulwr74` (`LAST_MODIFIER`),
  CONSTRAINT `FKbo171kv6f03w8fcm3t4ulwr74` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKmm13bp7kbtcta1f5ssq0sit13` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COMPENSATION_STRUCTURE`
--

DROP TABLE IF EXISTS `COMPENSATION_STRUCTURE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPENSATION_STRUCTURE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `ROLE_ID` int DEFAULT NULL,
  `TITLE` varchar(200) DEFAULT NULL,
  `ANNUAL_SALARY_RANGE_FROM` double DEFAULT NULL,
  `ANNUAL_SALARY_RANGE_TO` double DEFAULT NULL,
  `ANNUAL_BONUS_FROM` double DEFAULT NULL,
  `ANNUAL_BONUS_TO` double DEFAULT NULL,
  `LTI` enum('Yes','No') DEFAULT NULL,
  `RETIREMENT_PLAN` enum('Yes','No') DEFAULT NULL,
  `LIFE_INSURANCE_PACKAGES` varchar(200) DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FKg5b3f1fho6yh1xtd89v34x4vf` (`CREATOR`),
  KEY `FKsk56gikv8hi8haes6rolhsvv1` (`LAST_MODIFIER`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `compensation_structure_ibfk_1` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `compensation_structure_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `FKg5b3f1fho6yh1xtd89v34x4vf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKsk56gikv8hi8haes6rolhsvv1` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COMPENSATORY_REQUEST_SCHEDULER`
--

DROP TABLE IF EXISTS `COMPENSATORY_REQUEST_SCHEDULER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPENSATORY_REQUEST_SCHEDULER` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_NAME` varchar(50) DEFAULT NULL,
  `WORKED_DATE` datetime(6) DEFAULT NULL,
  `UNIT` bit(1) DEFAULT b'0',
  `DURATION` enum('Full Day','Half Day','Hours') DEFAULT NULL,
  `EXPIRY_DATE` datetime(6) DEFAULT NULL,
  `ATTENDANCE_DETAILS` longtext,
  `REASON` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6tlonshfpc4503nlice7nlb2a` (`CREATOR`),
  KEY `FKhul41g3aq8kxafqvc71yep4xx` (`LAST_MODIFIER`),
  CONSTRAINT `FK6tlonshfpc4503nlice7nlb2a` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKhul41g3aq8kxafqvc71yep4xx` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COMPETENCIES`
--

DROP TABLE IF EXISTS `COMPETENCIES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPETENCIES` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `COMPETENCY_NAME` varchar(200) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `ACTUAL_LEVEL` int DEFAULT NULL,
  `REQUIRED_LEVEL` int DEFAULT NULL,
  `weightage` double DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `EMPLOYEE_PROGRESS` double DEFAULT NULL,
  `REVIEWED_PROGRESS` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK16029eeo18ovyk8mdnru4m8ea` (`CREATOR`),
  KEY `FK1wwwbc933ia91ygksryvbvie2` (`LAST_MODIFIER`),
  CONSTRAINT `FK16029eeo18ovyk8mdnru4m8ea` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK1wwwbc933ia91ygksryvbvie2` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COMPONENT`
--

DROP TABLE IF EXISTS `COMPONENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPONENT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `NAME` varchar(100) DEFAULT NULL,
  `TYPE` enum('EARNING','DEDUCTION','BONUS') DEFAULT NULL,
  `IS_ATTENDANCE_IMPACTING` bit(1) DEFAULT b'0',
  `RECURRING_TYPE` enum('MONTHLY','YEARLY','QUATERLY','ONE_TIME_PAY') DEFAULT NULL,
  `DATA_FIELD` varchar(20) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `INPUT_MODEL` enum('CALCULATE','MANUAL') DEFAULT NULL,
  `INPUT` varchar(100) DEFAULT NULL,
  `FORMULA` varchar(200) DEFAULT NULL,
  `CALCULATION_CONDITION` varchar(200) DEFAULT NULL,
  `MODE` enum('INDEPENDENT','DERIVED') DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKsn3ffmvpgfx91vg4xjomrv7od` (`CREATOR`),
  KEY `FKb7ys1gjgi4ybgytlrahgg18l4` (`LAST_MODIFIER`),
  CONSTRAINT `FKb7ys1gjgi4ybgytlrahgg18l4` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKsn3ffmvpgfx91vg4xjomrv7od` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COUNTRY`
--

DROP TABLE IF EXISTS `COUNTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COUNTRY` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(50) DEFAULT NULL,
  `DISPLAY_NAME` varchar(50) DEFAULT NULL,
  `COUNTRY_CODE` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKcgggq7m5nu63ysv3jbvsowjof` (`CREATOR`),
  KEY `FKuqflkyaggxu9wegxajlddf60` (`LAST_MODIFIER`),
  CONSTRAINT `FKcgggq7m5nu63ysv3jbvsowjof` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKuqflkyaggxu9wegxajlddf60` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DELEGATIONS`
--

DROP TABLE IF EXISTS `DELEGATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DELEGATIONS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `DELEGATOR_ID` int DEFAULT NULL,
  `DELEGATEE_ID` int DEFAULT NULL,
  `TYPE` enum('Temporary','Permanent') DEFAULT NULL,
  `FROM_DATE` datetime(6) DEFAULT NULL,
  `TO_DATE` datetime(6) DEFAULT NULL,
  `NOTIFICATION` enum('Delegator And Delegatee','Delegatee') DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DELEGATOR_ID` (`DELEGATOR_ID`),
  KEY `DELEGATEE_ID` (`DELEGATEE_ID`),
  KEY `FKaeibeen9frhk1su9fks90vxuf` (`CREATOR`),
  KEY `FKfsq4s5ywswnd2oi636oqc8yci` (`LAST_MODIFIER`),
  CONSTRAINT `DELEGATIONS_ibfk_1` FOREIGN KEY (`DELEGATOR_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `DELEGATIONS_ibfk_2` FOREIGN KEY (`DELEGATEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKaeibeen9frhk1su9fks90vxuf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKfsq4s5ywswnd2oi636oqc8yci` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DEPARTMENT`
--



--
-- Table structure for table `DESIGNATION`
--

DROP TABLE IF EXISTS `DESIGNATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DESIGNATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `NAME` varchar(50) DEFAULT NULL,
  `DEPARTMENT_ID` varchar(100) DEFAULT NULL,
  `DESIGNATION_CODE` varchar(50) DEFAULT NULL,
  `PARENT_DESIGNATION` int DEFAULT NULL,
  `REPORTING_MANAGER` int DEFAULT NULL,
  `JOB_GRADE` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PARENT_DESIGNATION` (`PARENT_DESIGNATION`),
  KEY `REPORTING_MANAGER` (`REPORTING_MANAGER`),
  KEY `FK4jyd4ggcmvmdk740lcwmvomas` (`CREATOR`),
  KEY `FK445sht0j3up6irlr1sdcsnyx3` (`LAST_MODIFIER`),
  CONSTRAINT `designation_ibfk_1` FOREIGN KEY (`PARENT_DESIGNATION`) REFERENCES `DESIGNATION` (`ID`),
  CONSTRAINT `designation_ibfk_2` FOREIGN KEY (`REPORTING_MANAGER`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FK445sht0j3up6irlr1sdcsnyx3` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK4jyd4ggcmvmdk740lcwmvomas` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DESIGNATION_OLD`
--

DROP TABLE IF EXISTS `DESIGNATION_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DESIGNATION_OLD` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(50) DEFAULT NULL,
  `MAIL_ALIAS` varchar(100) DEFAULT NULL,
  `DESIGNATION_CODE` varchar(50) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `PARENT_DESIGNATION` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKp1tvilciqttd5k8ri3e8ka5ve` (`CREATOR`),
  KEY `FK74og0it085blf5f2j9f2vhi2k` (`LAST_MODIFIER`),
  CONSTRAINT `FK74og0it085blf5f2j9f2vhi2k` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKp1tvilciqttd5k8ri3e8ka5ve` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `EMPLOYEE`
--

DROP TABLE IF EXISTS `EMPLOYEE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `COUNTRY_OF_RESIDENCE` varchar(50) DEFAULT NULL,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `FULL_NAME` varchar(70) DEFAULT NULL,
  `ARABIC_FIRST_NAME` varchar(50) DEFAULT NULL,
  `ARABIC_MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `ARABIC_LAST_NAME` varchar(50) DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `LOCATION_ID` int DEFAULT NULL,
  `DESIGNATION_ID` int DEFAULT NULL,
  `EMPLOYMENT_TYPE` varchar(100) DEFAULT NULL,
  `EMPLOYMENT_STATUS` enum('Active','Inactive','Terminated') DEFAULT NULL,
  `DATE_OF_JOINING` datetime DEFAULT NULL,
  `REPORTING_MANAGER` int DEFAULT NULL,
  `DATE_OF_BIRTH` datetime DEFAULT NULL,
  `HIJRI_DATE_OF_BIRTH` datetime DEFAULT NULL,
  `GENDER` enum('Male','Female','Other') DEFAULT NULL,
  `MARITAL_STATUS` enum('Single','Married','Divorced','Widowed','Other') DEFAULT NULL,
  `SOURCE_HIRE` enum('Internal-Referral','Job-Boards','Career-Sites','Recruitment-Agencies','Social-Media','Direct-Applications','Campus-Recruitment','Employee-Referral-Programs') DEFAULT NULL,
  `WORK_PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `ALTERNATIVE_PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `WORK_EMAIL_ADDRESS` varchar(100) DEFAULT NULL,
  `PERSONAL_MOBILE_NUMBER` varchar(20) DEFAULT NULL,
  `PERSONAL_EMAIL_ADDRESS` varchar(100) DEFAULT NULL,
  `BLOOD_GROUP` varchar(50) DEFAULT NULL,
  `DATE_OF_EXIT` datetime DEFAULT NULL,
  `EMPLOYEE_ID` varchar(100) DEFAULT NULL,
  `USERID_PK` int DEFAULT NULL,
  `CITIZENSHIP` varchar(50) DEFAULT NULL,
  `COMPANY` varchar(50) DEFAULT NULL,
  `CONTRACT_TYPE` varchar(100) DEFAULT NULL,
  `PRIMARY_SHORT_ADDRESS` varchar(100) DEFAULT NULL,
  `PRIMARY_ADDRESS_BUILDING_NUMBER` varchar(30) DEFAULT NULL,
  `PRIMARY_ADDRESS_POSTAL_CODE` varchar(100) DEFAULT NULL,
  `PRIMARY_ADDRESS_CITY` varchar(100) DEFAULT NULL,
  `PRIMARY_ADDRESS_COUNTRY` varchar(100) DEFAULT NULL,
  `IBAN` varchar(100) DEFAULT NULL,
  `ATTACHMENT1` varchar(100) DEFAULT NULL,
  `ATTACHMENT2` varchar(100) DEFAULT NULL,
  `TEXT1` varchar(250) DEFAULT NULL,
  `TEXT2` varchar(250) DEFAULT NULL,
  `TEXT3` varchar(250) DEFAULT NULL,
  `TEXT4` varchar(250) DEFAULT NULL,
  `TEXT5` varchar(250) DEFAULT NULL,
  `TEXT6` varchar(250) DEFAULT NULL,
  `TEXT7` varchar(250) DEFAULT NULL,
  `TEXT8` varchar(250) DEFAULT NULL,
  `TEXT9` varchar(250) DEFAULT NULL,
  `TEXT10` varchar(250) DEFAULT NULL,
  `REPORTING_MANAGER_USERID_FK` int DEFAULT NULL,
  `RELIGION` varchar(50) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `JOB_GRADE` int DEFAULT NULL,
  `GIVEN_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `WORK_EMAIL_ADDRESS` (`WORK_EMAIL_ADDRESS`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `LOCATION_ID` (`LOCATION_ID`),
  KEY `DESIGNATION_ID` (`DESIGNATION_ID`),
  KEY `REPORTING_MANAGER` (`REPORTING_MANAGER`),
  KEY `FKryunt4ywp525bi3f53lk5qjam` (`CREATOR`),
  KEY `FKhfqj4fkf32oyx6srsihcnsyf` (`LAST_MODIFIER`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`LOCATION_ID`) REFERENCES `LOCATION` (`ID`),
  CONSTRAINT `employee_ibfk_3` FOREIGN KEY (`DESIGNATION_ID`) REFERENCES `DESIGNATION` (`ID`),
  CONSTRAINT `employee_ibfk_4` FOREIGN KEY (`REPORTING_MANAGER`) REFERENCES `EMPLOYEE` (`ID`),
  CONSTRAINT `FKhfqj4fkf32oyx6srsihcnsyf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKryunt4ywp525bi3f53lk5qjam` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=383 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_COMPETENCIES_MAPPING`
--

DROP TABLE IF EXISTS `EMPLOYEE_COMPETENCIES_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_COMPETENCIES_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `COMPETENCIES_ID` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REVIEWER_ID` int DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `COMPETENCIES_ID` (`COMPETENCIES_ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `REVIEWER_ID` (`REVIEWER_ID`),
  KEY `FKgspxybc8jyegexw93byvcc2in` (`CREATOR`),
  KEY `FKielyvt0upi5c90hy4oc4g00gv` (`LAST_MODIFIER`),
  CONSTRAINT `employee_competencies_mapping_ibfk_1` FOREIGN KEY (`COMPETENCIES_ID`) REFERENCES `COMPETENCIES` (`ID`),
  CONSTRAINT `employee_competencies_mapping_ibfk_2` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_competencies_mapping_ibfk_3` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKgspxybc8jyegexw93byvcc2in` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKielyvt0upi5c90hy4oc4g00gv` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_COMPLIANCE_LEGAL`
--

DROP TABLE IF EXISTS `EMPLOYEE_COMPLIANCE_LEGAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_COMPLIANCE_LEGAL` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `IBAN_CERTIFICATE` varchar(200) DEFAULT NULL,
  `NATIONAL_ADDRESS_CERTIFICATE` varchar(200) DEFAULT NULL,
  `NATIONAL_IDENTIFICATION_NUMBER` varchar(200) DEFAULT NULL,
  `NATIONAL_IDENTIFICATION_NUMBER_PASSPORT` varchar(200) DEFAULT NULL,
  `CODE_OF_CONDUCT` varchar(200) DEFAULT NULL,
  `CONFLICT_OF_INTEREST` varchar(200) DEFAULT NULL,
  `EMPLOYMENT_CONTRACT` varchar(200) DEFAULT NULL,
  `CYBER_COMPLIANCE` varchar(200) DEFAULT NULL,
  `COMPANY_ASSET_AGREEMENT` varchar(200) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKos17w34xr5oncrhaw9xmw7pdy` (`CREATOR`),
  KEY `FKg80s7x1cp53vx7ffkxv0yyqcx` (`LAST_MODIFIER`),
  CONSTRAINT `employee_compliance_legal_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKg80s7x1cp53vx7ffkxv0yyqcx` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKos17w34xr5oncrhaw9xmw7pdy` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_COMPONENT_MAPPING`
--

DROP TABLE IF EXISTS `EMPLOYEE_COMPONENT_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_COMPONENT_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `COMPONENT_FK` int DEFAULT NULL,
  `SALARY_STRUCTURE_FK` int DEFAULT NULL,
  `COMPONENT_PAY_START_DATE` datetime DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `VERSION` int DEFAULT NULL,
  `INPUT` varchar(100) DEFAULT NULL,
  `EMPOLYEE_PK` int DEFAULT NULL,
  `VALUE` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `COMPONENT_FK` (`COMPONENT_FK`),
  KEY `FKro6wgpmhdplftg2lawryhbwsh` (`CREATOR`),
  KEY `FKikwybiriql8n29errefsc67rf` (`LAST_MODIFIER`),
  CONSTRAINT `employee_component_mapping_ibfk_1` FOREIGN KEY (`COMPONENT_FK`) REFERENCES `COMPONENT` (`ID`),
  CONSTRAINT `FKikwybiriql8n29errefsc67rf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKro6wgpmhdplftg2lawryhbwsh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_DAILY_ATTENDANCE`
--

DROP TABLE IF EXISTS `EMPLOYEE_DAILY_ATTENDANCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_DAILY_ATTENDANCE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `LOGIN_DATE` datetime DEFAULT NULL,
  `EXPECTED_FROM_TIME` datetime DEFAULT NULL,
  `EXPECTED_TO_TIME` datetime DEFAULT NULL,
  `FIRST_CHECK_IN` datetime DEFAULT NULL,
  `FIRST_CHECK_OUT` datetime DEFAULT NULL,
  `EARLY_ENTRY` datetime DEFAULT NULL,
  `LATE_ENTRY` datetime DEFAULT NULL,
  `EARLY_EXIT` datetime DEFAULT NULL,
  `LATE_EXIT` datetime DEFAULT NULL,
  `OVERTIME` datetime DEFAULT NULL,
  `DEVIATION_TIME` datetime DEFAULT NULL,
  `FIRST_CHECK_IN_LOCATION` varchar(100) DEFAULT NULL,
  `LAST_CHECK_OUT_LOCATION` varchar(100) DEFAULT NULL,
  `TOTAL_HOURS` double DEFAULT NULL,
  `PAYABLE_HOURS` double DEFAULT NULL,
  `SHIFTS` bit(1) DEFAULT b'0',
  `TIME_ZONE` datetime DEFAULT NULL,
  `LATE_NIGHT_OVERTIME_HOURS` double DEFAULT NULL,
  `SHIFT_ALLOWANCE` varchar(20) DEFAULT NULL,
  `COMMENTS` varchar(40) DEFAULT NULL,
  `EMPLOYEEID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `STATUS` enum('Present','Absent','Leave') DEFAULT NULL,
  `EMPLOYEE_LEAVE_TYPE_FK` int DEFAULT NULL,
  `LEAVES_FK` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEEID` (`EMPLOYEEID`),
  KEY `FKojd5f8wf2xy5gu0s6jg47hhud` (`CREATOR`),
  KEY `FKbqhj95x8rcq569gu1s71end1h2d` (`LAST_MODIFIER`),
  KEY `EMPLOYEE_LEAVE_TYPE_FK` (`EMPLOYEE_LEAVE_TYPE_FK`),
  KEY `LEAVES_FK` (`LEAVES_FK`),
  CONSTRAINT `EMPLOYEE_DAILY_ATTENDANCE_ibfk_1` FOREIGN KEY (`EMPLOYEEID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `EMPLOYEE_DAILY_ATTENDANCE_ibfk_2` FOREIGN KEY (`EMPLOYEE_LEAVE_TYPE_FK`) REFERENCES `EMPLOYEE_LEAVE_TYPE` (`ID`),
  CONSTRAINT `EMPLOYEE_DAILY_ATTENDANCE_ibfk_3` FOREIGN KEY (`LEAVES_FK`) REFERENCES `LEAVES` (`ID`),
  CONSTRAINT `FKbqhj95x8rcq569gu1s71end1h2d` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8wf2xy5gu0s6jg47hhud` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_DEPENDENT_DETAILS`
--

DROP TABLE IF EXISTS `EMPLOYEE_DEPENDENT_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_DEPENDENT_DETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `FIRST_NAME` varchar(100) DEFAULT NULL,
  `MIDDLE_NAME` varchar(100) DEFAULT NULL,
  `LAST_NAME` varchar(100) DEFAULT NULL,
  `FULL_NAME` varchar(200) DEFAULT NULL,
  `CONTACT_NUMBER` varchar(100) DEFAULT NULL,
  `DATE_OF_BIRTH` datetime DEFAULT NULL,
  `DEPENDENT_IDENTIFICATION` varchar(255) DEFAULT NULL,
  `RELATIONSHIP` enum('Mother','Father','Brother','Sister','Wife','Child','Husband') DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FK2prwj6dip8i00m271j5yugni` (`CREATOR`),
  KEY `FKhvyljobhlmubhbm99woq26ygm` (`LAST_MODIFIER`),
  CONSTRAINT `employee_dependent_details_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FK2prwj6dip8i00m271j5yugni` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKhvyljobhlmubhbm99woq26ygm` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_EMERGENCY_CONTACT`
--

DROP TABLE IF EXISTS `EMPLOYEE_EMERGENCY_CONTACT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_EMERGENCY_CONTACT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `EMERGENCY_CONTACT_FIRST_NAME` varchar(50) DEFAULT NULL,
  `EMERGENCY_CONTACT_MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `EMERGENCY_CONTACT_LAST_NAME` varchar(50) DEFAULT NULL,
  `EMERGENCY_CONTACT_FULL_NAME` varchar(100) DEFAULT NULL,
  `EMERGENCY_CONTACT_NUMBER` varchar(50) DEFAULT NULL,
  `EMERGENCY_CONTACT_EMAIL_ADDRESS` varchar(80) DEFAULT NULL,
  `RELATIONSHIP` varchar(50) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKryunt4ywp525bi3f53jik5qjam` (`CREATOR`),
  KEY `FKhfqj4fkf32oyx6srfdihcnsyf` (`LAST_MODIFIER`),
  CONSTRAINT `employee_emegency_contact_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`),
  CONSTRAINT `FKhfqj4fkf32oyx6srfdihcnsyf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKryunt4ywp525bi3f53jik5qjam` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_EXPENSES`
--

DROP TABLE IF EXISTS `EMPLOYEE_EXPENSES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_EXPENSES` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `EXPENSE_CATEGORY` varchar(200) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CURRENCY` varchar(200) DEFAULT NULL,
  `AMOUNT_SPENT` double DEFAULT NULL,
  `TAX_AMOUNT` double DEFAULT NULL,
  `TOTAL_AMOUNT_REIMBURSED` double DEFAULT NULL,
  `PAYMENT_METHOD` varchar(200) DEFAULT NULL,
  `PAYMENT_STATUS` varchar(200) DEFAULT NULL,
  `RECIEPTS` varchar(200) DEFAULT NULL,
  `APPROVAL_STATUS` varchar(200) DEFAULT NULL,
  `REIMBURSEMENT_PAYMENT_METHOD` varchar(200) DEFAULT NULL,
  `DATE_OF_EXPENSE` datetime(6) DEFAULT NULL,
  `REIMBURSEMENT_DATE` datetime(6) DEFAULT NULL,
  `DESCRIPTION_OF_EXPENSE` longtext,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKb494fgxslqpf3gjbgwm2kkMI1` (`CREATOR`),
  KEY `FKgdf83vor7ln1l504p116v80SE2` (`LAST_MODIFIER`),
  CONSTRAINT `EMPLOYEE_EXPENSES_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKb494fgxslqpf3gjbgwm2kkMI1` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKgdf83vor7ln1l504p116v80SE2` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_FEEDBACK_MAPPING`
--

DROP TABLE IF EXISTS `EMPLOYEE_FEEDBACK_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_FEEDBACK_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `FEEDBACK_ID` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REVIEWER_ID` int DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FEEDBACK_ID` (`FEEDBACK_ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `REVIEWER_ID` (`REVIEWER_ID`),
  KEY `FKqpq2cp9ymnlcwumeik905rri7` (`CREATOR`),
  KEY `FKcbjl0ja16vp32jikrup6hu07y` (`LAST_MODIFIER`),
  CONSTRAINT `employee_feedback_mapping_ibfk_1` FOREIGN KEY (`FEEDBACK_ID`) REFERENCES `FEEDBACK` (`ID`),
  CONSTRAINT `employee_feedback_mapping_ibfk_2` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_feedback_mapping_ibfk_3` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKcbjl0ja16vp32jikrup6hu07y` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKqpq2cp9ymnlcwumeik905rri7` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_GOALS_MAPPING`
--

DROP TABLE IF EXISTS `EMPLOYEE_GOALS_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_GOALS_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `GOALS_ID` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REVIEWER_ID` int DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `GOALS_ID` (`GOALS_ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `REVIEWER_ID` (`REVIEWER_ID`),
  KEY `FKowx9r6lcwjabvo64es9smpbgq` (`CREATOR`),
  KEY `FKg7w0cp9bpmd1fiqpvmr46lyjk` (`LAST_MODIFIER`),
  CONSTRAINT `employee_goals_mapping_ibfk_1` FOREIGN KEY (`GOALS_ID`) REFERENCES `GOALS` (`ID`),
  CONSTRAINT `employee_goals_mapping_ibfk_2` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_goals_mapping_ibfk_3` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKg7w0cp9bpmd1fiqpvmr46lyjk` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKowx9r6lcwjabvo64es9smpbgq` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_KRA_MAPPING`
--

DROP TABLE IF EXISTS `EMPLOYEE_KRA_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_KRA_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `KRA_ID` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REVIEWER_ID` int DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KRA_ID` (`KRA_ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `REVIEWER_ID` (`REVIEWER_ID`),
  KEY `FKye48mv9cbugquo8mgvtaqnjw` (`CREATOR`),
  KEY `FKpgefdf2e2mj6hrbfepk3u12rr` (`LAST_MODIFIER`),
  CONSTRAINT `employee_kra_mapping_ibfk_1` FOREIGN KEY (`KRA_ID`) REFERENCES `KEY_RESULT_AREA` (`ID`),
  CONSTRAINT `employee_kra_mapping_ibfk_2` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_kra_mapping_ibfk_3` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKpgefdf2e2mj6hrbfepk3u12rr` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKye48mv9cbugquo8mgvtaqnjw` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_LEAVE_TYPE`
--

DROP TABLE IF EXISTS `EMPLOYEE_LEAVE_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_LEAVE_TYPE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `BALANCE` double DEFAULT NULL,
  `LEAVE_TYPE_FK` int DEFAULT NULL,
  `EMPLOYEE_ID_FK` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `TOTAL_BALANCE` double DEFAULT NULL,
  `YEAR_END_DATE` datetime(6) DEFAULT NULL,
  `YEAR_START_DATE` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `LEAVE_TYPE_FK` (`LEAVE_TYPE_FK`),
  KEY `EMPLOYEE_ID_FK` (`EMPLOYEE_ID_FK`),
  KEY `CREATOR` (`CREATOR`),
  KEY `LAST_MODIFIER` (`LAST_MODIFIER`),
  CONSTRAINT `EMPLOYEE_LEAVE_TYPE_ibfk_1` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `EMPLOYEE_LEAVE_TYPE_ibfk_2` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `EMPLOYEE_LEAVE_TYPE_ibfk_3` FOREIGN KEY (`LEAVE_TYPE_FK`) REFERENCES `LEAVE_TYPE` (`ID`),
  CONSTRAINT `EMPLOYEE_LEAVE_TYPE_ibfk_4` FOREIGN KEY (`EMPLOYEE_ID_FK`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_MONTHLY_ATTENDANCE`
--

DROP TABLE IF EXISTS `EMPLOYEE_MONTHLY_ATTENDANCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_MONTHLY_ATTENDANCE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_ID` int DEFAULT NULL,
  `PAYABLE_DAYS` int DEFAULT NULL,
  `WORKING_DAYS` int DEFAULT NULL,
  `TOTAL_PAYABLE_DAYS` int DEFAULT NULL,
  `TOTAL_WORKED_DAYS` int DEFAULT NULL,
  `PAID_LEAVE_IN_DAYS` int DEFAULT NULL,
  `GOV_HOLIDAYS_IN_DAYS` int DEFAULT NULL,
  `WEEKENDS_IN_DAYS` int DEFAULT NULL,
  `TOTAL_PAID_OFF_IN_DAYS` int DEFAULT NULL,
  `UNPAID_LEAVE_IN_DAYS` int DEFAULT NULL,
  `TOTAL_ABSENT_IN_DAYS` int DEFAULT NULL,
  `EXPECTED_PAYABLE_HOURS` varchar(50) DEFAULT NULL,
  `ACTUAL_WORKED_HOURS` varchar(50) DEFAULT NULL,
  `PAID_LEAVE_IN_HOURS` varchar(50) DEFAULT NULL,
  `TOTAL_PAYABLE_HOURS` varchar(50) DEFAULT NULL,
  `EXPECTED_WORKING_HOURS` varchar(50) DEFAULT NULL,
  `TOTAL_WORKED_HOURS` varchar(50) DEFAULT NULL,
  `OVERTIME_HOURS` varchar(50) DEFAULT NULL,
  `START_DATE` datetime(6) DEFAULT NULL,
  `END_DATE` datetime(6) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKs7bvsugsw1sod48o91i6c9dw1` (`CREATOR`),
  KEY `FKe9hpcxb30uj5jdxqj1t2juqi0` (`LAST_MODIFIER`),
  CONSTRAINT `EMPLOYEE_MONTHLY_ATTENDANCE_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKe9hpcxb30uj5jdxqj1t2juqi0` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKs7bvsugsw1sod48o91i6c9dw1` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_NATIONAL_IDENTIFICATION`
--

DROP TABLE IF EXISTS `EMPLOYEE_NATIONAL_IDENTIFICATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_NATIONAL_IDENTIFICATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `TYPE` varchar(100) DEFAULT NULL,
  `IDENTIFICATION_NUMBER` varchar(50) DEFAULT NULL,
  `SCANNED_IMAGE` varchar(255) DEFAULT NULL,
  `BORDER_NUMBER` varchar(100) DEFAULT NULL,
  `EXPIRY_DATE` datetime DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKn0l60sqh0s0by7j7ypqu7btxu` (`CREATOR`),
  KEY `FK10x3qf8l03l6mi3gd8f6tvu1c` (`LAST_MODIFIER`),
  CONSTRAINT `employee_national_identification_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FK10x3qf8l03l6mi3gd8f6tvu1c` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKn0l60sqh0s0by7j7ypqu7btxu` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_OLD`
--

DROP TABLE IF EXISTS `EMPLOYEE_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_OLD` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `WORKSPACE_ID` int DEFAULT '1',
  `ADDRESS_LINE1` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE2` varchar(100) DEFAULT NULL,
  `CITY` varchar(50) DEFAULT NULL,
  `STATE` varchar(50) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `POSTAL_CODE` int DEFAULT NULL,
  `ADDRESS_LINE11` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE21` varchar(100) DEFAULT NULL,
  `CITY1` varchar(50) DEFAULT NULL,
  `STATE1` varchar(50) DEFAULT NULL,
  `COUNTRY1` varchar(50) DEFAULT NULL,
  `POSTAL_CODE1` int DEFAULT NULL,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `NICK_NAME` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `LOCATION_ID` int DEFAULT NULL,
  `DESIGNATION_ID` int DEFAULT NULL,
  `AADHAR_CARD` varchar(50) DEFAULT NULL,
  `ORG_ROLE` varchar(50) DEFAULT NULL,
  `EMPLOYEMENT_TYPE` varchar(100) DEFAULT NULL,
  `EMPLOYEMENT_STATUS` enum('Active','Inactive') DEFAULT NULL,
  `SOURCE_HIRE` enum('Internal-Referral','Job-Boards','Career-Sites','Recruitment-Agencies','Social-Media','Direct-Applications','Campus-Recruitment','Employee-Referral-Programs') DEFAULT NULL,
  `DATE_OF_JOINING` datetime(6) DEFAULT NULL,
  `CURRENT_EXPERIENCE` int DEFAULT NULL,
  `TOTAL_EXPERIENCE` int DEFAULT NULL,
  `REPORTING_MANAGER` int DEFAULT NULL,
  `DATE_OF_BIRTH` datetime(6) DEFAULT NULL,
  `AGE` int DEFAULT NULL,
  `GENDER` enum('Male','Female','Other') DEFAULT NULL,
  `MARITIAL_STATUS` enum('Single','Married','Divorced','Widowed','Other') DEFAULT NULL,
  `ABOUT_ME` varchar(100) DEFAULT NULL,
  `EXPERTISE` varchar(100) DEFAULT NULL,
  `UNIQUE_IDENTIFICATION` varchar(20) DEFAULT NULL,
  `WORK_PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `EXTENSION` varchar(50) DEFAULT NULL,
  `SEATING_LOCATION` varchar(50) DEFAULT NULL,
  `TAGS` varchar(50) DEFAULT NULL,
  `PERSONAL_MOBILE_NUMBER` varchar(20) DEFAULT NULL,
  `PERSONAL_EMAIL_ADDRESS` varchar(100) DEFAULT NULL,
  `DATE_OF_EXIT` datetime(6) DEFAULT NULL,
  `ONBOARDING_STATUS` enum('Pending','In-progress','Completed','Probationary-Period','Extended-Onboarding','On-Hold','Exited-During-Onboarding') DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `ANNUAL_SALARY` varchar(255) DEFAULT NULL,
  `BONUSES` varchar(255) DEFAULT NULL,
  `BENEFITS` varchar(100) DEFAULT NULL,
  `ALLOWANCES` varchar(100) DEFAULT NULL,
  `TAXES` varchar(100) DEFAULT NULL,
  `TRAINING_COST` varchar(255) DEFAULT NULL,
  `RECRUITMENT_COST` varchar(255) DEFAULT NULL,
  `ONBOARDING_COST` varchar(255) DEFAULT NULL,
  `EMPLOYEE_ID` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `EMPLOYEE_PHOTO` varchar(100) DEFAULT NULL,
  `FULL_NAME` varchar(70) DEFAULT NULL,
  `USERID_PK` int DEFAULT NULL,
  `SKILL_SET` varchar(100) DEFAULT NULL,
  `ATTACHMENT1` varchar(100) DEFAULT NULL,
  `ATTACHMENT2` varchar(100) DEFAULT NULL,
  `CITIZENSHIP` varchar(50) DEFAULT NULL,
  `TEXT1` varchar(250) DEFAULT NULL,
  `TEXT2` varchar(250) DEFAULT NULL,
  `TEXT3` varchar(250) DEFAULT NULL,
  `TEXT4` varchar(250) DEFAULT NULL,
  `TEXT5` varchar(250) DEFAULT NULL,
  `TEXT6` varchar(250) DEFAULT NULL,
  `TEXT7` varchar(250) DEFAULT NULL,
  `TEXT8` varchar(250) DEFAULT NULL,
  `TEXT9` varchar(250) DEFAULT NULL,
  `TEXT10` varchar(250) DEFAULT NULL,
  `REPORTING_MANAGER_USERID_FK` int DEFAULT NULL,
  `RELIGION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uc_email` (`EMAIL`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `LOCATION_ID` (`LOCATION_ID`),
  KEY `DESIGNATION_ID` (`DESIGNATION_ID`),
  KEY `FKirmuqjf8k18t72nbyyw0p5cab` (`CREATOR`),
  KEY `FK2rq39dovka9cv5nneo3v9vkjr` (`LAST_MODIFIER`),
  KEY `FK_Employee_REPORTING_MANAGER` (`REPORTING_MANAGER`),
  CONSTRAINT `EMPLOYEE_OLD_ibfk_1` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `EMPLOYEE_OLD_ibfk_2` FOREIGN KEY (`LOCATION_ID`) REFERENCES `LOCATION` (`ID`),
  CONSTRAINT `EMPLOYEE_OLD_ibfk_3` FOREIGN KEY (`DESIGNATION_ID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `FK2rq39dovka9cv5nneo3v9vkjr` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK_Employee_REPORTING_MANAGER` FOREIGN KEY (`REPORTING_MANAGER`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKirmuqjf8k18t72nbyyw0p5cab` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=382 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_REVIEW`
--

DROP TABLE IF EXISTS `EMPLOYEE_REVIEW`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_REVIEW` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `APPRAISAL_CYCLE_FK` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REVIEWER_ID` int DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `KRA_AVERAGE_EMPLOYEE` double DEFAULT NULL,
  `COMPETENCIES_AVERAGE_EMPLOYEE` double DEFAULT NULL,
  `GOALS_AVERAGE_EMPLOYEE` double DEFAULT NULL,
  `SKILLS_AVERAGE_EMPLOYEE` double DEFAULT NULL,
  `FEEDBACK_AVERAGE_EMPLOYEE` double DEFAULT NULL,
  `KRA_AVERAGE_REVIEWER` double DEFAULT NULL,
  `SKILLS_AVERAGE_REVIEWER` double DEFAULT NULL,
  `COMPETENCIES_AVERAGE_REVIEWER` double DEFAULT NULL,
  `GOALS_AVERAGE_REVIEWER` double DEFAULT NULL,
  `FEEDBACK_AVERAGE_REVIEWER` double DEFAULT NULL,
  `OVERALL_AVERAGE_EMPLOYEE` double DEFAULT NULL,
  `OVERALL_AVERAGE_REVIEWER` double DEFAULT NULL,
  `COMMENT` varchar(200) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `APPRAISAL_CYCLE_FK` (`APPRAISAL_CYCLE_FK`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `REVIEWER_ID` (`REVIEWER_ID`),
  KEY `FKl5nhosx86fgrxaniqrpo19qre` (`CREATOR`),
  KEY `FKquq35mo706obtb5s2p60vjlvj` (`LAST_MODIFIER`),
  CONSTRAINT `employee_review_ibfk_1` FOREIGN KEY (`APPRAISAL_CYCLE_FK`) REFERENCES `APPRAISAL_CYCLES` (`ID`),
  CONSTRAINT `employee_review_ibfk_2` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_review_ibfk_3` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKl5nhosx86fgrxaniqrpo19qre` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKquq35mo706obtb5s2p60vjlvj` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_SHIFT`
--

DROP TABLE IF EXISTS `EMPLOYEE_SHIFT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_SHIFT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `SHIFT_NAME` varchar(30) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `SHIFT_HOURS` varchar(20) DEFAULT NULL,
  `WEEKEND` bit(1) DEFAULT b'0',
  `REASON` varchar(70) DEFAULT NULL,
  `EMPLOYEEID` int DEFAULT NULL,
  `SHIFTID` int DEFAULT NULL,
  `LOCATIONS` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEEID` (`EMPLOYEEID`),
  KEY `SHIFTID` (`SHIFTID`),
  KEY `LOCATIONS` (`LOCATIONS`),
  KEY `FKojd5f8wf2xy5nk0s6jg47hhud` (`CREATOR`),
  KEY `FKbqhj95x8rcq569ef1s71end1h2d` (`LAST_MODIFIER`),
  CONSTRAINT `employee_shift_ibfk_1` FOREIGN KEY (`EMPLOYEEID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_shift_ibfk_2` FOREIGN KEY (`SHIFTID`) REFERENCES `SHIFTS` (`ID`),
  CONSTRAINT `employee_shift_ibfk_3` FOREIGN KEY (`LOCATIONS`) REFERENCES `LOCATION` (`ID`),
  CONSTRAINT `FKbqhj95x8rcq569ef1s71end1h2d` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8wf2xy5nk0s6jg47hhud` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_SKILL_MAPPING`
--

DROP TABLE IF EXISTS `EMPLOYEE_SKILL_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_SKILL_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `PERFORMANCE_SKILLS_ID` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `REVIEWER_ID` int DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `REVIEWER_ID` (`REVIEWER_ID`),
  KEY `FKbj9te9echhg00isvkf6j0et6v` (`CREATOR`),
  KEY `FKhbr8la390rgdawfjvcxpjy148` (`LAST_MODIFIER`),
  KEY `PERFORMANCE_SKILLS_ID` (`PERFORMANCE_SKILLS_ID`),
  CONSTRAINT `employee_skill_mapping_ibfk_1` FOREIGN KEY (`PERFORMANCE_SKILLS_ID`) REFERENCES `PERFORMANCE_SKILLS` (`ID`),
  CONSTRAINT `employee_skill_mapping_ibfk_2` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `employee_skill_mapping_ibfk_3` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKbj9te9echhg00isvkf6j0et6v` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKhbr8la390rgdawfjvcxpjy148` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEE_WORK_EXPERIENCE`
--

DROP TABLE IF EXISTS `EMPLOYEE_WORK_EXPERIENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE_WORK_EXPERIENCE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `COMPANY_NAME` varchar(100) DEFAULT NULL,
  `JOB_TITLE` varchar(50) DEFAULT NULL,
  `EXPERIENCE` int DEFAULT NULL,
  `FROM_DATE` datetime(6) DEFAULT NULL,
  `TO_DATE` datetime(6) DEFAULT NULL,
  `JOB_DESCRIPTION` longtext,
  `RELEVANT` bit(1) DEFAULT b'0',
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FK3pnbhr4001qqv4omse4vfvv5u` (`CREATOR`),
  KEY `FKri6nmpow3f7aoj8q6au7gq9mr` (`LAST_MODIFIER`),
  CONSTRAINT `EMPLOYEE_WORK_EXPERIENCE_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FK3pnbhr4001qqv4omse4vfvv5u` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKri6nmpow3f7aoj8q6au7gq9mr` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMPLOYEEEDUCATIONDETAILS`
--

DROP TABLE IF EXISTS `EMPLOYEEEDUCATIONDETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEEEDUCATIONDETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `INSTITUTE_NAME` varchar(100) DEFAULT NULL,
  `DEGREE_DIPLOMA` varchar(50) DEFAULT NULL,
  `SPECIALIZATION` varchar(50) DEFAULT NULL,
  `ATTACHMENT1` varchar(255) DEFAULT NULL,
  `ATTACHMENT2` varchar(255) DEFAULT NULL,
  `ATTACHMENT3` varchar(255) DEFAULT NULL,
  `ATTACHMENT4` varchar(255) DEFAULT NULL,
  `ATTACHMENT5` varchar(255) DEFAULT NULL,
  `COURSE_NAME` varchar(100) DEFAULT NULL,
  `DATE_OF_COMPLETION` datetime(6) DEFAULT NULL,
  `ROW_ID` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKl3xtjxx9hn7ppkloud9pe4w3m` (`CREATOR`),
  KEY `FKg00aiqu579cfrattvp656g6ut` (`LAST_MODIFIER`),
  CONSTRAINT `EMPLOYEEEDUCATIONDETAILS_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `FKg00aiqu579cfrattvp656g6ut` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKl3xtjxx9hn7ppkloud9pe4w3m` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EXCEPTIONAL_WORKING_DAY`
--

DROP TABLE IF EXISTS `EXCEPTIONAL_WORKING_DAY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EXCEPTIONAL_WORKING_DAY` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(50) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `APPLICABLE_FOR` enum('All Employees','Specific Departments','Specific Designations') DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKiqtfwtnf3j8boptt5yho4r66p` (`CREATOR`),
  KEY `FK52nkrww28a0427jpap54wh6s` (`LAST_MODIFIER`),
  CONSTRAINT `FK52nkrww28a0427jpap54wh6s` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKiqtfwtnf3j8boptt5yho4r66p` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EXCEPTIONS`
--

DROP TABLE IF EXISTS `EXCEPTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EXCEPTIONS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `DEPARTMENT_ID` int DEFAULT NULL,
  `DESIGNATION_ID` int DEFAULT NULL,
  `LOCATION_ID` int DEFAULT NULL,
  `ROLE` varchar(55) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `DESIGNATION_ID` (`DESIGNATION_ID`),
  KEY `LOCATION_ID` (`LOCATION_ID`),
  KEY `FKp2mm3ww10l4t7xattbxpx5dp6` (`CREATOR`),
  KEY `FK7alm6iahynr0myd3muufihejy` (`LAST_MODIFIER`),
  CONSTRAINT `EXCEPTIONS_ibfk_1` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `EXCEPTIONS_ibfk_2` FOREIGN KEY (`DESIGNATION_ID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `EXCEPTIONS_ibfk_3` FOREIGN KEY (`LOCATION_ID`) REFERENCES `LOCATION` (`ID`),
  CONSTRAINT `FK7alm6iahynr0myd3muufihejy` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKp2mm3ww10l4t7xattbxpx5dp6` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EXIT_DETAILS`
--

DROP TABLE IF EXISTS `EXIT_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EXIT_DETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_ID` varchar(50) DEFAULT NULL,
  `INTERVIEWER` varchar(50) DEFAULT NULL,
  `SEPARATION_DATE` datetime(6) DEFAULT NULL,
  `REASON_FOR_LEAVING` enum('Better Employment Condition','Career Prospect','Death','Dessertion','Dismissed','Dissatisfaction with the job','Emigrating','Health','Higher Pay','Personality Conflict','Retirement','Retrenchment') DEFAULT NULL,
  `WORKING_FOR_ORGANIZATION_AGAIN` enum('Yes','No') DEFAULT NULL,
  `ORGANIZATION_IMPROVEMENT_SUGGESTIONS` varchar(100) DEFAULT NULL,
  `LIKED_MOST_ABOUT_ORGANIZATION` varchar(100) DEFAULT NULL,
  `ADDITIONAL_COMMENTS` varchar(100) DEFAULT NULL,
  `COMPANY_VEHICLE_HANDED_IN` varchar(100) DEFAULT NULL,
  `ALL_LIBRARY_BOOKS_SUBMITTED` varchar(100) DEFAULT NULL,
  `EXIT_INTERVIEW_CONDUCTED` varchar(100) DEFAULT NULL,
  `RESIGNATION_LETTER_SUBMITTED` varchar(100) DEFAULT NULL,
  `ALL_EQUIPMENTS_HANDED_IN` varchar(100) DEFAULT NULL,
  `SECURITY` varchar(100) DEFAULT NULL,
  `NOTICE_PERIOD_FOLLOWED` varchar(100) DEFAULT NULL,
  `MANAGER_SUPERVISOR_CLEARANCE` varchar(100) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold','Completed') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKgowiyvc49sbbh49cmpa1njuip` (`CREATOR`),
  KEY `FKstutjjgqln8gvkjsp1yg5cc3` (`LAST_MODIFIER`),
  CONSTRAINT `FKgowiyvc49sbbh49cmpa1njuip` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKstutjjgqln8gvkjsp1yg5cc3` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FAQ`
--

DROP TABLE IF EXISTS `FAQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FAQ` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `QUESTION` varchar(255) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `TAGS` enum('Onboarding','Payroll','Recruitment','Job Postings','Timesheet','Attendance','Leave','Login Issues','HRMS Overview') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `CATEGORY_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKirnmqenib82n5t8uchq09bau8` (`CREATOR`),
  KEY `FKb0cstoe25d3e95sbgard1qnps` (`LAST_MODIFIER`),
  CONSTRAINT `FKb0cstoe25d3e95sbgard1qnps` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKirnmqenib82n5t8uchq09bau8` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FEEDBACK`
--

DROP TABLE IF EXISTS `FEEDBACK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FEEDBACK` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `FEEDBACK_DATE` datetime DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `FEEDBACK_TYPE` enum('Reporting To','Peer-to-Peer','360 Degree') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK4v4dspyfvfs1laiob1gj5td6y` (`CREATOR`),
  KEY `FKdmi0j4loyhpj7rnsumgr1i4gc` (`LAST_MODIFIER`),
  CONSTRAINT `FK4v4dspyfvfs1laiob1gj5td6y` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKdmi0j4loyhpj7rnsumgr1i4gc` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GENERAL_SETTINGS_LEAVE`
--

DROP TABLE IF EXISTS `GENERAL_SETTINGS_LEAVE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GENERAL_SETTINGS_LEAVE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `CONSECUTIVE_LEAVES_CONSIDERED_AS_LEAVE` int DEFAULT NULL,
  `APPLICABLE_LEAVE_TYPES` enum('Annual Leave','Sick Leave','Compensatory Leave','Other') DEFAULT NULL,
  `COMPENSATORY_REQUEST_ENTRY_MODE` enum('Manual','Automatic') DEFAULT NULL,
  `ADD_COMPENSATORY_REQUEST_FOR_FUTURE_DATE` bit(1) DEFAULT b'0',
  `LEAVE_CREDITED_ON_WEEKEND` double DEFAULT NULL,
  `LEAVE_CREDITED_ON_HOLIDAYS` double DEFAULT NULL,
  `CREDITED_LEAVE_EXPIRATION_DAYS` int DEFAULT NULL,
  `UNITS_ALLOWED` enum('Full Day','Half Day','Hours') DEFAULT NULL,
  `DURATIONS_ALLOWED` enum('Full Day','Half Day','Hours') DEFAULT NULL,
  `TIME_INPUT_ALLOWED` bit(1) DEFAULT b'0',
  `MAKE_REASON_MANDATORY` bit(1) DEFAULT b'0',
  `RESOURCE_AVAILABILITY_REPORT_ACCESS` enum('All','Department','Individual','None') DEFAULT NULL,
  `ENABLE_PAYROLL_REPORT_FOR_ADMIN` bit(1) DEFAULT b'0',
  `INCLUDE_WEEKENDS` bit(1) DEFAULT b'0',
  `INCLUDE_HOLIDAYS` bit(1) DEFAULT b'0',
  `UNPAID_LEAVE_MARKED_AS` bit(1) DEFAULT b'0',
  `ENABLE_PASSWORD_PROTECTION` bit(1) DEFAULT b'0',
  `ALLOW_LEAVE_REQUEST_UNTIL_NEXT` enum('Pay Period','Month','Specific Date','None') DEFAULT NULL,
  `PAST_LEAVES_WITHIN_CURRENT_PAY_PERIOD` enum('All','Department','Individual','None') DEFAULT NULL,
  `CURRENT_DAY_AND_UPCOMING_LEAVE_REQUESTS` enum('All','Department','Individual','None') DEFAULT NULL,
  `MAKE_REASON_FOR_LEAVE_CANCELLATION_MANDATORY` bit(1) DEFAULT b'0',
  `LEAVE_DISPLAY_FORMAT` enum('Google','Office365') DEFAULT NULL,
  `REMINDER_EMAIL` bit(1) DEFAULT b'0',
  `CUSTOMIZE_EMAIL_TEMPLATE` longtext,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKooquftg6w5ukc650oh19fhejh` (`CREATOR`),
  KEY `FKearydhgki0bjiu7n85t2ihm1b` (`LAST_MODIFIER`),
  CONSTRAINT `FKearydhgki0bjiu7n85t2ihm1b` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKooquftg6w5ukc650oh19fhejh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GEO_RESTRICTIONS`
--

DROP TABLE IF EXISTS `GEO_RESTRICTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GEO_RESTRICTIONS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `RESTRICTION_NAME` varchar(30) DEFAULT NULL,
  `RESTRICTED_MODULES` enum('Time Tracker','Attendance','Files') DEFAULT NULL,
  `EMPLOYEEID` int DEFAULT NULL,
  `LOCATIONID` int DEFAULT NULL,
  `DEPARTMENTID` int DEFAULT NULL,
  `DESIGNATIONSID` int DEFAULT NULL,
  `ROLES` enum('Manager','Team Member','Team Incharge','Admin','Director') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEEID` (`EMPLOYEEID`),
  KEY `LOCATIONID` (`LOCATIONID`),
  KEY `DEPARTMENTID` (`DEPARTMENTID`),
  KEY `DESIGNATIONSID` (`DESIGNATIONSID`),
  KEY `FKojd5f8sd2xy5nk0s6jg47hhud` (`CREATOR`),
  KEY `FKbqhj95x8icq569ef1s71end1h2d` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqhj95x8icq569ef1s71end1h2d` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8sd2xy5nk0s6jg47hhud` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `GEO_Restrictions_ibfk_1` FOREIGN KEY (`EMPLOYEEID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `GEO_Restrictions_ibfk_2` FOREIGN KEY (`DESIGNATIONSID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `GEO_Restrictions_ibfk_3` FOREIGN KEY (`DEPARTMENTID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `GEO_Restrictions_ibfk_4` FOREIGN KEY (`LOCATIONID`) REFERENCES `LOCATION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GOALS`
--

DROP TABLE IF EXISTS `GOALS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GOALS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `progress` double DEFAULT NULL,
  `DESCRIPTION` longtext,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `STATUS` enum('In Progress','On Track','Behind Schedule','Completed','Abondoned','Pending') DEFAULT NULL,
  `GOALS_PRIORITY` enum('High','Medium','Low','Highest') DEFAULT NULL,
  `ALLIGNMENT` varchar(200) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3fnhtgxusv3irqdocvup4cdqf` (`CREATOR`),
  KEY `FKt1k12cnkk80plikld5o55k2m4` (`LAST_MODIFIER`),
  CONSTRAINT `FK3fnhtgxusv3irqdocvup4cdqf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKt1k12cnkk80plikld5o55k2m4` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GRADE_INFO`
--

DROP TABLE IF EXISTS `GRADE_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GRADE_INFO` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `MIN_BASIC` double DEFAULT NULL,
  `MAX_BASIC` double DEFAULT NULL,
  `STI_MULTIPLIER` double DEFAULT NULL,
  `GRADE` varchar(20) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKro6wgpdmhdplftg2lawryhbwsh` (`CREATOR`),
  KEY `FKikwybiriql8dn29errefsc67rf` (`LAST_MODIFIER`),
  CONSTRAINT `FKikwybiriql8n29edrrefsc67rf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKro6wgpmhdplftgd2lawryhbwsh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HOLIDAY`
--

DROP TABLE IF EXISTS `HOLIDAY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HOLIDAY` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(100) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `RESTRICTED` bit(1) DEFAULT b'0',
  `APPLICABLE_FOR` enum('All Employees','Specific Departments','Specific Designations') DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `REMINDER_DAYS` enum('1 Day','3 Days','7 Days') DEFAULT NULL,
  `NOTIFY_APPLICABLE_EMPLOYEES` bit(1) DEFAULT b'0',
  `REPROCESS_LEAVE_HOLIDAY` bit(1) DEFAULT b'0',
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKcv5ivawawmj0vlec8myia1bjn` (`CREATOR`),
  KEY `FKshe79j8qu9uqg1ux0bw4b443x` (`LAST_MODIFIER`),
  CONSTRAINT `FKcv5ivawawmj0vlec8myia1bjn` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKshe79j8qu9uqg1ux0bw4b443x` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HR_LETTER`
--

DROP TABLE IF EXISTS `HR_LETTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HR_LETTER` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `DATE_OF_JOINING` datetime(6) DEFAULT NULL,
  `DATE_OF_REQUEST` datetime(6) DEFAULT NULL,
  `IS_THERE_ANY_CHANGE_IN_PRESENT_ADDRESS` bit(1) DEFAULT b'0',
  `ADDRESS_LINE_1` varchar(200) DEFAULT NULL,
  `ADDRESS_LINE_2` varchar(200) DEFAULT NULL,
  `CITY` varchar(50) DEFAULT NULL,
  `STATE` varchar(50) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `REASON_FOR_REQUEST` enum('Gas Connection','BroadBand Connection','Opening Bank Account','Visa Application','Higher Studies') DEFAULT NULL,
  `POSTAL_CODE` int DEFAULT NULL,
  `CURRENT_EXPERIENCE` varchar(50) DEFAULT NULL,
  `LETTER_TYPE` enum('Offer Letter','Appointment Letter','Confirmation Letter','Performance Review Letter','Warning Letter','Termination Letter','Employment Verification Letter','Experience Certificate','Salary Increment Letter','Address Proof','Bonafide Letter') DEFAULT NULL,
  `SCHOOL_LEAVING_CERTIFICATE` varchar(100) DEFAULT NULL,
  `MARKSHEETS` varchar(100) DEFAULT NULL,
  `PASSPORT` varchar(100) DEFAULT NULL,
  `AADHAR_CARD` varchar(100) DEFAULT NULL,
  `RESUME` varchar(100) DEFAULT NULL,
  `COVER_LETTER` varchar(100) DEFAULT NULL,
  `EMPLOYEE_VERIFICATION_LETTERS` varchar(100) DEFAULT NULL,
  `SKILL_CERTIFICATES` varchar(100) DEFAULT NULL,
  `PERFORMANCE_APPRAISALS` varchar(100) DEFAULT NULL,
  `BANK_STATEMENTS` varchar(100) DEFAULT NULL,
  `LEASE_AGREEMENT` varchar(100) DEFAULT NULL,
  `VOTER_REGISTRATION_CARD` varchar(100) DEFAULT NULL,
  `DRIVING_LICENSE` varchar(100) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `BENEFITS` varchar(100) DEFAULT NULL,
  `PROBATION_PERIOD` varchar(100) DEFAULT NULL,
  `NOTICE_PERIOD` varchar(100) DEFAULT NULL,
  `REPORTING_MANAGER` varchar(100) DEFAULT NULL,
  `WORK_SCHEDULE` varchar(100) DEFAULT NULL,
  `REASONS_FOR_TERMINATION` varchar(100) DEFAULT NULL,
  `TEXT1` varchar(200) DEFAULT NULL,
  `TEXT2` varchar(200) DEFAULT NULL,
  `TEXT3` varchar(200) DEFAULT NULL,
  `EFFECTIVE_DATE_OF_TERMINATION` datetime(6) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `DESIGNATION_ID` int DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `DESIGNATION_ID` (`DESIGNATION_ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FKb1hjwg9ahr8hji8ixdbnpmygx` (`CREATOR`),
  KEY `FKapp4bu24mn5t76loltucsb1ac` (`LAST_MODIFIER`),
  CONSTRAINT `FKapp4bu24mn5t76loltucsb1ac` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKb1hjwg9ahr8hji8ixdbnpmygx` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `hr_letter_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `hr_letter_ibfk_2` FOREIGN KEY (`DESIGNATION_ID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `hr_letter_ibfk_3` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HR_LETTERS`
--

DROP TABLE IF EXISTS `HR_LETTERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HR_LETTERS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `DATE_OF_JOINING` datetime(6) DEFAULT NULL,
  `DESIGNATION_ID` int DEFAULT NULL,
  `DATE_OF_REQUEST` datetime(6) DEFAULT NULL,
  `IS_CHANGE_IN_PRESENT_ADDRESS` bit(1) DEFAULT b'0',
  `ADDRESS_LINE1` varchar(255) DEFAULT NULL,
  `ADDRESS_LINE2` varchar(255) DEFAULT NULL,
  `CITY` varchar(255) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `COUNTRY` varchar(255) DEFAULT NULL,
  `POSTAL_CODE` varchar(255) DEFAULT NULL,
  `DEPARTMENT` varchar(255) DEFAULT NULL,
  `CURRENT_EXPERIENCE` varchar(255) DEFAULT NULL,
  `LETTER_TYPE` enum('Offer letter','Appointment letter','Confirmation letter','Performance review letter','Warning letter','Termination letter','Employment verification letter','Experience certificate','Salary Increment Offer') DEFAULT NULL,
  `SCHOOL_LEAVING_CERTIFICATE` varchar(255) DEFAULT NULL,
  `MARKSHEETS` varchar(255) DEFAULT NULL,
  `PASSPORT` varchar(255) DEFAULT NULL,
  `AADHAR_CARD` varchar(255) DEFAULT NULL,
  `RESUME` varchar(255) DEFAULT NULL,
  `COVER_LETTER` varchar(255) DEFAULT NULL,
  `EMPLOYMENT_VERIFICATION_LETTERS` varchar(255) DEFAULT NULL,
  `SKILLS_CERTIFICATES` varchar(255) DEFAULT NULL,
  `PERFORMANCE_APPRAISALS` varchar(255) DEFAULT NULL,
  `BANK_STATEMENTS` varchar(255) DEFAULT NULL,
  `LEASE_AGREEMENT` varchar(255) DEFAULT NULL,
  `VOTER_REGISTRATION_CARD` varchar(255) DEFAULT NULL,
  `SALARY` varchar(255) DEFAULT NULL,
  `BENEFITS` varchar(255) DEFAULT NULL,
  `PROBATION_PERIOD` varchar(255) DEFAULT NULL,
  `NOTICE_PERIOD` varchar(255) DEFAULT NULL,
  `REPORTING_MANAGER` varchar(255) DEFAULT NULL,
  `WORK_SCHEDULE` varchar(255) DEFAULT NULL,
  `REASONS_FOR_TERMINATION` varchar(255) DEFAULT NULL,
  `EFFECTIVE_DATE_OF_TERMINATION` datetime(6) DEFAULT NULL,
  `DRIVER_LICENSE` datetime(6) DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKivsn5s4ebc94ovjsauiygb3wh` (`CREATOR`),
  KEY `FK7c2g8nx07s17pi5pmbuow4hhd` (`LAST_MODIFIER`),
  CONSTRAINT `FK7c2g8nx07s17pi5pmbuow4hhd` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKivsn5s4ebc94ovjsauiygb3wh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `HR_LETTERS_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HRMS_SYSTEM_CONFIG`
--

DROP TABLE IF EXISTS `HRMS_SYSTEM_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HRMS_SYSTEM_CONFIG` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `KEY_NAME` varchar(50) DEFAULT NULL,
  `KEY_VALUE` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKro6wgpdmhdplftg2lasswryhbwsh` (`CREATOR`),
  KEY `FKikwybiriql8dn29errefsssc67rf` (`LAST_MODIFIER`),
  CONSTRAINT `FKikwybiriql8n29edrrefsssc67rf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKro6wgpmhdplftgd2lawssryhbwsh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INTERVIEW`
--

DROP TABLE IF EXISTS `INTERVIEW`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INTERVIEW` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(100) DEFAULT NULL,
  `CANDIDATE_ID` int DEFAULT NULL,
  `JOB_OPENING_ID` int DEFAULT NULL,
  `FROM_DATE` datetime(6) DEFAULT NULL,
  `INTERVIEW_OWNER` varchar(100) DEFAULT NULL,
  `LOCATION` varchar(100) DEFAULT NULL,
  `COMMENTS_FOR_INTERVIEWERS` varchar(100) DEFAULT NULL,
  `DEPARTMENT_NAME` varchar(100) DEFAULT NULL,
  `TO_DATE` datetime(6) DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `INTERVIEWER` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(100) DEFAULT NULL COMMENT 'TO STORE ACTION FLOW PROCESSINSTANCEID',
  `WORKFLOW_STAGE` varchar(100) DEFAULT NULL COMMENT 'TO STORE ACTION FLOW CURRUNT STAGE',
  `INTERVIEW_JOIN_LINK` varchar(300) DEFAULT NULL,
  `MEETING_DESCRIPTION` longtext,
  `JOB_APPLICATION_ID` int DEFAULT NULL,
  `IS_FEEDBACK_UPDATED` bit(1) DEFAULT b'0',
  `OVERALL_IMPRESSION_VALUE` enum('Advance','Advance with reservation','Do not advance') DEFAULT NULL,
  `OVERALL_IMPRESSION_COMMENT` varchar(200) DEFAULT NULL,
  `OVERALL_IMPRESSION_QUESTION` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CANDIDATE_ID` (`CANDIDATE_ID`),
  KEY `JOB_OPENING_ID` (`JOB_OPENING_ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `INTERVIEWER` (`INTERVIEWER`),
  KEY `FK9pecec1p30ojpcvlngme1bmyy` (`CREATOR`),
  KEY `FKl675ajsbt8gw0ek8kh9i9eijn` (`LAST_MODIFIER`),
  KEY `JOB_APPLICATION_ID` (`JOB_APPLICATION_ID`),
  CONSTRAINT `FK9pecec1p30ojpcvlngme1bmyy` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKl675ajsbt8gw0ek8kh9i9eijn` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `INTERVIEW_ibfk_1` FOREIGN KEY (`CANDIDATE_ID`) REFERENCES `APPLICANT` (`ID`),
  CONSTRAINT `INTERVIEW_ibfk_2` FOREIGN KEY (`JOB_OPENING_ID`) REFERENCES `JOB_OPENING` (`ID`),
  CONSTRAINT `INTERVIEW_ibfk_3` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `INTERVIEW_ibfk_4` FOREIGN KEY (`INTERVIEWER`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `INTERVIEW_ibfk_5` FOREIGN KEY (`JOB_APPLICATION_ID`) REFERENCES `JOB_APPLICATION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INTERVIEW_FEEDBACK`
--

DROP TABLE IF EXISTS `INTERVIEW_FEEDBACK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INTERVIEW_FEEDBACK` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `INTERVIEW` int DEFAULT NULL,
  `COMMENTS` varchar(350) DEFAULT NULL,
  `RATING` varchar(10) DEFAULT NULL,
  `QUESTION` longtext,
  `TEXT1` varchar(200) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `INTERVIEW` (`INTERVIEW`),
  KEY `FKojf8wf2xy5gu0s6jg47gaf` (`CREATOR`),
  KEY `FKbqhj95x8rcq569gu1s78d` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqhj95x8rcq569gu1s78d` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojf8wf2xy5gu0s6jg47gaf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `INTERVIEW_FEEDBACK_ibfk_1` FOREIGN KEY (`INTERVIEW`) REFERENCES `INTERVIEW` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=448 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IP_RESTRICTIONS`
--

DROP TABLE IF EXISTS `IP_RESTRICTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IP_RESTRICTIONS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `FROM_IP_ADDRESS` varchar(30) DEFAULT NULL,
  `TO_IP_ADDRESS` varchar(30) DEFAULT NULL,
  `RESTRICTED_MODULES` enum('Time Tracker','Attendance','Files') DEFAULT NULL,
  `EMPLOYEEID` int DEFAULT NULL,
  `LOCATIONID` int DEFAULT NULL,
  `DEPARTMENTID` int DEFAULT NULL,
  `DESIGNATIONSID` int DEFAULT NULL,
  `ROLES` enum('Manager','Team Member','Team Incharge','Admin','Director') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEEID` (`EMPLOYEEID`),
  KEY `LOCATIONID` (`LOCATIONID`),
  KEY `DEPARTMENTID` (`DEPARTMENTID`),
  KEY `DESIGNATIONSID` (`DESIGNATIONSID`),
  KEY `FKojd5f8sd2xy5nk0s6jg89hhud` (`CREATOR`),
  KEY `FKbqhj95x8icq569ef1s14end1h2d` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqhj95x8icq569ef1s14end1h2d` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8sd2xy5nk0s6jg89hhud` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `IP_Restrictions_ibfk_1` FOREIGN KEY (`EMPLOYEEID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `IP_Restrictions_ibfk_2` FOREIGN KEY (`DESIGNATIONSID`) REFERENCES `DESIGNATION_OLD` (`ID`),
  CONSTRAINT `IP_Restrictions_ibfk_3` FOREIGN KEY (`DEPARTMENTID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `IP_Restrictions_ibfk_4` FOREIGN KEY (`LOCATIONID`) REFERENCES `LOCATION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_APPLICATION`
--

DROP TABLE IF EXISTS `JOB_APPLICATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_APPLICATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `REFERRED_BY` varchar(255) DEFAULT NULL,
  `MOBILE` varchar(50) DEFAULT NULL,
  `EMAIL_ID` varchar(50) DEFAULT NULL,
  `APPLICATION_STATUS` enum('Applied','Interview to be scheduled','Interview scheduled','Interview in progress','Interview completed','Offer planned','BGC initiated','BGC approved','BGC rejected','Offer made','Offer accepted','Offer rejected','Hired','Rejected in interview') DEFAULT NULL,
  `RELATIONSHIP` enum('Personally known','Former Colleague','Socially Connected','Got the resume through a common friend','Others') DEFAULT NULL,
  `RESUME` varchar(200) DEFAULT NULL,
  `APPLICANT_ID` int DEFAULT NULL,
  `JOB_OPENING_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `TIME_TO_CLOSE` int DEFAULT NULL,
  `JOB_APPLICATION_ID` varchar(50) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `APPLICANT_RESUME_FK` int DEFAULT NULL,
  `APPLICANT_RESUME_SUMMARY` json DEFAULT NULL,
  `PRE_QUESTIONAIRE_RESULT` longtext,
  PRIMARY KEY (`ID`),
  KEY `APPLICANT_ID` (`APPLICANT_ID`),
  KEY `JOB_OPENING_ID` (`JOB_OPENING_ID`),
  KEY `FKq40lpkk6ieu3lbnpcoc0w9d4q` (`CREATOR`),
  KEY `FKl58smqo4ijwg158kgj4jqbuiy` (`LAST_MODIFIER`),
  KEY `APPLICANT_RESUME_FK` (`APPLICANT_RESUME_FK`),
  CONSTRAINT `FKl58smqo4ijwg158kgj4jqbuiy` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKq40lpkk6ieu3lbnpcoc0w9d4q` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `JOB_APPLICATION_ibfk_1` FOREIGN KEY (`APPLICANT_ID`) REFERENCES `APPLICANT` (`ID`),
  CONSTRAINT `JOB_APPLICATION_ibfk_2` FOREIGN KEY (`JOB_OPENING_ID`) REFERENCES `JOB_OPENING` (`ID`),
  CONSTRAINT `JOB_APPLICATION_ibfk_3` FOREIGN KEY (`APPLICANT_RESUME_FK`) REFERENCES `APPLICANT_RESUME` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_BRIEF`
--

DROP TABLE IF EXISTS `JOB_BRIEF`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_BRIEF` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` longtext,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `POSTING_TITLE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKojf8wf2xy5gu0s6jg47hh` (`CREATOR`),
  KEY `FKbqhj95x8rcq569gu1s71en` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqhj95x8rcq569gu1s71en` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojf8wf2xy5gu0s6jg47hh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_INTERVIEW_QUESTION`
--

DROP TABLE IF EXISTS `JOB_INTERVIEW_QUESTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_INTERVIEW_QUESTION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `JOB_OPENING_FK` int DEFAULT NULL,
  `QUESTION` varchar(1000) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `JOB_OPENING_FK` (`JOB_OPENING_FK`),
  CONSTRAINT `JOB_INTERVIEW_QUESTION_ibfk_1` FOREIGN KEY (`JOB_OPENING_FK`) REFERENCES `JOB_OPENING` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=478 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_OPENING`
--

DROP TABLE IF EXISTS `JOB_OPENING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_OPENING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `WORKSPACE_ID` int DEFAULT '1',
  `POSTING_TITLE` varchar(250) DEFAULT NULL,
  `ASSIGNEDRECRUITER` varchar(255) DEFAULT NULL,
  `TARGET_CLOSING_DATE` datetime(6) DEFAULT NULL,
  `JOB_OPENING_STATUS` enum('Active','none','waiting for approval','on-hold','filled','cancelled','declined','inactive') DEFAULT NULL,
  `INDUSTRY` enum('Information Technology','Healthcare','Finance','Manufacturing','Retail','Hospitality and Tourism','Telecommunications','Education','Transportation and Logistics','Energy') DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `HIRING_MANAGER` varchar(255) DEFAULT NULL,
  `DATE_OPENED` datetime(6) DEFAULT NULL,
  `JOB_TYPE` enum('Direct Hire','Contractor Role Assignment','Aramco Digital Customers','Contractor Project Based','Aramco Secondee','Aramco Assignment','Trainee') DEFAULT NULL,
  `WORK_EXPERIENCE` double DEFAULT NULL,
  `SKILLS` varchar(500) DEFAULT NULL,
  `ADDRESS` varchar(500) DEFAULT NULL,
  `CITY` varchar(255) DEFAULT NULL,
  `PROVINCE` varchar(255) DEFAULT NULL,
  `POSTALCODE` varchar(20) DEFAULT NULL,
  `COUNTRY` varchar(255) DEFAULT NULL,
  `IS_REMOTE` enum('Yes','No') DEFAULT NULL,
  `DESCRIPTION_REQUIREMENTS` longtext,
  `ATTACHMENT1` varchar(200) DEFAULT NULL,
  `ATTACHMENT2` varchar(200) DEFAULT NULL,
  `ATTACHMENT3` varchar(200) DEFAULT NULL,
  `OPEN_POSITIONS` int DEFAULT NULL,
  `JOB_ID` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `TOTAL_POSITIONS` int DEFAULT NULL,
  `PRE_QUESTIONAIRE` longtext,
  `INTERVIEW_QUESTION` longtext,
  `FROM_SALARY` varchar(80) DEFAULT NULL,
  `END_SALARY` varchar(80) DEFAULT NULL,
  `SALARY_RANGE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FKa56gbqx4782g6dwtk36vj77bp` (`CREATOR`),
  KEY `FKn1m4kyikfe7mj6ga8phu66fk` (`LAST_MODIFIER`),
  CONSTRAINT `FKa56gbqx4782g6dwtk36vj77bp` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKn1m4kyikfe7mj6ga8phu66fk` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `JOB_OPENING_ibfk_1` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOBS`
--

DROP TABLE IF EXISTS `JOBS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOBS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `JOB_NAME` varchar(50) DEFAULT NULL,
  `PROJECT` int DEFAULT NULL,
  `START_DATE` datetime(6) DEFAULT NULL,
  `END_DATE` datetime(6) DEFAULT NULL,
  `hours` double DEFAULT NULL,
  `RATE_PER_HOUR` double DEFAULT NULL,
  `DESCRIPTION` longtext,
  `STATUS` enum('Open','In-Progress','Pending','Closed','On Hold') DEFAULT NULL,
  `REMINDER` varchar(100) DEFAULT NULL,
  `REMINDER_TIME` datetime(6) DEFAULT NULL,
  `BILLABLE_STATUS` enum('Billable','Non-Billable') DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKrn1xcks4glj9ycmn7gjr40tpr` (`CREATOR`),
  KEY `FKmrxnfqktkbtinyd3cxgl94alo` (`LAST_MODIFIER`),
  KEY `PROJECT` (`PROJECT`),
  CONSTRAINT `FKmrxnfqktkbtinyd3cxgl94alo` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKrn1xcks4glj9ycmn7gjr40tpr` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `jobs_ibfk_2` FOREIGN KEY (`PROJECT`) REFERENCES `PROJECTS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KEY_RESULT_AREA`
--

DROP TABLE IF EXISTS `KEY_RESULT_AREA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KEY_RESULT_AREA` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `TITLE` varchar(200) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `weightage` double DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `EMPLOYEE_PROGRESS` double DEFAULT NULL,
  `REVIEWED_PROGRESS` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKqnygqeyi280khv9guv86ivlnk` (`CREATOR`),
  KEY `FKn9vreferd9fxqp8khv2tqudpj` (`LAST_MODIFIER`),
  CONSTRAINT `FKn9vreferd9fxqp8khv2tqudpj` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKqnygqeyi280khv9guv86ivlnk` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KPI_BUILDER`
--

DROP TABLE IF EXISTS `KPI_BUILDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KPI_BUILDER` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CARD_TEXT` varchar(500) DEFAULT NULL,
  `VARIABLE` varchar(255) DEFAULT NULL,
  `QUESTION` varchar(255) DEFAULT NULL,
  `QUERY` longtext,
  `ROLE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LEAVE_ENTITLEMENT`
--

DROP TABLE IF EXISTS `LEAVE_ENTITLEMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LEAVE_ENTITLEMENT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `DATE_OF_JOINING` datetime(6) DEFAULT NULL,
  `ACTUAL` enum('Monthly','Annually','Custom') DEFAULT NULL,
  `ACTUAL_YEAR` varchar(50) DEFAULT NULL,
  `ACTUAL_ON` enum('Calendar Year','Service Anniversary','Custom Date') DEFAULT NULL,
  `ACTUAL_NO_OF_DAYS` int DEFAULT NULL,
  `RESET` enum('Never','Annually','Custom') DEFAULT NULL,
  `RESET_YEAR` varchar(50) DEFAULT NULL,
  `RESET_ON` enum('Calendar Year','Service Anniversary','Custom Date') DEFAULT NULL,
  `CARRY_FORWARD` int DEFAULT NULL,
  `ENCASHMENT` int DEFAULT NULL,
  `OPENING_BALANCE` int DEFAULT NULL,
  `MAXIMUM_BALANCE` int DEFAULT NULL,
  `DEDUCTIBLE_HOLIDAYS` bit(1) DEFAULT b'0',
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKlwelulxhfebqep49ahs7ir18h` (`CREATOR`),
  KEY `FK4d0wrypsbsvhbk05xm5u6e3ss` (`LAST_MODIFIER`),
  CONSTRAINT `FK4d0wrypsbsvhbk05xm5u6e3ss` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKlwelulxhfebqep49ahs7ir18h` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LEAVE_TYPE`
--

DROP TABLE IF EXISTS `LEAVE_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LEAVE_TYPE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(50) DEFAULT NULL,
  `CODE` int DEFAULT NULL,
  `LEAVE_TYPE` enum('Paid','Unpaid','Sick','Vacation','Other') DEFAULT NULL,
  `UNIT` enum('Half Day','Full Day','Hours','Shifts') DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `BALANCED_BASED_ON` enum('Fixed entitlement','Leave grant') DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `VALIDITY_TO` datetime(6) DEFAULT NULL,
  `IS_LEAVE_TYPE_ENABLED` bit(1) DEFAULT b'0',
  `LEAVE_TYPE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `EFFECTIVE_AFTER_COUNT` int DEFAULT NULL,
  `EFFECTIVE_AFTER_TYPE` enum('Year','Month','Day') DEFAULT NULL,
  `EFFECTIVE_AFTER_CONDITION` enum('Date-Of-Joining','Date-Of-Confirmation') DEFAULT NULL,
  `ACCRUAL_TYPE` enum('Yearly','Monthly','OneTime','HalfYearly','Quaterly') DEFAULT NULL,
  `ACCRUAL_ON_DAY` varchar(50) DEFAULT NULL,
  `ACCRUAL_ON_MONTH` varchar(50) DEFAULT NULL,
  `ACCRUAL_NO_OF_DAYS` double DEFAULT NULL,
  `ACCRUAL_APPLICABLE_IN` enum('Current','Next') DEFAULT NULL,
  `OPENING_BALANCE` double DEFAULT NULL,
  `MAXIMUM_BALANCE` double DEFAULT NULL,
  `IS_ACCRUAL` bit(1) DEFAULT b'0',
  `APPLICABLE_GENDER` enum('Male','Female','Others') DEFAULT NULL,
  `MARITAL_STATUS` enum('Single','Married') DEFAULT NULL,
  `APPLICABLE_DEPARTMENT` varchar(200) DEFAULT NULL,
  `APPLICABLE_DESIGNATION` varchar(200) DEFAULT NULL,
  `APPLICABLE_LOCATION` varchar(200) DEFAULT NULL,
  `VALIDITY_FROM` datetime(6) DEFAULT NULL,
  `LEAVE_TYPE_DAYS` enum('Business days','Calendar days') DEFAULT NULL,
  `APPLICABLE_RELIGION` enum('Muslim','All','Other') DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKi6us55shaoeenosb8djcfm6no` (`CREATOR`),
  KEY `FKghksaiu9qd6p2whx2dpcg4jos` (`LAST_MODIFIER`),
  CONSTRAINT `FKghksaiu9qd6p2whx2dpcg4jos` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKi6us55shaoeenosb8djcfm6no` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LEAVES`
--

DROP TABLE IF EXISTS `LEAVES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LEAVES` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_ID` int DEFAULT NULL,
  `LEAVE_TYPE_ID` int DEFAULT NULL,
  `IS_HALF_DAY_ENABLED` bit(1) DEFAULT b'0',
  `FROM_DATE` datetime(6) DEFAULT NULL,
  `TO_DATE` datetime(6) DEFAULT NULL,
  `TEAM_EMAIL_ID` varchar(50) DEFAULT NULL,
  `DATE_OF_REQUEST` datetime(6) DEFAULT NULL,
  `LEAVE_TAKEN` double DEFAULT NULL,
  `UNIT` enum('Half Day','Full Day','Hours','Shifts') DEFAULT NULL,
  `REASON_FOR_LEAVE` varchar(255) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `ATTACHMENT` varchar(255) DEFAULT NULL,
  `LEAVE_BALANCE` double DEFAULT NULL,
  `TEXT1` varchar(100) DEFAULT NULL,
  `EXCEPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `LEAVE_TYPE_ID` (`LEAVE_TYPE_ID`),
  KEY `FKpiehg4k3c0a2ljn5a47prfh2n` (`CREATOR`),
  KEY `FKemdn7xlk1ixn1ach7ptpr261g` (`LAST_MODIFIER`),
  CONSTRAINT `FKemdn7xlk1ixn1ach7ptpr261g` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKpiehg4k3c0a2ljn5a47prfh2n` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `LEAVES_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `LEAVES_ibfk_2` FOREIGN KEY (`LEAVE_TYPE_ID`) REFERENCES `LEAVE_TYPE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LOCATION`
--

DROP TABLE IF EXISTS `LOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LOCATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(100) DEFAULT NULL,
  `MAIL_ALIAS` varchar(100) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `STATE_PROVINCE` varchar(50) DEFAULT NULL,
  `TIME_ZONE` varchar(50) DEFAULT NULL,
  `LOCATION_ID` varchar(50) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKk1gsun5ont0ll06u7n082q41y` (`CREATOR`),
  KEY `FKs8bcn2f31g38oqkqa5pqiemid` (`LAST_MODIFIER`),
  CONSTRAINT `FKk1gsun5ont0ll06u7n082q41y` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKs8bcn2f31g38oqkqa5pqiemid` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NDA`
--

DROP TABLE IF EXISTS `NDA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NDA` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `JSON` longtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NON_USA_PERDIEM`
--

DROP TABLE IF EXISTS `NON_USA_PERDIEM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NON_USA_PERDIEM` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `COUNTRY` varchar(100) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  `CITY` varchar(100) NOT NULL,
  `MAXIMUM_LODGING` varchar(100) NOT NULL,
  `MIE_COST` varchar(100) NOT NULL,
  `TRAVEL_DAY_COST` varchar(100) NOT NULL,
  `MAXIMUM__PER_DIEM_RATE` varchar(100) NOT NULL,
  `SEASON_BEGIN` datetime DEFAULT NULL,
  `SEASON_END` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NOTIFICATION_DETAIL`
--

DROP TABLE IF EXISTS `NOTIFICATION_DETAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NOTIFICATION_DETAIL` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKFLOW_ACTION_FK` int DEFAULT NULL,
  `CC_RECEIVER` varchar(1000) DEFAULT NULL,
  `CC_USER_TYPE` enum('USER','VENDOR','WORKGROUP') DEFAULT NULL,
  `NOTIFICATION_TYPE` enum('ACTIVE','COMPLETE') DEFAULT NULL,
  `TEMPLATE_NAME` varchar(1000) DEFAULT NULL,
  `TO_RECEIVER` varchar(1000) DEFAULT NULL,
  `TO_USER_TYPE` enum('USER','VENDOR','WORKGROUP') DEFAULT NULL,
  `EMAIL` bit(1) DEFAULT NULL,
  `ENTITY_GEOGRAPHY` varchar(100) DEFAULT NULL,
  `ENTITY_OWNER` varchar(255) DEFAULT NULL,
  `GEOGRAPHY` bit(1) DEFAULT NULL,
  `NOTIFICATION` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKqlop64rf4y13n0w0esgtumsuq` (`WORKFLOW_ACTION_FK`),
  CONSTRAINT `FKqlop64rf4y13n0w0esgtumsuq` FOREIGN KEY (`WORKFLOW_ACTION_FK`) REFERENCES `WORKFLOW_ACTION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=427 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OFFERS`
--

DROP TABLE IF EXISTS `OFFERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OFFERS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `POSTING_TITLE` int DEFAULT NULL,
  `DEPARTMENT_ID` int DEFAULT NULL,
  `CANDIDATE_ID` int DEFAULT NULL,
  `COMPENSATION_AMOUNT` double DEFAULT NULL,
  `EMPLOYMENT_TYPE` enum('Direct Hire','Contractor Role Assignment','Aramco Digital Customers','Contractor Project Based','Aramco Secondee','Aramco Assignment','Trainee') DEFAULT NULL,
  `EXPECTED_JOINING_DATE` datetime(6) DEFAULT NULL,
  `OFFERS_EXPIRY` datetime(6) DEFAULT NULL,
  `OFFER_OWNER` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `OFFER_LETTER` varchar(200) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(100) DEFAULT NULL COMMENT 'TO STORE ACTION FLOW PROCESSINSTANCEID',
  `WORKFLOW_STAGE` varchar(100) DEFAULT NULL COMMENT 'TO STORE ACTION FLOW CURRUNT STAGE',
  `JOB_APPLICATION_ID` int DEFAULT NULL,
  `OFFER_STATUS` enum('Offer made','Accepted','Rejected') DEFAULT NULL,
  `OFFER_REJECT_REASON` enum('Personal Circumstances','Better offer elsewhere','Commute or Location Issues','Job responsibilities not aligned with JD','Dissatisfaction with company policies','Limited remote work option') DEFAULT NULL,
  `DESIGNATION_ID` int DEFAULT NULL,
  `OFFER_EMAIL_SENT` tinyint NOT NULL DEFAULT '0',
  `SECURITY_EMAIL_SENT` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `POSTING_TITLE` (`POSTING_TITLE`),
  KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `CANDIDATE_ID` (`CANDIDATE_ID`),
  KEY `FK47fp90maccg1ifiiucg9cucjx` (`CREATOR`),
  KEY `FKifbbe63owccr92pf489b2h099` (`LAST_MODIFIER`),
  KEY `JOB_APPLICATION_ID` (`JOB_APPLICATION_ID`),
  KEY `DESIGNATION_ID` (`DESIGNATION_ID`),
  CONSTRAINT `FK47fp90maccg1ifiiucg9cucjx` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKifbbe63owccr92pf489b2h099` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `OFFERS_ibfk_1` FOREIGN KEY (`POSTING_TITLE`) REFERENCES `JOB_OPENING` (`ID`),
  CONSTRAINT `OFFERS_ibfk_2` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
  CONSTRAINT `OFFERS_ibfk_3` FOREIGN KEY (`CANDIDATE_ID`) REFERENCES `APPLICANT` (`ID`),
  CONSTRAINT `OFFERS_ibfk_4` FOREIGN KEY (`JOB_APPLICATION_ID`) REFERENCES `JOB_APPLICATION` (`ID`),
  CONSTRAINT `OFFERS_ibfk_5` FOREIGN KEY (`DESIGNATION_ID`) REFERENCES `DESIGNATION_OLD` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PAY_PERIOD_SETTING`
--

DROP TABLE IF EXISTS `PAY_PERIOD_SETTING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAY_PERIOD_SETTING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `PAY_PERIOD_NAME` varchar(50) DEFAULT NULL,
  `PAY_PERIOD_CYCLE_START` datetime(6) DEFAULT NULL,
  `PAY_PERIOD_CYCLE_END` datetime(6) DEFAULT NULL,
  `PAYROLL_PROCESSING_DAY` datetime(6) DEFAULT NULL,
  `PAYROLL_REPORT_GENERATION_DAY` datetime(6) DEFAULT NULL,
  `PROCESS_LEAVE_ENCASHMENT` bit(1) DEFAULT b'0',
  `LOCK_SETTING` bit(1) DEFAULT b'0',
  `APPLICABLE_LOCATION` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKoayn9jps4kogmxc78kbg0dnm8` (`CREATOR`),
  KEY `FK5w3hocop6ssabxrrcthp3bl0x` (`LAST_MODIFIER`),
  CONSTRAINT `FK5w3hocop6ssabxrrcthp3bl0x` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKoayn9jps4kogmxc78kbg0dnm8` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PAYROLL`
--

DROP TABLE IF EXISTS `PAYROLL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAYROLL` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `PAY_PERIOD_NAME` varchar(30) DEFAULT NULL,
  `PAY_PERIOD_TYPE` enum('Weekly','Bi-Weekly','Monthly') DEFAULT NULL,
  `START_DATE` datetime(6) DEFAULT NULL,
  `END_DATE` datetime(6) DEFAULT NULL,
  `PAYROLL_DAY` varchar(10) DEFAULT NULL,
  `PAYROLL_REPORT_GENERATION_DAY` varchar(10) DEFAULT NULL,
  `LOCATION_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `LOCATION_ID` (`LOCATION_ID`),
  KEY `FKxndf00h2pwy81bmrkassa66y` (`CREATOR`),
  KEY `FKe7s9eut0txc2k2h35bsxegtdm` (`LAST_MODIFIER`),
  CONSTRAINT `FKe7s9eut0txc2k2h35bsxegtdm` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKxndf00h2pwy81bmrkassa66y` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `PAYROLL_ibfk_1` FOREIGN KEY (`LOCATION_ID`) REFERENCES `LOCATION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PAYROLL_RUN`
--

DROP TABLE IF EXISTS `PAYROLL_RUN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAYROLL_RUN` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `PROJECT` tinyint(1) NOT NULL DEFAULT '0',
  `PAYROLL_NAME` varchar(50) DEFAULT NULL,
  `RUN_DATE` datetime DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7f7fkdwki6il5g1xqs41hsl9k` (`CREATOR`),
  KEY `FKfbvxrqqtlsd6gc9aiac2fvv54` (`LAST_MODIFIER`),
  CONSTRAINT `FK7f7fkdwki6il5g1xqs41hsl9k` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKfbvxrqqtlsd6gc9aiac2fvv54` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PERFORMANCE_SKILLS`
--

DROP TABLE IF EXISTS `PERFORMANCE_SKILLS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PERFORMANCE_SKILLS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WEIGHTAGE` double DEFAULT NULL,
  `DESCRIPTION` longtext,
  `NAME` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `EMPLOYEE_PROGRESS` double DEFAULT NULL,
  `REVIEWED_PROGRESS` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3fnhtgxusv3irqdocvupdfedqf` (`CREATOR`),
  KEY `FKt1k12cnkk80plikfdsld5o55k2m4` (`LAST_MODIFIER`),
  CONSTRAINT `FK3fnhtgxusv3irqdocvupdfedqf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKt1k12cnkk80plikfdsld5o55k2m4` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJECTS`
--

DROP TABLE IF EXISTS `PROJECTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJECTS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `PROJECT_NAME` varchar(100) DEFAULT NULL,
  `CLIENT_ID` int DEFAULT NULL,
  `PROJECT_COST` double DEFAULT NULL,
  `STATUS` enum('Open','In-progress','Closed','On-hold') DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `PROJECT_HEAD` varchar(50) DEFAULT NULL,
  `PROJECT_MANAGER` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `RATE_PER_HOUR` double DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKq7u8t9ex2th3am1yb1hljchtf` (`CREATOR`),
  KEY `FK3xujrpywyuaku4688s2w8lrly` (`LAST_MODIFIER`),
  KEY `CLIENT_ID` (`CLIENT_ID`),
  CONSTRAINT `FK3xujrpywyuaku4688s2w8lrly` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKq7u8t9ex2th3am1yb1hljchtf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `projects_ibfk_2` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENTS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `REMINDER`
--

DROP TABLE IF EXISTS `REMINDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REMINDER` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CATEGORY` varchar(100) DEFAULT NULL,
  `TASK_DETAILS` varchar(255) DEFAULT NULL,
  `DUE_DATE` datetime DEFAULT NULL,
  `REMINDER_DATE` date DEFAULT NULL,
  `OWNER_ROLE` varchar(255) DEFAULT NULL,
  `QUESTION` varchar(255) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `STATUS` enum('DONE','DELETED','YET_TO_START') DEFAULT NULL,
  `IS_PINNED` bit(1) DEFAULT b'0',
  PRIMARY KEY (`ID`),
  KEY `FKojd5f8wf2xy5gu064jg47hhmh` (`CREATOR`),
  KEY `FKbqhj95x8rcq569231s71enfc1` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqhj95x8rcq569231s71enfc1` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8wf2xy5gu064jg47hhmh` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESOURCE_FIELD_AUDIT`
--

DROP TABLE IF EXISTS `RESOURCE_FIELD_AUDIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_FIELD_AUDIT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR_FK` int DEFAULT NULL,
  `CREATOR_NAME` varchar(100) DEFAULT NULL,
  `MODIFIER_FK` int DEFAULT NULL,
  `MODIFIER_NAME` varchar(100) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `USER_NAME` varchar(100) DEFAULT NULL,
  `ACTION` varchar(255) DEFAULT NULL,
  `APPLICATION_NAME` varchar(255) DEFAULT NULL,
  `COLUMN_NAME` varchar(255) DEFAULT NULL,
  `ENTITY_ID` varchar(255) DEFAULT NULL,
  `ENTITY_NAME` varchar(255) DEFAULT NULL,
  `ENTITY_TYPE` varchar(255) DEFAULT NULL,
  `FIELD_KEY` varchar(255) DEFAULT NULL,
  `FIELD_TYPE` varchar(255) DEFAULT NULL,
  `IDENTIFIER1` varchar(255) DEFAULT NULL,
  `IDENTIFIER2` varchar(255) DEFAULT NULL,
  `IDENTIFIER3` varchar(255) DEFAULT NULL,
  `IDENTIFIER4` varchar(255) DEFAULT NULL,
  `LABEL` varchar(255) DEFAULT NULL,
  `NEW_VALUE` varchar(255) DEFAULT NULL,
  `OLD_VALUE` varchar(255) DEFAULT NULL,
  `RESOURCE_NAME` varchar(255) DEFAULT NULL,
  `SEQUENCE` varchar(255) DEFAULT NULL,
  `TASK_CODE` varchar(255) DEFAULT NULL,
  `VIEW_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESOURCE_JSON_AUDIT`
--

DROP TABLE IF EXISTS `RESOURCE_JSON_AUDIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_JSON_AUDIT` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR_FK` int DEFAULT NULL,
  `CREATOR_NAME` varchar(100) DEFAULT NULL,
  `MODIFIER_FK` int DEFAULT NULL,
  `MODIFIER_NAME` varchar(100) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `USER_NAME` varchar(100) DEFAULT NULL,
  `ACTION_TYPE` varchar(255) DEFAULT NULL,
  `CONTEXT_JSON` varchar(10000) DEFAULT NULL,
  `PROCESS` bit(1) DEFAULT NULL,
  `VIEW_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESTRICTIONS`
--

DROP TABLE IF EXISTS `RESTRICTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESTRICTIONS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `WEEKEND_BETWEEN_LEAVE_PERIOD` bit(1) DEFAULT b'0',
  `HOLIDAY_BETWEEN_LEAVE_PERIOD` bit(1) DEFAULT b'0',
  `EXCEED_LEAVE_BALANCE` int DEFAULT NULL,
  `ALLOW_USERS_TO_VIEW` enum('All','Specific Roles','No One') DEFAULT NULL,
  `BALANCE_TO_BE_DISPLAYED` enum('Entitlement','Utilized','Remaining') DEFAULT NULL,
  `DURATION_ALLOWED` enum('Full Day','Half Day','Both') DEFAULT NULL,
  `PAST_DATES` int DEFAULT NULL,
  `FUTURE_DATE` int DEFAULT NULL,
  `ALLOW_ONLY_ADMINISTRATORS` bit(1) DEFAULT b'0',
  `MIN_LEAVE_PER_APPLICATION` int DEFAULT NULL,
  `MAX_LEAVE_PER_APPLICATION` int DEFAULT NULL,
  `MAX_CONSECUTIVE_DAYS_ALLOWED` int DEFAULT NULL,
  `MIN_GAP_BETWEEN_APPLICATIONS` int DEFAULT NULL,
  `ENABLE_FILE_UPLOAD` bit(1) DEFAULT b'0',
  `MAX_APPLICATIONS_ALLOWED` enum('Unlimited','Limited') DEFAULT NULL,
  `LEAVE_APPLICABLE_ON` varchar(100) DEFAULT NULL,
  `LEAVE_CANNOT_BE_TAKEN_TOGETHER_WITH` varchar(100) DEFAULT NULL,
  `MIN_LEAVE_PER_APPLICATION_DAYS` int DEFAULT NULL,
  `MAX_LEAVE_PER_APPLICATION_DAYS` int DEFAULT NULL,
  `MAX_CONSECUTIVE_DAYS_ALLOWED_DAYS` int DEFAULT NULL,
  `MIN_GAP_BETWEEN_APPLICATIONS_DAYS` int DEFAULT NULL,
  `ENABLE_FILE_UPLOAD_DAYS` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKfx24s9l5hj2s1mqa1cgbsnnhv` (`CREATOR`),
  KEY `FKm7htnojdy463mwg35s7vhqpmt` (`LAST_MODIFIER`),
  CONSTRAINT `FKfx24s9l5hj2s1mqa1cgbsnnhv` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKm7htnojdy463mwg35s7vhqpmt` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `REVINFO`
--

DROP TABLE IF EXISTS `REVINFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REVINFO` (
  `REV` int NOT NULL AUTO_INCREMENT,
  `REVTSTMP` bigint DEFAULT NULL,
  PRIMARY KEY (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RISK`
--

DROP TABLE IF EXISTS `RISK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RISK` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `RISK_OWNER` varchar(100) NOT NULL,
  `RESOLUTION_DUE_DATE` datetime DEFAULT NULL,
  `ACTUAL_RESOLVED_DATE` datetime DEFAULT NULL,
  `RISK_TYPE_ID` int DEFAULT NULL,
  `DEPARTMENT` varchar(50) NOT NULL,
  `RISK_LEVEL` enum('High','Medium','Low') NOT NULL,
  `REPORTER` varchar(100) NOT NULL,
  `STATUS` enum('Open','Mitigation-in-progress','Mitigated','Avoided') NOT NULL,
  `ATTACHMENT` varchar(255) DEFAULT NULL,
  `REMARKS` longtext,
  `PROBABILITY` enum('Rare','Unlikely','Possible','Probable') NOT NULL,
  `MITIGATION_NOTE` longtext,
  `IMPACT` enum('Large','Medium','Small','Low') DEFAULT NULL,
  `RISK_TAGS_ADDITIONAL` varchar(250) DEFAULT NULL,
  `RISK_TITLE` varchar(200) DEFAULT NULL,
  `RISK_ID` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RISK_TYPE_ID` (`RISK_TYPE_ID`),
  KEY `FKiuy5qiu6urhjx7uhcp2kyslck` (`CREATOR`),
  KEY `FK3blysrxtqubdxqnoijloi13yp` (`LAST_MODIFIER`),
  CONSTRAINT `FK3blysrxtqubdxqnoijloi13yp` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKiuy5qiu6urhjx7uhcp2kyslck` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `risk_ibfk_1` FOREIGN KEY (`RISK_TYPE_ID`) REFERENCES `RISK_TYPE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RISK_CUSTOM_FIELD`
--

DROP TABLE IF EXISTS `RISK_CUSTOM_FIELD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RISK_CUSTOM_FIELD` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'this is the primary key of RISK_CUSTOM_FIELD table',
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `RISK_CHECK_BOX1` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX2` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX3` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX4` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX5` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX6` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX7` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX8` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX9` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX10` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX11` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX12` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX13` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX14` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX15` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX16` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX17` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX18` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX19` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_CHECK_BOX20` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined checkbox value',
  `RISK_DATE1` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE2` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE3` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE4` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE5` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE6` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE7` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE8` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE9` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE10` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE11` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE12` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE13` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE14` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE15` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE16` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE17` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE18` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE19` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DATE20` datetime DEFAULT NULL COMMENT 'this field stores user defined date value',
  `RISK_DROP_DOWN1` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN2` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN3` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN4` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN5` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN6` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN7` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN8` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN9` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN0` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN11` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN12` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN13` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN14` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN15` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN16` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN17` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN18` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN19` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_DROP_DOWN20` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined dropdown value',
  `RISK_FILE_UPLOAD1` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD2` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD3` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD4` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD5` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD6` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD7` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD8` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD9` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD10` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD11` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD12` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD13` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD14` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD15` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD16` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD17` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD18` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD19` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_FILE_UPLOAD20` varchar(150) DEFAULT NULL COMMENT 'this field stores user defined file upload value',
  `RISK_NUMBER1` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER2` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER3` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER4` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER5` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER6` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER7` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER8` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER9` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER10` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER11` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER12` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER13` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER14` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER15` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER16` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER17` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER18` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER19` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_NUMBER20` double DEFAULT NULL COMMENT 'this field stores user defined number value',
  `RISK_RADIO_BUTTON1` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON2` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON3` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON4` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON5` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON6` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON7` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON8` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON9` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON10` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON11` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON12` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON13` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON14` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON15` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON16` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON17` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON18` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON19` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_RADIO_BUTTON20` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined radio button value',
  `RISK_TEXT_AREA1` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA2` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA3` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA4` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA5` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA6` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA7` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA8` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA9` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_AREA10` text COMMENT 'this field stores user defined text area value',
  `RISK_TEXT_FIELD2` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD3` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD4` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD5` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD6` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD7` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD8` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD9` text COMMENT 'this field stores user defined text field value',
  `RISK_TEXT_FIELD10` text COMMENT 'this field stores user defined text field value',
  `entity_id` int DEFAULT NULL COMMENT 'this field entity ID for current enabled value',
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKkqelplfsoh8rfcemrypehgukl` (`CREATOR`),
  KEY `FKrdunsnxmo1sawogk6hu4o6pl4` (`LAST_MODIFIER`),
  CONSTRAINT `FKkqelplfsoh8rfcemrypehgukl` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKrdunsnxmo1sawogk6hu4o6pl4` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RISK_SPECIAL_COMPONENT`
--

DROP TABLE IF EXISTS `RISK_SPECIAL_COMPONENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RISK_SPECIAL_COMPONENT` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'this is the primary key of RISK_SPECIAL_COMPONENT table',
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `ENTITY_ID` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined ENTITY ID value',
  `ENTITY_TYPE` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined ENTITY TYPE value',
  `RISK_CAMERA1` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA2` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA3` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA4` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA5` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA6` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA7` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA8` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA9` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA10` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA11` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA12` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA13` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA14` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA15` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA16` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA17` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA18` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA19` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_CAMERA20` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined CAMERA value',
  `RISK_VIDEO1` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO2` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO3` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO4` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO5` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO6` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO7` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO8` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO9` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO10` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO11` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO12` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO13` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO14` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO15` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO16` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO17` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO18` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO19` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_VIDEO20` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined VIDEO value',
  `RISK_BARCODE1` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE2` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE3` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE4` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE5` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE6` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE7` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE8` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE9` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE10` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE11` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE12` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE13` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE14` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE15` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE16` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE17` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE18` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE19` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_BARCODE20` varchar(100) DEFAULT NULL COMMENT 'this field stores user defined BARCODE value',
  `RISK_GPS1_LATITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LATITUDE value',
  `RISK_GPS2_LATITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LATITUDE value',
  `RISK_GPS3_LATITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LATITUDE value',
  `RISK_GPS4_LATITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LATITUDE value',
  `RISK_GPS5_LATITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LATITUDE value',
  `RISK_GPS1_LONGITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LONGITUDE value',
  `RISK_GPS2_LONGITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LONGITUDE value',
  `RISK_GPS3_LONGITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LONGITUDE value',
  `RISK_GPS4_LONGITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LONGITUDE value',
  `RISK_GPS5_LONGITUDE` double DEFAULT NULL COMMENT 'this field stores user defined GPS LONGITUDE value',
  `RISK_SIGNATURE1` varchar(255) DEFAULT NULL COMMENT 'this field stores user SIGNATURE value',
  `RISK_SIGNATURE2` varchar(255) DEFAULT NULL COMMENT 'this field stores user SIGNATURE value',
  `RISK_SIGNATURE3` varchar(255) DEFAULT NULL COMMENT 'this field stores user SIGNATURE value',
  `RISK_SIGNATURE4` varchar(255) DEFAULT NULL COMMENT 'this field stores user SIGNATURE value',
  `RISK_SIGNATURE5` varchar(255) DEFAULT NULL COMMENT 'this field stores user SIGNATURE value',
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3tb6pdqlpb7e4nuakq0l7j6ge` (`CREATOR`),
  KEY `FK223a1w3pm5y2wn3d3ep49v3s1` (`LAST_MODIFIER`),
  CONSTRAINT `FK223a1w3pm5y2wn3d3ep49v3s1` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK3tb6pdqlpb7e4nuakq0l7j6ge` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RISK_TAGS`
--

DROP TABLE IF EXISTS `RISK_TAGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RISK_TAGS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `TAG_NAME` varchar(50) NOT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKh088yu1au73flffrx77kea3np` (`CREATOR`),
  KEY `FKbpy2m8jxlapja49c0c0nitaq4` (`LAST_MODIFIER`),
  CONSTRAINT `FKbpy2m8jxlapja49c0c0nitaq4` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKh088yu1au73flffrx77kea3np` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RISK_TAGS_MAPPING`
--

DROP TABLE IF EXISTS `RISK_TAGS_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RISK_TAGS_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `RISK_ID` int DEFAULT NULL,
  `TAG_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RISK_ID` (`RISK_ID`),
  KEY `TAG_ID` (`TAG_ID`),
  KEY `FKgs85pb63jh774v851np8b1vb0` (`CREATOR`),
  KEY `FKth3d44lgkrd4ib0s3r0a95xfr` (`LAST_MODIFIER`),
  CONSTRAINT `FKgs85pb63jh774v851np8b1vb0` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKth3d44lgkrd4ib0s3r0a95xfr` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `risk_tags_mapping_ibfk_1` FOREIGN KEY (`RISK_ID`) REFERENCES `RISK` (`ID`),
  CONSTRAINT `risk_tags_mapping_ibfk_2` FOREIGN KEY (`TAG_ID`) REFERENCES `RISK_TAGS` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RISK_TYPE`
--

DROP TABLE IF EXISTS `RISK_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RISK_TYPE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `TYPE_NAME` varchar(50) NOT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK45cubo9aubt5kkaj19xjp74gd` (`CREATOR`),
  KEY `FKk43n2cekr27o234kkyge8rtpu` (`LAST_MODIFIER`),
  CONSTRAINT `FK45cubo9aubt5kkaj19xjp74gd` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKk43n2cekr27o234kkyge8rtpu` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RUN_DETAILS`
--

DROP TABLE IF EXISTS `RUN_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RUN_DETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `PAYROLL_RUN_FK` int DEFAULT NULL,
  `COMPONENT_NUMBER1` double DEFAULT NULL,
  `COMPONENT_NUMBER2` double DEFAULT NULL,
  `COMPONENT_NUMBER3` double DEFAULT NULL,
  `COMPONENT_NUMBER4` double DEFAULT NULL,
  `COMPONENT_NUMBER5` double DEFAULT NULL,
  `COMPONENT_NUMBER6` double DEFAULT NULL,
  `COMPONENT_NUMBER7` double DEFAULT NULL,
  `COMPONENT_NUMBER8` double DEFAULT NULL,
  `COMPONENT_NUMBER9` double DEFAULT NULL,
  `COMPONENT_NUMBER10` double DEFAULT NULL,
  `COMPONENT_NUMBER11` double DEFAULT NULL,
  `COMPONENT_NUMBER12` double DEFAULT NULL,
  `COMPONENT_NUMBER13` double DEFAULT NULL,
  `COMPONENT_NUMBER14` double DEFAULT NULL,
  `COMPONENT_NUMBER15` double DEFAULT NULL,
  `COMPONENT_NUMBER16` double DEFAULT NULL,
  `COMPONENT_NUMBER17` double DEFAULT NULL,
  `COMPONENT_NUMBER18` double DEFAULT NULL,
  `COMPONENT_NUMBER19` double DEFAULT NULL,
  `COMPONENT_NUMBER20` double DEFAULT NULL,
  `COMPONENT_NUMBER21` double DEFAULT NULL,
  `COMPONENT_NUMBER22` double DEFAULT NULL,
  `COMPONENT_NUMBER23` double DEFAULT NULL,
  `COMPONENT_NUMBER24` double DEFAULT NULL,
  `COMPONENT_NUMBER25` double DEFAULT NULL,
  `COMPONENT_NUMBER26` double DEFAULT NULL,
  `COMPONENT_NUMBER27` double DEFAULT NULL,
  `COMPONENT_NUMBER28` double DEFAULT NULL,
  `COMPONENT_NUMBER29` double DEFAULT NULL,
  `COMPONENT_NUMBER30` double DEFAULT NULL,
  `COMPONENT_NUMBER31` double DEFAULT NULL,
  `COMPONENT_NUMBER32` double DEFAULT NULL,
  `COMPONENT_NUMBER33` double DEFAULT NULL,
  `COMPONENT_NUMBER34` double DEFAULT NULL,
  `COMPONENT_NUMBER35` double DEFAULT NULL,
  `COMPONENT_NUMBER36` double DEFAULT NULL,
  `COMPONENT_NUMBER37` double DEFAULT NULL,
  `COMPONENT_NUMBER38` double DEFAULT NULL,
  `COMPONENT_NUMBER39` double DEFAULT NULL,
  `COMPONENT_NUMBER40` double DEFAULT NULL,
  `COMPONENT_NUMBER41` double DEFAULT NULL,
  `COMPONENT_NUMBER42` double DEFAULT NULL,
  `COMPONENT_NUMBER43` double DEFAULT NULL,
  `COMPONENT_NUMBER44` double DEFAULT NULL,
  `COMPONENT_NUMBER45` double DEFAULT NULL,
  `COMPONENT_NUMBER46` double DEFAULT NULL,
  `COMPONENT_NUMBER47` double DEFAULT NULL,
  `COMPONENT_NUMBER48` double DEFAULT NULL,
  `COMPONENT_NUMBER49` double DEFAULT NULL,
  `COMPONENT_NUMBER50` double DEFAULT NULL,
  `COMPONENT_NUMBER51` double DEFAULT NULL,
  `COMPONENT_NUMBER52` double DEFAULT NULL,
  `COMPONENT_NUMBER53` double DEFAULT NULL,
  `COMPONENT_NUMBER54` double DEFAULT NULL,
  `COMPONENT_NUMBER55` double DEFAULT NULL,
  `COMPONENT_NUMBER56` double DEFAULT NULL,
  `COMPONENT_NUMBER57` double DEFAULT NULL,
  `COMPONENT_NUMBER58` double DEFAULT NULL,
  `COMPONENT_NUMBER59` double DEFAULT NULL,
  `COMPONENT_NUMBER60` double DEFAULT NULL,
  `COMPONENT_NUMBER61` double DEFAULT NULL,
  `COMPONENT_NUMBER62` double DEFAULT NULL,
  `COMPONENT_NUMBER63` double DEFAULT NULL,
  `COMPONENT_NUMBER64` double DEFAULT NULL,
  `COMPONENT_NUMBER65` double DEFAULT NULL,
  `COMPONENT_NUMBER66` double DEFAULT NULL,
  `COMPONENT_NUMBER67` double DEFAULT NULL,
  `COMPONENT_NUMBER68` double DEFAULT NULL,
  `COMPONENT_NUMBER69` double DEFAULT NULL,
  `COMPONENT_NUMBER70` double DEFAULT NULL,
  `COMPONENT_NUMBER71` double DEFAULT NULL,
  `COMPONENT_NUMBER72` double DEFAULT NULL,
  `COMPONENT_NUMBER73` double DEFAULT NULL,
  `COMPONENT_NUMBER74` double DEFAULT NULL,
  `COMPONENT_NUMBER75` double DEFAULT NULL,
  `COMPONENT_NUMBER76` double DEFAULT NULL,
  `COMPONENT_NUMBER77` double DEFAULT NULL,
  `COMPONENT_NUMBER78` double DEFAULT NULL,
  `COMPONENT_NUMBER79` double DEFAULT NULL,
  `COMPONENT_NUMBER80` double DEFAULT NULL,
  `COMPONENT_NUMBER81` double DEFAULT NULL,
  `COMPONENT_NUMBER82` double DEFAULT NULL,
  `COMPONENT_NUMBER83` double DEFAULT NULL,
  `COMPONENT_NUMBER84` double DEFAULT NULL,
  `COMPONENT_NUMBER85` double DEFAULT NULL,
  `COMPONENT_NUMBER86` double DEFAULT NULL,
  `COMPONENT_NUMBER87` double DEFAULT NULL,
  `COMPONENT_NUMBER88` double DEFAULT NULL,
  `COMPONENT_NUMBER89` double DEFAULT NULL,
  `COMPONENT_NUMBER90` double DEFAULT NULL,
  `COMPONENT_NUMBER91` double DEFAULT NULL,
  `COMPONENT_NUMBER92` double DEFAULT NULL,
  `COMPONENT_NUMBER93` double DEFAULT NULL,
  `COMPONENT_NUMBER94` double DEFAULT NULL,
  `COMPONENT_NUMBER95` double DEFAULT NULL,
  `COMPONENT_NUMBER96` double DEFAULT NULL,
  `COMPONENT_NUMBER97` double DEFAULT NULL,
  `COMPONENT_NUMBER98` double DEFAULT NULL,
  `COMPONENT_NUMBER99` double DEFAULT NULL,
  `COMPONENT_NUMBER100` double DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `EMPLOYEE_ID` varchar(50) DEFAULT NULL,
  `EMPLOYEE_PK` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PAYROLL_RUN_FK` (`PAYROLL_RUN_FK`),
  KEY `FKh6y0f7s8pervcmmy7yjvy8qog` (`CREATOR`),
  KEY `FK6q2jlach9mqricc4ci4rb779t` (`LAST_MODIFIER`),
  KEY `FK7f7fkdwki6il5g1erd41hsl9k` (`EMPLOYEE_PK`),
  CONSTRAINT `FK6q2jlach9mqricc4ci4rb779t` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK7f7fkdwki6il5g1erd41hsl9k` FOREIGN KEY (`EMPLOYEE_PK`) REFERENCES `EMPLOYEE` (`ID`),
  CONSTRAINT `FKh6y0f7s8pervcmmy7yjvy8qog` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `run_details_ibfk_1` FOREIGN KEY (`PAYROLL_RUN_FK`) REFERENCES `COMPONENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SALARY_STRUCTURE`
--

DROP TABLE IF EXISTS `SALARY_STRUCTURE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SALARY_STRUCTURE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `NAME` varchar(100) DEFAULT NULL,
  `GRADE` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKstyr6y2sm7ggjlcwui5g79y2p` (`CREATOR`),
  KEY `FKnijxqa34s9yt71n74lw4jxqmx` (`LAST_MODIFIER`),
  CONSTRAINT `FKnijxqa34s9yt71n74lw4jxqmx` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKstyr6y2sm7ggjlcwui5g79y2p` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SALARY_STRUCTURE_COMPONENT_MAPPING`
--

DROP TABLE IF EXISTS `SALARY_STRUCTURE_COMPONENT_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SALARY_STRUCTURE_COMPONENT_MAPPING` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `COMPONENT_FK` int DEFAULT NULL,
  `SALARY_STRUCTURE_FK` int DEFAULT NULL,
  `CALCULATION_TYPE` varchar(50) DEFAULT NULL,
  `CALCULATION_EXPRESSION` longtext,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `COMPONENT_FK` (`COMPONENT_FK`),
  KEY `SALARY_STRUCTURE_FK` (`SALARY_STRUCTURE_FK`),
  KEY `FKq8yupfhx7gkquf5w27vlirutq` (`CREATOR`),
  KEY `FKejgfslot9wjv3h90bqyx16j4k` (`LAST_MODIFIER`),
  CONSTRAINT `FKejgfslot9wjv3h90bqyx16j4k` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKq8yupfhx7gkquf5w27vlirutq` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `salary_structure_component_mapping_ibfk_1` FOREIGN KEY (`COMPONENT_FK`) REFERENCES `COMPONENT` (`ID`),
  CONSTRAINT `salary_structure_component_mapping_ibfk_2` FOREIGN KEY (`SALARY_STRUCTURE_FK`) REFERENCES `SALARY_STRUCTURE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SALES_DETAILS`
--

DROP TABLE IF EXISTS `SALES_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SALES_DETAILS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DATE` datetime(6) DEFAULT NULL,
  `DEPARTMENT_NAME` varchar(100) DEFAULT NULL,
  `PLANNED_SALES` double DEFAULT NULL,
  `ACTUAL_SALES` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SHIFT_ROTATION`
--

DROP TABLE IF EXISTS `SHIFT_ROTATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SHIFT_ROTATION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `SCHEDULE_NAME` varchar(30) DEFAULT NULL,
  `SCHEDULE_FREQUENCY` enum('Weekly','Daily','Monthly','Bi Weekly') DEFAULT NULL,
  `SCHEDULING_TIME` datetime DEFAULT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `SHIFT_HOURS` varchar(20) DEFAULT NULL,
  `WEEKEND` bit(1) DEFAULT b'0',
  `SHIFT_ALLOWANCE` varchar(25) DEFAULT NULL,
  `DEPARTMENTS` varchar(100) DEFAULT NULL,
  `DESIGNATIONS` varchar(100) DEFAULT NULL,
  `LOCATIONS` varchar(100) DEFAULT NULL,
  `GROUPS` varchar(100) DEFAULT NULL,
  `EMPLOYEE` varchar(100) DEFAULT NULL,
  `SHIFT_ROTATION_FROM` varchar(100) DEFAULT NULL,
  `SHIFT_ROTATION_TO` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `SHIFT_GROUP` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKojd5f8sd2xy5nk0s6jg89hqd` (`CREATOR`),
  KEY `FKbqhj95x8icq569ef1s14end1hs` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqhj95x8icq569ef1s14end1hs` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKojd5f8sd2xy5nk0s6jg89hqd` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SHIFTS`
--

DROP TABLE IF EXISTS `SHIFTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SHIFTS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `SHIFT_NAME` varchar(30) DEFAULT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `SHIFT_HOURS` varchar(20) DEFAULT NULL,
  `WEEKEND` enum('Location Based','Shift Based') DEFAULT NULL,
  `SHIFT_ALLOWANCE` enum('Enable','Disable') DEFAULT NULL,
  `DEPARTMENTS` varchar(100) DEFAULT NULL,
  `LOCATIONS` varchar(100) DEFAULT NULL,
  `DIVISION` varchar(100) DEFAULT NULL,
  `EMPLOYEE` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `SHIFT_MARGIN` enum('Enable','Disable') DEFAULT NULL,
  `RATE_PER_DAY` double DEFAULT NULL,
  `HOURS_BEFORE_SHIFT_STARTS` datetime DEFAULT NULL,
  `HOURS_BEFORE_SHIFT_ENDS` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKovfd5f8sd2xy5nk0s6jg89hqd` (`CREATOR`),
  KEY `FKbqh8kjj5x8icq569ef1s14end1hs` (`LAST_MODIFIER`),
  CONSTRAINT `FKbqh8kjj5x8icq569ef1s14end1hs` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKovfd5f8sd2xy5nk0s6jg89hqd` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `STATE`
--

DROP TABLE IF EXISTS `STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STATE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `COUNTRY_ID` int DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `DISPLAY_NAME` varchar(100) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `COUNTRY_ID` (`COUNTRY_ID`),
  KEY `FKdg1484q1ncjlm7ywymgsex983` (`CREATOR`),
  KEY `FKsxsu79ga474h4iwfmbihrqawt` (`LAST_MODIFIER`),
  CONSTRAINT `FKdg1484q1ncjlm7ywymgsex983` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKsxsu79ga474h4iwfmbihrqawt` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `STATE_ibfk_1` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `COUNTRY` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SUB_CONTRACTOR`
--

DROP TABLE IF EXISTS `SUB_CONTRACTOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SUB_CONTRACTOR` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `NAME` varchar(100) DEFAULT NULL,
  `CODE` varchar(100) DEFAULT NULL,
  `BUSINESS_NAME` varchar(100) DEFAULT NULL,
  `EMAIL_ID` varchar(255) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `CITY` varchar(255) DEFAULT NULL,
  `TEXT1` varchar(255) DEFAULT NULL,
  `TEXT2` varchar(255) DEFAULT NULL,
  `TEXT3` varchar(255) DEFAULT NULL,
  `COUNTRY` varchar(255) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `PAYMENT_TERMS` varchar(255) DEFAULT NULL,
  `CONTRACT_START_DATE` datetime(6) DEFAULT NULL,
  `CONTRACT_END_DATE` datetime(6) DEFAULT NULL,
  `TOTAL_ANNUAL_COST` double DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1edk67yh7gc32etjyb4au7uwf` (`CREATOR`),
  KEY `FKmtclr5u27swa3pk5bw6p71wwn` (`LAST_MODIFIER`),
  CONSTRAINT `FK1edk67yh7gc32etjyb4au7uwf` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKmtclr5u27swa3pk5bw6p71wwn` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TIME_LOGS`
--

DROP TABLE IF EXISTS `TIME_LOGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TIME_LOGS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `PROJECT` int DEFAULT NULL,
  `JOB_ID` int DEFAULT NULL,
  `WORK_ITEM` varchar(100) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `TOTAL_MINUTES` int DEFAULT NULL,
  `BILLABLE_STATUS` enum('Billable','Non-Billable') DEFAULT NULL,
  `FROM_TIME` datetime(6) DEFAULT NULL,
  `TO_TIME` datetime(6) DEFAULT NULL,
  `USER_NAME` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `MONTH` int DEFAULT NULL,
  `WEEK_NO` int DEFAULT NULL,
  `SUNDAY` int DEFAULT NULL,
  `MONDAY` int DEFAULT NULL,
  `TUESDAY` int DEFAULT NULL,
  `WEDNESDAY` int DEFAULT NULL,
  `THURSDAY` int DEFAULT NULL,
  `FRIDAY` int DEFAULT NULL,
  `SATURDAY` int DEFAULT NULL,
  `TASK_NAME` varchar(100) DEFAULT NULL,
  `YEAR` int DEFAULT NULL,
  `TIMESHEET_FK` int DEFAULT NULL,
  `CLIENT_FK` int DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PROJECT` (`PROJECT`),
  KEY `JOB_ID` (`JOB_ID`),
  KEY `FK7cuo1f4lfos14jht02f55biad` (`CREATOR`),
  KEY `FKqgj7131peo00782yim2rokqtu` (`LAST_MODIFIER`),
  KEY `TIME_LOGS_ibfk_3` (`TIMESHEET_FK`),
  KEY `TIME_LOGS_ibfk_4` (`CLIENT_FK`),
  KEY `TIME_LOGS_ibfk_5` (`EMPLOYEE_ID`),
  CONSTRAINT `FK7cuo1f4lfos14jht02f55biad` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKqgj7131peo00782yim2rokqtu` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `TIME_LOGS_ibfk_1` FOREIGN KEY (`PROJECT`) REFERENCES `PROJECTS` (`ID`),
  CONSTRAINT `TIME_LOGS_ibfk_2` FOREIGN KEY (`JOB_ID`) REFERENCES `JOBS` (`ID`),
  CONSTRAINT `TIME_LOGS_ibfk_3` FOREIGN KEY (`TIMESHEET_FK`) REFERENCES `TIME_SHEET` (`ID`),
  CONSTRAINT `TIME_LOGS_ibfk_4` FOREIGN KEY (`CLIENT_FK`) REFERENCES `CLIENTS` (`ID`),
  CONSTRAINT `TIME_LOGS_ibfk_5` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TIME_SHEET`
--

DROP TABLE IF EXISTS `TIME_SHEET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TIME_SHEET` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `CURRENCY_CODE` varchar(50) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `SUBMITTED_BILLABLE_HOURS` double DEFAULT NULL,
  `SUBMITTEDN_ON_BILLABLE_HOURS` double DEFAULT NULL,
  `SUBMITTED_TOTAL_MINUTES` int DEFAULT NULL,
  `APPROVED_BILLABLE_HOURS` double DEFAULT NULL,
  `APPROVED_NON_BILLABLE_HOURS` double DEFAULT NULL,
  `APPROVED_TOTAL_HOURS` double DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `BILLABLE_STATUS` enum('Billable','Non-Billable') DEFAULT NULL,
  `PERIOD` enum('Today','This Week','This Month','Last Weak','Last Month') DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `WEEK_NUMBER` int DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(100) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(100) DEFAULT NULL,
  `REPORTING_MANAGER_ID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1tv8eufpeg2rw8qgmil61ei6k` (`CREATOR`),
  KEY `FKcj0k9rpfnyc47xx81pis0grob` (`LAST_MODIFIER`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `TIME_SHEET_ibfk_2` (`REPORTING_MANAGER_ID`),
  CONSTRAINT `FK1tv8eufpeg2rw8qgmil61ei6k` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKcj0k9rpfnyc47xx81pis0grob` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `TIME_SHEET_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`),
  CONSTRAINT `TIME_SHEET_ibfk_2` FOREIGN KEY (`REPORTING_MANAGER_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRAVEL_EXPENSE`
--

DROP TABLE IF EXISTS `TRAVEL_EXPENSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRAVEL_EXPENSE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_ID` int DEFAULT NULL,
  `TRAVEL_ID` int DEFAULT NULL,
  `TRAVEL_EXPENSE_ID` double DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FKcto2jxitp6p6mt02tm50md63n` (`CREATOR`),
  KEY `FK95hf76ho5ykssreeq9pne36ks` (`LAST_MODIFIER`),
  CONSTRAINT `FK95hf76ho5ykssreeq9pne36ks` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKcto2jxitp6p6mt02tm50md63n` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `TRAVEL_EXPENSE_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRAVEL_REQUEST`
--

DROP TABLE IF EXISTS `TRAVEL_REQUEST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRAVEL_REQUEST` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `EMPLOYEE_ID` int DEFAULT NULL,
  `TRAVEL_ID` int DEFAULT NULL,
  `EMPLOYEE_DEPARTMENT` varchar(50) DEFAULT NULL,
  `PLACE_OF_VISIT` varchar(50) DEFAULT NULL,
  `EXPECTED_DATE_OF_DEPARTURE` datetime(6) DEFAULT NULL,
  `EXPECTED_DATE_OF_ARRIVAL` datetime(6) DEFAULT NULL,
  `PURPOSE_OF_VISIT` varchar(100) DEFAULT NULL,
  `EXPECTED_DURATION_IN_DAYS` int DEFAULT NULL,
  `IS_BILLABLE_TO_CUSTOMER` varchar(20) DEFAULT NULL,
  `CUSTOMER_NAME` varchar(50) DEFAULT NULL,
  `APPROVAL_STATUS` enum('Pending','Approved','Reject','Under-Review','Closed','On Hold') DEFAULT NULL,
  `TRAVEL_INITIATE_ID` varchar(50) DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FK586lugtm0hx3tapjf649mbl69` (`CREATOR`),
  KEY `FKt9txe6qh39w51yj2b1oivap2` (`LAST_MODIFIER`),
  CONSTRAINT `FK586lugtm0hx3tapjf649mbl69` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKt9txe6qh39w51yj2b1oivap2` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `TRAVEL_REQUEST_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USA_BASED_LODGING_COSTS`
--

DROP TABLE IF EXISTS `USA_BASED_LODGING_COSTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USA_BASED_LODGING_COSTS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `COUNTY` varchar(100) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  `CITY` varchar(100) NOT NULL,
  `JANUARY` varchar(100) NOT NULL,
  `FEBRUARY` varchar(100) NOT NULL,
  `MARCH` varchar(100) NOT NULL,
  `APRIL` varchar(100) NOT NULL,
  `MAY` varchar(100) NOT NULL,
  `JUNE` varchar(100) NOT NULL,
  `JULY` varchar(100) NOT NULL,
  `AUGUST` varchar(100) NOT NULL,
  `SEPTEMBER` varchar(100) NOT NULL,
  `OCTOBER` varchar(100) NOT NULL,
  `NOVEMBER` varchar(100) NOT NULL,
  `DECEMBER` varchar(100) NOT NULL,
  `YEAR` int DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USA_BASED_MEALS_COSTS`
--

DROP TABLE IF EXISTS `USA_BASED_MEALS_COSTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USA_BASED_MEALS_COSTS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `COUNTY` varchar(100) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  `CITY` varchar(100) NOT NULL,
  `MIE__TOTAL` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `WORKFLOW_ACTION`
--

DROP TABLE IF EXISTS `WORKFLOW_ACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WORKFLOW_ACTION` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CREATED_TIME` datetime DEFAULT NULL,
  `MODIFIED_TIME` datetime DEFAULT NULL,
  `ACTION_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `ACTION_NAME` varchar(255) DEFAULT NULL,
  `ACTION_TYPE` varchar(255) DEFAULT NULL,
  `ACTIVE_EMAIL_DATA` varchar(255) DEFAULT NULL,
  `ACTIVE_NOTIFICATION_DATA` varchar(255) DEFAULT NULL,
  `ACTIVE_TEMPLATE_NAME` longtext,
  `COMPLETE_EMAIL_DATA` varchar(255) DEFAULT NULL,
  `COMPLETE_NOTIFICATION_DATA` varchar(255) DEFAULT NULL,
  `COMPLETE_TEMPLATE_NAME` longtext,
  `CONTEXT_JSON` varchar(255) DEFAULT NULL,
  `ENTITY_ID` int DEFAULT NULL,
  `ENTITY_NAME` varchar(255) DEFAULT NULL,
  `ICON_NAME` varchar(255) DEFAULT NULL,
  `OPEN_DIALOG` bit(1) DEFAULT NULL,
  `PROCESS_INSTANCE_ID` varchar(255) DEFAULT NULL,
  `ROLE_NAME` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `TASK_DEFINITION_ID` varchar(255) DEFAULT NULL,
  `VIEW_NAME` varchar(255) DEFAULT NULL,
  `WORKFLOW_NAME` varchar(255) DEFAULT NULL,
  `WORKFLOW_STAGE` varchar(255) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  `ASSIGNEE_GROUP_NAME` varchar(255) DEFAULT NULL,
  `ASSIGNEE_USER_NAME` varchar(255) DEFAULT NULL,
  `ACTIVE` bit(1) DEFAULT NULL,
  `ASSIGNEE_VENDOR_NAME` varchar(100) DEFAULT NULL,
  `CREATOR_NAME` varchar(50) DEFAULT NULL,
  `MODIFIER_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK14qjvhqon19d1q8w8878xij0o` (`CREATOR`),
  KEY `FKon7y7nikd8ale7ss0hvm4vknq` (`LAST_MODIFIER`),
  CONSTRAINT `FK14qjvhqon19d1q8w8878xij0o` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FKon7y7nikd8ale7ss0hvm4vknq` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=1830 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WORKING_HOURS`
--

DROP TABLE IF EXISTS `WORKING_HOURS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WORKING_HOURS` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `WORKSPACE_ID` int DEFAULT '1',
  `FIRST_CHECKIN` datetime(6) DEFAULT NULL,
  `LAST_CHECKOUT` datetime(6) DEFAULT NULL,
  `Min_Clocked_Hours` double DEFAULT NULL,
  `Max_Clocked_Hours` double DEFAULT NULL,
  `EMPLOYEE_ID` int DEFAULT NULL,
  `CREATED_TIME` datetime(6) DEFAULT NULL,
  `MODIFIED_TIME` datetime(6) DEFAULT NULL,
  `CREATOR` int DEFAULT NULL,
  `LAST_MODIFIER` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMPLOYEE_ID` (`EMPLOYEE_ID`),
  KEY `FK8vbnm0pkfsswlbyij6b1nqwju` (`CREATOR`),
  KEY `FK234m7sln2i4bmr75msw6yuidf` (`LAST_MODIFIER`),
  CONSTRAINT `FK234m7sln2i4bmr75msw6yuidf` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `FK8vbnm0pkfsswlbyij6b1nqwju` FOREIGN KEY (`CREATOR`) REFERENCES `User` (`userid_pk`),
  CONSTRAINT `WORKING_HOURS_ibfk_1` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE_OLD` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-26  9:14:08
