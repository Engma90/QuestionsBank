package controllers;

import models.Exam;

//Todo: Convert to Abstract class
public interface IFileExporter {
    boolean exportExam(Exam exam, String dest, String format);
    boolean checkForDependencies();
}
