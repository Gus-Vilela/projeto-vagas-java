package cliente.controllers;

import cliente.Session;
import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApagarEmpresa
{
    @javafx.fxml.FXML
    private Button btnCancelar;
    @javafx.fxml.FXML
    private Button btnDeletar;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void onClickDeletar(ActionEvent actionEvent) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();

        data.put("operacao", "apagarEmpresa");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try{
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if(status == 201){
                System.out.println("Cadastro deletado com sucesso");
                Session.endSession();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("loginEmpresa.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnDeletar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else if(status == 404){
                System.out.println("Erro ao deletar cadastro");
            } else {
                System.out.println("Erro ao deletar cadastro");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    @javafx.fxml.FXML
    public void onClickCancelar(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}