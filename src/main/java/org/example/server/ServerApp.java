package org.example.server;

import org.example.model.*;
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
                oos.flush();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Request req = (Request) ois.readObject();
                Response res = handleRequest(req);

                oos.writeObject(res);
                oos.flush();

                oos.close();
                ois.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Response handleRequest(Request req) {
        try {
            StudentDAO dao = new StudentDAO();
            switch (req.getAction()) {
                case "GET_ALL":
                    List<Student> list = dao.getAllStudents();
                    return new Response(true, list, "Danh sách sinh viên");
                case "ADD":
                    Student sAdd = (Student) req.getData();
                    return new Response(dao.addStudent(sAdd), null, "Thêm thành công!");
                case "UPDATE":
                    Student sUpdate = (Student) req.getData();
                    return new Response(dao.updateStudent(sUpdate), null, "Cập nhật thành công!");
                case "DELETE":
                    int id = (Integer) req.getData();
                    return new Response(dao.deleteStudent(id), null, "Xóa thành công!");
                default:
                    return new Response(false, null, "Action không hợp lệ");
            }
        } catch (Exception e) {
            return new Response(false, null, "Lỗi server: " + e.getMessage());
        }
    }
}
