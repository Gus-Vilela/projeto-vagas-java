package server.controllers.Vaga;

import com.google.gson.Gson;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.dao.VagaDao;
import server.hibernate.models.Candidato;
import server.hibernate.models.Empresa;
import server.hibernate.models.Vaga;
import server.utils.ConversionUtil;

import java.util.HashMap;
import java.util.Map;

public class ApagarVaga {
    public static String apagarVaga(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "apagarVaga");
        int status = 422;
        String mensagem = "Erro ao apagar vaga";

        try {
            if (!receivedData.containsKey("token")) {
                mensagem = "Token não fornecido";
                response.put("mensagem", mensagem);
                response.put("status", status);
                return gson.toJson(response);
            }

            if(!receivedData.containsKey("email")){
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

            vagaDao.delete(vaga);

            status = 201;
            mensagem = "Vaga apagada com sucesso";
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
