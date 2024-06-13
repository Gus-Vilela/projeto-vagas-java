package server.hibernate.models;

import jakarta.persistence.*;

@Entity
@Table(name = "experiencia", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_candidato", "id_competencia"})})
public class Experiencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidato")
    private Candidato idCandidato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_competencia")
    private Competencia idCompetencia;

    @Column(name = "tempo")
    private Integer tempo;

    public Experiencia() {
    }

    public Experiencia(Candidato idCandidato, Competencia idCompetencia, Integer tempo) {
        this.idCandidato = idCandidato;
        this.idCompetencia = idCompetencia;
        this.tempo = tempo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Candidato getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Candidato idCandidato) {
        this.idCandidato = idCandidato;
    }

    public Competencia getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(Competencia idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

}