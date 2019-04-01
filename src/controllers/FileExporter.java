package controllers;

import models.Exam;

public class FileExporter {
    public static final String LIBRE_OFFICE = "libre";
    public static final String MS_OFFICE = "ms";
    public static final String ENGLISH = "English";
    public static final String ARABIC = "Arabic";

    private IFileExporter iFileExporter = null;
    public IFileExporter getExporter(String Type){

        if(Type.equals(LIBRE_OFFICE)){
            iFileExporter = new FileExporterLibreOffice();
        }
        else if(Type.equals(MS_OFFICE)){
            iFileExporter = new FileExporterMSOffice();
        }

        if (iFileExporter.checkForDependencies())
            return iFileExporter;
        else {
            return null;
        }
    }
}
