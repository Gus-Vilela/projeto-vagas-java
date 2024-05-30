package server.controllers.Empresa;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.models.Empresa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadastrarEmpresa {
    public static String cadastrarEmpresa(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "cadastrarEmpresa");
        int status = 422;

        String cnpj = (String) receivedData.get("cnpj");
        String email = (String) receivedData.get("email");
        String senha = (String) receivedData.get("senha");
        String descricao = (String) receivedData.get("descricao");
        String razaoSocial = (String) receivedData.get("razaoSocial");
        String ramo = (String) receivedData.get("ramo");

        EmpresaDao empresaDao = new EmpresaDao();

        Empresa empresa = empresaDao.getByEmail(email);

        if(empresa != null){
            response.put("mensagem", "E-mail já cadastrado");
            response.put("status", status);
            return gson.toJson(response);
        }

        empresa = empresaDao.getByCnpj(cnpj);

        if(empresa != null){
            response.put("mensagem", "CNPJ já cadastrado");
            response.put("status", status);
            return gson.toJson(response);
        }

        boolean cadastroSuccessful = performCadastro(cnpj, email, senha, descricao, razaoSocial, ramo);

        if (cadastroSuccessful) {
            status = 201;
            response.put("token", UUID.randomUUID().toString());
        }else {
            response.put("mensagem", "");
        }

        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performCadastro(String cnpj, String email, String senha, String descricao, String razaoSocial, String ramo) {
       try{
           EmpresaDao empresaDao = new EmpresaDao();
           Empresa empresa = new Empresa(razaoSocial, ramo, descricao, senha, email, cnpj);
           empresaDao.save(empresa);
           return true;
       } catch (Exception e) {
           return false;
       }
    }
}
