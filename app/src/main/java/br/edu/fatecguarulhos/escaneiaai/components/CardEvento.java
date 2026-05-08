package br.edu.fatecguarulhos.escaneiaai.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.edu.fatecguarulhos.escaneiaai.R;

public class CardEvento extends ConstraintLayout {
    private TextView textTitulo, textCorpo;

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
    }
    public void alterarConteudo(String titulo, String corpo){
        TextView textTitulo, textCorpo;
        textTitulo = findViewById(R.id.textView_tituloCard);
        textCorpo = findViewById(R.id.textView_corpoCard);
        textTitulo.setText(titulo);
        textCorpo.setText(corpo);
    }

}
