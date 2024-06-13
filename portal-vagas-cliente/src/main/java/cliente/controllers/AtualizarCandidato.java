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

public class AtualizarCandidato {
    @javafx.fxml.FXML
    private TextField senhaInput;
    @javafx.fxml.FXML
    private TextField nomeInput;
    @javafx.fxml.FXML
    private Label errorLabel;
    @javafx.fxml.FXML
    private Button btnAtualizar;
    @javafx.fxml.FXML
    private Button btnVoltar;

    @javafx.fxml.FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("candidatoHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void onClickAtualizar(ActionEvent event) throws Exception {
        int isValid = 1;
        errorLabel.setOpacity(0);
        errorLabel.setText("");
        nomeInput.setStyle("-fx-border-color:transparent;");
        senhaInput.setStyle("-fx-border-color:transparent;");

        if (!senhaInput.getText().matches("^\\d+$") || senhaInput.getText().length() < 3 || senhaInput.getText().length() > 8) {
            errorLabel.setText("Senha inválida");
            nomeInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (nomeInput.getText().length() < 6 || nomeInput.getText().length() > 30) {
            errorLabel.setText("Nome inválido");
            nomeInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (nomeInput.getText().isEmpty()) {
            nomeInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }
        if (senhaInput.getText().isEmpty()) {
            senhaInput.setStyle("-fx-border-color:red;");
            isValid = 0;
        }

        if (isValid == 0) {
            errorLabel.setText("Preencha todos os campos!");
            errorLabel.setOpacity(1);
        } else {
            errorLabel.setOpacity(0);

            SingletonEchoClient client = SingletonEchoClient.getInstance();

            Map<String, Object> data = new HashMap<>();

            data.put("operacao", "atualizarCandidato");
            data.put("nome", nomeInput.getText());
            data.put("senha", senhaInput.getText());
            data.put("email", cliente.Session.getInstance().getEmail());
            data.put("token", cliente.Session.getInstance().getToken());

            client.sendJson(data);
            String response = client.listenToServer();

            try{
                Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

                Object statusObject = receivedData.get("status");
                int status = StatusUtil.getStatus(statusObject);

                if(status == 201){
                    System.out.println("Atualizado com sucesso");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("candidatoHome.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) btnAtualizar.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else if (status == 404) {
                    System.out.println("E-mail não encontrado");
                    errorLabel.setText("E-mail não encontrado");
                    errorLabel.setOpacity(1);
                }else {
                    System.out.println("Erro ao atualizar candidato");
                    errorLabel.setText("Erro ao atualizar candidato");
                    errorLabel.setOpacity(1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setNome(String nome) {
        nomeInput.setText(nome);
    }

    public void setSenha(String senha) {
        senhaInput.setText(senha);
    }

}
