package br.edu.fatecguarulhos.escaneiaai;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class TelaEditarEvento extends AppCompatActivity {
    private EditText edtNome, edtLocal, edtDescricao, edtDataInicio, edtDataFim;
    private Button btnVoltar, btnAlterar, btnExcluir;
    private Evento evento;

    private Calendar calendario = Calendar.getInstance();
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
        edtDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarEscolhaDateTime(edtDataFim);
            }
        });
        edtDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarEscolhaDateTime(edtDataInicio);
            }
        });
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
                        try{
                            EventoDao eventoDAO = new EventoDao();
                            eventoDAO.deleteEvento(evento.getId());
                            Toast.makeText(TelaEditarEvento.this, "Evento excluido!", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent();
                            it.putExtra("excluido", true);
                            setResult(AppCompatActivity.RESULT_OK, it);
                            finish();
                        } catch (RuntimeException re){
                            Toast.makeText(TelaEditarEvento.this, re.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
    public void editarEvento(){
        try{
            evento.setTitulo(edtNome.getText().toString());
            evento.setLocal(edtLocal.getText().toString());
            evento.setDescricao(edtDescricao.getText().toString());
            evento.setDataInicio(edtDataInicio.getText().toString());
            evento.setDataFim(edtDataFim.getText().toString());
            if(evento.getTitulo().isEmpty() || evento.getTitulo().equals("")){
                Toast.makeText(this, "NOme obrigatório", Toast.LENGTH_SHORT).show();
                return;
            }
            if(evento.getLocal().isEmpty() || evento.getLocal().equals("")){
                Toast.makeText(this, "Local obrigatório", Toast.LENGTH_SHORT).show();
                return;
            }
            if(validarDatas(edtDataInicio, edtDataFim)){
                Toast.makeText(this, "Data inicio/fim inválida!", Toast.LENGTH_SHORT).show();
                return;
            }
            EventoDao eventoDAO = new EventoDao();
            eventoDAO.updateEvento(evento);
            Toast.makeText(this, "Evento editado!", Toast.LENGTH_SHORT).show();

        } catch (RuntimeException re){
            Toast.makeText(TelaEditarEvento.this, re.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(TelaEditarEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarDatas(EditText dataInicio1, EditText dataFim1){
        if(dataFim1.getText().toString().equals("") || dataInicio1.getText().toString().equals(""))
            return false;

        String strInicio = dataInicio1.getText().toString();
        String strFim = dataFim1.getText().toString();
        // os 2 campos tem que estar cheios
        if(strInicio.equals("") || strFim.equals(""))
            return false;
        String[] dataHoraInicio = strInicio.split(" ");
        String[] dataInicio = dataHoraInicio[0].split("-");
        String[] horaInicio = dataHoraInicio[1].split(":");

        String[] dataHoraFim = strFim.split(" ");
        String[] dataFim = dataHoraFim[0].split("-");
        String[] horaFim = dataHoraFim[1].split(":");

        // ano seguite
        if(Integer.parseInt(dataFim[0]) > Integer.parseInt(dataInicio[0]))
            return true;
        // mesmo ano
        if(Integer.parseInt(dataFim[0]) == Integer.parseInt(dataInicio[0])){
            // mes seguinte
            if(Integer.parseInt(dataFim[1]) > Integer.parseInt(dataInicio[1]))
                return true;
            // mesmo mes
            if(Integer.parseInt(dataFim[1]) == Integer.parseInt(dataInicio[1])) {
                // dia seguinte
                if(Integer.parseInt(dataFim[2]) > Integer.parseInt(dataInicio[2]))
                    return true;
                // mesmo dia
                if(Integer.parseInt(dataFim[2]) == Integer.parseInt(dataInicio[2])) {
                    // hora seguinte
                    if(Integer.parseInt(horaFim[0]) > Integer.parseInt(horaInicio[0]))
                        return true;
                    // mesma hora
                    if(Integer.parseInt(horaFim[0]) == Integer.parseInt(horaInicio[0])) {
                        // minuto seguinte
                        if(Integer.parseInt(horaFim[0]) > Integer.parseInt(horaInicio[0]))
                            return true;
                    }
                }
            }
        }
        return false;


    }
    private void mostrarEscolhaDateTime(EditText edtData){
        new DatePickerDialog(this, (view, ano, mes, dia) -> {
            calendario.set(Calendar.YEAR, ano);
            calendario.set(Calendar.MONTH, mes);
            calendario.set(Calendar.DAY_OF_MONTH, dia);

            new TimePickerDialog(this, (timeView, hora, minuto) -> {
                calendario.set(Calendar.HOUR_OF_DAY, hora);
                calendario.set(Calendar.MINUTE, minuto);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                edtData.setText(sdf.format(calendario.getTime()));

            }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), false).show();

        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }




}