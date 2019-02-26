package models;

public class Doctor {
    public Doctor(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    String id;
    public String name;
    String email;
    String password;

    public Doctor() {

    }
}
