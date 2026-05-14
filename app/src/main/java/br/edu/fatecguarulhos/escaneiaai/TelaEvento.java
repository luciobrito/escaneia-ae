package br.edu.fatecguarulhos.escaneiaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.edu.fatecguarulhos.escaneiaai.paginas.TelaQrCode;

public class TelaEvento extends AppCompatActivity {
    private TextView txtTituloEvento;
    private FloatingActionButton fabRetorno;
    private String idEvento;
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
        Intent it = getIntent();
        // coletar id do evento
        idEvento = it.getStringExtra("id");
        txtTituloEvento = findViewById(R.id.txtTituloEvento_actv_tela_evento);
        txtTituloEvento.setText(it.getStringExtra("titulo"));
        fabRetorno = findViewById(R.id.fabQrCode_telaEvento);
        fabRetorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telaQrCode();
            }
        });
    }
    private void telaQrCode(){
        Intent it  = new Intent(this, TelaQrCode.class);
        // enviar id para uso na criação do qrCode
        it.putExtra("id",idEvento);
        startActivity(it);
    }
    // appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_exibir_evento, menu);
        return true;
    }


    public void voltar(View view){
        finish();
    }

    public void voltar(MenuItem menuItem){
        finish();
    }
}