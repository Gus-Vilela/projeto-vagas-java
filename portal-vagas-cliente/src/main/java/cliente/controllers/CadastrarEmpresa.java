package cliente.controllers;

import cliente.Session;
import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CadastrarEmpresa
{
    @javafx.fxml.FXML
    private TextField senhaInput;
    @javafx.fxml.FXML
    private TextField razaoSocialInput;
    @javafx.fxml.FXML
    private Button btnCadastrar;
    @javafx.fxml.FXML
    private TextField cnpjInput;
    @javafx.fxml.FXML
    private TextField descricaoInput;
    @javafx.fxml.FXML
    private Label errorLabel;
    @javafx.fxml.FXML
    private TextField ramoInput;
    @javafx.fxml.FXML
    private TextField emailInput;
    @FXML
    private Button btnVoltar;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void onClickCadastrar() throws IOException {
        int isValid = 1;
        errorLabel.setOpacity(0);
        errorLabel.setText("");
        emailInput.setStyle("-fx-border-color:transparent;");
        senhaInput.setStyle("-fx-border-color:transparent;");
        cnpjInput.setStyle("-fx-border-color:transparent;");
        razaoSocialInput.setStyle("-fx-border-color:transparent;");
        descricaoInput.setStyle("-fx-border-color:transparent;");
        ramoInput.setStyle("-fx-border-color:transparent;");


        if (!emailInput.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || emailInput.getText().length() < 7 || emailInput.getText().length() > 50) {
            errorLabel.setText("E-mail inválido");
            emailInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (!senhaInput.getText().matches("^\\d+$") || senhaInput.getText().length() < 3 || senhaInput.getText().length() > 8) {
            errorLabel.setText("Senha inválida");
            senhaInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (cnpjInput.getText().isEmpty()) {
            cnpjInput.setStyle("-fx-border-color:red;");
            errorLabel.setText("Preencha todos os campos");
            isValid = 0;
        }
        if (emailInput.getText().isEmpty()) {
            emailInput.setStyle("-fx-border-color:red;");
            errorLabel.setText("Preencha todos os campos");
            isValid = 0;
        }
        if (senhaInput.getText().isEmpty()) {
            senhaInput.setStyle("-fx-border-color:red;");
            errorLabel.setText("Preencha todos os campos");
            isValid = 0;
        }

        if (razaoSocialInput.getText().isEmpty()) {
            razaoSocialInput.setStyle("-fx-border-color:red;");
            errorLabel.setText("Preencha todos os campos");
            isValid = 0;
        }

        if (descricaoInput.getText().isEmpty()) {
            descricaoInput.setStyle("-fx-border-color:red;");
            errorLabel.setText("Preencha todos os campos");
            isValid = 0;
        }

        if (ramoInput.getText().isEmpty()) {
            ramoInput.setStyle("-fx-border-color:red;");
            errorLabel.setText("Preencha todos os campos");
            isValid = 0;
        }

        if (isValid == 0) {
            errorLabel.setOpacity(1);
        } else {
            errorLabel.setOpacity(0);
            SingletonEchoClient client = SingletonEchoClient.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("operacao", "cadastrarEmpresa");
            data.put("email", emailInput.getText());
            data.put("senha", senhaInput.getText());
            data.put("cnpj", cnpjInput.getText());
            data.put("razaoSocial", razaoSocialInput.getText());
            data.put("descricao", descricaoInput.getText());
            data.put("ramo", ramoInput.getText());

            client.sendJson(data);
            String response = client.listenToServer();

            try{
                Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

                Object statusObject = receivedData.get("status");
                int status = StatusUtil.getStatus(statusObject);

                if (status == 201 && receivedData.get("operacao")!=null && receivedData.get("operacao").equals("cadastrarEmpresa") &&
                        receivedData.get("token") != null
                ) {
                    System.out.println("Empresa cadastrada com sucesso");

                    Session.getInstance().setToken((String) receivedData.get("token"));
                    Session.getInstance().setEmail(emailInput.getText());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnCadastrar.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                } else if (status == 422 && receivedData.get("mensagem") != null){
                    if (receivedData.get("mensagem").equals("E-mail já cadastrado")){
                        System.out.println("E-mail já cadastrado");
                        emailInput.setStyle("-fx-border-color:red;");
                        errorLabel.setText("E-mail já cadastrado");
                        errorLabel.setOpacity(1);
                    }else if (receivedData.get("mensagem").equals("CNPJ já cadastrado")){
                        System.out.println("CNPJ já cadastrado");
                        cnpjInput.setStyle("-fx-border-color:red;");
                        errorLabel.setText("CNPJ já cadastrado");
                        errorLabel.setOpacity(1);
                    }else {
                        System.out.println("Erro ao cadastrar empresa");
                        errorLabel.setText("Erro ao cadastrar empresa");
                        errorLabel.setOpacity(1);
                    }
                } else {
                    System.out.println("Erro ao cadastrar empresa");
                    errorLabel.setText("Erro ao cadastrar empresa");
                    errorLabel.setOpacity(1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    public void onClickVoltar(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginEmpresa.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}