package Student_Registration_System;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO dao = new StudentDAO();

    public static void main(String[] args) {

        System.out.println("Student Registration System (CLI demo)");
        boolean exit = false;

        while (!exit) {
            try {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        registerStudent();
                        break;
                    case "2":
                        listStudents();
                        break;
                    case "3":
                        viewStudent();
                        break;
                    case "4":
                        updateStudent();
                        break;
                    case "5":
                        deleteStudent();
                        break;
                    case "6":
                        authenticate();
                        break;
                    case "0":
                        exit = true;
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n1. Register student");
        System.out.println("2. List all students");
        System.out.println("3. View student by ID");
        System.out.println("4. Update student");
        System.out.println("5. Delete student");
        System.out.println("6. Authenticate (login)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void registerStudent() throws SQLException {
        System.out.println("=== Register Student ===");
        System.out.print("First name: ");
        String fn = scanner.nextLine().trim();

        System.out.print("Last name: ");
        String ln = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("DOB (YYYY-MM-DD) or blank: ");
        String dobStr = scanner.nextLine().trim();
        LocalDate dob = dobStr.isEmpty() ? null : LocalDate.parse(dobStr);

        System.out.print("Gender (MALE/FEMALE/OTHER): ");
        String gender = scanner.nextLine().trim().toUpperCase();

        System.out.print("Course: ");
        String course = scanner.nextLine().trim();

        System.out.print("Password: ");
        char[] password = System.console() != null
                ? System.console().readPassword()
                : scanner.nextLine().toCharArray();

        Student s = new Student(fn, ln, email, dob, gender, course);
        boolean ok = dao.createStudent(s, password);

        java.util.Arrays.fill(password, '0');

        if (ok)
            System.out.println("Student registered with ID: " + s.getId());
        else
            System.out.println("Failed to register student.");
    }

    private static void listStudents() throws SQLException {
        List<Student> students = dao.getAllStudents();
        System.out.println("=== Students (" + students.size() + ") ===");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void viewStudent() throws SQLException {
        System.out.print("Enter student ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Student s = dao.getStudentById(id);

        if (s == null)
            System.out.println("Student not found.");
        else
            System.out.println(s);
    }

    private static void updateStudent() throws SQLException {
        System.out.print("Enter ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Student s = dao.getStudentById(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("First name [" + s.getFirstName() + "]: ");
        String fn = scanner.nextLine().trim();
        if (!fn.isEmpty()) s.setFirstName(fn);

        System.out.print("Last name [" + s.getLastName() + "]: ");
        String ln = scanner.nextLine().trim();
        if (!ln.isEmpty()) s.setLastName(ln);

        System.out.print("Email [" + s.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) s.setEmail(email);

        System.out.print("DOB (YYYY-MM-DD) [" + s.getDob() + "]: ");
        String dobStr = scanner.nextLine().trim();
        if (!dobStr.isEmpty()) s.setDob(LocalDate.parse(dobStr));

        System.out.print("Gender [" + s.getGender() + "]: ");
        String gender = scanner.nextLine().trim();
        if (!gender.isEmpty()) s.setGender(gender.toUpperCase());

        System.out.print("Course [" + s.getCourse() + "]: ");
        String course = scanner.nextLine().trim();
        if (!course.isEmpty()) s.setCourse(course);

        System.out.println(dao.updateStudent(s) ? "Updated successfully." : "Update failed.");
    }

    private static void deleteStudent() throws SQLException {
        System.out.print("Enter ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.println(dao.deleteStudent(id) ? "Deleted." : "Delete failed.");
    }

    private static void authenticate() throws SQLException {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        char[] pwd = System.console() != null
                ? System.console().readPassword()
                : scanner.nextLine().toCharArray();

        boolean ok = dao.authenticate(email, pwd);
        java.util.Arrays.fill(pwd, '0');

        System.out.println(ok ? "Login successful." : "Invalid credentials.");
    }
}