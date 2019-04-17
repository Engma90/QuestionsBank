package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import models.*;
import org.apache.commons.io.FileUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Body;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.SectPr;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


//Todo: check converter race condition on deleting and using large files with images
//Todo: cleanup after finish
public class FileExporterDocx4j implements IFileExporter {
    private static final String TEMP_DIR_NAME = "export_temp";
    private static final String APP_LOCATION = new File(FileExporterDocx4j.class
            .getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath()).getParent();
    private static final String TEMP_DIR_LOCATION = APP_LOCATION + File.separator + TEMP_DIR_NAME;

    private String selectedBinaryPath = "";


    @Override
    public boolean checkForDependencies() {
        return true;
    }


    static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        System.out.println(new String(encoded, StandardCharsets.UTF_8));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static void ConvertDoc(String src, String outputPath) throws Docx4JException, IOException {
        //.. HTML Code
        String html;
        html = readFile(src);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);

        try {
            Body body = wordMLPackage.getMainDocumentPart().getContents().getBody();
            PageDimensions page = new PageDimensions();
            SectPr.PgMar pgMar = page.getPgMar();
            pgMar.setBottom(BigInteger.valueOf(pixelsToDxa(40)));
            pgMar.setTop(BigInteger.valueOf(pixelsToDxa(40)));
            pgMar.setLeft(BigInteger.valueOf(pixelsToDxa(40)));
            pgMar.setRight(BigInteger.valueOf(pixelsToDxa(40)));
            SectPr sectPr = Context.getWmlObjectFactory().createSectPr();
            body.setSectPr(sectPr);
            sectPr.setPgMar(pgMar);
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
        AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
        afiPart.setBinaryData(html.getBytes(StandardCharsets.UTF_8));
        afiPart.setContentType(new ContentType("text/html"));
        Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);
        CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
        ac.setId(altChunkRel.getId());
        wordMLPackage.getMainDocumentPart().addObject(ac);
        wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");
        WordprocessingMLPackage pkgout = wordMLPackage.getMainDocumentPart().convertAltChunks();
        pkgout.save(new java.io.File(outputPath));
    }


    protected static int getDPI() {
        return GraphicsEnvironment.isHeadless() ? 96 :
                Toolkit.getDefaultToolkit().getScreenResolution();
    }

    private static int pixelsToDxa(int pixels) {
        return (1440 * pixels / getDPI());
    }

    public boolean exportExam(Exam exam, String dest, String format) {
        try {
            FileUtils.deleteDirectory(new File(TEMP_DIR_LOCATION));

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (!new File(TEMP_DIR_LOCATION).exists()) {
            File tmp = new File(TEMP_DIR_LOCATION);
            boolean done = tmp.mkdir();

            if (done) {
                try {
                    File copied = new File(
                            tmp.getAbsolutePath() + File.separator + "logo.png");
                    String logoLocation = new File(FileExporterDocx4j.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath()).getParent() + File.separator + "logo.png";
                    File original = new File(logoLocation);
                    if (!original.exists()) {
                        logoLocation = new File(FileExporterDocx4j.class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .getPath()) + File.separator + "logo.png";
                        original = new File(logoLocation);
                    }
                    FileUtils.copyFile(original, copied);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }

        for (ExamModel examModel : exam.getExamModelList()) {
            htmlExamWriter(exam, examModel);

            try {
                ConvertDoc(TEMP_DIR_LOCATION + "/" + exam.getExamName() + "_" + examModel.getExamModelNumber() + ".html",
                        dest + "/" + exam.getExamName() + "_" + examModel.getExamModelNumber() + ".docx");
            } catch (Docx4JException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileUtils.deleteDirectory(new File(TEMP_DIR_LOCATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void htmlExamWriter(Exam model, ExamModel examModel) {

        try {
            generateQRCodeImage(model.getId() + "-" + examModel.getId(), TEMP_DIR_LOCATION + "/QR_" + examModel.getExamModelNumber() + ".png");
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        String university = (model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ?
                DashboardController.doctor.getUniversity().toUpperCase() :
                DashboardController.doctor.getAltUniversity().toUpperCase();
        String collage = model.getCollege();
        String department = model.getDepartment();
        String year = model.getYear();
        String type = model.getExamType();
        String name = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ? "Subject: " : "المادة: ") +
                model.getExamName();
        String date = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ? "Date: " : "التاريخ: ") +
                model.getDate();
        String duration = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ? "Duration: " : "الزمن: ") +
                model.getDuration();
        String totalMarks = ((model.getExamLanguage().equals(FileExporterFactory.ENGLISH)) ? "Total Marks: " : "المجموع: ") +
                model.getTotalMarks();


        String htmlHeaderToRemove = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>";
        String htmlFooterToRemove = "</p></body></html>";
        String examHeader2 =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html dir=\"" +
                (model.getExamLanguage().equals(FileExporterFactory.ENGLISH) ? "ltr" : "rtl") + "\">" +
                "<head>" +
                "<style>td{ font-size: 0.9em; margin:0;} body{ margin:0 0 0 0;}" +
                ".avoid-page-break{}" +
                "</style>" +
                //"<meta charset=\"utf-8\"/>"
                "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>" +
                "</head>\n" +
                "<body>\n" +
                "<table width=\"100%\">\n" +
                "    <tr>\n" +
                "        <td width=\"35%\">\n" +
                "            " + university + "<br/>\n" +
                "            " + collage + "<br/>\n" +
                "            " + department + "<br/>\n" +
                "            " + year + " (" + type +")\n" +
                "        </td>\n" +
                "\n" +
                "        <td align=\"center\" width=\"30%\">\n" +
                "            <img alt=\"\" height=\"64px\" width=\"64px\" src=\"" + getImageBase64(TEMP_DIR_LOCATION + "/logo.png") + "\" />\n" +
                "            <br />\n" +
                "            <img alt=\"\" height=\"64px\" width=\"64px\" src=\"" + getImageBase64(TEMP_DIR_LOCATION + "/QR_" + examModel.getExamModelNumber() + ".png") + "\"/>\n" +
                "        </td>\n" +
                "\n" +
                "        <td width=\"35%\">\n" +
                "            " + name + "<br/>\n" +
                "            " + date + "<br/>\n" +
                "            " + duration + "<br/>\n" +
                "            " + totalMarks + "<br/>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>" +
                "<hr/>" + model.getNote() + (model.getNote().isEmpty() ? "" : "<hr/>");
        StringBuilder body = new StringBuilder();

        String footer = "</body></html>";
        int q_counter = 1;
        for (ExamQuestion qm : examModel.getExamQuestionsList()) {

            if (qm.getQuestionType().equals(Vars.QuestionType.EXTENDED_MATCH)) {
                int i = 0;
                body.append("<div class=\"avoid-page-break\">");
                for (Answer answer : qm.getContents().get(0).getAnswers()) {
                    body.append("<div class=\"avoid-page-break\">");
                    body.append((char) (65 + i));
                    body.append(") ");
                    body.append(answer.answer_text.replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
                    //body.append("<br />");
                    body.append("</div>");
                    i++;
                }
                body.append("</div>");
            }

            for (QuestionContent questionContent : qm.getContents()) {
                body.append("<div class=\"avoid-page-break\">");
                body.append("<b><font size=\"4\">");
                body.append(model.getExamLanguage().equals(Vars.Languages.ENGLISH) ? "Q " : "س ");
                body.append((q_counter++));
                body.append(": </font></b>");
                body.append("<div class=\"avoid-page-break\">");

                body.append(questionContent.getContent().replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
                body.append("</div>");

                if (!qm.getQuestionType().equals(Vars.QuestionType.EXTENDED_MATCH)) {
                    int i = 0;
                    body.append("<div class=\"avoid-page-break\">");
                    for (Answer answer : questionContent.getAnswers()) {
                        body.append("<div class=\"avoid-page-break\">");
                        body.append((char) (65 + i));
                        body.append(") ");
                        body.append(answer.answer_text.replace(htmlHeaderToRemove, "").replace(htmlFooterToRemove, ""));
                        //body.append("<br />");
                        body.append("</div>");
                        i++;
                    }
                    body.append("</div>");
                }
                body.append("</div>");
            }
            body.append("<hr/>");
        }

        String html = examHeader2 + body.toString() + footer;
        try {
            Save_to_file(html, TEMP_DIR_LOCATION + "/" + model.getExamName() + "_"
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
            byte[] data = FileUtils.readFileToByteArray(file);
            String base64data = Base64.getEncoder().encodeToString(data);
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
