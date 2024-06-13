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

public class CadastrarVaga
{
    @javafx.fxml.FXML
    private TextField nomeInput;
    @javafx.fxml.FXML
    private TextField faixaSalarialInput;
    @javafx.fxml.FXML
    private TextField estadoInput;
    @javafx.fxml.FXML
    private TextField descricaoInput;
    @javafx.fxml.FXML
    private TextField competenciasInput;
    @javafx.fxml.FXML
    private Button btnVoltar;
    @javafx.fxml.FXML
    private Button btnCadastrar;
    @javafx.fxml.FXML
    private Label errorLabel;
    @javafx.fxml.FXML
    private Label titleLabel;

    private int idVaga = 0;

    public void setIdVaga(int idVaga) {
        this.idVaga = idVaga;
    }

    public void setNomeInput(String nome) {
        this.nomeInput.setText(nome);
    }

    public void initVagaDetails() {
        if (idVaga > 0) {
            try {
                fetchVagaDetails(idVaga);
                titleLabel.setText("Atualizar Vaga");
                btnCadastrar.setText("Atualizar");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void fetchVagaDetails(int idVaga) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "visualizarVaga");
        data.put("idVaga", idVaga);
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try {
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao") != null && receivedData.get("operacao").equals("visualizarVaga")) {
                faixaSalarialInput.setText(String.valueOf(receivedData.get("faixaSalarial")));
                estadoInput.setText(receivedData.get("estado").toString());
                descricaoInput.setText(receivedData.get("descricao").toString());
                competenciasInput.setText(String.join(",", (List<String>) receivedData.get("competencias")));
            } else if (status == 422) {
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao visualizar vaga");
                errorLabel.setOpacity(1);
                errorLabel.setText(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao visualizar vaga");
            } else {
                System.out.println("Erro ao visualizar vaga");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @javafx.fxml.FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void sendCadastrarVagaRequest() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "cadastrarVaga");
        data.put("nome", nomeInput.getText());
        data.put("email", Session.getInstance().getEmail());
        data.put("faixaSalarial", Double.parseDouble(faixaSalarialInput.getText()));
        data.put("descricao", descricaoInput.getText());
        data.put("estado", estadoInput.getText());

        List<String> competencias = Arrays.asList(competenciasInput.getText().split(","));
        data.put("competencias", competencias);

        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try{
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao")!=null && receivedData.get("operacao").equals("cadastrarVaga")
            ) {
                System.out.println("Vaga cadastrada com sucesso");
                errorLabel.setOpacity(1);
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Vaga cadastrada com sucesso");
            } else if (status == 422) {
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao cadastrar vaga");
                errorLabel.setOpacity(1);
                errorLabel.setText(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao cadastrar vaga");
            } else {
                System.out.println("Erro ao cadastrar vaga");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendAtualizarVagaRequest() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "atualizarVaga");
        data.put("idVaga", idVaga);
        data.put("nome", nomeInput.getText());
        data.put("email", Session.getInstance().getEmail());
        data.put("faixaSalarial", Double.parseDouble(faixaSalarialInput.getText()));
        data.put("descricao", descricaoInput.getText());
        data.put("estado", estadoInput.getText());

        List<String> competencias = Arrays.asList(competenciasInput.getText().split(","));
        data.put("competencias", competencias);

        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try{
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao")!=null && receivedData.get("operacao").equals("atualizarVaga")
            ) {
                System.out.println("Vaga atualizada com sucesso");
                errorLabel.setOpacity(1);
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Vaga atualizada com sucesso");
            } else if (status == 422) {
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao atualizar vaga");
                errorLabel.setOpacity(1);
                errorLabel.setText(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao atualizar vaga");
            } else {
                System.out.println("Erro ao atualizar vaga");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @javafx.fxml.FXML
    public void onClickCadastrar(ActionEvent event) throws IOException {
        int isValid = 1;
        errorLabel.setOpacity(0);
        errorLabel.setText("");
        errorLabel.setStyle("-fx-text-fill: red;");
        nomeInput.setStyle("-fx-border-color:transparent;");
        faixaSalarialInput.setStyle("-fx-border-color:transparent;");
        estadoInput.setStyle("-fx-border-color:transparent;");
        descricaoInput.setStyle("-fx-border-color:transparent;");
        competenciasInput.setStyle("-fx-border-color:transparent;");

        if (nomeInput.getText().isEmpty()) {
            errorLabel.setText("Nome inválido");
            nomeInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (faixaSalarialInput.getText().isEmpty()) {
            errorLabel.setText("Faixa salarial inválida");
            faixaSalarialInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (estadoInput.getText().isEmpty()) {
            errorLabel.setText("Estado inválido");
            estadoInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (descricaoInput.getText().isEmpty()) {
            errorLabel.setText("Descrição inválida");
            descricaoInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (competenciasInput.getText().isEmpty()) {
            errorLabel.setText("Competências inválidas");
            competenciasInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (isValid == 0) {
            errorLabel.setOpacity(1);
        } else {
           if (idVaga > 0) {
                try {
                    sendAtualizarVagaRequest();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    sendCadastrarVagaRequest();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}