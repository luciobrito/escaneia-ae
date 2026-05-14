package br.edu.fatecguarulhos.escaneiaai.paginas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.util.TempDbManager;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class FormCriarEvento extends AppCompatActivity {
    private EditText edtNomeEvento;
    private Button btnCriar, btnVoltar;
    private TempDbManager dbConnection;
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
        dbConnection = new TempDbManager();
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

}