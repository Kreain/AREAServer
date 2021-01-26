package com.area.server.routes;

import com.area.server.AreaHttpRequest;
import com.area.server.AreaHttpResponse;
import org.json.JSONObject;

import java.sql.*;

public class RegisterResponse extends AreaHttpResponse {

    private String username;
    private String email;
    private String password;

    private static Connection connection;

    public RegisterResponse(AreaHttpRequest request) {
        super(request);

        if (request.getMode() != AreaHttpRequest.RequestMode.POST)
            return;
        if (!requestData.has("credentials")) {
            setErrorData(new JSONObject("reason", "No credentials provided"));
            return;
        }
        JSONObject credentials = requestData.getJSONObject("credentials");
        username = credentials.getString("username");
        email = credentials.getString("email");
        password = credentials.getString("password");

        System.out.println("username: " + username);
        System.out.println("email: " + email);
        System.out.println("password: " + password);

        try {
            if (emailAlreadyExist()) {
                setErrorData(new JSONObject("reason", "Email address already in use"));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setErrorData(new JSONObject("reason", "An unknown error has occurred"));
            return;
        }
    }

    public boolean emailAlreadyExist() throws Exception {
        String sqlHost = "localhost";
        String sqlPort = "3306";
        String sqlDatabase = "AREA";
        String sqlUsername = "root";
        String sqlPassword = "root";

        if (connection != null && !connection.isClosed())
            throw new Exception();
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + sqlHost + ":" +
                sqlPort + "/" + sqlDatabase + "?autoReconnect=true&useSSL=false", sqlUsername, sqlPassword);

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM users WHERE email=\"" + email + "\";";
        ResultSet result = statement.executeQuery(query);
        System.out.println(result.next());
        statement.close();
        connection.close();
        return (true);
    }
}
