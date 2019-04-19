package models;

public class Doctor {

    private String id;
    private String name;
    private String university;

    public String getAltUniversity() {
        return altUniversity;
    }

    public void setAltUniversity(String altUniversity) {
        this.altUniversity = altUniversity;
    }

    private String altUniversity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPreferredExamLayout() {
        return preferredExamLayout;
    }

    public void setPreferredExamLayout(String preferredExamLayout) {
        this.preferredExamLayout = preferredExamLayout;
    }

    private String email;
    private String password;
    private String college;
    private String department;



    private String preferredExamLayout;



    public Doctor() {
    }
}
