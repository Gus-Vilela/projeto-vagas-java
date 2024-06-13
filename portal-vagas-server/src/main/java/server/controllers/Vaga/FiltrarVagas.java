package server.controllers.Vaga;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.dao.VagaDao;
import server.hibernate.models.Vaga;
import server.hibernate.models.Competencia;
import server.hibernate.dao.CompetenciaDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FiltrarVagas {

    public static String filtrarVagas(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "filtrarVagas");
        int status = 422;
        String mensagem = "Erro ao filtrar Vagas";

        try {
            if (!receivedData.containsKey("token")) {
                mensagem = "Token não fornecido";
                response.put("mensagem", mensagem);
                response.put("status", status);
                return gson.toJson(response);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> filtros = (Map<String, Object>) receivedData.get("filtros");
            @SuppressWarnings("unchecked")
            List<String> competencias = (List<String>) filtros.get("competencias");
            String tipo = (String) filtros.get("tipo");

            CompetenciaDao competenciaDao = new CompetenciaDao();
            VagaDao vagaDao = new VagaDao();
            EmpresaDao empresaDao = new EmpresaDao();

            List<Competencia> competenciasList = competencias.stream()
                    .map(competenciaDao::getByName)
                    .toList();
            List<Integer> competenciasIds = competenciasList.stream()
                    .map(Competencia::getId)
                    .toList();

            List<Vaga> vagas;

            if ("AND".equalsIgnoreCase(tipo)) {
                vagas = vagaDao.getVagasByCompetenciasAnd(competenciasIds);
            } else {
                vagas = vagaDao.getVagasByCompetenciasOr(competenciasIds);
            }

            List<Map<String, Object>> vagasMap = vagas.stream().map(vaga -> {
                Map<String, Object> vagaMap = new HashMap<>();
                vagaMap.put("idVaga", vaga.getId());
                vagaMap.put("nome", vaga.getNome());
                vagaMap.put("faixaSalarial", vaga.getFaixaSalarial());
                vagaMap.put("descricao", vaga.getDescricao());
                vagaMap.put("estado", vaga.getEstado());
                vagaMap.put("email", empresaDao.get(vaga.getIdEmpresa().getId()).getEmail());
                List<String> competenciasNomes = vaga.getRequisitos().stream()
                        .map(requisito -> {
                            Competencia competencia = competenciaDao.get(requisito.getIdCompetencia().getId());
                            return competencia.getNome();
                        })
                        .collect(Collectors.toList());
                vagaMap.put("competencias", competenciasNomes);
                return vagaMap;
            }).collect(Collectors.toList());

            response.put("vagas", vagasMap);
            status = 201;
            mensagem = "Vagas filtradas com sucesso";
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
