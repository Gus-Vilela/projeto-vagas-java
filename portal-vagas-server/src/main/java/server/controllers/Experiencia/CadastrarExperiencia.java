package server.controllers.Experiencia;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.dao.CompetenciaDao;
import server.hibernate.dao.ExperienciaDao;
import server.hibernate.models.Candidato;
import server.hibernate.models.Competencia;
import server.hibernate.models.Experiencia;
import server.utils.ConversionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastrarExperiencia {
    public static String cadastrarExperiencia(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "cadastrarCompetenciaExperiencia");
        int status = 422;
        String mensagem = "Erro ao cadastrar competência e experiência";

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

            System.out.println(competenciaExperiencia);

            boolean cadastroSuccessful = performCadastro(competenciaExperiencia, candidato);

            if (cadastroSuccessful) {
                status = 201;
                mensagem = "Competencia/Experiencia cadastrada com sucesso";
            }

            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        } catch (jakarta.persistence.PersistenceException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                mensagem = "O candidato já possui uma experiência com essa competência";
            }
            System.out.println(e.getMessage());
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        }
    }

    private static boolean performCadastro(List<Map<String, Object>> competenciaExperiencia, Candidato candidato) {
        ExperienciaDao experienciaDao = new ExperienciaDao();
        CompetenciaDao competenciaDao = new CompetenciaDao();

        for (Map<String, Object> item : competenciaExperiencia) {
            String competencia = (String) item.get("competencia");
            int experiencia = ConversionUtil.convertToInteger(item.get("experiencia"));
            Experiencia exp;
            Competencia comp = competenciaDao.getByName(competencia);

            if (comp == null) {
                comp = new Competencia(competencia);
                competenciaDao.save(comp);
            }

            exp = new Experiencia(candidato, comp, experiencia);
            experienciaDao.save(exp);
        }
        return true;
    }
}