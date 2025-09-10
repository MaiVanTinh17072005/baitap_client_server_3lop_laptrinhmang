package org.example.server;

import org.example.model.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class ServerApp {
    // Danh sách client đang kết nối (lưu OutputStream để broadcast)
    private static final List<ObjectOutputStream> clientOutputs = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Server is running on port 9999...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // Mỗi client chạy trên 1 thread riêng
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Broadcast đến tất cả client
    public static void broadcast(Response res, ObjectOutputStream exclude) {
        synchronized (clientOutputs) {
            for (ObjectOutputStream oos : clientOutputs) {
                if (oos == exclude) continue; // không gửi lại cho client đã gửi request
                try {
                    oos.writeObject(res);
                    oos.flush();
                } catch (IOException e) {
                    System.out.println("Broadcast lỗi: " + e.getMessage());
                }
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private StudentDAO dao = new StudentDAO();

        public ClientHandler(Socket socket) throws SQLException {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

                // Thêm client vào danh sách broadcast
                clientOutputs.add(oos);

                while (true) {
                    Object obj = ois.readObject();
                    if (!(obj instanceof Request)) break;

                    Request req = (Request) obj;
                    Response res = handleRequest(req);

                    // Gửi kết quả cho client hiện tại
                    oos.writeObject(res);
                    oos.flush();

                    // Nếu là thao tác làm thay đổi DB -> broadcast cho client khác
                    if (req.getAction().equals("ADD") ||
                            req.getAction().equals("UPDATE") ||
                            req.getAction().equals("DELETE")) {
                        Response notifyRes = new Response(true, dao.getAllStudents(), "Realtime update");

                        ServerApp.broadcast(notifyRes, null);
                    }
                }
            } catch (Exception e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                clientOutputs.remove(oos);
            }
        }

        private Response handleRequest(Request req) {
            try {
                switch (req.getAction()) {
                    case "GET_ALL":
                        return new Response(true, dao.getAllStudents(), "Danh sách sinh viên");
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
}
