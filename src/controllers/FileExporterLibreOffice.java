package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.javafx.PlatformUtil;
import javafx.scene.control.Alert;
import models.Answer;
import models.Exam;
import models.ExamModel;
import models.ExamQuestion;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
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
            new Alert(Alert.AlertType.ERROR, "Please install LibreOffice").show();
            return false;

        } else if (PlatformUtil.isLinux()) {
            //Todo: check for libreOffice on linux (/bin/bash -c soffice) (/opt/libreoffice/program/soffice)
            possibleBinaryPath.add("soffice");

            try {
                Process p = Runtime.getRuntime().exec
                        ("soffice -h");
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getInputStream()));
                String outputLine;
                while ((outputLine = input.readLine()) != null) {
                    if (outputLine.toLowerCase().contains("LibreOffice".toLowerCase())) {
                        selectedBinaryPath = "soffice";
                        return true;
                    }
                    //System.out.println(outputLine);
                }
                input.close();
                new Alert(Alert.AlertType.ERROR, "Please install LibreOffice").show();
                return false;
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        new Alert(Alert.AlertType.ERROR, "Not supported OS").show();
        return false;
    }

    public boolean exportExam(Exam exam, String dest, String format) {
        try {
            FileUtils.deleteDirectory(new File(TEMP_DIR));

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!new File(TEMP_DIR).exists()) {
            File tmp = new File(TEMP_DIR);
            boolean done = tmp.mkdir();

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
//            Process p = Runtime.getRuntime().exec
//                    (cmd);
//            BufferedReader input =
//                    new BufferedReader
//                            (new InputStreamReader(p.getInputStream()));
//            String outputLine;
//            while ((outputLine = input.readLine()) != null) {
//                System.out.println(outputLine);
//            }
//            input.close();
            //System.out.println(cmd);
            List<String> commands = new ArrayList<>();
            ProcessBuilder builder = new ProcessBuilder();
            if (PlatformUtil.isWindows()) {
                commands.add("C:\\Windows\\System32\\cmd.exe");
                commands.add("/c");
                commands.add(cmd);
                builder.command(commands);
                //builder = new ProcessBuilder(commands);
            } else if (PlatformUtil.isLinux()) {
                //commands.add("/bin/bash -c " + cmd);
                //commands.add(selectedBinaryPath);
                //commands.add(cmd);
                //builder = new ProcessBuilder(commands);
                builder.command("/bin/bash", "-c", cmd);
            }


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

        try {
            generateQRCodeImage(model.getId() + "-" + examModel.getId(), "./" + TEMP_DIR + "/QR_" + examModel.getExamModelNumber() + ".png");
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }


        String htmlHeaderToRemove = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>";
        String htmlFooterToRemove = "</p></body></html>";
        String examHeader2 = "<html dir=\"" +
                (model.getExamLanguage().equals(FileExporter.ENGLISH) ? "ltr" : "rtl") + "\">" +
                "<head>" +
                "<style>td{ font-size: 0.9em; margin:0;} body{ margin:0 0 0 0;}" +
                ".avoid-page-break{}" +
                "</style>" +
                "<meta charset=\"utf-8\"/>" +
                "</head>\n" +
                "<body>\n" +
                "<table width=\"100%\">\n" +
                "    <tr>\n" +
                "        <td width=\"35%\">\n" +
                "            " + DashboardController.doctor.getUniversity().toUpperCase() + " UNIVERSITY<br/>\n" +
                "            " + "Faculty of " + model.getCollege() + "<br/>\n" +
                "            " + model.getDepartment() + "<br/>\n" +
                "            " + model.getYear() + "\n" +
                "        </td>\n" +
                "\n" +
                "        <td align=\"center\" width=\"30%\">\n" +
                "            <img align=\"center\" src=\"" + getImageBase64("./" + TEMP_DIR + "/logo.png") + "\" height=\"64\" width=\"64\"/>\n" +
                "            <br />\n" +
                "            <img align=\"center\" src=\"" + getImageBase64("./" + TEMP_DIR + "/QR_" + examModel.getExamModelNumber() + ".png") + "\" height=\"32\" width=\"32\"/>\n" +
                "        </td>\n" +
                "\n" +
                "        <td width=\"35%\">\n" +
                "            " + model.getExamType() + "<br/>\n" +
                "            " + model.getExamName() + "<br/>\n" +
                "            " + model.getDate() + "<br/>\n" +
                "            " + model.getDuration() + "\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>" +
                "<hr/>" + model.getNote() + (model.getNote().isEmpty() ? "" : "<hr/>");
        StringBuilder body = new StringBuilder();

        String footer = "</body></html>";
        int q_counter = 1;
        for (ExamQuestion qm : examModel.getExamQuestionsList()) {

            body.append("<div class=\"avoid-page-break\">");
            body.append("<b><font size=\"4\">Question ");
            body.append((q_counter++));
            body.append(": </font></b>");
            body.append("<div class=\"avoid-page-break\">");
            body.append(qm.getQuestionContent().replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
            body.append("</div>");
            int i = 0;
            body.append("<div class=\"avoid-page-break\">");
            for (Answer answer : qm.getAnswers()) {
                body.append("<div class=\"avoid-page-break\">");
                body.append((char) (65 + i));
                body.append(") ");
                body.append(answer.answer_text.replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
                //body.append("<br />");
                body.append("</div>");
                i++;
            }
            body.append("</div>");
            body.append("<hr/>");
            body.append("</div>");

        }

        String html = examHeader2 + body.toString() + footer;
        try {
            Save_to_file(html, "./" + TEMP_DIR + "/" + model.getExamName() + "_"
                    + examModel.getExamModelNumber() + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
