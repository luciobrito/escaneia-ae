package br.edu.fatecguarulhos.escaneiaai.util;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class TempDbManager {
    private View v;
    private FirebaseDatabase database;
    private FirebaseFirestore firestore;
    //private DatabaseReference myRef;
    public TempDbManager(){
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
                callback.onCallback(listaEventos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Falha na leitura.", databaseError.toException());
            }
        });
    }
}
