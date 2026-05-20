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
                if(validarDatas(edtDataInicio, edtDataFim)){
                    Evento e = criarEvento();
                    registrarEvento(e);
                }
                else
                    Toast.makeText(v.getContext(), "Data inicio/fim inválida", Toast.LENGTH_SHORT).show();
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
        if(e.getTitulo().isEmpty() || e.getTitulo().equals("")){
            Toast.makeText(this, "NOme obrigatório", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(e.getLocal().isEmpty() || e.getLocal().equals("")){
            Toast.makeText(this, "Local obrigatório", Toast.LENGTH_SHORT).show();
            return null;
        }
            return e;


    }
    private void registrarEvento(Evento e){
        if(e != null){
            dbConnection.adicionarEvento(e);
            Toast.makeText(this, "Evento criado com sucesso",Toast.LENGTH_SHORT).show();
            finish();
        }
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
    // para definir o criador do evento
    private String getIdCelular(){
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}


