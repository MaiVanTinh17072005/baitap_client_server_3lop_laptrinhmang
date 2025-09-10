package org.example.client;

import org.example.model.*;
import org.example.server.Request;
import org.example.server.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ClientUI() {
        setTitle("Quản lý Sinh viên - Client");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ======= Table =======
        model = new DefaultTableModel(new String[]{"Mã SV", "Họ tên", "Tuổi", "SĐT"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // ======= Buttons =======
        JButton btnLoad = new JButton("Load DS");
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnLoad);
        panelBtn.add(btnAdd);
        panelBtn.add(btnUpdate);
        panelBtn.add(btnDelete);

        // ======= Layout =======
        add(scrollPane, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);

        // ======= Event =======
        btnLoad.addActionListener(e -> loadStudents());
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        // Load khi mở app
        loadStudents();
    }

    private void loadStudents() {
        try {
            Response res = ClientApp.sendRequest(new Request("GET_ALL", null));
            List<Student> list = (List<Student>) res.getData();

            model.setRowCount(0); // clear
            for (Student s : list) {
                model.addRow(new Object[]{s.getMasv(), s.getHoten(), s.getTuoi(), s.getSdt()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi load DS: " + ex.getMessage());
        }
    }

    private void addStudent() {
        JTextField tfTen = new JTextField();
        JTextField tfTuoi = new JTextField();
        JTextField tfSdt = new JTextField();

        Object[] msg = { "Họ tên:", tfTen, "Tuổi:", tfTuoi, "SĐT:", tfSdt};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION);

        if (opt == JOptionPane.OK_OPTION) {
            try {
                Student s = new Student(
                        tfTen.getText(),
                        Integer.parseInt(tfTuoi.getText()),
                        tfSdt.getText()
                );
                Response res = ClientApp.sendRequest(new Request("ADD", s));
                JOptionPane.showMessageDialog(this, res.getMessage());
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm: " + ex.getMessage());
            }
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn 1 sinh viên để sửa");
            return;
        }

        int masv = (int) model.getValueAt(row, 0);
        String hoten = (String) model.getValueAt(row, 1);
        int tuoi = (int) model.getValueAt(row, 2);
        String sdt = (String) model.getValueAt(row, 3);

        JTextField tfTen = new JTextField(hoten);
        JTextField tfTuoi = new JTextField(String.valueOf(tuoi));
        JTextField tfSdt = new JTextField(sdt);

        Object[] msg = {"Họ tên:", tfTen, "Tuổi:", tfTuoi, "SĐT:", tfSdt};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Sửa sinh viên", JOptionPane.OK_CANCEL_OPTION);

        if (opt == JOptionPane.OK_OPTION) {
            try {
                Student s = new Student(
                        masv,
                        tfTen.getText(),
                        Integer.parseInt(tfTuoi.getText()),
                        tfSdt.getText()
                );
                Response res = ClientApp.sendRequest(new Request("UPDATE", s));
                JOptionPane.showMessageDialog(this, res.getMessage());
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi sửa: " + ex.getMessage());
            }
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn 1 sinh viên để xóa");
            return;
        }

        int masv = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa SV mã " + masv + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Response res = ClientApp.sendRequest(new Request("DELETE", masv));
                JOptionPane.showMessageDialog(this, res.getMessage());
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
