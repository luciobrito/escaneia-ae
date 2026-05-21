package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.components.CardEvento;
import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.interfaces.FirebaseCallback;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class MainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private EventoDao dbConnection;
    private FloatingActionButton btnQrCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures());
            // Apply the insets as padding to the view. Here, set all the dimensions
            // as appropriate to your layout. You can also update the view's margin if
            // more appropriate.
            findViewById(R.id.main).setPadding(insets.left, insets.top, insets.right, insets.bottom);

            // Return CONSUMED if you don't want the window insets to keep passing down
            // to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });
        // configiração base/inicial do codigo
        try{
            inicializarValores();
            configurarComponentes();
        } catch (RuntimeException re){
            Toast.makeText(this, re.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void inicializarValores(){
        try{
            ll = findViewById(R.id.layout_dados_main);
            dbConnection = new EventoDao();
            btnQrCode = findViewById(R.id.fabAbrirLeitorQrCode_main);
        } catch (RuntimeException re){
            Toast.makeText(this, re.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarComponentes(){
        dbConnection.getAllEventos(new FirebaseCallback() {
            @Override
            public void onCallbackForAll(List<Evento> lista) {

                atualizarListaEventos(lista);
            }

            @Override
            public void onCallBackByid(Evento e) {

            }
        });
        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCameraQrCode();
            }
        });
    }

    // botão para abrir camera e ler QrCode
    public void abrirCameraQrCode(){
        // jogar em outra activity para evitar erros com o onBackPressed
        Intent it = new Intent(this, CameraLeitorCode.class);
        startActivity(it);
    }

    public void atualizarListaEventos(List<Evento> lista){
        ll.removeAllViewsInLayout();
        List<CardEvento> eventosEncerrados = new ArrayList<>();
        for(Evento e : lista) {
            CardEvento card = new CardEvento(this);
            card.alterarConteudo(e);

            if (card.getAndamento() == 2)
                eventosEncerrados.add(card);
            else
                ll.addView(card);
        }
        for(CardEvento ce : eventosEncerrados){
            ll.addView(ce);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_tela_inicial, menu);
        return true;
    }
    public void sair(MenuItem menuItem){
        finish();
    }
    public void adicionarEvento(MenuItem item){
        Intent it = new Intent(this, TelaCriarEvento.class);
        startActivity(it);
    }
}