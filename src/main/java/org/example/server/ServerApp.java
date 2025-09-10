package org.example.server;

import org.example.model.Student;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerApp {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Server is running on port 9999...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush(); // 👈 cần thiết
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                String request = (String) ois.readObject();
                if ("GET_ALL".equals(request)) {
                    StudentDAO dao = new StudentDAO();
                    List<Student> list = dao.getAllStudents();
                    oos.writeObject(list);
                    oos.flush(); // 👈 đảm bảo gửi đi
                }

                oos.close();
                ois.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
