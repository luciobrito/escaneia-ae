package br.edu.fatecguarulhos.escaneiaai.paginas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.util.DbManager;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class FormCriarEvento extends AppCompatActivity {
    private EditText edtNomeEvento, edtDataInicio, edtDataFim;
    private Button btnCriar, btnVoltar;
    private DbManager dbConnection;
    private Calendar calendar = Calendar.getInstance();
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

        dbConnection = new DbManager();
        edtDataInicio = findViewById(R.id.edtdataInicio_formCriaEvento);
        edtDataFim = findViewById(R.id.edtdataFim_formCriaEvento);
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
        edtNomeEvento = findViewById(R.id.edtNomeEvento_formCriarEvento);
        btnCriar = findViewById(R.id.btnCriarEvento_formCriarEvento);
        btnVoltar = findViewById(R.id.btnVoltar_formCriarEvento);
        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarEvento();
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void criarEvento(){
        Evento e = new Evento();
        e.setTitulo(edtNomeEvento.getText().toString());
        dbConnection.adicionarEvento(e);
        finish();
    }

    private DateTime dataInicio(){
        return null;
    }

    private void formatacaoDataEvento(EditText edtData){
        String data = edtData.getText().toString();
        if(data.length() == 2 || data.length() == 5)
            edtData.setText(data + "/");
    }

    private void mostrarEscolhaDateTime(EditText edtData){
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                edtData.setText(sdf.format(calendar.getTime()));

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}


