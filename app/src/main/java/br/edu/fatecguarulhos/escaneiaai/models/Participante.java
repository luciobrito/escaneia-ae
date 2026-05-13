package br.edu.fatecguarulhos.escaneiaai.models;

public class Participante {
    private String nome;
    //private boolean entrada;
    private boolean saida;

    public Participante() {
    }

    public Participante(String nome) {
        this.nome = nome;
        //this.entrada = false;
        this.saida = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
/*
    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }
*/
    public boolean isSaida() {
        return saida;
    }

    public void setSaida(boolean saida) {
        this.saida = saida;
    }
}
