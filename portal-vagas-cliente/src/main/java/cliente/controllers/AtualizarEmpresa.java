package cliente.controllers;

import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.event.ActionEvent;
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

public class AtualizarEmpresa
{
    @javafx.fxml.FXML
    private TextField senhaInput;
    @javafx.fxml.FXML
    private TextField razaoSocialInput;
    @javafx.fxml.FXML
    private Button btnVoltar;
    @javafx.fxml.FXML
    private TextField cnpjInput;
    @javafx.fxml.FXML
    private TextField descricaoInput;
    @javafx.fxml.FXML
    private Label errorLabel;
    @javafx.fxml.FXML
    private TextField ramoInput;
    @javafx.fxml.FXML
    private Button btnAtualizar;

    @javafx.fxml.FXML
    public void initialize() {
    }

    public void setDescricao(String descricao) {
        descricaoInput.setText(descricao);
    }

    public void setSenha(String senha) {
        senhaInput.setText(senha);
    }

    public void setRazaoSocial(String razaoSocial) {
        razaoSocialInput.setText(razaoSocial);
    }

    public void setCnpj(String cnpj) {
        cnpjInput.setText(cnpj);
    }

    public void setRamo(String ramo) {
        ramoInput.setText(ramo);
    }



    @javafx.fxml.FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Deprecated
    public void onClickCadastrar(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void onClickAtualizar(ActionEvent actionEvent) throws IOException {
        int isValid = 1;
        errorLabel.setOpacity(0);
        errorLabel.setText("");
        senhaInput.setStyle("-fx-border-color:transparent;");
        cnpjInput.setStyle("-fx-border-color:transparent;");
        razaoSocialInput.setStyle("-fx-border-color:transparent;");
        descricaoInput.setStyle("-fx-border-color:transparent;");
        ramoInput.setStyle("-fx-border-color:transparent;");

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

            data.put("operacao", "atualizarEmpresa");
            data.put("email", cliente.Session.getInstance().getEmail());
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

                if(status == 201){
                    System.out.println("Atualizado com sucesso");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnAtualizar.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else if (status == 404) {
                    System.out.println("E-mail não encontrado");
                    errorLabel.setText("E-mail não encontrado");
                    errorLabel.setOpacity(1);
                }else {
                    System.out.println("Erro ao atualizar empresa");
                    errorLabel.setText("Erro ao atualizar empresa");
                    errorLabel.setOpacity(1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}