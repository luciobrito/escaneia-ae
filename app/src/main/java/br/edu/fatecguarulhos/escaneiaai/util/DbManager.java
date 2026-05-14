package br.edu.fatecguarulhos.escaneiaai.util;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;

public class DbManager {
    private View v;
    private FirebaseDatabase database;
    private FirebaseFirestore firestore;
    //private DatabaseReference myRef;
    public DbManager(){
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference();
        /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                //System.out.println("Value: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Erro: " + error.toString());
            }
        });
        */
        firestore = FirebaseFirestore.getInstance();
    }
    public void adicionarEvento(Evento e){
        DatabaseReference myRef = database.getReference("eventos");
        String eventoId = myRef.push().getKey();
        e.setId(eventoId);
        myRef.child(eventoId).setValue(e);
    }


    public void lerTodos(FirebaseCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Evento> listaEventos = new ArrayList<Evento>();
                //listaEventos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Evento evento = postSnapshot.getValue(Evento.class);
                    listaEventos.add(evento);
                }
                callback.onCallbackForAll(listaEventos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Falha na leitura.", databaseError.toException());
            }
        });
    }

    public void lerPorId(String id, FirebaseCallback callback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Evento e = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Evento evento = postSnapshot.getValue(Evento.class);
                    if(evento.getId().equals(id))
                        e = evento;
                }
                callback.onCallBackByid(e);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Falha na leitura.", databaseError.toException());
            }
        });
    }
    public void registrarEntradaParticipante(Evento e, Participante p) {

        DatabaseReference myRef = database.getReference("eventos").child(e.getId());
        List<Participante> participantes = e.getParticipantes();
        participantes.add(p);
        e.setParticipantes(participantes);
        myRef.setValue(e);

/*
        DatabaseReference myRef = database.getReference("eventos").
                child(e.getId()).
                child("participantes");
        myRef.child(p.getNome()).setValue(p);
*/
    }
    public void registrarSaidaParticipante(Evento e, Participante participante){
        DatabaseReference myRef = database.getReference("eventos").child(e.getId());
        List<Participante> lista = e.getParticipantes();
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
        e.setParticipantes(lista);
        myRef.setValue(e);
        }
    }



