package br.edu.fatecguarulhos.escaneiaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;
import br.edu.fatecguarulhos.escaneiaai.paginas.ListaEventosFragment;
import br.edu.fatecguarulhos.escaneiaai.paginas.Pagina2Fragment;
import br.edu.fatecguarulhos.escaneiaai.util.DbManager;
import br.edu.fatecguarulhos.escaneiaai.util.FirebaseCallback;
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
            selectedFragment = new ListaEventosFragment();
        }
        if(id == R.id.item_perfil){
            // outra pagina
            selectedFragment = new Pagina2Fragment();
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

    }
    // Resultado da leitura do QR code na tela "HomeFragment"
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String msgQrCode = QrCodeManager.getResultadoLeitor(requestCode, resultCode, data);
        if(msgQrCode == null){
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            DbManager dbConnection = new DbManager();
            dbConnection.lerPorId(
                    msgQrCode,
                    // garantir q estejam sincronos
                    new FirebaseCallback() {
                        @Override
                        public void onCallbackForAll(List<Evento> lista) { }

                        @Override
                        public void onCallBackByid(Evento e) {
                            confirmarEntrada(e);
                        }
                    });
        }
    }
    public void confirmarEntrada(Evento e){
        DbManager dbConnection = new DbManager();
        dbConnection.updateEventoV1(e, new Participante("Caio1"));
    }
}