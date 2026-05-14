package br.edu.fatecguarulhos.escaneiaai.paginas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.components.CardEvento;
import br.edu.fatecguarulhos.escaneiaai.util.FirebaseCallback;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;
import br.edu.fatecguarulhos.escaneiaai.util.DbManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaEventosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton btnQrCode;
    private DbManager dbConnection;
    private List<Evento> eventos = new ArrayList<>();

    public ListaEventosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaEventosFragment newInstance(String param1, String param2) {
        ListaEventosFragment fragment = new ListaEventosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        QrCodeManager.eventoList.clear();
        assert container != null;
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout ll = v.findViewById(R.id.layout_dados);
        dbConnection = new DbManager();
        dbConnection.lerTodos(new FirebaseCallback() {
            @Override
            public void onCallbackForAll(List<Evento> lista) {
                for(int i = 0; i < lista.size(); i++){
                    CardEvento card = new CardEvento((getContext()));
                    card.alterarConteudo(lista.get(i));
                    ll.addView(card);
                }
            }

            @Override
            public void onCallBackByid(Evento e) {

            }
        });

        btnQrCode = v.findViewById(R.id.fabAbrirLeitorQrCode_fragmentHome);
        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lerQrCode();
            }
        });
        return v;
    }
    // botão para abrir camera e ler QrCode
    public void lerQrCode(){
        QrCodeManager.lerQrCode(new IntentIntegrator(getActivity()));
    }
}