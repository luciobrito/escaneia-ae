package br.edu.fatecguarulhos.escaneiaai.telas;

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
import java.util.Date;
import java.util.Locale;

import br.edu.fatecguarulhos.escaneiaai.R;
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
            if(validarDados()){
                evento.setTitulo(edtNome.getText().toString().trim());
                evento.setLocal(edtLocal.getText().toString().trim());
                evento.setDescricao(edtDescricao.getText().toString().trim());
                String dataInicio = edtDataInicio.getText().toString();
                evento.setDataInicio(dataInicio);
                evento.setMomentoInicio(converterData(dataInicio));
                evento.setDataFim(edtDataFim.getText().toString());
                EventoDao eventoDAO = new EventoDao();
                eventoDAO.updateEvento(evento);
                Toast.makeText(this, "Evento editado!", Toast.LENGTH_SHORT).show();
            }

        } catch (RuntimeException re){
            Toast.makeText(TelaEditarEvento.this, re.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(TelaEditarEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validarDados(){
        if(!validarTitulo(edtNome)){
            Toast.makeText(this, "NOme obrigatório", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!validarDatas(edtDataInicio, edtDataFim)){
            Toast.makeText(this, "Data inicio/fim inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!validarLocal(edtLocal)){
            Toast.makeText(this, "Local obrigatório", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    private boolean validarDatas(EditText dataInicio1, EditText dataFim1){
        if(dataFim1.getText().toString().equals("") || dataInicio1.getText().toString().equals(""))
            return false;
        String strInicio = dataInicio1.getText().toString();
        String strFim = dataFim1.getText().toString();
        Calendar d1 = stringToCalendar(strInicio);
        Calendar d2 = stringToCalendar(strFim);
        if(d1.equals(d2))
            return true;
        return(d1.before(d2));
    }
    private boolean validarTitulo(EditText campoNome){
        String input = campoNome.getText().toString().trim();
        return !input.isEmpty();
    }
    private boolean validarLocal(EditText campoLocal){
        String input = campoLocal.getText().toString().trim();
        return !input.isEmpty();
    }
    private void mostrarEscolhaDateTime(EditText edtData){
        new DatePickerDialog(this, (view, ano, mes, dia) -> {
            calendario.set(Calendar.YEAR, ano);
            calendario.set(Calendar.MONTH, mes);
            calendario.set(Calendar.DAY_OF_MONTH, dia);

            new TimePickerDialog(this, (timeView, hora, minuto) -> {
                calendario.set(Calendar.HOUR_OF_DAY, hora);
                calendario.set(Calendar.MINUTE, minuto);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
                edtData.setText(sdf.format(calendario.getTime()));

            }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), false).show();

        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    public Calendar stringToCalendar(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String converterData(String dataInicio){
        String[] dataHora = dataInicio.split(" - ");
        String[] dataFatia = dataHora[0].split("/");
        String[] horaFatia = dataHora[1].split(":");
        String momentoInicio = dataFatia[2];
        momentoInicio = momentoInicio + dataFatia[1];
        momentoInicio = momentoInicio + dataFatia[0];
        momentoInicio = momentoInicio + horaFatia[0];
        momentoInicio = momentoInicio + horaFatia[1];
        return momentoInicio;
    }



}