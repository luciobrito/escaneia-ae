package br.edu.fatecguarulhos.escaneiaai.models;

public class Participante {
    private String nome;
    private boolean saida;

    public Participante() {
    }

    public Participante(String nome) {
        this.nome = nome;
        this.saida = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isSaida() {
        return saida;
    }

    public void setSaida(boolean saida) {
        this.saida = saida;
    }
}
