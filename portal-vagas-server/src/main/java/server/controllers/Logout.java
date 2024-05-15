package server.controllers;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Logout {
    public static String logout(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "logout");
        int status = 404;

        String token = (String) receivedData.get("token");

        boolean logoutSuccessful = performLogout(token);

        if (logoutSuccessful) {
            status = 204;
        }

        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performLogout(String token) {
        return true;
    }
}
