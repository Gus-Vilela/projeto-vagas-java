package server.controllers.Empresa;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.models.Empresa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginEmpresa {
    public static String loginEmpresa(Map<String, Object> receivedData, Gson gson) {
        String email = (String) receivedData.get("email");
        String senha = (String) receivedData.get("senha");
        int status = 401;

        boolean loginSuccessful = performLogin(email, senha);

        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "loginEmpresa");

        if (loginSuccessful) {
            status = 200;
            response.put("token", UUID.randomUUID().toString());
        } else {
            response.put("mensagem", "Login ou senha incorretos");
        }

        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performLogin( String email, String senha) {
        EmpresaDao empresaDao = new EmpresaDao();
        Empresa empresa = empresaDao.getByEmail(email);
        return empresa != null && empresa.getSenha().equals(senha);
    }
}