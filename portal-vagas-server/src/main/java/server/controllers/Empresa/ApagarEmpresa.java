package server.controllers.Empresa;

import com.google.gson.Gson;
import server.hibernate.dao.EmpresaDao;
import server.hibernate.models.Empresa;

import java.util.HashMap;
import java.util.Map;

public class ApagarEmpresa {
    public static String apagarEmpresa(Map<String, Object> receivedData, Gson gson) {
        Map<String, Object> response = new HashMap<>();
        response.put("operacao", "apagarEmpresa");
        int status = 404;

        String email = (String) receivedData.get("email");

        EmpresaDao empresaDao = new EmpresaDao();
        Empresa empresa = empresaDao.getByEmail(email);

        if (empresa == null) {
            response.put("mensagem", "E-mail não encontrado");
            response.put("status", status);
            return gson.toJson(response);
        }

        boolean apagarSuccessful = performApagar(empresa);

        if (apagarSuccessful) {
            status = 201;
        }

        response.put("status", status);
        return gson.toJson(response);
    }

    private static boolean performApagar(Empresa empresa) {
        try {
            EmpresaDao empresaDao = new EmpresaDao();
            empresaDao.delete(empresa);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}