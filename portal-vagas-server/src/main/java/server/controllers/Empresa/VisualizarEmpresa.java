package server.controllers.Empresa;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.models.Empresa;

import java.util.HashMap;
import java.util.Map;

public class VisualizarEmpresa {
    public static String visualizarEmpresa(Map<String, Object> receivedData, Gson gson) {
        String email = (String) receivedData.get("email");
        System.out.println("Email: " + email);
        EmpresaDao empresaDao = new EmpresaDao();
        Empresa empresa = empresaDao.getByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "visualizarEmpresa");
        int status = 404;

        if (empresa != null) {
            status = 201;
            response.put("cnpj", empresa.getCnpj());
            response.put("senha", empresa.getSenha());
            response.put("descricao", empresa.getDescricao());
            response.put("razaoSocial", empresa.getRazaoSocial());
            response.put("ramo", empresa.getRamo());
        } else {
            response.put("mensagem", "E-mail não encontrado");
        }

        response.put("status", status);
        return gson.toJson(response);
    }
}
