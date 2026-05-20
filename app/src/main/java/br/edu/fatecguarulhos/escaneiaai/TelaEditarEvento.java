package br.edu.fatecguarulhos.escaneiaai;

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

import com.google.gson.Gson;

import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class TelaEditarEvento extends AppCompatActivity {
    private EditText edtNome, edtLocal, edtDescricao, edtDataInicio, edtDataFim;
    private Button btnVoltar, btnAlterar, btnExcluir;
    private Evento evento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_editar_evento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarValores();
        inicializarComponentes();
        configurarComponentes();
    }

    private void inicializarValores() {
        Intent it = getIntent();
        String jsonEvento = it.getStringExtra("jsonEvento");
        evento =  new Gson().fromJson(jsonEvento, Evento.class);
    }

    private void inicializarComponentes(){
        edtNome = findViewById(R.id.edtNomeEvento_telaEditarEvento);
        edtDescricao = findViewById(R.id.edtDescricao_telaEditarEvento);
        edtLocal = findViewById(R.id.edtLocal_telaEditarEvento);
        edtDataInicio = findViewById(R.id.edtdataInicio_telaEditarEvento);
        edtDataFim = findViewById(R.id.edtdataFim_telaEditarEvento);
        btnAlterar = findViewById(R.id.btnEditarEvento_telaEditarEvento);
        btnExcluir = findViewById(R.id.btnExcluirEvento_telaEditarEvento);
        btnVoltar = findViewById(R.id.btnVoltar_telaEditarEvento);
    }
    private void configurarComponentes(){

        edtNome.setText(evento.getTitulo());
        edtLocal.setText(evento.getLocal());
        edtDescricao.setText(evento.getDescricao());
        edtDataInicio.setText(evento.getDataInicio());
        edtDataFim.setText(evento.getDataFim());
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarEvento();
            }
        });
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirEvento();
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void excluirEvento(){
        new AlertDialog.Builder(this)
                .setTitle("Excluindo evento")
                .setMessage("Deseja realmente excluir " + evento.getTitulo() +"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        EventoDao eventoDAO = new EventoDao();
                        eventoDAO.deleteEvento(evento.getId());
                        Toast.makeText(TelaEditarEvento.this, "Evento excluido!", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent();
                        it.putExtra("excluido", true);
                        setResult(AppCompatActivity.RESULT_OK, it);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
    public void editarEvento(){
        evento.setTitulo(edtNome.getText().toString());
        evento.setLocal(edtLocal.getText().toString());
        evento.setDescricao(edtDescricao.getText().toString());
        evento.setDataInicio(edtDataInicio.getText().toString());
        evento.setDataFim(edtDataFim.getText().toString());
        EventoDao eventoDAO = new EventoDao();
        eventoDAO.updateEvento(evento);
        Toast.makeText(this, "Evento editado!", Toast.LENGTH_SHORT).show();
    }



}