package Student_Registration_System;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String gender;
    private String course;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Student() {}

    public Student(String firstName, String lastName, String email, LocalDate dob, String gender, String course) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.course = course;
    }

    // getters & setters omitted for brevity — implement all standard getters/setters
    // ... generate them in your IDE
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", name='" + firstName + " " + lastName + '\'' +
            ", email='" + email + '\'' +
            ", dob=" + dob +
            ", gender=" + gender +
            ", course='" + course + '\'' +
            '}';
    }
}