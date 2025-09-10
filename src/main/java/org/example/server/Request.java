package org.example.server;

import java.io.Serializable;

public class Request implements Serializable {
    private String action;   // GET_ALL, ADD, UPDATE, DELETE
    private Object data;     // Dữ liệu gửi kèm (ví dụ Student)

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() { return action; }
    public Object getData() { return data; }
}
