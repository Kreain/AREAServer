package com.area.server;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AreaHttpRequest {

    public enum RequestMode {
        GET,
        POST
    }

    private JSONObject data;
    private RequestMode mode;

    public AreaHttpRequest(HttpExchange exchange) throws IOException, JSONException {
        if (exchange.getRequestMethod().equals("POST")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder(512);
            int b;
            while ((b = bufferedReader.read()) != -1)
                buffer.append((char) b);
            bufferedReader.close();
            data = new JSONObject(buffer.toString());
            mode = RequestMode.POST;
        } else if (exchange.getRequestMethod().equals("GET")) {
            String[] params;
            data = new JSONObject();
            mode = RequestMode.GET;

            if (exchange.getRequestURI().toString().contains("?")) {
                params = exchange.getRequestURI().toString().split("\\?")[1].split("&");
                for (String key : params) {
                    String param[] = key.split("=");
                    data.put(param[0], (param.length == 1) ? true : param[1]);
                }
            }
        }
    }

    public JSONObject getData() {
        return (data);
    }

    public RequestMode getMode() {
        return (mode);
    }
}
