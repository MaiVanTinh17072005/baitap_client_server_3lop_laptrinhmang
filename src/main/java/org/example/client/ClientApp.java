package org.example.client;

import org.example.model.*;
import org.example.server.Request;
import org.example.server.Response;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientApp {
    public static Response sendRequest(Request req) throws Exception {
        try (Socket socket = new Socket("localhost", 9999)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(req);
            oos.flush();

            Response res = (Response) ois.readObject();
            return res;
        }
    }

    // Test console song song với GUI
    public static void main(String[] args) {
        try {
            // 1. Lấy danh sách
            Response res1 = sendRequest(new Request("GET_ALL", null));
            List<Student> list = (List<Student>) res1.getData();
            System.out.println("Danh sách ban đầu:");
            list.forEach(System.out::println);

            // 2. Thêm sinh viên
            Student s1 = new Student(123, "Nguyen Van A", 20, "0123456789");
            Response res2 = sendRequest(new Request("ADD", s1));
            System.out.println(res2.getMessage());

            // 3. Cập nhật sinh viên
            Student s2 = new Student(123, "Nguyen Van B Updated", 21, "0987654321");
            Response res3 = sendRequest(new Request("UPDATE", s2));
            System.out.println(res3.getMessage());

            // 4. Xóa sinh viên
            Response res4 = sendRequest(new Request("DELETE", 123));
            System.out.println(res4.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
