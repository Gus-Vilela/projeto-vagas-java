package server.controllers.Experiencia;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.dao.CompetenciaDao;
import server.hibernate.dao.ExperienciaDao;
import server.hibernate.models.Candidato;
import server.hibernate.models.Competencia;
import server.hibernate.models.Experiencia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApagarExperiencias {
    public static String apagarExperiencias(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "apagarCompetenciaExperiencia");
        int status = 422;
        String mensagem = "Erro ao apagar Competencia/Experiencia";

        try {
            if (!receivedData.containsKey("token")) {
                mensagem = "Token não fornecido";
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

            List<Map<String, Object>> competenciaExperiencia = (List<Map<String, Object>>) receivedData.get("competenciaExperiencia");

            ExperienciaDao experienciaDao = new ExperienciaDao();
            CompetenciaDao competenciaDao = new CompetenciaDao();

            for (Map<String, Object> item : competenciaExperiencia) {
                String competencia = (String) item.get("competencia");

                Competencia comp = competenciaDao.getByName(competencia);

                if (comp == null) {
                    response.put("mensagem", "Competencia não encontrada");
                    response.put("status", status);
                    return gson.toJson(response);
                }

                Experiencia exp = experienciaDao.getByCandidatoIdAndCompetenciaId(candidato.getId(), comp.getId());

                if (exp == null) {
                    response.put("mensagem", "Experiencia não encontrada");
                    response.put("status", status);
                    return gson.toJson(response);
                }

                experienciaDao.delete(exp);
            }

            status = 201;
            mensagem = "Competencia/Experiencia apagada com sucesso";
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        }
    }
}