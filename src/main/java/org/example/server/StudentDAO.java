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
                    "jdbc:mysql://localhost:3306/bai_tap_cl_sv_3lop", // tên DB
                    "root",  // user MySQL
                    ""       // password MySQL
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
}
