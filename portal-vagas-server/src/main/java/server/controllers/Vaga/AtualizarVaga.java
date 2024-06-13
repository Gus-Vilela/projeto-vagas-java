package server.controllers.Vaga;

import com.google.gson.Gson;
import server.hibernate.dao.CompetenciaDao;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.dao.RequisitoDao;
import server.hibernate.dao.VagaDao;
import server.hibernate.models.Competencia;
import server.hibernate.models.Empresa;
import server.hibernate.models.Requisito;
import server.hibernate.models.Vaga;
import server.utils.ConversionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtualizarVaga {
    public static String atualizarVaga(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "atualizarVaga");
        int status = 422;
        String mensagem = "Erro ao atualizar vaga";

        try {
            if (!receivedData.containsKey("token")) {
                mensagem = "Token não fornecido";
                response.put("mensagem", mensagem);
                response.put("status", status);
                return gson.toJson(response);
            }

            if (!receivedData.containsKey("email")) {
                mensagem = "E-mail não fornecido";
                response.put("mensagem", mensagem);
                response.put("status", status);
                return gson.toJson(response);
            }

            String email = (String) receivedData.get("email");

            EmpresaDao empresaDao = new EmpresaDao();
            Empresa empresa = empresaDao.getByEmail(email);

            if (empresa == null) {
                response.put("mensagem", "Empresa não encontrada");
                response.put("status", status);
                return gson.toJson(response);
            }

            int idVaga = ConversionUtil.convertToInteger(receivedData.get("idVaga"));
            String nome = (String) receivedData.get("nome");
            List<String> competencias = (List<String>) receivedData.get("competencias");
            Double faixaSalarial = (Double) receivedData.get("faixaSalarial");
            String descricao = (String) receivedData.get("descricao");
            String estado = (String) receivedData.get("estado");

            boolean atualizacaoSuccessful = performAtualizacao(idVaga, nome, faixaSalarial, descricao, estado, competencias);

            if (atualizacaoSuccessful) {
                status = 201;
                mensagem = "Vaga atualizada com sucesso";
            }

            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        } catch (jakarta.persistence.PersistenceException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                mensagem = "A vaga já possui um requisito com essa competência";
            }
            System.out.println(e.getMessage());
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

    private static boolean performAtualizacao(int idVaga, String nome, Double faixaSalarial, String descricao, String estado, List<String> competencias) {
        VagaDao vagaDao = new VagaDao();
        CompetenciaDao competenciaDao = new CompetenciaDao();
        RequisitoDao requisitoDao = new RequisitoDao();

        Vaga vaga = vagaDao.get(idVaga);

        if (vaga == null) {
            return false;
        }

        vaga.setNome(nome);
        vaga.setFaixaSalarial(faixaSalarial);
        vaga.setDescricao(descricao);
        vaga.setEstado(estado);

        vagaDao.update(vaga);

        requisitoDao.deleteAllByVagaId(vaga.getId());

        for (String competenciaNome : competencias) {
            Competencia competencia = competenciaDao.getByName(competenciaNome);

            if (competencia == null) {
                competencia = new Competencia(competenciaNome);
                competenciaDao.save(competencia);
            }

            Requisito requisito = new Requisito();
            requisito.setIdVaga(vaga);
            requisito.setIdCompetencia(competencia);

            requisitoDao.save(requisito);
        }
        return true;
    }
}
