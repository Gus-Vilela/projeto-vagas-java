package cliente.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
    public static Map<String, Object> parseJsonToMap(String json) throws Exception {
        try{
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new Exception("Erro no formato da requisição");
        }
    }
}
