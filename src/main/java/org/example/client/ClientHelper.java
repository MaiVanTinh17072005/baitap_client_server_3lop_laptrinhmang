package org.example.client;

import org.example.model.*;
import org.example.server.Request;
import org.example.server.Response;

import java.io.*;
import java.net.Socket;

public class ClientHelper {
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
}
