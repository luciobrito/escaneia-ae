package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.TelaEditarEvento;
import br.edu.fatecguarulhos.escaneiaai.adapter.ParticipanteAdapter;
import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.interfaces.FirebaseCallback;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;

public class TelaEvento extends AppCompatActivity {
    private TextView txtTituloEvento, txtLocalEvento, txtDataEvento, txtDescricaoEvento;
    private FloatingActionButton fabQrCode, fabEditarEvento;
    private Evento evento;
    private RecyclerView rvParticipantes;
    private ParticipanteAdapter adapter;
    private ArrayList<Participante> participantesArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_evento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarValores();
        inicializarComponentes();
        configurarComponentes();
        gerarListaCardParticipantes();
    }

    private void inicializarValores() {
        Intent it = getIntent();
        evento = new Gson().fromJson(it.getStringExtra("eventoJson"), Evento.class);
    }

    private void inicializarComponentes(){
        txtTituloEvento = findViewById(R.id.txtTituloEvento_actv_tela_evento);
        txtDataEvento = findViewById(R.id.txtHorarioEvento_telaEvento);
        txtLocalEvento = findViewById(R.id.txtLocalEvento_telaEvento);
        txtDescricaoEvento = findViewById(R.id.txtDescricaoEvento_telaEvento);
        fabQrCode = findViewById(R.id.fabQrCode_telaEvento);
        fabEditarEvento = findViewById(R.id.fabEditarEvento_telaEvento);
        rvParticipantes = findViewById(R.id.rvPresentes);
    }
    private void configurarComponentes(){
        txtTituloEvento.setText(evento.getTitulo());
        txtDataEvento.setText("Inicio: " + evento.getDataInicio() + "\nFim: " + evento.getDataFim());
        txtLocalEvento.setText("Local: " + evento.getLocal());

        if(evento.getDescricao() != null && !(evento.getDescricao().equals("")))
            txtDescricaoEvento.setText("Descrição: " + evento.getDescricao());
        else
            txtDescricaoEvento.setText("Descrição: Sem descricao");
        fabQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaQrCode();
            }
        });
        fabEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telaEditarEvento();
            }
        });
    }
    private void gerarListaCardParticipantes() {
        rvParticipantes.setLayoutManager(new LinearLayoutManager(this));
        participantesArrayList = new ArrayList<>();
        adapter = new ParticipanteAdapter(this, participantesArrayList);
        participantesArrayList.addAll(evento.getParticipantes());
        rvParticipantes.setAdapter(adapter);
    }
    private void abrirTelaQrCode(){
        Intent it  = new Intent(this, TelaQrCode.class);
        // enviar id para uso na criação do qrCode
        String id = evento.getId();
        String titulo = evento.getTitulo();
        it.putExtra("id", id);
        it.putExtra("titulo",titulo);
        startActivity(it);
    }
    // appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_exibir_evento, menu);
        return true;
    }

    private void telaEditarEvento(){
        String eventoJson = new Gson().toJson(evento);
        Intent it = new Intent(this, TelaEditarEvento.class);
        it.putExtra("jsonEvento", eventoJson);
        startActivity(it);
    }

    public void voltar(View view){
        finish();
    }

    public void voltar(MenuItem menuItem){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventoDao eventoDao = new EventoDao();
        String idEvento = evento.getId();
        eventoDao.getEventoById(idEvento, new FirebaseCallback() {
            @Override
            public void onCallbackForAll(List<Evento> lista) {

            }

            @Override
            public void onCallBackByid(Evento e) {
                evento = e;
                if(evento != null){
                    inicializarComponentes();
                    configurarComponentes();
                    gerarListaCardParticipantes();
                } else {
                    Toast.makeText(TelaEvento.this,"Evento não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}