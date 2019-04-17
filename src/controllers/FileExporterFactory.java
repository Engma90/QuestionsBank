package controllers;

import models.Exam;

public class FileExporterFactory {
    public static final String LIBRE_OFFICE = "libre";
    public static final String MS_OFFICE = "ms";
    public static final String DOCX4J = "docx4j";
    public static final String DEFAULT = "docx4j";

    public static final String ENGLISH = "English";
    public static final String ARABIC = "Arabic";
    private String selected = "";
    private String error = "";

    private IFileExporter iFileExporter = null;
    public IFileExporter createExporter(String Type){

        if(Type.equals(LIBRE_OFFICE)){
            selected = "LibreOffice";
            iFileExporter = new FileExporterLibreOffice();
        }
        else if(Type.equals(MS_OFFICE)){
            selected = "MSOffice";
            iFileExporter = new FileExporterMSOffice();
        }
        else if(Type.equals(DOCX4J)){
            selected = "docx4j";
            iFileExporter = new FileExporterDocx4j();
        }

        if (iFileExporter.checkForDependencies())
            return iFileExporter;
        else {
            error = "Please install " + selected;
            return null;
        }
    }
    public String getError(){
        return error;
    }
}
