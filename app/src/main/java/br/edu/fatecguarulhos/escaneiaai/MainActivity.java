package br.edu.fatecguarulhos.escaneiaai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.dao.ParticipanteDao;
import br.edu.fatecguarulhos.escaneiaai.interfaces.Imprimivel;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;
import br.edu.fatecguarulhos.escaneiaai.paginas.PaginaListaEventos;
import br.edu.fatecguarulhos.escaneiaai.paginas.PaginaEventos;
import br.edu.fatecguarulhos.escaneiaai.telas.TelaRegistrarParticipante;
import br.edu.fatecguarulhos.escaneiaai.util.FirebaseCallback;
import br.edu.fatecguarulhos.escaneiaai.util.ImpressoraTermica;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
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
        inicializarValores();
        configurarNavbar();
    }
    private void inicializarValores(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
    private void configurarNavbar(){
        iniciarMenuItem(R.id.item_eventos);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                iniciarMenuItem(id);
                return true;
            }
        });
    }
    // identificar qual dos 2 botões de baixo foi clicado
    private void iniciarMenuItem(int id) {
        Fragment selectedFragment = null;
        if (id == R.id.item_eventos) {
            // Lista de eventos
            selectedFragment = new PaginaListaEventos();
        }
        if(id == R.id.item_perfil){
            // outra pagina
            selectedFragment = new PaginaEventos();
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

    }
    // Resultado da leitura do QR code na tela "HomeFragment"
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String msgQrCode = QrCodeManager.getResultadoLeitor(requestCode, resultCode, data);
        // dividir o id[0] do tipo de qr code[1]
        String[] msgFatiada = msgQrCode.split("/type=");
        if(msgQrCode == null){
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            EventoDao dbConnection = new EventoDao();
            dbConnection.getEventoById(
                    msgFatiada[0],
                    // garantir q estejam sincronos
                    new FirebaseCallback() {
                        @Override
                        public void onCallbackForAll(List<Evento> lista) { }

                        @Override
                        public void onCallBackByid(Evento e) {
                            registrarLeituraQC(e, msgFatiada[1]);
                        }
                    });
        }
    }
    public void imprimir(View view){
        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH},1);
            } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 2);
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 4);
            } else {

                ImpressoraTermica i = new ImpressoraTermica(this, this);
                i.imprimirComPermissao((Imprimivel) new Evento("titulo"));
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
    public void registrarLeituraQC(Evento e, String tipoQrCode){
        Participante p = new Participante();
        p.setNome("TesteEntrada");
        p.setEmail("email1");
        p.setRa("123");
        ParticipanteDao dbConnection = new ParticipanteDao();
        /*
        if(tipoQrCode.equals("entrada"))
            dbConnection.registrarEntradaParticipante(e, p);
        else if(tipoQrCode.equals("saida"))
            dbConnection.registrarSaidaParticipante(e, p);
        else
            Toast.makeText(this
                            ,"Leitura inválida tente novamente"
                            , Toast.LENGTH_SHORT)
                    .show();

         */
    }
}