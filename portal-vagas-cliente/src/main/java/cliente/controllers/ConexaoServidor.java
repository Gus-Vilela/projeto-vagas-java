package cliente.controllers;

import cliente.socket.SingletonEchoClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ConexaoServidor
{
    @javafx.fxml.FXML
    private TextField hostnameInput;
    @javafx.fxml.FXML
    private Button connectBtn;
    @javafx.fxml.FXML
    private Label errorLabel;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void onClickConnect() throws IOException {
        errorLabel.setOpacity(0);
        if(hostnameInput.getText().isEmpty()){
            errorLabel.setText("Preencha o campo");
            errorLabel.setOpacity(1);
            return;
        }
        try{
            SingletonEchoClient.getInstance(hostnameInput.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginCandidato.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) connectBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Conectado com sucesso");
        }
        catch (Exception e) {
            errorLabel.setText("Erro ao conectar com o servidor: " + e.getMessage());
            errorLabel.setOpacity(1);
        }
    }
}

