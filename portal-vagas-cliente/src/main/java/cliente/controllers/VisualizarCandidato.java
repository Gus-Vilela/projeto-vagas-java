package cliente.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class VisualizarCandidato
{
    @javafx.fxml.FXML
    private Label senhaLabel;
    @javafx.fxml.FXML
    private Label nomeLabel;
    @javafx.fxml.FXML
    private Label emailLabel;
    @javafx.fxml.FXML
    private Button btnVoltar;

    @javafx.fxml.FXML
    public void initialize() {
    }

    public void setNome(String nome) {
        nomeLabel.setText(nome);
    }

    public void setEmail(String email) {
        emailLabel.setText(email);
    }

    public void setSenha(String senha) {
        senhaLabel.setText(senha);
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