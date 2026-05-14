package br.edu.fatecguarulhos.escaneiaai.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class TempDbManager {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public TempDbManager(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
    public void adicionarEvento(Evento e){
        /*
        Evento e = new Evento("Grande evento");
        List<Participante> p = new ArrayList<>();
        p.add(new Participante("Fulano1"));
        p.add(new Participante("Ciclano1"));
        p.add(new Participante("Fulano21"));
        e.setParticipantes(p);
         */
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                System.out.println("DADOs -> " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("foi nao");
            }
        });
        myRef.child("eventos").push().setValue(e);


    }

    public void lerV1(){
        myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    System.out.println("fudeu");
                } else {
                    System.out.println(":D");
                }
            }
        });


    }

}
