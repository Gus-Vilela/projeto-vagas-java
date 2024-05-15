package server.controllers;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.models.Candidato;

import java.util.HashMap;
import java.util.Map;

public class VisualizarCandidato {
    public static String visualizarCandidato(Map<String, Object> receivedData, Gson gson) {
        String email = (String) receivedData.get("email");
        System.out.println("Email: " + email);
        CandidatoDao candidatoDao = new CandidatoDao();
        Candidato candidato = candidatoDao.getByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "visualizarCandidato");
        int status = 404;

        if (candidato != null) {
            status = 201;
            response.put("nome", candidato.getNome());
            response.put("senha", candidato.getSenha());
        } else {
            response.put("mensagem", "E-mail não encontrado");
        }

        response.put("status", status);
        return gson.toJson(response);
    }
}
