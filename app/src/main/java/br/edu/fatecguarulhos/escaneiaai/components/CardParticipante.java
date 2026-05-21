package br.edu.fatecguarulhos.escaneiaai.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;

public class CardParticipante extends CardView {
    private TextView txtEmail, txtDados, txtNome;
    public CardParticipante(@NonNull Context context) {
        super(context);
        inicializarComponentes(context);
    }

    public void inicializarComponentes(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_participante, this, true);
        CardView card = findViewById(R.id.cardView_participante);
        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
    }
}
