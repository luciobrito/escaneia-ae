package br.edu.fatecguarulhos.escaneiaai.components;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.telas.TelaEvento;
import com.google.gson.Gson;
public class CardEvento extends ConstraintLayout {
    private String titulo, corpo, jsonEvento;
    private TextView textTitulo, textCorpo;
    private Evento evento;
    private CardView cardView;
    public CardEvento(Context context){
        super(context);
        inicializarComponentes(context);
    }
    public CardEvento(Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
        inicializarComponentes(context);
    }
    private void inicializarComponentes(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_evento, this, true);
        CardView card = findViewById(R.id.cardView_evento);
        //jsonEvento =
        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // definir ação ao clicar em um cartão de evento
                Intent it = new Intent(v.getContext() , TelaEvento.class);
                //it.putExtra("titulo", titulo);
                //it.putExtra("id",evento.getId());
                //String jsonEvento = new Gson().toJson(evento);
                it.putExtra("eventoJson", jsonEvento);
                v.getContext().startActivity(it);
            }
        });
    }
    public void alterarConteudo(Evento e){

        jsonEvento = new Gson().toJson(e);
        evento = e;
        titulo = e.getTitulo();
        corpo = e.getDataInicio() + " < - > " + e.getDataFim();
        TextView textTitulo, textCorpo;
        textTitulo = findViewById(R.id.textView_tituloCard);
        textCorpo = findViewById(R.id.textView_corpoCard);
        textTitulo.setText(titulo);
        textCorpo.setText(corpo);
    }

}
