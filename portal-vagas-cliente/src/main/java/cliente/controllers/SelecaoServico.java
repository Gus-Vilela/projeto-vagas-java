package cliente.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SelecaoServico
{
    @javafx.fxml.FXML
    private Button btnEmpresa;
    @javafx.fxml.FXML
    private Button btnCandidato;

    @javafx.fxml.FXML
    public void initialize() {
    }

    public void onClickEmpresa() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginEmpresa.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnEmpresa.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onClickCandidato() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginCandidato.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnCandidato.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}