package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.NoSuchElementException;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.dao.ParticipanteDao;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;
import br.edu.fatecguarulhos.escaneiaai.util.CacheHelper;

public class TelaRegistrarParticipante extends AppCompatActivity {
    private boolean isEntrada;
    private String eventoJson, inputNome, inputEmail, inputRa;
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
        inicializarComponentes();
        configurarComponentes();
    }
    private void inicializarValores(){
        Intent it = getIntent();
        isEntrada = it.getBooleanExtra("isEntrada", true);
        eventoJson = it.getStringExtra("eventoJson");
        }
    private void inicializarComponentes() {
        edtNome = findViewById(R.id.edtNome_FormRegistrarParticipante);
        edtEmail = findViewById(R.id.edtEmail_FormRegistrarParticipante);
        edtRa = findViewById(R.id.edtRa_FormRegistrarParticipante);
        btnRegistrar = findViewById(R.id.btnRegistrar_FormRegistrarParticipante);
    }
    private void configurarComponentes(){
        pegarDadosCace();
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
        inputNome = edtNome.getText().toString();
        inputEmail = edtEmail.getText().toString();
        inputRa = edtRa.getText().toString();
        p.setNome(inputNome);
        p.setEmail(inputEmail);
        p.setRa(inputRa);
        return p;
    }
    private void registrarParticipante(Participante p){
        try{
            if(isEntrada)
                registrarEntrada(p);
            else
                registrarSaida(p);
            salvarCache();
            finish();
        } catch (IllegalArgumentException iae){
            Toast.makeText(this, iae.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (NoSuchElementException nsee){
            Toast.makeText(this, nsee.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void registrarEntrada(Participante p){
        ParticipanteDao dbConnection = new ParticipanteDao();
        dbConnection.registrarEntradaParticipante(eventoJson, p);
        Toast.makeText(this, "Entrada confirmada!", Toast.LENGTH_SHORT).show();
    }
    private void registrarSaida(Participante p){
        ParticipanteDao dbConnection = new ParticipanteDao();
        dbConnection.registrarSaidaParticipante(eventoJson, p);
        Toast.makeText(this, "Saida confirmada!", Toast.LENGTH_SHORT).show();
    }
    public void salvarCache(){
        CacheHelper.saveToCache(this, inputNome, inputEmail, inputRa);
    }
    public void pegarDadosCace(){
        String[] dadosCache = CacheHelper.getFromCache(this);
        for(String s : dadosCache){
            if(s.equals("") || s.isEmpty())
                return;
        }
        mostrarDadosCache(dadosCache);
    }
    private void mostrarDadosCache(String[] dadosCache){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usar ultimos dados salvos?");
        builder.setMessage(
                "Nome: " + dadosCache[0] +
                        "\nEmail: " + dadosCache[1] +
                        "\nRA: " + dadosCache[2]
        );
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtNome.setText(dadosCache[0]);
                edtEmail.setText(dadosCache[1]);
                edtRa.setText(dadosCache[2]);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}