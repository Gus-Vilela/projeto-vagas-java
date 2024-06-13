package cliente.models;

import javafx.scene.control.Button;

public class ExperienciaModel {
    public String competencia;
    public int experiencia;
    public Button btnEditar;
    public Button btnDeletar;

    public ExperienciaModel(String competencia, int experiencia, Button btnEditar, Button btnDeletar) {
        this.competencia = competencia;
        this.experiencia = experiencia;
        this.btnEditar = btnEditar;
        this.btnDeletar = btnDeletar;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
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
