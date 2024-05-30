package cliente.controllers;

import cliente.Session;
import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginEmpresa {
    @FXML
    public TextField usuarioInput;
    @FXML
    public PasswordField senhaInput;
    @FXML
    public Button btnLogin;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Label errorLabel;

    @FXML
    public void onClickCadastrar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cadastrarEmpresa.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnCadastrar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onClickLogin(ActionEvent event) throws IOException {
        int isValid = 1;
        errorLabel.setOpacity(0);

        if (usuarioInput.getText().isEmpty()) {
            usuarioInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        } else {
            usuarioInput.setStyle("-fx-border-color:transparent;");
        }
        if (senhaInput.getText().isEmpty()) {
            senhaInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        } else {
            senhaInput.setStyle("-fx-border-color:transparent;");
        }

        if (isValid == 0) {
            errorLabel.setText("Preencha todos os campos!");
            errorLabel.setOpacity(1);
        } else {
            errorLabel.setOpacity(0);
            SingletonEchoClient client = SingletonEchoClient.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("operacao", "loginEmpresa");
            data.put("email", usuarioInput.getText());
            data.put("senha", senhaInput.getText());

            client.sendJson(data);
            String response = client.listenToServer();

            try {
                Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

                Object statusObject = receivedData.get("status");

                int status = StatusUtil.getStatus(statusObject);

                if (
                        status == 200 && receivedData.get("operacao")!=null &&
                                receivedData.get("operacao").equals("loginEmpresa") &&
                                receivedData.get("token") != null){
                    System.out.println("Login efetuado com sucesso");

                    Session.getInstance().setToken((String) receivedData.get("token"));
                    Session.getInstance().setEmail(usuarioInput.getText());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else if (status == 401) {
                    System.out.println("Login ou senha icorretos");
                    usuarioInput.setStyle("-fx-border-color:red;");
                    senhaInput.setStyle("-fx-border-color:red;");
                    errorLabel.setText("Login ou senha incorretos");
                    errorLabel.setOpacity(1);
                } else {
                    System.out.println("Erro ao efetuar login");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

//    @Deprecated
//    public void onClickVoltar(ActionEvent actionEvent) throws IOException {
//        SingletonEchoClient client = SingletonEchoClient.getInstance();
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("operacao", "logout");
//        data.put("token" , UUID.randomUUID().toString());
//
//        client.sendJson(data);
//        String response = client.listenToServer();
//
//        try {
//            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);
//
//            Object statusObject = receivedData.get("status");
//            int status = StatusUtil.getStatus(statusObject);
//
//            if (status == 204) {
//                System.out.println("Logout efetuado com sucesso");
//                client.close();
//                Session.endSession();
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("conexaoServidor.fxml"));
//                Parent root = loader.load();
//                Stage stage = (Stage) btnVoltar.getScene().getWindow();
//                stage.setScene(new Scene(root));
//                stage.show();
//            } else {
//                System.out.println("Erro ao efetuar logout");
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

}
