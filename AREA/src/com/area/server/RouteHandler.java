package com.area.server;

import com.area.server.routes.RegisterResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
        AreaHttpRequest request = new AreaHttpRequest(exchange);
        AreaHttpResponse response = new AreaHttpResponse(request);
        String URI = exchange.getRequestURI().toString().split("\\?")[0];
        switch (URI) {
            case "/register" : response = new RegisterResponse(request); break;
//            case "/login" : response = new LoginResponse(request); break;
            case "/" : response.setResponseCode(true);
        }
        exchange.sendResponseHeaders(200, response.getResponse().toString().getBytes().length);
        outputStream.write(response.getResponse().toString().getBytes());
        outputStream.close();
    }
}
