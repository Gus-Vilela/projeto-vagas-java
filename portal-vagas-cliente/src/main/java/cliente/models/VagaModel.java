package cliente.models;

import javafx.scene.control.Button;

public class VagaModel {
    private Integer idVaga;
    private String nome;
    private Button btnEditar;
    private Button btnDeletar;

    public VagaModel(Integer idVaga, String nome, Button btnEditar, Button btnDeletar) {
        this.idVaga = idVaga;
        this.nome = nome;
        this.btnEditar = btnEditar;
        this.btnDeletar = btnDeletar;
    }

    public Integer getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(Integer idVaga) {
        this.idVaga = idVaga;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Button getBtnEditar() {
        return btnEditar;
    }

    public void setBtnEditar(Button btnEditar) {
        this.btnEditar = btnEditar;
    }

    public Button getBtnDeletar() {
        return btnDeletar;
    }

    public void setBtnDeletar(Button btnDeletar) {
        this.btnDeletar = btnDeletar;
    }

}
