package br.edu.fatecguarulhos.escaneiaai.paginas;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.components.CardEvento;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.util.FirebaseCallback;
import br.edu.fatecguarulhos.escaneiaai.util.TempDbManager;

public class PaginaEventos extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAdd, btnLer;
    private TextView textIdDispositivo;
    private TempDbManager dbConnection;

    public PaginaEventos(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
        dbConnection = new TempDbManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert container != null;
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        btnAdd = v.findViewById(R.id.btnTempAddEvento);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execBtnAdd(v);
            }
        });
        btnLer = v.findViewById(R.id.btnTempLer);
        btnLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lerEventosTemp();
            }
        });
        String idDispositivo = Settings.Secure.getString(v.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        textIdDispositivo = v.findViewById(R.id.id_dispositivo);
        textIdDispositivo.setText("Id do dispositivo:" + idDispositivo);
        return v;
    }

    public void execBtnAdd(View view){
        Intent it = new Intent(view.getContext(), FormCriarEvento.class);
        startActivity(it);
    }
    public void lerEventosTemp(){
        dbConnection.lerTodos(new FirebaseCallback() {
            @Override
            public void onCallback(List<Evento> lista) {

            }
        });
    }

}
