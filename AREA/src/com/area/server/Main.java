package com.area.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Main {

    private static String hostname;
    private static InetAddress ip;
    private static HttpServer server = null;

    private static final String[] routes = {
        "/", "/register", "/login"
    };

    private static void addRoutes() {
        for (String route : routes)
            server.createContext(route, new RouteHandler());
    }

    public static void main(String[] args) {
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostAddress();
            server = HttpServer.create(new InetSocketAddress(hostname, 6565), 0);
            addRoutes();
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on " + server.getAddress().getHostString()
                    + ":" + server.getAddress().getPort());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(84);
        }
    }
}
