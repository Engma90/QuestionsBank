package models;

import java.sql.*;

public class DBHandler {

    private static volatile DBHandler instance = null;
    private static volatile Connection connection;

//    private static final String SERVER = "localhost";
//    private static final String PORT = "3306";
//    private static final String DB_NAME = "questionbank";
//    private static final String USER = "root";
//    private static final String PASS = "Root@1234";

//    private static final String SERVER = "remotemysql.com";
//    private static final String PORT = "3306";
//    private static final String DB_NAME = "9OIidHK4UF";
//    private static final String USER = "9OIidHK4UF";
//    private static final String PASS = "NGr5dSWL9z";

    private static final String SERVER = "196.221.207.37";
    private static final String PORT = "3306";
    private static final String DB_NAME = "questionbank_sara";
    private static final String USER = "root";
    private static final String PASS = "Root@1234";


    private DBHandler() {
        //connect();
    }

    public static DBHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (DBHandler.class) {
                if (instance == null) {
                    instance = new DBHandler();
                }
            }
        }
        return instance;
    }

    // For close connection onAppExit
    public static DBHandler tryGetInstance() {
        if (instance != null) {
            return instance;
        } else {
            return null;
        }
    }


    private void connect(boolean isFirstConnection) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (isFirstConnection) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + SERVER + ":" + PORT + "?allowMultiQueries=true", USER, PASS);
            } else {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + SERVER + ":" + PORT + "/" + DB_NAME, USER, PASS);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    private void connect() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection(
//                    "jdbc:mysql://" + SERVER + ":" + PORT + "/" + DB_NAME, USER, PASS);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public void disconnect() {
        try {
            if (isConnected()) {
                connection.close();
                connection = null;
            }
            instance = null;
        } catch (SQLException ignored) {

        }
    }


    private boolean isConnected() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is closed");
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    boolean execute_sql(String sql) {

        if (!isConnected()) {
            connect(false);
        }
        try {
            Statement stmt = connection.createStatement();
            System.out.println(sql);
            stmt.execute(sql);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    ResultSet execute_query(String sql) {
        if (!isConnected()) {
            connect(false);
        }
        try {
            Statement stmt = connection.createStatement();
            System.out.println(sql);
            return stmt.executeQuery(sql);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    int execute_PreparedStatement(String sql, String[] params) {
        if (!isConnected()) {
            connect(false);
        }
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++)
                pstmt.setString(i + 1, params[i]);

            System.out.println(sql);
            for (String s:params){
                System.out.print(s + ",  ");
            }
            System.out.println();

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int last_inserted_id = -1;
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
            pstmt.close();
            return last_inserted_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    public boolean createSchema() {
        String sql = "-- MySQL Workbench Forward Engineering\n" +
                "\n" +
                "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;\n" +
                "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;\n" +
                "SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Schema model_db\n" +
                "-- -----------------------------------------------------\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Schema model_db\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE SCHEMA IF NOT EXISTS `model_db` DEFAULT CHARACTER SET utf8 ;\n" +
                "USE `model_db` ;\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`University`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`University` (\n" +
                "  `idUniversity` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Name` VARCHAR(100) NOT NULL,\n" +
                "  `AltName` VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (`idUniversity`),\n" +
                "  UNIQUE INDEX `idUniversity_UNIQUE` (`idUniversity` ASC))\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`College`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`College` (\n" +
                "  `idCollege` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `University_idUniversity` INT NOT NULL,\n" +
                "  `Name` VARCHAR(100) NOT NULL,\n" +
                "  `AltName` VARCHAR(100) NULL,\n" +
                "  PRIMARY KEY (`idCollege`),\n" +
                "  UNIQUE INDEX `idCollege_UNIQUE` (`idCollege` ASC),\n" +
                "  INDEX `fk_College_University1_idx` (`University_idUniversity` ASC),\n" +
                "  CONSTRAINT `fk_College_University1`\n" +
                "    FOREIGN KEY (`University_idUniversity`)\n" +
                "    REFERENCES `model_db`.`University` (`idUniversity`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`Doctor`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`Doctor` (\n" +
                "  `idDoctor` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `College_idCollege` INT NOT NULL,\n" +
                "  `DoctorName` VARCHAR(100) NOT NULL,\n" +
                "  `DoctorPassword` VARCHAR(45) NOT NULL,\n" +
                "  `DoctorEmail` VARCHAR(100) NOT NULL,\n" +
                "  `DoctorDepartment` VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (`idDoctor`),\n" +
                "  UNIQUE INDEX `idDoctor_UNIQUE` (`idDoctor` ASC),\n" +
                "  UNIQUE INDEX `DoctorEmail_UNIQUE` (`DoctorEmail` ASC),\n" +
                "  INDEX `fk_Doctor_College1_idx` (`College_idCollege` ASC),\n" +
                "  CONSTRAINT `fk_Doctor_College1`\n" +
                "    FOREIGN KEY (`College_idCollege`)\n" +
                "    REFERENCES `model_db`.`College` (`idCollege`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`Course`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`Course` (\n" +
                "  `idCourse` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Doctor_idDoctor` INT NOT NULL,\n" +
                "  `CourseLevel` VARCHAR(45) NOT NULL,\n" +
                "  `CourseName` VARCHAR(100) NOT NULL,\n" +
                "  `CourseCode` VARCHAR(45) NOT NULL,\n" +
                "  `CourseYear` VARCHAR(45) NOT NULL,\n" +
                "  `PreferredExamLayout` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`idCourse`),\n" +
                "  UNIQUE INDEX `idCourse_UNIQUE` (`idCourse` ASC),\n" +
                "  INDEX `fk_Course_Doctor_idx` (`Doctor_idDoctor` ASC),\n" +
                "  CONSTRAINT `fk_Course_Doctor`\n" +
                "    FOREIGN KEY (`Doctor_idDoctor`)\n" +
                "    REFERENCES `model_db`.`Doctor` (`idDoctor`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`Chapter`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`Chapter` (\n" +
                "  `idChapter` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Course_idCourse` INT NOT NULL,\n" +
                "  `ChapterName` VARCHAR(100) NOT NULL,\n" +
                "  `ChapterNumber` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idChapter`),\n" +
                "  UNIQUE INDEX `idChapter_UNIQUE` (`idChapter` ASC),\n" +
                "  INDEX `fk_Chapter_Course1_idx` (`Course_idCourse` ASC),\n" +
                "  CONSTRAINT `fk_Chapter_Course1`\n" +
                "    FOREIGN KEY (`Course_idCourse`)\n" +
                "    REFERENCES `model_db`.`Course` (`idCourse`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`Topic`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`Topic` (\n" +
                "  `idTopic` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Chapter_idChapter` INT NOT NULL,\n" +
                "  `Name` VARCHAR(200) NOT NULL,\n" +
                "  PRIMARY KEY (`idTopic`),\n" +
                "  UNIQUE INDEX `idTopic_UNIQUE` (`idTopic` ASC),\n" +
                "  INDEX `fk_Topic_Chapter1_idx` (`Chapter_idChapter` ASC),\n" +
                "  CONSTRAINT `fk_Topic_Chapter1`\n" +
                "    FOREIGN KEY (`Chapter_idChapter`)\n" +
                "    REFERENCES `model_db`.`Chapter` (`idChapter`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`Question`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`Question` (\n" +
                "  `idQuestion` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Topic_idTopic` INT NOT NULL,\n" +
                "  `QuestionType` VARCHAR(45) NOT NULL,\n" +
                "  `QuestionDifficulty` INT NOT NULL,\n" +
                "  `QuestionWeight` INT NOT NULL,\n" +
                "  `QuestionExpectedTime` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idQuestion`),\n" +
                "  UNIQUE INDEX `idQuestion_UNIQUE` (`idQuestion` ASC),\n" +
                "  INDEX `fk_Question_Topic1_idx` (`Topic_idTopic` ASC),\n" +
                "  CONSTRAINT `fk_Question_Topic1`\n" +
                "    FOREIGN KEY (`Topic_idTopic`)\n" +
                "    REFERENCES `model_db`.`Topic` (`idTopic`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`QuestionAnswer`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`QuestionAnswer` (\n" +
                "  `idAnswer` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Question_idQuestion` INT NOT NULL,\n" +
                "  `AnswerContent` TEXT NOT NULL,\n" +
                "  PRIMARY KEY (`idAnswer`),\n" +
                "  UNIQUE INDEX `idAnswer_UNIQUE` (`idAnswer` ASC),\n" +
                "  INDEX `fk_QuestionAnswer_Question1_idx` (`Question_idQuestion` ASC),\n" +
                "  CONSTRAINT `fk_QuestionAnswer_Question1`\n" +
                "    FOREIGN KEY (`Question_idQuestion`)\n" +
                "    REFERENCES `model_db`.`Question` (`idQuestion`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`Exam`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`Exam` (\n" +
                "  `idExam` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Doctor_idDoctor` INT NOT NULL,\n" +
                "  `Date` VARCHAR(45) NOT NULL,\n" +
                "  `ExamName` VARCHAR(100) NOT NULL,\n" +
                "  `College` VARCHAR(100) NOT NULL,\n" +
                "  `Department` VARCHAR(100) NOT NULL,\n" +
                "  `Note` TEXT NULL,\n" +
                "  `ExamType` VARCHAR(45) NOT NULL,\n" +
                "  `Duration` VARCHAR(45) NOT NULL,\n" +
                "  `TotalMarks` VARCHAR(45) NOT NULL,\n" +
                "  `ExamLanguage` VARCHAR(45) NOT NULL,\n" +
                "  `CourseName` VARCHAR(100) NOT NULL,\n" +
                "  `CourseCode` VARCHAR(45) NOT NULL,\n" +
                "  `CourseLevel` VARCHAR(45) NOT NULL,\n" +
                "  `CourseYear` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`idExam`),\n" +
                "  UNIQUE INDEX `idExam_UNIQUE` (`idExam` ASC),\n" +
                "  INDEX `fk_Exam_Doctor1_idx` (`Doctor_idDoctor` ASC),\n" +
                "  CONSTRAINT `fk_Exam_Doctor1`\n" +
                "    FOREIGN KEY (`Doctor_idDoctor`)\n" +
                "    REFERENCES `model_db`.`Doctor` (`idDoctor`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`ExamModel`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`ExamModel` (\n" +
                "  `idExamModel` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Exam_idExam` INT NOT NULL,\n" +
                "  `ExamModelNumber` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idExamModel`),\n" +
                "  UNIQUE INDEX `idExamModel_UNIQUE` (`idExamModel` ASC),\n" +
                "  INDEX `fk_ExamModel_Exam1_idx` (`Exam_idExam` ASC),\n" +
                "  CONSTRAINT `fk_ExamModel_Exam1`\n" +
                "    FOREIGN KEY (`Exam_idExam`)\n" +
                "    REFERENCES `model_db`.`Exam` (`idExam`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`ExamQuestion`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`ExamQuestion` (\n" +
                "  `idQuestion` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `ExamModel_idExamModel` INT NOT NULL,\n" +
                "  `QuestionType` VARCHAR(45) NOT NULL,\n" +
                "  `QuestionDifficulty` INT NOT NULL,\n" +
                "  `QuestionWeight` INT NOT NULL,\n" +
                "  `QuestionExpectedTime` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idQuestion`),\n" +
                "  UNIQUE INDEX `idQuestion_UNIQUE` (`idQuestion` ASC),\n" +
                "  INDEX `fk_ExamQuestion_ExamModel1_idx` (`ExamModel_idExamModel` ASC),\n" +
                "  CONSTRAINT `fk_ExamQuestion_ExamModel1`\n" +
                "    FOREIGN KEY (`ExamModel_idExamModel`)\n" +
                "    REFERENCES `model_db`.`ExamModel` (`idExamModel`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`ExamQuestionAnswer`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`ExamQuestionAnswer` (\n" +
                "  `idAnswer` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `ExamQuestion_idQuestion` INT NOT NULL,\n" +
                "  `AnswerContent` TEXT NOT NULL,\n" +
                "  PRIMARY KEY (`idAnswer`),\n" +
                "  UNIQUE INDEX `idAnswer_UNIQUE` (`idAnswer` ASC),\n" +
                "  INDEX `fk_ExamQuestionAnswer_ExamQuestion1_idx` (`ExamQuestion_idQuestion` ASC),\n" +
                "  CONSTRAINT `fk_ExamQuestionAnswer_ExamQuestion1`\n" +
                "    FOREIGN KEY (`ExamQuestion_idQuestion`)\n" +
                "    REFERENCES `model_db`.`ExamQuestion` (`idQuestion`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`QuestionContent`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`QuestionContent` (\n" +
                "  `idQuestionContent` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `Question_idQuestion` INT NOT NULL,\n" +
                "  `QuestionContent` TEXT NOT NULL,\n" +
                "  PRIMARY KEY (`idQuestionContent`),\n" +
                "  INDEX `fk_QuestionContent_Question1_idx` (`Question_idQuestion` ASC),\n" +
                "  UNIQUE INDEX `idQuestionContent_UNIQUE` (`idQuestionContent` ASC),\n" +
                "  CONSTRAINT `fk_QuestionContent_Question1`\n" +
                "    FOREIGN KEY (`Question_idQuestion`)\n" +
                "    REFERENCES `model_db`.`Question` (`idQuestion`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`ExamQuestionContent`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`ExamQuestionContent` (\n" +
                "  `idQuestionContent` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `ExamQuestion_idQuestion` INT NOT NULL,\n" +
                "  `QuestionContent` TEXT NOT NULL,\n" +
                "  PRIMARY KEY (`idQuestionContent`),\n" +
                "  INDEX `fk_ExamQuestionContent_ExamQuestion1_idx` (`ExamQuestion_idQuestion` ASC),\n" +
                "  UNIQUE INDEX `idQuestionContent_UNIQUE` (`idQuestionContent` ASC),\n" +
                "  CONSTRAINT `fk_ExamQuestionContent_ExamQuestion1`\n" +
                "    FOREIGN KEY (`ExamQuestion_idQuestion`)\n" +
                "    REFERENCES `model_db`.`ExamQuestion` (`idQuestion`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`ExamContentRightAnswer`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`ExamContentRightAnswer` (\n" +
                "  `idExamQuestionContentRightAnswer` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  `ExamQuestionContent_idQuestionContent` INT NOT NULL,\n" +
                "  `ExamQuestionAnswer_idAnswer` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idExamQuestionContentRightAnswer`),\n" +
                "  UNIQUE INDEX `idExamQuestionContentRightAnswer_UNIQUE` (`idExamQuestionContentRightAnswer` ASC),\n" +
                "  INDEX `fk_ExamContentRightAnswer_ExamQuestionContent1_idx` (`ExamQuestionContent_idQuestionContent` ASC),\n" +
                "  INDEX `fk_ExamContentRightAnswer_ExamQuestionAnswer1_idx` (`ExamQuestionAnswer_idAnswer` ASC),\n" +
                "  CONSTRAINT `fk_ExamContentRightAnswer_ExamQuestionContent1`\n" +
                "    FOREIGN KEY (`ExamQuestionContent_idQuestionContent`)\n" +
                "    REFERENCES `model_db`.`ExamQuestionContent` (`idQuestionContent`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_ExamContentRightAnswer_ExamQuestionAnswer1`\n" +
                "    FOREIGN KEY (`ExamQuestionAnswer_idAnswer`)\n" +
                "    REFERENCES `model_db`.`ExamQuestionAnswer` (`idAnswer`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `model_db`.`ContentRightAnswer`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `model_db`.`ContentRightAnswer` (\n" +
                "  `idQuestionContentRightAnswer` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  `QuestionContent_idQuestionContent` INT NOT NULL,\n" +
                "  `QuestionAnswer_idAnswer` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idQuestionContentRightAnswer`),\n" +
                "  UNIQUE INDEX `idExamQuestionContentRightAnswer_UNIQUE` (`idQuestionContentRightAnswer` ASC),\n" +
                "  INDEX `fk_ExamContentRightAnswer_QuestionAnswer1_idx` (`QuestionAnswer_idAnswer` ASC),\n" +
                "  INDEX `fk_ContentRightAnswer_QuestionContent1_idx` (`QuestionContent_idQuestionContent` ASC),\n" +
                "  CONSTRAINT `fk_ExamContentRightAnswer_QuestionAnswer1`\n" +
                "    FOREIGN KEY (`QuestionAnswer_idAnswer`)\n" +
                "    REFERENCES `model_db`.`QuestionAnswer` (`idAnswer`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_ContentRightAnswer_QuestionContent1`\n" +
                "    FOREIGN KEY (`QuestionContent_idQuestionContent`)\n" +
                "    REFERENCES `model_db`.`QuestionContent` (`idQuestionContent`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "SET SQL_MODE=@OLD_SQL_MODE;\n" +
                "SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;\n" +
                "SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`University`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`University` (`idUniversity`, `Name`, `AltName`) VALUES (1, 'Benha university', 'جامعة بنها');\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`College`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (1, 1, 'Faculty of Agriculture', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (2, 1, 'Faculty of Applied Arts', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (3, 1, 'Faculty of Arts', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (4, 1, 'Faculty of Commerce', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (5, 1, 'Faculty of Computers and Informatics', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (6, 1, 'Faculty of Education', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (7, 1, 'Faculty of Engineering, Benha', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (8, 1, 'Faculty of Engineering, Shoubra', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (9, 1, 'Faculty of Law', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (10, 1, 'Faculty of Medicine', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (11, 1, 'Faculty of Nursing', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (12, 1, 'Faculty of Physical Education', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (13, 1, 'Faculty of Science', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (14, 1, 'Faculty of Specific Education', NULL);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `University_idUniversity`, `Name`, `AltName`) VALUES (15, 1, 'Faculty of Veterinary Medicine', NULL);\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`Doctor`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`Doctor` (`idDoctor`, `College_idCollege`, `DoctorName`, `DoctorPassword`, `DoctorEmail`, `DoctorDepartment`) VALUES (1, 8, 'TestAccount', 'a', 'a', 'Electrical Engineering Department ');\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`Course`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`Course` (`idCourse`, `Doctor_idDoctor`, `CourseLevel`, `CourseName`, `CourseCode`, `CourseYear`, `PreferredExamLayout`) VALUES (1, 1, 'Undergraduates', 'course1', 'EE1', '1st Year', 'English');\n" +
                "INSERT INTO `model_db`.`Course` (`idCourse`, `Doctor_idDoctor`, `CourseLevel`, `CourseName`, `CourseCode`, `CourseYear`, `PreferredExamLayout`) VALUES (2, 1, 'Undergraduates', 'course2', 'EE2', '1st Year', 'English');\n" +
                "INSERT INTO `model_db`.`Course` (`idCourse`, `Doctor_idDoctor`, `CourseLevel`, `CourseName`, `CourseCode`, `CourseYear`, `PreferredExamLayout`) VALUES (3, 1, 'Postgraduates', 'Field', 'EEF', '1st Year', 'English');\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`Chapter`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`Chapter` (`idChapter`, `Course_idCourse`, `ChapterName`, `ChapterNumber`) VALUES (1, 2, 'course2_chapter1', 1);\n" +
                "INSERT INTO `model_db`.`Chapter` (`idChapter`, `Course_idCourse`, `ChapterName`, `ChapterNumber`) VALUES (2, 2, 'course2_chapter2', 2);\n" +
                "INSERT INTO `model_db`.`Chapter` (`idChapter`, `Course_idCourse`, `ChapterName`, `ChapterNumber`) VALUES (3, 1, 'course1_chapter1', 1);\n" +
                "INSERT INTO `model_db`.`Chapter` (`idChapter`, `Course_idCourse`, `ChapterName`, `ChapterNumber`) VALUES (4, 1, 'course1_chapter2', 2);\n" +
                "INSERT INTO `model_db`.`Chapter` (`idChapter`, `Course_idCourse`, `ChapterName`, `ChapterNumber`) VALUES (5, 2, 'course2_chapter3', 3);\n" +
                "INSERT INTO `model_db`.`Chapter` (`idChapter`, `Course_idCourse`, `ChapterName`, `ChapterNumber`) VALUES (6, 2, 'course2_chapter4', 4);\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`Topic`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`Topic` (`idTopic`, `Chapter_idChapter`, `Name`) VALUES (1, 1, 'topic1');\n" +
                "INSERT INTO `model_db`.`Topic` (`idTopic`, `Chapter_idChapter`, `Name`) VALUES (2, 1, 'tobic2');\n" +
                "\n" +
                "COMMIT;\n" +
                "\n";
        sql = sql
                .replace("model_db", DB_NAME)
                .replace("\n", System.lineSeparator());

        connect(true);

        if (!isConnected())
            return false;
        else {
            PreparedStatement pstmt = null;
            try {
                pstmt =
                        connection.prepareStatement(sql);
                pstmt.addBatch();
                pstmt.executeBatch();

                return true;
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry '1' for key 'PRIMARY'")
                        || e.getErrorCode() == 121) // means db already there (Successfully Connected)
                    return true;
                e.printStackTrace();
                return false;
            } finally {
                if (null != pstmt) {
                    try {
                        pstmt.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
