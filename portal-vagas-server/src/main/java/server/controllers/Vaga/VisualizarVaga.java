package server.controllers.Vaga;

import com.google.gson.Gson;
import server.hibernate.dao.CompetenciaDao;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.dao.VagaDao;
import server.hibernate.dao.RequisitoDao;
import server.hibernate.models.Competencia;
import server.hibernate.models.Empresa;
import server.hibernate.models.Vaga;
import server.hibernate.models.Requisito;
import server.utils.ConversionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class VisualizarVaga {
    public static String visualizarVaga(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "visualizarVaga");
        int status = 422;
        String mensagem = "Erro ao visualizar vaga";

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
                response.put("mensagem", "E-mail não encontrado");
                response.put("status", status);
                return gson.toJson(response);
            }

            int idVaga = ConversionUtil.convertToInteger(receivedData.get("idVaga"));

            VagaDao vagaDao = new VagaDao();
            Vaga vaga = vagaDao.get(idVaga);

            if (vaga == null) {
                response.put("mensagem", "Vaga não encontrada");
                response.put("status", status);
                return gson.toJson(response);
            }

            if (!Objects.equals(vaga.getIdEmpresa().getId(), empresa.getId())) {
                response.put("mensagem", "Vaga não pertence à empresa fornecida");
                response.put("status", status);
                return gson.toJson(response);
            }

            RequisitoDao requisitoDao = new RequisitoDao();
            List<Requisito> requisitos = requisitoDao.fetchAllByVagaId(idVaga);

            CompetenciaDao competenciaDao = new CompetenciaDao();
            List<String> competencias = requisitos.stream()
                    .map(requisito -> {
                        Competencia competencia = competenciaDao.get(requisito.getIdCompetencia().getId());
                        return competencia.getNome();
                    })
                    .collect(Collectors.toList());

            status = 201;
            response.put("status", status);
            response.put("faixaSalarial", vaga.getFaixaSalarial());
            response.put("descricao", vaga.getDescricao());
            response.put("estado", vaga.getEstado());
            response.put("competencias", competencias);

            return gson.toJson(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        }
    }
}
