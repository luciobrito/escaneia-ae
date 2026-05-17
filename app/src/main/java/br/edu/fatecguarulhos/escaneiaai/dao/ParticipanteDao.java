package br.edu.fatecguarulhos.escaneiaai.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;
import java.util.NoSuchElementException;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;
import br.edu.fatecguarulhos.escaneiaai.paginas.PaginaEventos;

public class ParticipanteDao {
    private FirebaseDatabase database;
    public ParticipanteDao(){
        database = FirebaseDatabase.getInstance();
    }
    public void registrarEntradaParticipante(String eventoJson, Participante participante) {
        Evento evento = new Gson().fromJson(eventoJson, Evento.class);
        DatabaseReference myRef = database.getReference("eventos").child(evento.getId());
        int idParticipante = encontrarParticipante(evento.getParticipantes(), participante);
        boolean participanteRegistrado =  idParticipante != -1;
        if(participanteRegistrado)
            throw new IllegalArgumentException("Participante ja registrado");

        List<Participante> participantes = evento.getParticipantes();
        participantes.add(participante);
        evento.setParticipantes(participantes);
        myRef.setValue(evento);


    }
    public void registrarSaidaParticipante(String eventoJson, Participante participante){
        Evento evento = new Gson().fromJson(eventoJson, Evento.class);
        DatabaseReference myRef = database.getReference("eventos").child(evento.getId());
        List<Participante> lista = evento.getParticipantes();
        int idParticipante = encontrarParticipante(lista, participante);
        boolean entradaRegistrada = idParticipante != -1;
        if(entradaRegistrada){
            // participante registrado, porém com saida já escaneada
            if(lista.get(idParticipante).isSaida())
                throw new IllegalArgumentException("Saída ja registrada");
            lista.get(idParticipante).setSaida(true);
            evento.setParticipantes(lista);
            myRef.setValue(evento);
        } else
            throw new NoSuchElementException("Participante não registrado");
    }
    public int encontrarParticipante(List<Participante> lista,Participante participante){
        for(int i = 0; i < lista.size(); i++){
            Participante p = lista.get(i);
            boolean isParticipante = (
                    p.getNome().equals(participante.getNome()) &&
                            p.getEmail().equals(participante.getEmail()) &&
                            p.getRa().equals(participante.getRa())
                    );
            if(isParticipante)
                // participante encontrado se retorno for >= 0
                // se for encontrado, ele retorna um numero diferente de -1, que é a posição na lista
                return i;
        }
        // participante não encontrado se retorno for  == -1
        return -1;
    }


}
