package br.edu.fatecguarulhos.escaneiaai.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;
import br.edu.fatecguarulhos.escaneiaai.paginas.PaginaEventos;

public class ParticipanteDao {
    private FirebaseDatabase database;
    public ParticipanteDao(){
        database = FirebaseDatabase.getInstance();
    }
    public void registrarEntradaParticipante(Evento evento, Participante participante) {
        DatabaseReference myRef = database.getReference("eventos").child(evento.getId());
        List<Participante> participantes = evento.getParticipantes();
        participantes.add(participante);
        evento.setParticipantes(participantes);
        myRef.setValue(evento);

    }
    public void registrarSaidaParticipante(Evento evento, Participante participante){
        DatabaseReference myRef = database.getReference("eventos").child(evento.getId());
        List<Participante> lista = evento.getParticipantes();
        for(int i = 0; i < lista.size(); i++){
            Participante p = lista.get(i);
            if(
                    p.getNome().equals(participante.getNome())
                            && p.getEmail().equals(participante.getEmail())
                            && p.getRa().equals(participante.getRa())
            ) {
                lista.get(i).setSaida(true);
            }
        }
        evento.setParticipantes(lista);
        myRef.setValue(evento);
    }

}
