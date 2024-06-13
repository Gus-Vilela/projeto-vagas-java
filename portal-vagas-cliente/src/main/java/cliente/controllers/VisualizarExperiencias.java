package cliente.controllers;

import cliente.Session;
import cliente.models.ExperienciaModel;
import cliente.socket.SingletonEchoClient;
import cliente.utils.ConversionUtil;
import cliente.utils.JsonUtil;
import cliente.utils.StatusUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class VisualizarExperiencias {
    @javafx.fxml.FXML
    private TableView<ExperienciaModel> experienciasTable;
    @javafx.fxml.FXML
    private TableColumn<ExperienciaModel, String> competenciaColumn;
    @javafx.fxml.FXML
    private Button btnVoltar;
    @javafx.fxml.FXML
    private TableColumn<ExperienciaModel, Button> editColumn;
    @javafx.fxml.FXML
    private TableColumn<ExperienciaModel, Integer> experienciaColumn;
    @javafx.fxml.FXML
    private TableColumn<ExperienciaModel, Button> deleteColumn;
    private List<Map<String, Object>> competenciaExperienciaMap;

    @javafx.fxml.FXML
    public void initialize() {
        try {
            fetchExperiencias();
            loadTableData();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        competenciaColumn.setCellValueFactory(new PropertyValueFactory<>("competencia"));
        experienciaColumn.setCellValueFactory(new PropertyValueFactory<>("experiencia"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("btnEditar"));
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("btnDeletar"));
    }

    private String sendVisualizarExperienciasRequest() throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("operacao", "visualizarCompetenciaExperiencia");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());

        client.sendJson(data);
        return client.listenToServer();
    }

    private void fetchExperiencias() throws IOException {
        try {
            final String response = sendVisualizarExperienciasRequest();
            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (!receivedData.containsKey("operacao") || !receivedData.get("operacao").equals("visualizarCompetenciaExperiencia")) {
                System.out.println("Operação não permitida");
                return;
            }

            if (status == 422){
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao visualizar competências");
            }
            else if (status == 201 && receivedData.containsKey("competenciaExperiencia")) {
                System.out.println("Competências visualizadas com sucesso");
                List<Map<String, Object>> competenciaExperienciaMap = (List<Map<String, Object>>) receivedData.get("competenciaExperiencia");
                setCompetenciaExperiencia(competenciaExperienciaMap);
            }
            else {
                System.out.println("Erro ao visualizar competências");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String sendApagarExperienciaRequest(ExperienciaModel experiencia) throws IOException {
        SingletonEchoClient client = SingletonEchoClient.getInstance();

        Map<String, Object> data = new HashMap<>();

        data.put("operacao", "apagarCompetenciaExperiencia");
        data.put("email", Session.getInstance().getEmail());
        data.put("token", Session.getInstance().getToken());
        List<Map<String, Object>> competenciaExperiencia = new ArrayList<>();
        Map<String, Object> competenciaExperienciaMap = new HashMap<>();
        competenciaExperienciaMap.put("competencia", experiencia.getCompetencia());
        competenciaExperienciaMap.put("experiencia", experiencia.getExperiencia());
        competenciaExperiencia.add(competenciaExperienciaMap);

        data.put("competenciaExperiencia", competenciaExperiencia);

        client.sendJson(data);
        return client.listenToServer();
    }

    private void onClickApagarExperiencia(ExperienciaModel experiencia) throws IOException {
        try {
            final String response = sendApagarExperienciaRequest(experiencia);

            Map<String, Object> receivedData = JsonUtil.parseJsonToMap(response);

            Object statusObject = receivedData.get("status");
            int status = StatusUtil.getStatus(statusObject);

            if (!receivedData.containsKey("operacao") || !receivedData.get("operacao").equals("apagarCompetenciaExperiencia")) {
                System.out.println("Operação não permitida");
                return;
            }
            if (status == 422){
                System.out.println(receivedData.get("mensagem") != null ? receivedData.get("mensagem").toString() : "Erro ao apagar competências");
            }
            else if (status == 201){
                System.out.println("Competencia/Experiencia apagada com sucesso");
                experienciasTable.getItems().remove(experiencia);
            }
            else {
                System.out.println("Erro ao apagar competencia/experiencia");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onClickEditarExperiencia(ExperienciaModel experiencia) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("atualizarExperiencia.fxml"));
        Parent root = loader.load();
        AtualizarExperiencia controller = loader.getController();

        controller.setNomeInput(experiencia.getCompetencia());
        controller.setTempoInput(experiencia.getExperiencia());

        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadTableData() {
        ObservableList<ExperienciaModel> tableData = FXCollections.observableArrayList();

        for (Map<String, Object> map : competenciaExperienciaMap) {
            String competencia = (String) map.get("competencia");
            int experiencia = ConversionUtil.convertToInteger(map.get("experiencia"));
            Button edit = new Button("Editar");
            Button delete = new Button("Deletar");

            ExperienciaModel experienciaModel = new ExperienciaModel(competencia, experiencia, edit, delete);

            edit.setOnAction(event -> {
                try {
                    onClickEditarExperiencia(experienciaModel);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
            delete.setOnAction(event -> {
                try {
                    onClickApagarExperiencia(experienciaModel);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });

            tableData.add(experienciaModel);
        }

        experienciasTable.setItems(tableData);
    }

    public void setCompetenciaExperiencia(List<Map<String, Object>> competenciaExperienciaMap) {
        this.competenciaExperienciaMap = competenciaExperienciaMap;
    }

    @javafx.fxml.FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("candidatoHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
