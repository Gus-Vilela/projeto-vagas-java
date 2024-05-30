package server.controllers.Candidato;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.models.Candidato;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginCandidato {
    public static String loginCandidato(Map<String, Object> receivedData, Gson gson) {
        String email = (String) receivedData.get("email");
        String senha = (String) receivedData.get("senha");
        int status = 401;

        boolean loginSuccessful = performLogin(email, senha);

        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "loginCandidato");

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
        CandidatoDao candidatoDao = new CandidatoDao();
        Candidato candidato = candidatoDao.getByEmail(email);
        System.out.println(candidato);
        return candidato != null && candidato.getSenha().equals(senha);
    }
}
