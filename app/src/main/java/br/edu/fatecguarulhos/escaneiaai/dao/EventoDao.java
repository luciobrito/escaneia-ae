package br.edu.fatecguarulhos.escaneiaai.dao;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.interfaces.FirebaseCallback;

public class EventoDao {
    private FirebaseDatabase database;
    public EventoDao(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d(TAG, "connected");
                } else {
                    Log.d(TAG, "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
        database = FirebaseDatabase.getInstance();

    }
    public void adicionarEvento(Evento e){
        DatabaseReference myRef = database.getReference("eventos");
        String eventoId = myRef.push().getKey();
        e.setId(eventoId);
        myRef.child(eventoId).setValue(e)
                .addOnSuccessListener(a -> {
                    //Toast informando sucesso
                })
                .addOnFailureListener(ex->{
                    throw new RuntimeException(ex.getMessage());
                });
    }


    public void getAllEventos(FirebaseCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventos");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Evento> listaEventos = new ArrayList<Evento>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Evento evento = postSnapshot.getValue(Evento.class);
                    listaEventos.add(evento);
                }
                try{
                    callback.onCallbackForAll(listaEventos);
                } catch (NullPointerException npe){
                    System.out.println("Erro -> " + npe.getMessage());
                } catch (Exception e){
                    System.out.println("Erro -> " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Falha na leitura.", databaseError.toException());
            }
        });
    }

    public void getEventoById(String id, FirebaseCallback callback){
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
    public void updateEvento(Evento evento){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventos").child(evento.getId());;
        ref.setValue(evento);
    }
    public void deleteEvento(String idEvento){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventos").child(idEvento);;
        ref.setValue(null);
    }


}
