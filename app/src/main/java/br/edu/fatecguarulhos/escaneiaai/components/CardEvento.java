package br.edu.fatecguarulhos.escaneiaai.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CardEvento extends ConstraintLayout {
    private String titulo, corpo, jsonEvento;
    private TextView txtAndamento;
    private Evento evento;
    private int andamento;
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
        card.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // definir ação ao clicar em um cartão de evento
                Intent it = new Intent(v.getContext() , TelaEvento.class);
                it.putExtra("eventoJson", jsonEvento);
                v.getContext().startActivity(it);
            }
        });
    }
    public void alterarConteudo(Evento e){

        jsonEvento = new Gson().toJson(e);
        evento = e;
        titulo = e.getTitulo();
        corpo = "Inicio: " + e.getDataInicio() + "\nFim:    " + e.getDataFim();
        TextView textTitulo, textCorpo;
        textTitulo = findViewById(R.id.textView_tituloCard);

        txtAndamento = findViewById(R.id.txtAndamento_cardEvento);
        textCorpo = findViewById(R.id.textHorarioEvento_cardEvento);
        textTitulo.setText(titulo);
        textCorpo.setText(corpo);
        alterarAndamentoEvento();
    }
    public void alterarAndamentoEvento(){
        int momenotInt = momentoEvento();
        andamento = momenotInt;
        if(momenotInt == 0){
            txtAndamento.setText("Em breve");
            txtAndamento.setTextColor(Color.parseColor("#40E0D0"));
        }
        else if(momenotInt == 1){
            txtAndamento.setText("Em andamento!");
            txtAndamento.setTextColor(Color.parseColor("#E4B32E"));
        }
        else if(momenotInt == 2){
            txtAndamento.setText("Encerrado");
            txtAndamento.setTextColor(Color.parseColor("#EF3232"));
        }
        else {
            txtAndamento.setText("Erro");
        }
    }
    public int momentoEvento(){
        Calendar momentoInicio, momentoFim, dataHoraAtual;
        momentoInicio = stringToCalendar(evento.getDataInicio());
        momentoFim = stringToCalendar(evento.getDataFim());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
        String formattedDate = df.format(Calendar.getInstance().getTime());
        dataHoraAtual = stringToCalendar(formattedDate);
        if(dataHoraAtual.before(momentoInicio))
            return 0;
        if(dataHoraAtual.after(momentoFim))
            return 2;
        return 1;
    }
    public Calendar stringToCalendar(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int getAndamento(){
        return andamento;
    }

}
