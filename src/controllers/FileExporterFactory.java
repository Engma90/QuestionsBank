package controllers;

import models.Exam;

public class FileExporterFactory {
    public static final String LIBRE_OFFICE = "libre";
    public static final String MS_OFFICE = "ms";
    public static final String ENGLISH = "English";
    public static final String ARABIC = "Arabic";
    private String selected = "";
    private String error = "";

    private IFileExporter iFileExporter = null;
    public IFileExporter getExporter(String Type){

        if(Type.equals(LIBRE_OFFICE)){
            selected = "LibreOffice";
            iFileExporter = new FileExporterLibreOffice();
        }
        else if(Type.equals(MS_OFFICE)){
            selected = "MSOffice";
            iFileExporter = new FileExporterMSOffice();
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
