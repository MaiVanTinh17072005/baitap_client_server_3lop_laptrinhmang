package org.example.client;

import org.example.server.Request;
import org.example.server.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientApp {

    // Hàm gửi request chung cho GUI hoặc console test
    public static Response sendRequest(Request req) throws Exception {
        try (Socket socket = new Socket("localhost", 9999)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Gửi request
            oos.writeObject(req);
            oos.flush();

            // Nhận response
            return (Response) ois.readObject();
        }
    }
}
