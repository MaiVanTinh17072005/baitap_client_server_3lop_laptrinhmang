package org.example.client;

import org.example.model.*;
import org.example.server.Request;
import org.example.server.Response;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientApp {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientApp(String host, int port) throws Exception {
        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    // Gửi request lên server
    public void sendRequest(Request req) throws Exception {
        oos.writeObject(req);
        oos.flush();
    }

    // Nhận response realtime từ server
    public void startListening(Consumer<Response> callback) {
        new Thread(() -> {
            try {
                while (true) {
                    Object obj = ois.readObject();
                    if (obj instanceof Response) {
                        Response res = (Response) obj;
                        callback.accept(res); // gọi callback để UI cập nhật
                    }
                }
            } catch (Exception e) {
                System.out.println("Ngắt kết nối: " + e.getMessage());
            }
        }).start();
    }

    public void close() throws Exception {
        socket.close();
    }
}
