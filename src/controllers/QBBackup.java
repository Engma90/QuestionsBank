package controllers;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import models.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class QBBackup {
    private List<String[]> CourseCSVList, ChapterCSVList, TopicCSVList,
            QuestionCSVList, QuestionContentCSVList, AnswerCSVList,RightAnswerCSVList;

    public QBBackup() {
        CourseCSVList = new ArrayList<>();
        ChapterCSVList = new ArrayList<>();
        TopicCSVList = new ArrayList<>();
        QuestionCSVList = new ArrayList<>();
        QuestionContentCSVList = new ArrayList<>();
        AnswerCSVList = new ArrayList<>();
        RightAnswerCSVList = new ArrayList<>();
    }

    void qbExport(Course _course, String DestPath) {

        cleanup();

        add_row(CourseCSVList, new String[]{"Id", "Name", "Code", "Program", "Level", "Year", "PreferredExamLayout"});
        add_row(ChapterCSVList, new String[]{"Id", "Course_ID", "Name", "Number"});
        add_row(TopicCSVList, new String[]{"Id", "Chapter_ID", "Name"});
        add_row(QuestionCSVList, new String[]{"Id", "Topic_ID", "Type", "Difficulty", "Time", "Weight"});
        add_row(QuestionContentCSVList, new String[]{"Id", "Question_ID", "Content"});
        add_row(AnswerCSVList, new String[]{"Id", "Question_ID", "Content"});
        add_row(RightAnswerCSVList, new String[]{"Id", "Question_Content_ID", "Answer_ID"});
        List<Course> coursesList = FXCollections.observableArrayList();//CoursesListHandler.getInstance().getList("All");
        coursesList.add(_course);
        int course_id = 0;
        int chapter_id = 0;
        int topic_id = 0;
        int question_id = 0;
        int question_content_id = 0;
        int answer_id = 0;
        int right_answer_id = 0;
        int accum_answer_id = 0;

        for (Course course : coursesList) {
            add_row(CourseCSVList,
                    new String[]{(course_id++ + 1) + "", course.getName(), course.getCode(), course.getProgram(), course.getLevel(), course.getYear(), course.getPreferredExamLayout()});

            List<Chapter> chaptersList = ChaptersListHandler.getInstance().getList(course);


            for (Chapter chapter : chaptersList) {
                add_row(ChapterCSVList,
                        new String[]{(chapter_id++ + 1) + "", (course_id) + "", chapter.name, chapter.number});

                List<Topic> topicsList = TopicListHandler.getInstance().getList(chapter);


                for (Topic topic : topicsList) {
                    add_row(TopicCSVList,
                            new String[]{(topic_id++ + 1) + "", (chapter_id) + "", topic.name});
                    List<Question> questionsList = QuestionsTableHandler.getInstance().getQuestionList(topic);


                    for (Question question : questionsList) {
                        add_row(QuestionCSVList,
                                new String[]{(question_id++ + 1) + "", (topic_id) + "",
                                        question.getQuestion_type(),
                                        question.getQuestion_diff(),
                                        question.getExpected_time(),
                                        question.getQuestion_weight()
                                });
                        List<Answer> answerList = question.getAnswers();
                        for (Answer answer : answerList) {
                            add_row(AnswerCSVList,
                                    new String[]{(answer_id++ + 1) + "", (question_id) + "",
                                            answer.answer_text});
                        }

                        List<QuestionContent> questionContentsList = question.getContents();

                        for (QuestionContent questionContent : questionContentsList) {

                            //Todo: Accumulative rightAnswer_ID
                            add_row(QuestionContentCSVList,
                                    new String[]{(question_content_id++ + 1) + "", (question_id) + "",
                                            questionContent.getContent()
                                    });

                            List<Answer> rightAnswersList = questionContent.getRightAnswers();
                            for (Answer rightAnswer : rightAnswersList) {
                                add_row(RightAnswerCSVList,
                                        new String[]{(right_answer_id++ + 1) + "", (question_content_id) + "",
                                                (question.getAnswers().indexOf(rightAnswer) + accum_answer_id +1) + ""
                                        });
                            }


                        }
                        accum_answer_id += question.getAnswers().size();
                    }
                }
            }
        }
        writeData("Course.csv", CourseCSVList);
        writeData("Chapter.csv", ChapterCSVList);
        writeData("Topic.csv", TopicCSVList);
        writeData("Question.csv", QuestionCSVList);
        writeData("QuestionContent.csv", QuestionContentCSVList);
        writeData("Answer.csv", AnswerCSVList);
        writeData("RightAnswer.csv", RightAnswerCSVList);
        try {
            zip(DestPath);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Export Error").show();
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void cleanup() {

        try {
            new File("tmp.zip").delete();
            FileUtils.deleteDirectory(new File("tmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void qbImport(String SourcePath) {

        cleanup();

        try {
            File copied = new File(
                    "tmp.zip");
            File original = new File(SourcePath);
            FileUtils.copyFile(original, copied);
            unzip("tmp.zip");

            //CSVReader reader = new CSVReader(new FileReader("data.csv"));
            String path = FileSystems.getDefault().getPath("tmp").toString();
            //System.out.println(path);
            CourseCSVList = readData(path + "/Course.csv");
            ChapterCSVList = readData(path + "/Chapter.csv");
            TopicCSVList = readData(path + "/Topic.csv");
            QuestionCSVList = readData(path + "/Question.csv");
            QuestionContentCSVList = readData(path + "/QuestionContent.csv");
            AnswerCSVList = readData(path + "/Answer.csv");
            RightAnswerCSVList = readData(path + "/RightAnswer.csv");
            CourseCSVList.remove(0);
            ChapterCSVList.remove(0);
            TopicCSVList.remove(0);
            QuestionCSVList.remove(0);
            QuestionContentCSVList.remove(0);
            AnswerCSVList.remove(0);
            RightAnswerCSVList.remove(0);
            for (String[] course_data : CourseCSVList) {
                //String fake_course_id = course_data[0];
                Course course = new Course();
                course.name = course_data[1];
                course.code = course_data[2];
                course.program = course_data[3];
                course.level = course_data[4];
                course.year = course_data[5];
                course.setPreferredExamLayout(course_data[6]);
                course.id = CoursesListHandler.getInstance().Add(course) + "";
                for (String[] chapter_data : ChapterCSVList) {
                    //String fake_chapter_id = chapter_data[0];
                    if (course_data[0].equals(chapter_data[1])) {
                        Chapter chapter = new Chapter();
                        chapter.name = chapter_data[2];
                        chapter.number = chapter_data[3];
                        chapter.id = ChaptersListHandler.getInstance().Add(course, chapter) + "";
                        for (String[] topic_data : TopicCSVList) {
                            //String fake_topic_id = topic_data[0];
                            if (chapter_data[0].equals(topic_data[1])) {
                                Topic topic = new Topic();
                                topic.name = topic_data[2];
                                topic.id = TopicListHandler.getInstance().Add(chapter, topic) + "";
                                for (String[] question_data : QuestionCSVList) {
                                    //String fake_question_id = question_data[0];
                                    if (topic_data[0].equals(question_data[1])) {
                                        Question question = new Question();
                                        question.setQuestion_type(question_data[2]);
                                        question.setQuestion_diff(question_data[3]);
                                        question.setExpected_time(question_data[4]);
                                        question.setQuestion_weight(question_data[5]);

                                        List<Answer> temp_answer_list = new ArrayList<>();
                                        for (String[] answer_data : AnswerCSVList) {
                                            if (question_data[0].equals(answer_data[1])) {
                                                Answer answer = new Answer();
                                                answer.id = answer_data[0];
                                                answer.answer_text = answer_data[2];
                                                temp_answer_list.add(answer);
                                            }
                                        }
                                        question.setAnswers(temp_answer_list);


                                        List<QuestionContent> temp_content_list = new ArrayList<>();
                                        for (String[] content_data : QuestionContentCSVList) {
                                            if (question_data[0].equals(content_data[1])) {
                                                QuestionContent questionContent = new QuestionContent();
                                                questionContent.setContent(content_data[2]);
                                                temp_content_list.add(questionContent);



                                                List<Answer> temp_right_answer_list = new ArrayList<>();
                                                for (String[] right_answer_data : RightAnswerCSVList) {
                                                    if (content_data[0].equals(right_answer_data[1])) {
                                                        for(Answer answer:question.getAnswers()){
                                                            if (right_answer_data[2].equals(answer.id)){
                                                                temp_right_answer_list.add(answer);
                                                            }
                                                        }

                                                    }
                                                }
                                                questionContent.setRightAnswers(temp_right_answer_list);



                                            }
                                        }
                                        question.setContents(temp_content_list);


                                        QuestionsTableHandler.getInstance().Add(topic, question);

                                    }

                                }

                            }

                        }
                    }


                }

            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Import Error").show();
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private List<String[]> readData(String csvFile) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(csvFile));
        List<String[]> temp_list = reader.readAll();
        reader.close();
        return temp_list;
    }

    private void add_row(List<String[]> sheet, String[] row_cells) {
        sheet.add(row_cells);
    }

    private void writeData(String filePath, List<String[]> data) {
        File file = createFileWithDir(filePath).toFile();

        try {
            //FileWriter output_file = new FileWriter(file);
//            CSVWriter writer = new CSVWriter(output_file,
//                    CSVWriter.DEFAULT_SEPARATOR,
//                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
//                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//                    CSVWriter.DEFAULT_LINE_END);

            FileOutputStream os = new FileOutputStream(file);
            os.write(0xef);
            os.write(0xbb);
            os.write(0xbf);
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(os));


            writer.writeAll(data);
            writer.close();
            //output_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path createFileWithDir(String filename) {
        File dir = new File("tmp");
        if (!dir.exists()) {
            boolean done = dir.mkdir();
        }
        return Paths.get("tmp" + File.separatorChar + filename);
    }


    private void zip(String SavePath) throws IOException {
        String sourceFile = "tmp";
        FileOutputStream fos = new FileOutputStream(SavePath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        //zipFile(fileToZip, fileToZip.getName(), zipOut);
        File[] children = fileToZip.listFiles();
        if (children != null) {
            for (File childFile : children) {
                zipFile(childFile, zipOut);
            }
        }
        zipOut.close();
        fos.close();
    }

    private void zipFile(File fileToZip, ZipOutputStream zipOut) throws IOException {
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }


    private void unzip(String ArchivePath) throws IOException {
        //String fileZip = "src/main/resources/unzipTest/compressed.zip";
        File destDir = createFileWithDir("/").toFile();
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(ArchivePath));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }


}