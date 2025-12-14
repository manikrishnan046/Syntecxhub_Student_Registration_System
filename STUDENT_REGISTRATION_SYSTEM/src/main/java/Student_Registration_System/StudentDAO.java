package Student_Registration_System;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Create (register) a new student; stores password hash & salt securely
    public boolean createStudent(Student s, char[] password) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, email, password_hash, password_salt, dob, gender, course) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        byte[] salt = PBKDF2Util.generateSalt();
        byte[] hash = PBKDF2Util.hashPassword(password, salt);

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            try {
                ps.setString(1, s.getFirstName());
                ps.setString(2, s.getLastName());
                ps.setString(3, s.getEmail());
                ps.setBytes(4, hash);
                ps.setBytes(5, salt);
                if (s.getDob() != null) ps.setDate(6, Date.valueOf(s.getDob())); else ps.setNull(6, Types.DATE);
                ps.setString(7, s.getGender());
                ps.setString(8, s.getCourse());

                int affected = ps.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        s.setId(keys.getInt(1));
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, dob, gender, course, created_at, updated_at FROM students WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToStudent(rs);
            }
        }
        return null;
    }

    public Student findByEmail(String email) throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, dob, gender, course, created_at, updated_at FROM students WHERE email = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToStudent(rs);
            }
        }
        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, dob, gender, course, created_at, updated_at FROM students ORDER BY id";
        List<Student> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToStudent(rs));
        }
        return list;
    }

    public boolean updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, dob = ?, gender = ?, course = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getEmail());
            if (s.getDob() != null) ps.setDate(4, Date.valueOf(s.getDob())); else ps.setNull(4, Types.DATE);
            ps.setString(5, s.getGender());
            ps.setString(6, s.getCourse());
            ps.setInt(7, s.getId());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // Authentication: verifies email + password
    public boolean authenticate(String email, char[] password) throws SQLException {
        String sql = "SELECT password_hash, password_salt FROM students WHERE email = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                byte[] hash = rs.getBytes("password_hash");
                byte[] salt = rs.getBytes("password_salt");
                return PBKDF2Util.verifyPassword(password, salt, hash);
            }
        }
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setEmail(rs.getString("email"));
        Date d = rs.getDate("dob");
        if (d != null) s.setDob(d.toLocalDate());
        s.setGender(rs.getString("gender"));
        s.setCourse(rs.getString("course"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) s.setCreatedAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) s.setUpdatedAt(updated.toLocalDateTime());
        return s;
    }
}