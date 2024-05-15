package cliente.controllers;

import cliente.Session;
import cliente.socket.SingletonEchoClient;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CandidatoHome
{
    @javafx.fxml.FXML
    private Button btnMeuCadastro;
    @javafx.fxml.FXML
    private Button btnLogout;
    @javafx.fxml.FXML
    private Button btnEditarCadastro;
    @javafx.fxml.FXML
    private Button btnApagarCadastro;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void onClickMeuCadastro() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "visualizarCandidato");
        data.put("email", Session.getInstance().getEmail());

        client.sendJson(data);
        String response = client.listenToServer();

        try {
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");

            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao").equals("visualizarCandidato")
            ) {
                String email = Session.getInstance().getEmail();
                String nome = (String) receivedData.get("nome");
                String senha = (String) receivedData.get("senha");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("visualizarCandidato.fxml"));
                Parent root = loader.load();
                VisualizarCandidato controller = loader.getController();

                controller.setNome(nome);
                controller.setEmail(email);
                controller.setSenha(senha);

                Stage stage = (Stage) btnMeuCadastro.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else if (status == 404) {
                System.out.println("E-mail não encontrado");
            } else {
                System.out.println("Erro ao visualizar candidato");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void onClickAtualizarCadastro() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "visualizarCandidato");
        data.put("email", Session.getInstance().getEmail());

        client.sendJson(data);
        String response = client.listenToServer();

        try {
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");

            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao")!= null && receivedData.get("operacao").equals("visualizarCandidato") &&
                    receivedData.get("nome") != null && receivedData.get("senha") != null
            ) {
                String nome = (String) receivedData.get("nome");
                String senha = (String) receivedData.get("senha");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("atualizarCandidato.fxml"));
                Parent root = loader.load();
                AtualizarCandidato controller = loader.getController();

                controller.setNome(nome);
                controller.setSenha(senha);

                Stage stage = (Stage) btnEditarCadastro.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else if (status == 404) {
                System.out.println("E-mail não encontrado");
            } else {
                System.out.println("Erro ao visualizar candidato");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void onClickApagarCadastro() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("apagarCandidato.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnApagarCadastro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void onClickLogout() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "logout");
        data.put("token" , Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try {
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (status == 204) {
                System.out.println("Logout efetuado com sucesso");
                client.close();
                Session.endSession();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("conexaoServidor.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                System.out.println("Erro ao efetuar logout");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}