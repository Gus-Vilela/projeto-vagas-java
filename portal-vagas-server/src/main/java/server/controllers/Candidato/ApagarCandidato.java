package server.controllers.Candidato;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.models.Candidato;

import java.util.HashMap;
import java.util.Map;

public class ApagarCandidato {
    public static String apagarCandidato(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "apagarCandidato");
        int status = 404;

        String email = (String) receivedData.get("email");

        CandidatoDao candidatoDao = new CandidatoDao();
        Candidato candidato = candidatoDao.getByEmail(email);

        if (candidato == null) {
            response.put("mensagem", "E-mail não encontrado");
            response.put("status", status);
            return gson.toJson(response);
        }

        boolean apagarSuccessful = performApagar(candidato);

        if (apagarSuccessful) {
            status = 201;
        }

        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performApagar(Candidato candidato) {
        try {
            CandidatoDao candidatoDao = new CandidatoDao();
            candidatoDao.delete(candidato);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
