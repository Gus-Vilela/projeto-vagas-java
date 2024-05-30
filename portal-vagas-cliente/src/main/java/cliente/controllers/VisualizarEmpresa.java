package cliente.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class VisualizarEmpresa
{
    @javafx.fxml.FXML
    private Label descricaoLabel;
    @javafx.fxml.FXML
    private Label senhaLabel;
    @javafx.fxml.FXML
    private Button btnVoltar;
    @javafx.fxml.FXML
    private Label emailLabel;
    @javafx.fxml.FXML
    private Label cnpjLabel;
    @javafx.fxml.FXML
    private Label razaoSocialLabel;
    @javafx.fxml.FXML
    private Label ramoLabel;

    @javafx.fxml.FXML
    public void initialize() {
    }

    public void setDescricao(String descricao) {
        descricaoLabel.setText(descricao);
    }

    public void setSenha(String senha) {
        senhaLabel.setText(senha);
    }

    public void setEmail(String email) {
        emailLabel.setText(email);
    }

    public void setCnpj(String cnpj) {
        cnpjLabel.setText(cnpj);
    }

    public void setRazaoSocial(String razaoSocial) {
        razaoSocialLabel.setText(razaoSocial);
    }

    public void setRamo(String ramo) {
        ramoLabel.setText(ramo);
    }



    @javafx.fxml.FXML
    public void onClickVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("empresaHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}