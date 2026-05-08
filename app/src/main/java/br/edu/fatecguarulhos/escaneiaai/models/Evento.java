package br.edu.fatecguarulhos.escaneiaai.models;

import com.google.type.DateTime;

import java.time.LocalDateTime;

public class Evento {
    private String id;
    private String titulo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private LocalDateTime previsaoInicio;
    private LocalDateTime previsaoFim;
    private boolean cancelado;
    private String descricao;
    private Palestrante palestrante;
    public boolean eventoIniciado(){
        return dataInicio != null;
    }
    public boolean eventoEmAtraso(){
        return false;
    }
    public boolean eventoFinalizado(){
        return false;
    }
    public void iniciarEvento(){
        //Hora atual;
    }
    public void cancelarEvento(){
        cancelado = true;
    }
    private void notificarUsuarios(){
        //Lógica aqui
    }
}
