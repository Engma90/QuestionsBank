package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.javafx.PlatformUtil;
import models.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


//Todo: check converter race condition on deleting and using large files with images
//Todo: cleanup after finish
public class FileExporterLibreOffice implements IFileExporter {
    private static final String TEMP_DIR = "exportTMP";
    private String selectedBinaryPath = "";


    @Override
    public boolean checkForDependencies() {
        //Todo: Enable configurable (custom) path from ini file and remove temp D:\\
        List<String> possibleBinaryPath = new ArrayList<>();
        if (PlatformUtil.isWindows()) {
            possibleBinaryPath.add("C:\\Program Files\\LibreOffice\\program\\soffice.exe");
            possibleBinaryPath.add("C:\\Program Files (x86)\\LibreOffice\\program\\soffice.exe");
            possibleBinaryPath.add("D:\\QB\\lo\\LibreOfficePortable\\App\\libreoffice\\program\\soffice.exe");

            for (String path : possibleBinaryPath) {
                if (new File(path).exists()) {
                    selectedBinaryPath = path;
                    return true;
                }
            }
            return false;

        } else if (PlatformUtil.isLinux()) {
            //Todo: check for libreOffice on linux (/bin/bash -c soffice) (/opt/libreoffice/program/soffice)
            possibleBinaryPath.add("/opt/libreoffice*/program/soffice");

            try {
                List<String> commands = new ArrayList<>();
                ProcessBuilder builder = new ProcessBuilder();
                commands.add("/bin/bash");
                commands.add("-c");
                commands.add(possibleBinaryPath.get(0) + " --version");
                builder.command(commands);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                Scanner s = new Scanner(process.getInputStream());
                while (s.hasNextLine()) {
                    String outputLine = s.nextLine();
                    System.out.println(outputLine);
                    if (outputLine.startsWith("LibreOffice")) {
                        selectedBinaryPath = possibleBinaryPath.get(0);
                        return true;
                    }
                }
                s.close();
                int result = process.waitFor();
                return false;
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean exportExam(Exam exam, String dest, String format) {
        try {
            FileUtils.deleteDirectory(new File(TEMP_DIR));

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (!new File(TEMP_DIR).exists()) {
            File tmp = new File(TEMP_DIR);
            boolean done = tmp.mkdir();

            if (done) {
                //if(PlatformUtil.isLinux()){
                try {
                    File copied = new File(
                            tmp.getAbsolutePath() + File.separator + "logo.png");
                    String logoLocation = new File(FileExporterLibreOffice.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath()).getParent() + File.separator + "logo.png";
                    File original = new File(logoLocation);
                    if (!original.exists()) {
                        logoLocation = new File(FileExporterLibreOffice.class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .getPath()) + File.separator + "logo.png";
                        original = new File(logoLocation);
                    }
                    FileUtils.copyFile(original, copied);
//                System.out.println(original.getAbsolutePath());
//                System.out.println(copied.getAbsolutePath());
//                new Alert(Alert.AlertType.INFORMATION,
//                        original.getAbsolutePath()
//                                + "\n"
//                                + copied.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
            //}
        }

        for (ExamModel examModel : exam.getExamModelList()) {
            htmlExamWriter(exam, examModel);
        }


        String cmd = "";
        if (PlatformUtil.isWindows()) {
            cmd = //"C:\\Windows\\System32\\cmd.exe /c "
                    "For %f in " +
                            "(" + FileSystems.getDefault().getPath("").toAbsolutePath() + "\\" + TEMP_DIR + "\\" + "*.html) " +
                            "do (" +
                            "\"" + selectedBinaryPath + "\"" + " --headless --norestore --writer --convert-to docx " +
                            "\"" + "%f" + "\"";
            if (format.equals("PDF")) {

                String tmp_cmd = cmd + " --outdir "
                        + "\"" + FileSystems.getDefault().getPath("").toAbsolutePath() + "\\" + TEMP_DIR + "\"" + ")";
                runCMD(tmp_cmd);
                cmd = cmd.replace("--convert-to docx", "--convert-to pdf")
                        .replace("*.html", "*.docx");
                cmd += " --outdir "
                        + "\"" + dest + "\"" + ")";
            } else {
                cmd += " --outdir "
                        + "\"" + dest + "\"" + ")";
            }

        } else if (PlatformUtil.isLinux()) {
            //cmd = "/bin/bash -c ";
            //cmd += "cd " + "\"" +FileSystems.getDefault().getPath("").toAbsolutePath() + "/" + TEMP_DIR + "/" + "\"";
            //cmd += "cd \"/home/mohammad/Desktop/exams\"";
            //cmd +=" ; ";
            cmd = selectedBinaryPath + " --headless --norestore --writer --convert-to docx *.html";

            if (format.equals("PDF")) {

                String tmp_cmd = cmd + " --outdir "
                        + "\"" + FileSystems.getDefault().getPath("").toAbsolutePath() + "/" + TEMP_DIR + "\"";
                runCMD(tmp_cmd);
                cmd = cmd.replace("--convert-to docx", "--convert-to pdf")
                        .replace("*.html", "*.docx");
                cmd += " --outdir "
                        + "\"" + dest + "\"";
            } else {
                cmd += " --outdir "
                        + "\"" + dest + "\"";
            }
        }


        boolean success = runCMD(cmd);
        try {
            FileUtils.deleteDirectory(new File(TEMP_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    private boolean runCMD(String cmd) {
        try {
            List<String> commands = new ArrayList<>();
            ProcessBuilder builder = new ProcessBuilder();
            if (PlatformUtil.isWindows()) {
                commands.add("C:\\Windows\\System32\\cmd.exe");
                commands.add("/c");
                commands.add(cmd);
                //builder.command(commands);
            } else if (PlatformUtil.isLinux()) {
                commands.add("/bin/bash");
                commands.add("-c");
                commands.add(cmd);
                //builder.command("/bin/bash", "-c", cmd);
            }
            builder.command(commands);


            builder.directory(new File(TEMP_DIR).getAbsoluteFile());
            builder.redirectErrorStream(true);
            //System.out.println(Arrays.toString(builder.command().toArray()));
            Process process = builder.start();

            Scanner s = new Scanner(process.getInputStream());
            StringBuilder text = new StringBuilder();
            while (s.hasNextLine()) {
                text.append(s.nextLine());
                text.append("\n");
            }
            s.close();
            int result = process.waitFor();
            System.out.printf("Process exited with result %d and output %s%n", result, text);
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }


    private void htmlExamWriter(Exam model, ExamModel examModel) {
//
//        try {
//            generateQRCodeImage(model.getId() + "-" + examModel.getId(), "./" + TEMP_DIR + "/QR_" + examModel.getExamModelNumber() + ".png");
//        } catch (WriterException e) {
//            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//        }
//
//        String university = (model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ?
//                DashboardController.doctor.getUniversity().toUpperCase():
//                DashboardController.doctor.getAltUniversity().toUpperCase();
//        String collage = model.getCollege();
//        String department = model.getDepartment();
//        String year = model.getYear();
//        String type = model.getExamType();
//        String name =  ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ?"Subject: ":"المادة: ") +
//                model.getExamName();
//        String date = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ?"Date: ":"التاريخ: ") +
//                model.getDate();
//        String duration = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ?"Duration: ":"الزمن: ") +
//                model.getDuration();
//        String totalMarks = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ?"Total Marks: ":"المجموع: ") +
//                model.getTotalMarks();
//
//
//        String htmlHeaderToRemove = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>";
//        String htmlFooterToRemove = "</p></body></html>";
//        String examHeader2 = "<html dir=\"" +
//                (model.getExamLanguage().equals(FileExporterFactory.ENGLISH) ? "ltr" : "rtl") + "\">" +
//                "<head>" +
//                "<style>td{ font-size: 0.9em; margin:0;} body{ margin:0 0 0 0;}" +
//                ".avoid-page-break{}" +
//                "</style>" +
//                "<meta charset=\"utf-8\"/>" +
//                "</head>\n" +
//                "<body>\n" +
//                "<table width=\"100%\">\n" +
//                "    <tr>\n" +
//                "        <td width=\"35%\">\n" +
//                "            " + university + "<br/>\n" +
//                "            " + collage + "<br/>\n" +
//                "            " + department + "<br/>\n" +
//                "            " + year + "\n" +
//                "        </td>\n" +
//                "\n" +
//                "        <td align=\"center\" width=\"30%\">\n" +
//                "            <img align=\"center\" src=\"" + getImageBase64("./" + TEMP_DIR + "/logo.png") + "\" height=\"64\" width=\"64\"/>\n" +
//                "            <br />\n" +
//                "            <img align=\"center\" src=\"" + getImageBase64("./" + TEMP_DIR + "/QR_" + examModel.getExamModelNumber() + ".png") + "\" height=\"32\" width=\"32\"/>\n" +
//                "            <br />\n" +
//                "            " + type + "\n" +
//                "        </td>\n" +
//                "\n" +
//                "        <td width=\"35%\">\n" +
//                "            " + name + "<br/>\n" +
//                "            " + date + "<br/>\n" +
//                "            " + duration + "<br/>\n" +
//                "            " + totalMarks + "<br/>\n" +
//                "        </td>\n" +
//                "    </tr>\n" +
//                "</table>" +
//                "<hr/>" + model.getNote() + (model.getNote().isEmpty() ? "" : "<hr/>");
//        StringBuilder body = new StringBuilder();
//
//        String footer = "</body></html>";
//        int q_counter = 1;
//        for (ExamQuestion qm : examModel.getExamQuestionsList()) {
//
//            if(qm.getQuestionType().equals(Vars.QuestionType.EXTENDED_MATCH)){
//                int i = 0;
//                body.append("<div class=\"avoid-page-break\">");
//                for (Answer answer : qm.getContents().get(0).getAnswers()) {
//                    body.append("<div class=\"avoid-page-break\">");
//                    body.append((char) (65 + i));
//                    body.append(") ");
//                    body.append(answer.answer_text.replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
//                    //body.append("<br />");
//                    body.append("</div>");
//                    i++;
//                }
//                body.append("</div>");
//            }
//
//            for (QuestionContent questionContent : qm.getContents()) {
//                body.append("<div class=\"avoid-page-break\">");
//                body.append("<b><font size=\"4\">");
//                body.append(model.getExamLanguage().equals(Vars.Languages.ENGLISH)?"Q ":"س ");
//                body.append((q_counter++));
//                body.append(": </font></b>");
//                body.append("<div class=\"avoid-page-break\">");
//
//                body.append(questionContent.getContent().replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
//                body.append("</div>");
//
//                if(!qm.getQuestionType().equals(Vars.QuestionType.EXTENDED_MATCH)) {
//                    int i = 0;
//                    body.append("<div class=\"avoid-page-break\">");
//                    for (Answer answer : questionContent.getAnswers()) {
//                        body.append("<div class=\"avoid-page-break\">");
//                        body.append((char) (65 + i));
//                        body.append(") ");
//                        body.append(answer.answer_text.replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
//                        //body.append("<br />");
//                        body.append("</div>");
//                        i++;
//                    }
//                    body.append("</div>");
//                }
//                body.append("</div>");
//            }
//            body.append("<hr/>");
//        }
//
//        String html = examHeader2 + body.toString() + footer;
//        try {
//            Save_to_file(html, "./" + TEMP_DIR + "/" + model.getExamName() + "_"
//                    + examModel.getExamModelNumber() + ".html");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private String getImageBase64(String imgPath) {
        try {
            File file = new File(imgPath);
            if (!file.exists()) {
                Path path = FileSystems.getDefault().getPath(imgPath.replace("./", "")).toAbsolutePath();
                //new Alert(Alert.AlertType.INFORMATION, path.toString()).show();
                file = new File(path.toString());
            }

            //check if file is too big
            if (file.length() > 1024 * 1024) {
                throw new VerifyError("File is too big.");
            }
            //get mime type of the file
            String type = java.nio.file.Files.probeContentType(file.toPath());
            //new Alert(Alert.AlertType.INFORMATION, type).show();
            //get html exam_content
            byte[] data = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            String base64data = java.util.Base64.getEncoder().encodeToString(data);
            return String.format(
                    "data:%s;base64,%s",
                    type, base64data);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    private void Save_to_file(String s, String path) throws IOException {
        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(new File(path)), StandardCharsets.UTF_8)) {
            writer.write(s);
        }
    }

    private static void generateQRCodeImage(String text, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 32, 32);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
