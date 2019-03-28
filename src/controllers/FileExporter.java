package controllers;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
//Todo: check converter race condition on deleting an using files
public class FileExporter {
    public void Export(Exam exam, String dest, String format) {
        for (ExamModel examModel : exam.getExamModelList()) {

            htmlExamWriter(exam, examModel);
            File htmlFile = new File("./" + exam.getExamName() + examModel.getExamModelNumber() + ".html");
            File target = new File(dest + "\\" + exam.getExamName() + examModel.getExamModelNumber() + (format.equals("PDF")?".pdf":".docx"));
            IConverter converter = LocalConverter.make();
            Future<Boolean> conversion = converter
                    .convert(htmlFile).as(DocumentType.MHTML)
                    .to(target).as((format.equals("PDF")?DocumentType.PDF:DocumentType.DOCX))
                    .schedule();
//            Future<Boolean> conversion = converter
//                    .convert(new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile)))).as(DocumentType.MHTML)
//                    .to(target).as((format.equals("PDF")?DocumentType.PDF:DocumentType.DOCX))
//                    .schedule();
//            converter.shutDown();
            converter.shutDown();
            htmlFile.delete();
            new File("QR_"+examModel.getExamModelNumber()+".png").delete();
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
    }


    private void htmlExamWriter(Exam model, ExamModel examModel){

        try {
            generateQRCodeImage(model.getId() +"-"+ examModel.getId(), "QR_"+examModel.getExamModelNumber()+".png");
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }


        String htmlHeaderToRemove = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>";
        String htmlFooterToRemove = "</p></body></html>";
        String examHeader2 = "<html dir=\"ltr\">" +
                "<head>" +
                "<style>td{ font-size: 0.9em; margin:0;} body{ margin:0 0 0 0;}" +
                ".avoid-page-break{page-break-inside: avoid !important; margin: 4px 0 4px 0 !important; display:inline-block !important; position:relative;}" +
                "</style>" +
                "<meta charset=\"utf-8\"/>"+
                "</head>\n" +
                "<body>\n" +
                "<Table width=\"100%\">\n" +
                "<tr>\n" +
                "<td align=\"left\" width=\"40%\">\n" +
                "<Table align=\"left\" >\n" +
                "<tr>\n" +
                "<td><b>Benha University</b></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>"+model.getCollege()+"</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td><b>"+model.getDepartment()+"</b></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td><b>"+model.getYear()+"</b></td>\n" +
                "</tr>\n" +
                "</Table>\n" +
                "</td>\n" +
                "\n" +
                "<td align=\"center\" width=\"20%\">\n" +
                "<Table align=\"center\">\n" +
                "<tr>\n" +
                "<td align=\"center\"><img src=\"./src/views/images/logo.png\" height=\"64\" width=\"64\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td align=\"center\"><img src=\"QR_"+examModel.getExamModelNumber()+".png\" height=\"32\" width=\"32\"></td>\n" +
                "</tr>\n" +
                "</Table>\n" +
                "</td>\n" +
                "\n" +
                "<td align=\"right\" width=\"40%\">\n" +
                "<Table align=\"right\">\n" +
                "<tr>\n" +
                "<td><b>"+model.getExamType()+"</b></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>"+model.getExamName()+"</font></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td><b>Date:</b> "+model.getDate()+"</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td><b>Duration: "+model.getDuration()+"</td>\n" +
                "</tr>\n" +
                "</Table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</Table>\n" +
                "<hr>"+model.getNote()+(model.getNote().isEmpty()?"":"<hr>");
        StringBuilder body = new StringBuilder();

        String footer = "</body></html>";
        int q_counter = 1;
        for(ExamQuestion qm: examModel.getExamQuestionsList()) {

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
            body.append("<hr>");
            body.append("</div>");

        }

        String html = examHeader2+body.toString()+footer;
        try {
            Save_to_file(html, model.getExamName()+ examModel.getExamModelNumber()+".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void Save_to_file(String s, String path) throws IOException {

//        FileOutputStream outputStream = new FileOutputStream(path);
//        byte[] strToBytes = s.getBytes();
//        outputStream.write(strToBytes);
//        outputStream.close();
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
