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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        } else if (PlatformUtil.isLinux()) {
            possibleBinaryPath.add("soffice");
            return true;
        }
        new Alert(Alert.AlertType.ERROR, "Please install LibreOffice").show();
        return false;
    }

    public boolean exportExam(Exam exam, String dest, String format) {
        for (ExamModel examModel : exam.getExamModelList()) {
            if (!new File(TEMP_DIR).exists()) {
                boolean done = new File(TEMP_DIR).mkdirs();
            }
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
        } else if (PlatformUtil.isLinux()) {
            //cmd = //"/bin/bash -c " +
                    //selectedBinaryPath + " --headless --norestore --writer --convert-to docx *.html";
        }
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
                ".avoid-page-break{page-break-inside: avoid !important; margin: 4px 0 4px 0 !important; display:inline-block !important; position:relative;}" +
                "</style>" +
                "<meta charset=\"utf-8\"/>" +
                "</head>\n" +
                "<body>\n" +
                "<table width=\"100%\">\n" +
                "    <tr>\n" +
                "        <td width=\"25%\">\n" +
                "            " + DashboardController.doctor.getUniversity() + "<br/>\n" +
                "            " + model.getCollege() + "<br/>\n" +
                "            " + model.getDepartment() + "<br/>\n" +
                "            " + model.getYear() + "\n" +
                "        </td>\n" +
                "\n" +
                "        <td align=\"center\" width=\"50%\">\n" +
                "            <img align=\"center\" src=\"../logo.png\" height=\"64\" width=\"64\"/>\n" +
                "            <br/>\n" +
                "            <img align=\"center\" src=\"QR_" + examModel.getExamModelNumber() + ".png\" height=\"32\" width=\"32\"/>\n" +
                "        </td>\n" +
                "\n" +
                "        <td width=\"25%\">\n" +
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
            body.append(q_counter++);
            body.append(":</font></b>");
            body.append(qm.getQuestionContent().replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
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
            Save_to_file(html, "./" + TEMP_DIR + "/" + model.getExamName() + examModel.getExamModelNumber() + ".html");
        } catch (IOException e) {
            e.printStackTrace();
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
