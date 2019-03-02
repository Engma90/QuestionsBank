package controllers;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import models.ExamModel;
import models.QuestionModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Future;

public class FileExporter {
    public void Export(ExamModel model, String dest){

        htmlExamWriter(model);
        File htmlFile = new File("./"+model.ExamName+model.ExamModel+".html"), target = new File(dest+"\\"+model.ExamName+model.ExamModel+".pdf");
        IConverter converter = LocalConverter.make();
        Future<Boolean> conversion = converter
                .convert(htmlFile).as(DocumentType.MHTML)
                .to(target).as(DocumentType.PDF)
                .schedule();
        converter.shutDown();
    }


    private void htmlExamWriter(ExamModel model){
        String htmlHeaderToRemove = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\">";
        String examHeader = "<html dir=\"ltr\"><head><style>.padding-table-columns {padding:0 10px 0 10px; } body{ font-size: 12px; margin:0 0 0 0;}</style></head><body contenteditable=\"true\"><Table ><tr><td><b>Benha University</b></td><td rowspan=\"4\" class=\"padding-table-columns\"><img src=\"logo.png\" height=\"64\" width=\"64\"></td><td><b>"+model.ExamType+"</b></td></tr><tr><td><b>"+model.College+"</b></td><td><b>Subject:</b><font size=\"2\"> "+model.ExamName+"</font></td></tr><tr><td><b>"+model.Department+"</b></td><td><b>Date:</b> "+model.Date+"</td></tr><tr><td><b>1st Year </b><font size=\"1\">(Electronics and Communications Engineering)</font></td><td><b>Duration: "+model.Duration+"</b></td></tr></Table><hr>"+model.Note+"<hr>";
        StringBuilder body = new StringBuilder();
        String footer = "</body></html>";
        for(QuestionModel qm:model.questionModelList){
            body.append("<div>");
            body.append(qm.getQuestion_text().replace(htmlHeaderToRemove,"").replace(footer,""));
            int i =0;
            for (String answer: qm.getAnswers()){
                body.append((char) (65 + i));
                body.append(" ");
                body.append(answer);
                body.append("<br />");
                i++;
            }
            body.append("</div>");
        }

        String html = examHeader+body.toString()+footer;
        try {
            Save_to_file(html, model.ExamName+model.ExamModel+".html");
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
}
