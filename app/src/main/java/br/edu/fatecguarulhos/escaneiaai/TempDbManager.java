package br.edu.fatecguarulhos.escaneiaai;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;

public class TempDbManager {
    public void dbAddTest(){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Evento e = new Evento("Grande evento3");
        List<Participante> p = new ArrayList<>();
        p.add(new Participante("Fulano"));
        p.add(new Participante("Ciclano"));
        p.add(new Participante("Fulano2"));
        e.setParticipantes(p);
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

    public void dbReadTest(){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
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
