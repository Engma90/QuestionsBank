package controllers;

import com.opencsv.CSVWriter;
import models.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class QBBackup {

    void qbExport(String DestPath) {
        List<String[]> CourseCSVList, ChapterCSVList, TopicCSVList, QuestionCSVList, AnswerCSVList;
        CourseCSVList = new ArrayList<>();
        ChapterCSVList = new ArrayList<>();
        TopicCSVList = new ArrayList<>();
        QuestionCSVList = new ArrayList<>();
        AnswerCSVList = new ArrayList<>();
        add_row(CourseCSVList, new String[]{"Id", "Name", "Code", "Level"});
        add_row(ChapterCSVList, new String[]{"Id", "Course_ID", "Name", "Number"});
        add_row(TopicCSVList, new String[]{"Id", "Chapter_ID", "Name"});
        add_row(QuestionCSVList, new String[]{"Id", "Topic_ID", "Content", "Type", "Difficulty", "Time", "Weight"});
        add_row(AnswerCSVList, new String[]{"Id", "Question_ID", "Content", "isRightAnswer"});
        List<Course> coursesList = CoursesListHandler.getInstance().getCoursesList("All");
        int course_id = 0;
        int chapter_id = 0;
        int topic_id = 0;
        int question_id = 0;
        int answer_id = 0;

        for (Course course : coursesList) {
            add_row(CourseCSVList,
                    new String[]{(course_id++ + 1) + "", course.getName(), course.getCode(), course.getLevel()});

            List<Chapter> chaptersList = ChaptersListHandler.getInstance().getChaptersList(course);


            for (Chapter chapter : chaptersList) {
                add_row(ChapterCSVList,
                        new String[]{(chapter_id++ + 1) + "", (course_id) + "", chapter.name, chapter.number});

                List<Topic> topicsList = TopicListHandler.getInstance().getTopicsList(chapter);


                for (Topic topic : topicsList) {
                    add_row(TopicCSVList,
                            new String[]{(topic_id++ + 1) + "", (chapter_id) + "", topic.name});
                    List<Question> questionsList = QuestionsTableHandler.getInstance().getQuestionList(topic);


                    for (Question question : questionsList) {
                        add_row(QuestionCSVList,
                                new String[]{(question_id++ + 1) + "", (topic_id) + "",
                                        question.getQuestion_text(),
                                        question.getQuestion_type(),
                                        question.getQuestion_diff(),
                                        question.getExpected_time(),
                                        question.getQuestion_weight()
                                });
                        List<Answer> answerList = question.getAnswers();

                        for (Answer answer : answerList) {
                            add_row(AnswerCSVList,
                                    new String[]{(answer_id++ + 1) + "", (question_id) + "",
                                            answer.answer_text, answer.is_right_answer + ""});
                        }

                    }
                }
            }
        }
        writeData("Course.csv", CourseCSVList);
        writeData("Chapter.csv", ChapterCSVList);
        writeData("Topic.csv", TopicCSVList);
        writeData("Question.csv", QuestionCSVList);
        writeData("Answer.csv", AnswerCSVList);
        try {
            zip(DestPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void qbImport(String SourcePath) {
        try {
            File copied = new File(
                    "tmp.zip");
            File original = new File(SourcePath);
            FileUtils.copyFile(original, copied);
            unzip("tmp.zip");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void add_row(List<String[]> sheet, String[] row_cells) {

        sheet.add(row_cells);
    }

    private void writeData(String filePath, List<String[]> data) {
        File file = createFileWithDir(filePath).toFile();

        try {
            FileWriter output_file = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output_file,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(data);
            writer.close();
            output_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path createFileWithDir(String filename) {
        File dir = new File("tmp");
        if (!dir.exists()) {boolean done = dir.mkdirs();}
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