package br.edu.fatecguarulhos.escaneiaai.models;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String titulo;
    private List<Participante> participantes = new ArrayList<>();
    public Evento(String titulo){
        this.titulo = titulo;
    }

    public Evento() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }
}
