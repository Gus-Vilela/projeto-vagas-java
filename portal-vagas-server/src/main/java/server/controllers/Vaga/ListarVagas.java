package server.controllers.Vaga;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.dao.VagaDao;
import server.hibernate.models.Empresa;
import server.hibernate.models.Vaga;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListarVagas {
    public static String listarVagas(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "listarVagas");
        int status = 422;
        String mensagem = "Erro ao listar vagas";

        try {
            if (!receivedData.containsKey("email") || !receivedData.containsKey("token")) {
                mensagem = "Email ou token não fornecido";
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

            VagaDao vagaDao = new VagaDao();
            List<Vaga> vagas = vagaDao.fetchAllByEmpresaId(empresa.getId());

            List<Map<String, Object>> vagaList = vagas.stream().map(vaga -> {
                Map<String, Object> item = new HashMap<>();
                item.put("idVaga", vaga.getId());
                item.put("nome", vaga.getNome());
                return item;
            }).collect(Collectors.toList());

            status = 201;
            response.put("status", status);
            response.put("vagas", vagaList);
            return gson.toJson(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("status", status);
            response.put("mensagem", mensagem);
            return gson.toJson(response);
        }
    }
}
