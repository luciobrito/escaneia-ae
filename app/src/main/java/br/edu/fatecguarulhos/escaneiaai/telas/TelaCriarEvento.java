package br.edu.fatecguarulhos.escaneiaai.telas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.TelaEditarEvento;
import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class TelaCriarEvento extends AppCompatActivity {
    private EditText edtNomeEvento, edtDataInicio, edtDataFim, edtDescricao, edtLocal;
    private Button btnCriar, btnVoltar;
    private EventoDao dbConnection;
    private Calendar calendario = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_criar_evento);
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
        dbConnection = new EventoDao();
    }

    private void inicializarComponentes(){
        edtDataInicio = findViewById(R.id.edtdataInicio_formCriaEvento);
        edtDataFim = findViewById(R.id.edtdataFim_formCriaEvento);
        edtNomeEvento = findViewById(R.id.edtNomeEvento_formCriarEvento);
        edtLocal = findViewById(R.id.edtLocal_criarEvento);
        edtDescricao = findViewById(R.id.edtDescricao_criarEvento);
        btnCriar = findViewById(R.id.btnCriarEvento_formCriarEvento);
        btnVoltar = findViewById(R.id.btnVoltar_formCriarEvento);
    }
    private void configurarComponentes(){
        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDados()){
                    Evento e = criarEvento();
                    registrarEvento(e);
                }
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    }

    private Evento criarEvento(){

            Evento e = new Evento();
            e.setTitulo(edtNomeEvento.getText().toString());
            e.setDataInicio(edtDataInicio.getText().toString());
            e.setDataFim(edtDataFim.getText().toString());
            e.setIdCriador(getIdCelular());
            e.setLocal(edtLocal.getText().toString());
            e.setDescricao(edtDescricao.getText().toString());


        return e;


    }
    private void registrarEvento(Evento e){
        if(e != null){
            dbConnection.adicionarEvento(e);
            Toast.makeText(this, "Evento criado com sucesso",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private boolean validarDados(){
        if(!validarTitulo(edtNomeEvento)){
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
    private void mostrarEscolhaDateTime(EditText edtData){
        new DatePickerDialog(this, (view, ano, mes, dia) -> {
            calendario.set(Calendar.YEAR, ano);
            calendario.set(Calendar.MONTH, mes);
            calendario.set(Calendar.DAY_OF_MONTH, dia);

            new TimePickerDialog(this, (timeView, hora, minuto) -> {
                calendario.set(Calendar.HOUR_OF_DAY, hora);
                calendario.set(Calendar.MINUTE, minuto);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY - HH:mm", Locale.getDefault());
                edtData.setText(sdf.format(calendario.getTime()));

            }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), false).show();

        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
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
        if(campoNome.getText().toString().isEmpty() || campoNome.getText().equals("")){
            return false;
        }
        return true;
    }
    private boolean validarLocal(EditText campoLocal){
        if(campoLocal.getText().toString().isEmpty() || campoLocal.getText().equals("")){
            return false;
        }
        return true;
    }
    // para definir o criador do evento
    private String getIdCelular(){
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
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

}


