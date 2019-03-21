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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.Future;

public class FileExporter {
    private static final String QR_CODE_IMAGE_PATH = "./QRCode.png";
    public void Export(ExamModel examModel, String dest){

        for (ExamModelModel examModelModel:examModel.getExamModelModelList()) {

            htmlExamWriter(examModel, examModelModel);
            File htmlFile = new File("./" + examModel.getExamName() + examModelModel.getExamModelNumber() + ".html"), target = new File(dest + "\\" + examModel.getExamName() + examModelModel.getExamModelNumber() + ".pdf");
            IConverter converter = LocalConverter.make();
            Future<Boolean> conversion = converter
                    .convert(htmlFile).as(DocumentType.MHTML)
                    .to(target).as(DocumentType.PDF)
                    .schedule();
            converter.shutDown();
            htmlFile.delete();

        }
    }


    private void htmlExamWriter(ExamModel model, ExamModelModel examModelModel){

        try {
            generateQRCodeImage(model.getId() +"-"+ examModelModel.getId(), 32, 32, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }


        String htmlHeaderToRemove = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\">";
        String examHeader = "<html dir=\"ltr\"><head>" +
                "<style>.padding-table-columns {padding:0 10px 0 10px; } body{ font-size: 12px; margin:0 0 0 0;}</style>" +
                "</head><body contenteditable=\"true\">" +
                "<Table ><tr><td><b>Benha University</b></td><td rowspan=\"2\" class=\"padding-table-columns\">" +
                "<img src=\"logo.png\" height=\"64\" width=\"64\"></td><td><b>"+model.getExamType()+"</b></td></tr>" +
                "<tr><td><b>"+model.getCollege()+"</b></td><td><b>Subject:</b><font size=\"2\"> "+model.getExamName()+"</font></td></tr>" +
                "<tr><td><b>"+model.getDepartment()+"</b></td><td rowspan=\"2\" class=\"padding-table-columns\"><img src=\"QRCode.png\" height=\"32\" width=\"32\">" +
                "<td><b>Date:</b> "+model.getDate()+"</td></tr>" +
                "<tr><td><b>1st Year </b><font size=\"1\">(Electronics and Communications Engineering)</font></td><td><b>Duration: "+model.getDuration()+"</b></td></tr>" +
                "</Table><hr>"+model.getNote()+"<hr>";
        StringBuilder body = new StringBuilder();
        String footer = "</body></html>";
        for(ExamQuestionModel qm:examModelModel.getExamQuestionsList()){
            body.append("<div>");
            body.append(qm.getQuestionContent().replace(htmlHeaderToRemove,"").replace(footer,""));
            int i =0;
            for (Answer answer: qm.getAnswers()){
                body.append((char) (65 + i));
                body.append(" ");
                body.append(answer.answer_text);
                body.append("<br />");
                i++;
            }
            body.append("</div>");
        }

        String html = examHeader+body.toString()+footer;
        try {
            Save_to_file(html, model.getExamName()+examModelModel.getExamModelNumber()+".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void Save_to_file(String s, String path) throws IOException {

        FileOutputStream outputStream = new FileOutputStream(path);
        byte[] strToBytes = s.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }


    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
