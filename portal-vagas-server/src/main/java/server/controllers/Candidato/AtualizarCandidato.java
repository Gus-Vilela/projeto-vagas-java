package server.controllers.Candidato;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.models.Candidato;

import java.util.HashMap;
import java.util.Map;

public class AtualizarCandidato {
    public static String atualizarCandidato(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "atualizarCandidato");
        int status = 404;

        String email = (String) receivedData.get("email");
        String nome = (String) receivedData.get("nome");
        String senha = (String) receivedData.get("senha");

        CandidatoDao candidatoDao = new CandidatoDao();
        Candidato candidato = candidatoDao.getByEmail(email);

        if (candidato == null) {
            response.put("mensagem", "E-mail não encontrado");
            response.put("status", status);
            return gson.toJson(response);
        }
        candidato.setNome(nome);
        candidato.setSenha(senha);

        boolean atualizacaoSuccessful = performAtualizacao(candidato);

        if (atualizacaoSuccessful) {
            status = 201;
        }
        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performAtualizacao(Candidato candidato) {
        try{
            CandidatoDao candidatoDao = new CandidatoDao();
            candidatoDao.update(candidato);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
