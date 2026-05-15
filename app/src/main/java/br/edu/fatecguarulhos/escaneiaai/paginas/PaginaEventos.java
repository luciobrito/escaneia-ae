package br.edu.fatecguarulhos.escaneiaai.paginas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.telas.TelaCriarEvento;

public class PaginaEventos extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAdd, btnLer;
    private TextView textIdDispositivo;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert container != null;
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        inicializarComponentes(v);
        configurarComponentes();
        //String idDispositivo = Settings.Secure.getString(v.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //textIdDispositivo = v.findViewById(R.id.id_dispositivo);
        //textIdDispositivo.setText("Id do dispositivo:" + idDispositivo);
        return v;
    }
    private void inicializarComponentes(View v){
        btnAdd = v.findViewById(R.id.btnTempAddEvento);
    }
    private void configurarComponentes(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TelaCriarEvento.class);
                startActivity(it);
            }
        });
    }
}
