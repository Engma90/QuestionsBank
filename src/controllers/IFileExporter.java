package controllers;

import models.Exam;

public interface IFileExporter {
    boolean exportExam(Exam exam, String dest, String format);
    boolean checkForDependencies();
}
