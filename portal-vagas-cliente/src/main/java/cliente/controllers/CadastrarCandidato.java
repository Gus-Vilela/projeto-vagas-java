package cliente.controllers;

import cliente.Session;
import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.google.gson.reflect.TypeToken;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CadastrarCandidato
{
    @javafx.fxml.FXML
    private TextField senhaInput;
    @javafx.fxml.FXML
    private TextField nomeInput;
    @javafx.fxml.FXML
    private Button btnCadastrar;
    @FXML
    private TextField emailInput;
    @FXML
    private Label errorLabel;
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
        nomeInput.setStyle("-fx-border-color:transparent;");
        emailInput.setStyle("-fx-border-color:transparent;");
        senhaInput.setStyle("-fx-border-color:transparent;");

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

        if (nomeInput.getText().length() < 6 || nomeInput.getText().length() > 30) {
            errorLabel.setText("Nome inválido");
            nomeInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (nomeInput.getText().isEmpty()) {
            nomeInput.setStyle("-fx-border-color:red;");
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

        if (isValid == 0) {
            errorLabel.setOpacity(1);
        } else {
            errorLabel.setOpacity(0);
            SingletonEchoClient client = SingletonEchoClient.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("operacao", "cadastrarCandidato");
            data.put("nome", nomeInput.getText());
            data.put("email", emailInput.getText());
            data.put("senha", senhaInput.getText());

            client.sendJson(data);
            String response = client.listenToServer();

            try{
                Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

                Object statusObject = receivedData.get("status");
                int status = StatusUtil.getStatus(statusObject);

                if (status == 201 && receivedData.get("operacao")!=null && receivedData.get("operacao").equals("cadastrarCandidato") &&
                        receivedData.get("token") != null
                ) {
                    System.out.println("Candidato cadastrado com sucesso");

                    Session.getInstance().setToken((String) receivedData.get("token"));
                    Session.getInstance().setEmail(emailInput.getText());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("candidatoHome.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnCadastrar.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                } else if (status == 404) {
                    System.out.println("Erro ao cadastrar candidato");
                    errorLabel.setText("Erro ao cadastrar candidato");
                    errorLabel.setOpacity(1);
                }  else if (status == 422) {
                    System.out.println("E-mail já cadastrado");
                    emailInput.setStyle("-fx-border-color:red;");
                    errorLabel.setText("E-mail já cadastrado");
                    errorLabel.setOpacity(1);
                } else {
                    System.out.println("Erro ao cadastrar candidato");
                    errorLabel.setText("Erro ao cadastrar candidato");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginCandidato.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


