package com.area.server.routes;

import com.area.server.AreaHttpRequest;
import com.area.server.AreaHttpResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginResponse extends AreaHttpResponse {

    private final String email;
    private final String password;

    private static Connection connection;

    public LoginResponse(AreaHttpRequest request) throws Exception {
        super(request);

        if (request.getMode() != AreaHttpRequest.RequestMode.POST)
            throw new IOException("Not a POST request");
        if (!requestData.has("credentials")) {
            setErrorData(new JSONObject("reason", "No credentials provided"));
            throw new IOException("No credentials provided");
        }
        JSONObject credentials = requestData.getJSONObject("credentials");
        email = credentials.getString("email");
        password = credentials.getString("password");

        if (wrongCredentials()) {
            setErrorData(new JSONObject().put("reason", "Wrong Credentials"));
            throw new IOException("Wrong Credentials");
        } else
            setSuccessData(new JSONObject().put("response", "User has logged in"));
    }

    private boolean wrongCredentials() throws Exception {
        boolean wrong = false;

        if (connection != null && !connection.isClosed())
            throw new Exception("Connection already opened");
        Class.forName("com.mysql.jdbc.Driver");
        String sqlHost = "localhost";
        String sqlPort = "3306";
        String sqlDatabase = "AREA";
        String sqlUsername = "root";
        String sqlPassword = "root";
        connection = DriverManager.getConnection("jdbc:mysql://" + sqlHost + ":" +
                sqlPort + "/" + sqlDatabase + "?autoReconnect=true&useSSL=false", sqlUsername, sqlPassword);

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM users WHERE email=\"" + email + "\";";
        ResultSet result = statement.executeQuery(query);
        if (!result.next() || !result.getString("password").equals(password))
            wrong = true;
        statement.close();
        connection.close();
        return (wrong);
    }
}
