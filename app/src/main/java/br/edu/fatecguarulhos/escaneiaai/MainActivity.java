package br.edu.fatecguarulhos.escaneiaai;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.edu.fatecguarulhos.escaneiaai.paginas.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView text_teste;
    private LinearLayout layoutEventos;
    private MaterialCardView mcard;
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
        //text_teste = findViewById(R.id.text_teste);
        //dbConnect();
        //add();
        inicializarValores();
        configurarNavbar();
    }
    private void inicializarValores(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);

    }
    private void preencherLayoutDados(){
        LinearLayout ll = findViewById(R.id.layout_dados);
        for(int i = 0; i < 50; i++){
            TextView txt = new TextView(this);
            txt.setText(" " + i);
            ll.addView(txt);
        }
    }
    private void configurarNavbar(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment selectedFragment = null;
                if(id == R.id.item_eventos){
                    selectedFragment = new HomeFragment();


                }
                    if(selectedFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });
    }
    private void add(){
        //layoutEventos = findViewById(R.id.layout_eventos);
        for(int i = 0; i < 50; i++){
            TextView txt = new TextView(this);
            txt.setText(""+i);
            //layoutEventos.addView(txt);
        }
    }
    private void dbConnect(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("eventos");
        Evento evento = new Evento("Grande evento");
        cr.add(evento).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                text_teste.setText("Dados enviados com sucesso!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                text_teste.setText("Erro: " + e.getMessage());
            }
        });
    }
}