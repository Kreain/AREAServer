package com.area.server.routes;

import com.area.server.AreaHttpRequest;
import com.area.server.AreaHttpResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

public class RegisterResponse extends AreaHttpResponse {

    private final String username;
    private final String email;
    private final String password;

    private final String sqlHost = "localhost";
    private final String sqlPort = "3306";
    private final String sqlDatabase = "AREA";
    private final String sqlUsername = "root";
    private final String sqlPassword = "root";

    private static Connection connection;

    public RegisterResponse(AreaHttpRequest request) throws Exception {
        super(request);

        if (request.getMode() != AreaHttpRequest.RequestMode.POST)
            throw new IOException("Not a POST request");
        if (!requestData.has("credentials")) {
            setErrorData(new JSONObject("reason", "No credentials provided"));
            throw new IOException("No credentials provided");
        }
        JSONObject credentials = requestData.getJSONObject("credentials");
        username = credentials.getString("username");
        email = credentials.getString("email");
        password = credentials.getString("password");

        if (emailAlreadyExist()) {
            setErrorData(new JSONObject().put("reason", "Email address already in use"));
            throw new IOException("Email address already in use");
        } else {
            registerNewUser();
            setSuccessData(new JSONObject().put("response", "User has been registered"));
        }
    }
    
    private void registerNewUser() throws Exception {
        if (connection != null && !connection.isClosed())
            throw new Exception("Connection already opened");
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + sqlHost + ":" +
                sqlPort + "/" + sqlDatabase + "?autoReconnect=true&useSSL=false", sqlUsername, sqlPassword);

        Statement statement = connection.createStatement();
        String query = "INSERT INTO users(username, email, password) VALUES(\"" +
                username + "\", \"" + email + "\", \"" + password + "\");";
        statement.executeUpdate(query);
    }

    private boolean emailAlreadyExist() throws Exception {
        boolean exists;

        if (connection != null && !connection.isClosed())
            throw new Exception("Connection already opened");
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + sqlHost + ":" +
                sqlPort + "/" + sqlDatabase + "?autoReconnect=true&useSSL=false", sqlUsername, sqlPassword);

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM users WHERE email=\"" + email + "\";";
        ResultSet result = statement.executeQuery(query);
        exists = result.next();
        statement.close();
        connection.close();
        return (exists);
    }
}
