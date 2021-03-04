package com.area.server;

import com.area.server.routes.LoginResponse;
import com.area.server.routes.RegisterResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class RouteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        AreaHttpRequest request = null;
        try {
            request = new AreaHttpRequest(exchange);
        } catch (IOException | JSONException e) {
            System.out.println("Reason: " + e.getMessage());
        }
        AreaHttpResponse response = new AreaHttpResponse(request);
        String URI = exchange.getRequestURI().toString().split("\\?")[0];
        try {
            switch (URI) {
                case "/register": response = new RegisterResponse(request); break;
                case "/login" : response = new LoginResponse(request); break;
                case "/": response.setResponseCode(true); break;
            }
        } catch (Exception e) {
            //if (!(e instanceof IOException))
                System.out.println("REASON: " + e.getMessage());
                //e.printStackTrace();
                response.setErrorData(new JSONObject().put("reason", e.getMessage()));
        }
        exchange.sendResponseHeaders(200, response.getResponse().toString().getBytes().length);
        outputStream.write(response.getResponse().toString().getBytes());
        outputStream.close();
    }
}
