package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.NoSuchElementException;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.dao.ParticipanteDao;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;

public class TelaRegistrarParticipante extends AppCompatActivity {
    private boolean isEntrada;
    private String eventoJson;
    private EditText edtNome, edtEmail, edtRa;
    private Button btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_form_registrar_participante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarValores();
        configurarComponentes();
    }
    private void inicializarValores(){
        Intent it = getIntent();
        isEntrada = it.getBooleanExtra("isEntrada", true);
        eventoJson = it.getStringExtra("eventoJson");
        edtNome = findViewById(R.id.edtNome_FormRegistrarParticipante);
        edtEmail = findViewById(R.id.edtEmail_FormRegistrarParticipante);
        edtRa = findViewById(R.id.edtRa_FormRegistrarParticipante);

        btnRegistrar = findViewById(R.id.btnRegistrar_FormRegistrarParticipante);
    }
    private void configurarComponentes(){
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Participante p = criarParticipante();
                registrarParticipante(p);
            }
        });
    }
    private Participante criarParticipante(){
        Participante p = new Participante();
        String nome, email, ra;
        nome = edtNome.getText().toString();
        email = edtEmail.getText().toString();
        ra = edtRa.getText().toString();
        p.setNome(nome);
        p.setEmail(email);
        p.setRa(ra);
        return p;
    }
    private void registrarParticipante(Participante p){
        try{
            if(p != null){
                ParticipanteDao dbConnection = new ParticipanteDao();
                if(isEntrada){
                    dbConnection.registrarEntradaParticipante(eventoJson, p);
                    Toast.makeText(this, "Entrada confirmada!", Toast.LENGTH_SHORT).show();
                } else{
                    dbConnection.registrarSaidaParticipante(eventoJson, p);
                    Toast.makeText(this, "Saida confirmada!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        } catch (IllegalArgumentException iae){
            Toast.makeText(this, iae.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (NoSuchElementException nsee){
            Toast.makeText(this, nsee.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}