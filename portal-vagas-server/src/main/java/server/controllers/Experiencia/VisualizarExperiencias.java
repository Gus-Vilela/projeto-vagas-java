package server.controllers.Experiencia;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.dao.CompetenciaDao;
import server.hibernate.dao.ExperienciaDao;
import server.hibernate.models.Candidato;
import server.hibernate.models.Experiencia;

import java.util.*;

public class VisualizarExperiencias {
    public static String visualizarExperiencias(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "visualizarCompetenciaExperiencia");
        int status = 422;
        String mensagem = "Erro ao visualizar competências";

        try {
            if (!receivedData.containsKey("email") || !receivedData.containsKey("token")) {
                mensagem = "Email ou token não fornecido";
                response.put("mensagem", mensagem);
                response.put("status", status);
                return gson.toJson(response);
            }

            String email = (String) receivedData.get("email");

            CandidatoDao candidatoDao = new CandidatoDao();
            Candidato candidato = candidatoDao.getByEmail(email);

            if (candidato == null) {
                response.put("mensagem", "E-mail não encontrado");
                response.put("status", status);
                return gson.toJson(response);
            }

            ExperienciaDao experienciaDao = new ExperienciaDao();

            List<Experiencia> experiencias = experienciaDao.fetchAllbyCandidatoId(candidato.getId());

            CompetenciaDao competenciaDao = new CompetenciaDao();

            List<Map<String, Object>> competenciaExperiencia = new ArrayList<>();
            for (Experiencia experiencia : experiencias) {
                Map<String, Object> item = new HashMap<>();
                item.put("competencia", competenciaDao.get(experiencia.getIdCompetencia().getId()).getNome());
                item.put("experiencia", experiencia.getTempo());
                competenciaExperiencia.add(item);
            }

            status = 201;
            response.put("status", status);
            response.put("competenciaExperiencia", competenciaExperiencia);
            return gson.toJson(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        }
    }
}