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

public class EmpresaHome {
    @javafx.fxml.FXML
    private Button btnLogout;
    @javafx.fxml.FXML
    private Button btnEditarCadastro;
    @javafx.fxml.FXML
    private Button btnMeuCadastro;
    @javafx.fxml.FXML
    private Button btnApagarCadastro;
    @javafx.fxml.FXML
    private Button btnCadastrarVaga;
    @javafx.fxml.FXML
    private Button btnListarVagas;

    @javafx.fxml.FXML
    public void onClickApagarCadastro(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("apagarEmpresa.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnApagarCadastro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void onClickCadastrarVaga(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cadastrarVaga.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnCadastrarVaga.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void onClickAtualizarCadastro(ActionEvent actionEvent) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "visualizarEmpresa");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try {
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");

            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao").equals("visualizarEmpresa")
            ) {
                String cnpj = (String) receivedData.get("cnpj");
                String senha = (String) receivedData.get("senha");
                String descricao = (String) receivedData.get("descricao");
                String razaoSocial = (String) receivedData.get("razaoSocial");
                String ramo = (String) receivedData.get("ramo");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("atualizarEmpresa.fxml"));
                Parent root = loader.load();
                AtualizarEmpresa controller = loader.getController();

                controller.setCnpj(cnpj);
                controller.setSenha(senha);
                controller.setDescricao(descricao);
                controller.setRazaoSocial(razaoSocial);
                controller.setRamo(ramo);

                Stage stage = (Stage) btnEditarCadastro.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else if (status == 404) {
                System.out.println("E-mail não encontrado");
            } else {
                System.out.println("Erro ao visualizar empresa");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @javafx.fxml.FXML
    public void onClickLogout(ActionEvent actionEvent) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "logout");
        data.put("token", Session.getInstance().getToken());

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

    @javafx.fxml.FXML
    public void onClickMeuCadastro(ActionEvent actionEvent) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "visualizarEmpresa");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        String response = client.listenToServer();

        try {
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");

            int status = StatusUtil.getStatus(statusObject);

            if (status == 201 && receivedData.get("operacao").equals("visualizarEmpresa")
            ) {
                String email = Session.getInstance().getEmail();
                String cnpj = (String) receivedData.get("cnpj");
                String senha = (String) receivedData.get("senha");
                String descricao = (String) receivedData.get("descricao");
                String razaoSocial = (String) receivedData.get("razaoSocial");
                String ramo = (String) receivedData.get("ramo");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("visualizarEmpresa.fxml"));
                Parent root = loader.load();
                VisualizarEmpresa controller = loader.getController();

                controller.setCnpj(cnpj);
                controller.setEmail(email);
                controller.setSenha(senha);
                controller.setDescricao(descricao);
                controller.setRazaoSocial(razaoSocial);
                controller.setRamo(ramo);

                Stage stage = (Stage) btnMeuCadastro.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else if (status == 404) {
                System.out.println("E-mail não encontrado");
            } else {
                System.out.println("Erro ao visualizar empresa");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void onClickListarVagas(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listarVagas.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnListarVagas.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
