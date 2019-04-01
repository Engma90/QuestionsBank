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
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Todo: check converter race condition on deleting and using large files with images
//Todo: cleanup after finish
class FileExporterLibreOffice implements IFileExporter {
    private static final String TEMP_DIR = "exportTMP";
    private List<String> possibleBinaryPath;
    private String selectedBinaryPath = "";

    public FileExporterLibreOffice() {
    }

    @Override
    public boolean checkForDependencies() {
        //Todo: Enable configurable (custom) path from ini file and remove temp D:\\
        possibleBinaryPath = new ArrayList<>();
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
            //Todo: check for libreOffice on linux
            possibleBinaryPath.add("soffice");

            try {
                Process p = Runtime.getRuntime().exec
                        ("soffice -h");
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getInputStream()));
                String outputLine;
                while ((outputLine = input.readLine()) != null) {
                    if(outputLine.toLowerCase().contains("LibreOffice".toLowerCase())){
                        selectedBinaryPath = "soffice";
                        return true;
                    }
                    System.out.println(outputLine);
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
            boolean done = new File(TEMP_DIR).mkdirs();
        }

        for (ExamModel examModel : exam.getExamModelList()) {
            htmlExamWriter(exam, examModel);

//            try {
//            ProcessBuilder pb = new ProcessBuilder(
//                    "C:/Program Files/WinRAR/winrar",
//                    "x",
//                    "myjar.jar",
//                    "*.*",
//                    "new");
//
//            pb.directory(new File("H:/"));
//            pb. redirectErrorStream(true);
//                Process p = pb.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//            File htmlFile = new File("./"+TEMP_DIR+"/" + exam.getExamName() + examModel.getExamModelNumber() + ".html");
//            File target = new File(dest + "\\" + exam.getExamName() + examModel.getExamModelNumber() + (format.equals("PDF")?".pdf":".docx"));

//            int retries = 0;
//            while (retries++ < 10) {
//                try {
//                    if (conversion.get()){
//                        htmlFile.delete();
//                        new File("QR_"+examModel.getExamModelNumber()+".png").delete();
//                        break;
//                    }
//                    Thread.sleep(100);
//                } catch (InterruptedException | ExecutionException e) {
//                    htmlFile.delete();
//                    new File("QR_"+examModel.getExamModelNumber()+".png").delete();
//                    e.printStackTrace();
//                }
//
//            }

        }


        String cmd = "";
        if (PlatformUtil.isWindows()) {
            cmd = "cmd.exe /c "
                    + "For %f in " +
                    "(" + FileSystems.getDefault().getPath("").toAbsolutePath() + "\\" + TEMP_DIR + "\\" + "*.html) " +
                    "do (" +
                    "\"" + selectedBinaryPath + "\"" + " --headless --norestore --writer --convert-to docx " +
                    "\"" + "%f" + "\"";
            if (format.equals("PDF")) {

                String tmp_cmd = cmd + " --outdir "
                        + "\"" + FileSystems.getDefault().getPath("").toAbsolutePath() + "\\" + TEMP_DIR + "\"" + ")";
                runCMD(tmp_cmd);
                cmd = cmd.replace("--convert-to docx","--convert-to pdf")
                        .replace("*.html","*.docx");
                cmd += " --outdir "
                        + "\"" + dest + "\"" + ")";
            } else {
                cmd += " --outdir "
                        + "\"" + dest + "\"" + ")";
            }

        } else if (PlatformUtil.isLinux()) {
            //cmd = //"/bin/bash -c " +
                    cmd = selectedBinaryPath + " --headless --norestore --writer --convert-to docx "+
                            FileSystems.getDefault().getPath("").toAbsolutePath() + "\\" + TEMP_DIR + "\\" + "*.html";

            if (format.equals("PDF")) {

                String tmp_cmd = cmd + " --outdir "
                        + "\"" + FileSystems.getDefault().getPath("").toAbsolutePath() + "\\" + TEMP_DIR + "\"" ;
                runCMD(tmp_cmd);
                cmd = cmd.replace("--convert-to docx","--convert-to pdf")
                        .replace("*.html","*.docx");
                cmd += " --outdir "
                        + "\"" + dest + "\"" ;
            } else {
                cmd += " --outdir "
                        + "\"" + dest + "\"";
            }
        }


        return runCMD(cmd);
    }

    private boolean runCMD(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec
                    (cmd);
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String outputLine;
            while ((outputLine = input.readLine()) != null) {
                System.out.println(outputLine);
            }
            input.close();
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
                "            " + "Faculty of "+ model.getCollege() + "<br/>\n" +
                "            " + model.getDepartment() + "<br/>\n" +
                "            " + model.getYear() + "\n" +
                "        </td>\n" +
                "\n" +
                "        <td align=\"center\" width=\"30%\">\n" +
                "            <img align=\"center\" src=\""+ getImageBase64("logo.png")+"\" height=\"64\" width=\"64\"/>\n" +
                "            <br />\n" +
                "            <img align=\"center\" src=\""+ getImageBase64("./"+TEMP_DIR+"/QR_" + examModel.getExamModelNumber() + ".png")+"\" height=\"32\" width=\"32\"/>\n" +
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
                    +examModel.getExamModelNumber() + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getImageBase64(String imgPath) {
        try {
            File file = new File(imgPath);
            //check if file is too big
            if (file.length() > 1024 * 1024) {
                throw new VerifyError("File is too big.");
            }
            //get mime type of the file
            String type = java.nio.file.Files.probeContentType(file.toPath());
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
