package org.example.server;

import org.example.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection conn;

    public StudentDAO() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bai_tap_cl_sv_3lop",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found");
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM baitap_clsv3lop";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Student s = new Student(
                    rs.getInt("Masv"),
                    rs.getString("Hoten"),
                    rs.getInt("Tuoi"),
                    rs.getString("Sdt")
            );
            list.add(s);
        }
        return list;
    }

    public boolean addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO baitap_clsv3lop ( Hoten, Tuoi, Sdt) VALUES ( ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, s.getHoten());
        ps.setInt(2, s.getTuoi());
        ps.setString(3, s.getSdt());
        return ps.executeUpdate() > 0;
    }

    public boolean updateStudent(Student s) throws SQLException {
        String sql = "UPDATE baitap_clsv3lop SET Hoten=?, Tuoi=?, Sdt=? WHERE Masv=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, s.getHoten());
        ps.setInt(2, s.getTuoi());
        ps.setString(3, s.getSdt());
        ps.setInt(4, s.getMasv());
        return ps.executeUpdate() > 0;
    }

    public boolean deleteStudent(int masv) throws SQLException {
        String sql = "DELETE FROM baitap_clsv3lop WHERE Masv=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, masv);
        return ps.executeUpdate() > 0;
    }
}
