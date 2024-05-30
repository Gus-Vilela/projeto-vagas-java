package server.controllers.Empresa;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.models.Empresa;

import java.util.HashMap;
import java.util.Map;

public class AtualizarEmpresa {
    public static String atualizarEmpresa(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "atualizarEmpresa");
        int status = 404;

        String email = (String) receivedData.get("email");
        String cnpj = (String) receivedData.get("cnpj");
        String senha = (String) receivedData.get("senha");
        String descricao = (String) receivedData.get("descricao");
        String razaoSocial = (String) receivedData.get("razaoSocial");
        String ramo = (String) receivedData.get("ramo");

        EmpresaDao empresaDao = new EmpresaDao();
        Empresa empresa = empresaDao.getByEmail(email);

        if (empresa == null) {
            response.put("mensagem", "E-mail não encontrado");
            response.put("status", status);
            return gson.toJson(response);
        }
        empresa.setCnpj(cnpj);
        empresa.setSenha(senha);
        empresa.setDescricao(descricao);
        empresa.setRazaoSocial(razaoSocial);
        empresa.setRamo(ramo);

        boolean atualizacaoSuccessful = performAtualizacao(empresa);

        if (atualizacaoSuccessful) {
            status = 201;
        }
        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performAtualizacao(Empresa empresa) {
        try{
            EmpresaDao empresaDao = new EmpresaDao();
            empresaDao.update(empresa);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}