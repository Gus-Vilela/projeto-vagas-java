package server.controllers;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.models.Candidato;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadastrarCandidato {
    public static String cadastrarCandidato(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "cadastrarCandidato");
        int status = 404;

        String nome = (String) receivedData.get("nome");
        String email = (String) receivedData.get("email");
        String senha = (String) receivedData.get("senha");

        CandidatoDao candidatoDao = new CandidatoDao();
        Candidato candidato = candidatoDao.getByEmail(email);

        if(candidato != null){
            response.put("mensagem", "E-mail já cadastrado");
            status = 422;
            response.put("status", status);
            return gson.toJson(response);
        }

        boolean cadastroSuccessful = performCadastro(nome, email, senha);

        if (cadastroSuccessful) {
            status = 201;
            response.put("token", UUID.randomUUID().toString());
        }else {
            response.put("mensagem", "");
        }

        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performCadastro(String nome, String email, String senha) {
        CandidatoDao candidatoDao = new CandidatoDao();
        Candidato candidato = new Candidato(nome, senha, email);
        candidatoDao.save(candidato);
        return true;
    }
}
