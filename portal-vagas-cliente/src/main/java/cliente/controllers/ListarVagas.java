package cliente.controllers;

import cliente.Session;
import cliente.models.VagaModel;
import cliente.socket.SingletonEchoClient;
import cliente.utils.ConversionUtil;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarVagas {
    @FXML
    private TableView<VagaModel> vagasTable;
    @FXML
    private TableColumn<VagaModel, Integer> idVagaColumn;
    @FXML
    private TableColumn<VagaModel, String> nomeColumn;
    @FXML
    private TableColumn<VagaModel, Button> editColumn;
    @FXML
    private TableColumn<VagaModel, Button> deleteColumn;
    @FXML
    private Button btnVoltar;
    private List<Map<String, Object>> vagasList;

    @FXML
    public void initialize() {
        try {
            fetchVagas();
            loadTableData();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        idVagaColumn.setCellValueFactory(new PropertyValueFactory<>("idVaga"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("btnEditar"));
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("btnDeletar"));
    }

    private String sendListarVagasRequest() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "listarVagas");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        return client.listenToServer();
    }

    private void fetchVagas() throws IOException {
        try {
            final String response = sendListarVagasRequest();
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (!receivedData.containsKey("operacao") || !receivedData.get("operacao").equals("listarVagas")) {
                System.out.println("Operação não permitida");
                return;
            }

            if (status == 422){
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao listar vagas");
            }
            else if (status == 201 && receivedData.containsKey("vagas")) {
                System.out.println("Vagas listadas com sucesso");
                List<Map<String, Object>> vagasList = (List<Map<String, Object>>) receivedData.get("vagas");
                setVagasList(vagasList);
            }
            else {
                System.out.println("Erro ao listar vagas");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String sendApagarVagaRequest(VagaModel vaga) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();

        data.put("operacao", "apagarVaga");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());
        data.put("idVaga", vaga.getIdVaga());

        client.sendJson(data);
        return client.listenToServer();
    }

    private void onClickApagarVaga(VagaModel vaga) throws IOException {
        try {
            final String response = sendApagarVagaRequest(vaga);

            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (!receivedData.containsKey("operacao") || !receivedData.get("operacao").equals("apagarVaga")) {
                System.out.println("Operação não permitida");
                return;
            }
            if (status == 422){
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao apagar vaga");
            }
            else if (status == 201){
                System.out.println("Vaga apagada com sucesso");
                vagasTable.getItems().remove(vaga);
            }
            else {
                System.out.println("Erro ao apagar vaga");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onClickEditarVaga(VagaModel vaga) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cadastrarVaga.fxml"));

        Parent root = loader.load();
        CadastrarVaga controller = loader.getController();

        controller.setIdVaga(vaga.getIdVaga());
        controller.setNomeInput(vaga.getNome());
        controller.initVagaDetails();

        Stage stage = (Stage) vagasTable.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadTableData() {
        ObservableList<VagaModel> tableData = FXCollections.observableArrayList();

        for (Map<String, Object> map : vagasList) {
            Integer idVaga = ConversionUtil.convertToInteger(map.get("idVaga"));
            String nome = (String) map.get("nome");
            Button edit = new Button("Editar");
            Button delete = new Button("Deletar");

            VagaModel vagaModel = new VagaModel(idVaga, nome, edit, delete);

            edit.setOnAction(event -> {
                try {
                    onClickEditarVaga(vagaModel);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
            delete.setOnAction(event -> {
                try {
                    onClickApagarVaga(vagaModel);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });

            tableData.add(vagaModel);
        }

        vagasTable.setItems(tableData);
    }

    public void setVagasList(List<Map<String, Object>> vagasList) {
        this.vagasList = vagasList;
    }

    @FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
