package models;

import javafx.scene.control.Alert;

import java.sql.*;

// Double Check Locking Principle
public class DBSingletonHandler {

    private static volatile DBSingletonHandler instance = null;

    private static final String SERVER = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "questionbank";
    private static final String USER = "root";
    private static final String PASS = "Root@1234";

    private Connection connection;

    private DBSingletonHandler() {
        if (!createSchema()) {
            new Alert(Alert.AlertType.ERROR, "Couldn't connect to database").show();
        } else {
            connection = getConnection();
        }
    }

    public static DBSingletonHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (DBSingletonHandler.class) {
                if (instance == null) {
                    instance = new DBSingletonHandler();
                }
            }
        }
        return instance;
    }


    private Connection getCreationConnection() {
        try {
            Connection _connection;
            Class.forName("com.mysql.jdbc.Driver");
            _connection = DriverManager.getConnection(
                    "jdbc:mysql://" + SERVER + ":" + PORT + "?allowMultiQueries=true", USER, PASS);
            return _connection;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Connection getConnection() {
        try {
            Connection _connection;
            Class.forName("com.mysql.jdbc.Driver");
            _connection = DriverManager.getConnection(
                    "jdbc:mysql://" + SERVER + ":" + PORT + "/" + DB_NAME, USER, PASS);
            return _connection;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Todo: call on program exit
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is closed, reconnecting...");
                connection = getConnection();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean execute_sql(String sql) {
        checkConnection();
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
        checkConnection();
        try {
            Statement stmt = connection.createStatement();
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    int execute_PreparedStatement(String sql, String[] params) {
        checkConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++)
                pstmt.setString(i + 1, params[i]);
            System.out.println(sql);
            for (String s : params)
                System.out.print(s + "   ");
            System.out.println();
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int last_inserted_id = -1;
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
            //System.out.println(rs);
            pstmt.close();
            return last_inserted_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    private boolean createSchema() {
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
                "  `Name` VARCHAR(45) NOT NULL,\n" +
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
                "  `Name` VARCHAR(45) NOT NULL,\n" +
                "  `University_idUniversity` INT NOT NULL,\n" +
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
                "  `DoctorName` VARCHAR(45) NOT NULL,\n" +
                "  `DoctorPassword` VARCHAR(45) NOT NULL,\n" +
                "  `DoctorEmail` VARCHAR(45) NOT NULL,\n" +
                "  `College_idCollege` INT NOT NULL,\n" +
                "  `DoctorDepartment` VARCHAR(45) NOT NULL,\n" +
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
                "  `CourseName` VARCHAR(45) NOT NULL,\n" +
                "  `CourseCode` VARCHAR(45) NOT NULL,\n" +
                "  `CourseYear` VARCHAR(45) NOT NULL,\n" +
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
                "  `ChapterName` VARCHAR(45) NOT NULL,\n" +
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
                "  `Name` VARCHAR(45) NOT NULL,\n" +
                "  `Chapter_idChapter` INT NOT NULL,\n" +
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
                "  `QuestionContent` TEXT NOT NULL,\n" +
                "  `QuestionType` VARCHAR(10) NOT NULL,\n" +
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
                "  `AnswerLabel` VARCHAR(1) NOT NULL,\n" +
                "  `AnswerContent` TEXT NOT NULL,\n" +
                "  `IsRightAnswer` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idAnswer`),\n" +
                "  UNIQUE INDEX `idAnswer_UNIQUE` (`idAnswer` ASC),\n" +
                "  INDEX `fk_Answer_Question1_idx` (`Question_idQuestion` ASC),\n" +
                "  CONSTRAINT `fk_Answer_Question1`\n" +
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
                "  `ExamName` VARCHAR(45) NOT NULL,\n" +
                "  `College` VARCHAR(45) NOT NULL,\n" +
                "  `Department` VARCHAR(45) NOT NULL,\n" +
                "  `Note` TEXT NOT NULL,\n" +
                "  `ExamType` VARCHAR(45) NOT NULL,\n" +
                "  `Duration` VARCHAR(45) NOT NULL,\n" +
                "  `TotalMarks` VARCHAR(45) NOT NULL,\n" +
                "  `ExamLanguage` VARCHAR(45) NOT NULL,\n" +
                "  `CourseName` VARCHAR(45) NOT NULL,\n" +
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
                "  `QuestionContent` TEXT NOT NULL,\n" +
                "  `QuestionType` VARCHAR(10) NOT NULL,\n" +
                "  `QuestionDifficulty` INT NOT NULL,\n" +
                "  `QuestionWeight` INT NOT NULL,\n" +
                "  `QuestionExpectedTime` INT NOT NULL,\n" +
                "  `ExamModel_idExamModel` INT NOT NULL,\n" +
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
                "  `AnswerLabel` VARCHAR(1) NOT NULL,\n" +
                "  `AnswerContent` TEXT NOT NULL,\n" +
                "  `IsRightAnswer` INT NOT NULL,\n" +
                "  `ExamQuestion_idQuestion` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idAnswer`),\n" +
                "  UNIQUE INDEX `idAnswer_UNIQUE` (`idAnswer` ASC),\n" +
                "  INDEX `fk_QuestionAnswer_copy1_ExamQuestion1_idx` (`ExamQuestion_idQuestion` ASC),\n" +
                "  CONSTRAINT `fk_QuestionAnswer_copy1_ExamQuestion1`\n" +
                "    FOREIGN KEY (`ExamQuestion_idQuestion`)\n" +
                "    REFERENCES `model_db`.`ExamQuestion` (`idQuestion`)\n" +
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
                "INSERT INTO `model_db`.`University` (`idUniversity`, `Name`) VALUES (1, 'Benha');\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`College`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (1, 'Agriculture', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (2, 'Applied Arts', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (3, 'Arts', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (4, 'Commerce', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (5, 'Computers and Informatics', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (6, 'Education', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (7, 'Engineering, Benha', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (8, 'Engineering, Shoubra', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (9, 'Law', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (10, 'Medicine', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (11, 'Nursing', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (12, 'Physical Education', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (13, 'Science', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (14, 'Specific Education', 1);\n" +
                "INSERT INTO `model_db`.`College` (`idCollege`, `Name`, `University_idUniversity`) VALUES (15, 'Veterinary Medicine', 1);\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`Doctor`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`Doctor` (`idDoctor`, `DoctorName`, `DoctorPassword`, `DoctorEmail`, `College_idCollege`, `DoctorDepartment`) VALUES (1, 'TestAccount', 'a', 'a', 8, 'Electrical Engineering Department ');\n" +
                "\n" +
                "COMMIT;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Data for table `model_db`.`Course`\n" +
                "-- -----------------------------------------------------\n" +
                "START TRANSACTION;\n" +
                "USE `model_db`;\n" +
                "INSERT INTO `model_db`.`Course` (`idCourse`, `Doctor_idDoctor`, `CourseLevel`, `CourseName`, `CourseCode`, `CourseYear`) VALUES (1, 1, 'Undergraduates', 'course1', 'EE1', '1st Year');\n" +
                "INSERT INTO `model_db`.`Course` (`idCourse`, `Doctor_idDoctor`, `CourseLevel`, `CourseName`, `CourseCode`, `CourseYear`) VALUES (2, 1, 'Undergraduates', 'course2', 'EE2', '1st Year');\n" +
                "INSERT INTO `model_db`.`Course` (`idCourse`, `Doctor_idDoctor`, `CourseLevel`, `CourseName`, `CourseCode`, `CourseYear`) VALUES (3, 1, 'Postgraduates', 'Field', 'EEF', '1st Year');\n" +
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
                "INSERT INTO `model_db`.`Topic` (`idTopic`, `Name`, `Chapter_idChapter`) VALUES (1, 'topic1', 1);\n" +
                "INSERT INTO `model_db`.`Topic` (`idTopic`, `Name`, `Chapter_idChapter`) VALUES (2, 'tobic2', 1);\n" +
                "\n" +
                "COMMIT;\n" +
                "\n";
        sql = sql
                .replace("model_db", DB_NAME)
                .replace("\n", System.lineSeparator());
//                .replace("University","University".toLowerCase())
//                .replace("College","College".toLowerCase())
//                .replace("Doctor","Doctor".toLowerCase())
//
//                .replace("Course","Course".toLowerCase())
//                .replace("Chapter","Chapter".toLowerCase())
//                .replace("Topic","Topic".toLowerCase())
//
//                .replace("Question","Question".toLowerCase())
//                .replace("QuestionAnswer","QuestionAnswer".toLowerCase())
//
//                .replace("Exam","Exam".toLowerCase())
//                .replace("ExamModel","ExamModel".toLowerCase())
//                .replace("ExamQuestion","ExamQuestion".toLowerCase())
//                .replace("ExamQuestionAnswer","ExamQuestionAnswer".toLowerCase());

        System.out.println(sql);
        try {
            Connection _connection = getCreationConnection();
            if (_connection != null) {
                PreparedStatement pstmt =
                        _connection.prepareStatement(sql);
                pstmt.addBatch();
                pstmt.executeBatch();
                pstmt.close();
                _connection.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry '1' for key 'PRIMARY'")
            || e.getErrorCode()== 121) // means db already there (Successfully Connected)
                return true;
            e.printStackTrace();
            return false;
        }

    }
}
