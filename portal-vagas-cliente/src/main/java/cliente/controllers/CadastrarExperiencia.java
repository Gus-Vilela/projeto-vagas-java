package cliente.controllers;

import cliente.Session;
import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class CadastrarExperiencia
{
    @javafx.fxml.FXML
    private TextField nomeInput;
    @javafx.fxml.FXML
    private Button btnVoltar;
    @javafx.fxml.FXML
    private Button btnCadastrar;
    @javafx.fxml.FXML
    private Label errorLabel;
    @javafx.fxml.FXML
    private Spinner<Integer> tempoInput;

    @javafx.fxml.FXML
    public void initialize() {
        tempoInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    }

    @javafx.fxml.FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("candidatoHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void onClickCadastrar(ActionEvent event) throws IOException {
        int isValid = 1;
        errorLabel.setOpacity(0);
        errorLabel.setText("");
        errorLabel.setStyle("-fx-text-fill: red;");
        nomeInput.setStyle("-fx-border-color:transparent;");
        tempoInput.setStyle("-fx-border-color:transparent;");

        if (nomeInput.getText().isEmpty()) {
            errorLabel.setText("Nome inválido");
            nomeInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (tempoInput.getValue() == 0) {
            errorLabel.setText("Tempo inválido");
            tempoInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (isValid == 0) {
            errorLabel.setOpacity(1);
        } else {
            SingletonEchoClient client = SingletonEchoClient.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("operacao", "cadastrarCompetenciaExperiencia");
            data.put("email", Session.getInstance().getEmail());

            List<Map<String, Object>> competenciaExperiencia = new ArrayList<>();
            Map<String, Object> competenciaExperienciaMap = new HashMap<>();
            competenciaExperienciaMap.put("competencia", nomeInput.getText());
            competenciaExperienciaMap.put("experiencia", tempoInput.getValue());
            competenciaExperiencia.add(competenciaExperienciaMap);

            data.put("competenciaExperiencia", competenciaExperiencia);
            data.put("token", Session.getInstance().getToken());

            client.sendJson(data);
            String response = client.listenToServer();

            try{
                Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

                Object statusObject = receivedData.get("status");
                int status = StatusUtil.getStatus(statusObject);

                if (status == 201 && receivedData.get("operacao")!=null && receivedData.get("operacao").equals("cadastrarCompetenciaExperiencia")
                ) {
                    System.out.println("Competencia/Experiencia cadastrada com sucesso");
                    errorLabel.setOpacity(1);
                    errorLabel.setStyle("-fx-text-fill: green;");
                    errorLabel.setText("Competencia/Experiencia cadastrada com sucesso");
                } else if (status == 422) {
                    System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao cadastrar competencia/experiencia");
                    errorLabel.setOpacity(1);
                    errorLabel.setText(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao cadastrar competencia/experiencia");
                } else {
                    System.out.println("Erro ao cadastrar competencia/experiencia");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}