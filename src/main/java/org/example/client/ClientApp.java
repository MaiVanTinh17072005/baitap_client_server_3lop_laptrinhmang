package org.example.client;

import org.example.model.Student;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientApp {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 9999)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush(); // 👈 cần thiết
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Gửi request
            oos.writeObject("GET_ALL");
            oos.flush(); // 👈 đảm bảo gửi đi ngay

            // Nhận danh sách sinh viên
            List<Student> list = (List<Student>) ois.readObject();
            System.out.println("Danh sách sinh viên:");
            for (Student s : list) {
                System.out.println(s);
            }

            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
