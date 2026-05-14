package br.edu.fatecguarulhos.escaneiaai.paginas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.util.TempDbManager;

public class PaginaEventos extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAdd;
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
        return v;
    }

    public void execBtnAdd(View view){
        Intent it = new Intent(view.getContext(), FormCriarEvento.class);
        startActivity(it);
    }

}
