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
import br.edu.fatecguarulhos.escaneiaai.TelaEvento;

public class CardEvento extends ConstraintLayout {
    private String titulo, corpo;
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
        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT).show();
                Intent it = new Intent(v.getContext() , TelaEvento.class);
                it.putExtra("titulo", titulo);
                v.getContext().startActivity(it);
            }
        });
    }
    public void alterarConteudo(String titulo, String corpo){
        TextView textTitulo, textCorpo;
        textTitulo = findViewById(R.id.textView_tituloCard);
        textCorpo = findViewById(R.id.textView_corpoCard);
        textTitulo.setText(titulo);
        textCorpo.setText(corpo);
    }
    public void alterarConteudo(Evento evento){
        titulo = evento.getTitulo();
        corpo = "corpo";
        TextView textTitulo, textCorpo;
        textTitulo = findViewById(R.id.textView_tituloCard);
        textCorpo = findViewById(R.id.textView_corpoCard);
        textTitulo.setText(titulo);
        textCorpo.setText(corpo);
    }

}
