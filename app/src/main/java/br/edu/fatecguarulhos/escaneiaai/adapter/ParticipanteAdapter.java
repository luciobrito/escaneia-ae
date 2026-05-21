package br.edu.fatecguarulhos.escaneiaai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.models.Participante;

public class ParticipanteAdapter extends RecyclerView.Adapter<ParticipanteAdapter.ParticipanteHolder> {

    private Context context;
    private ArrayList<Participante> participantes;

    public ParticipanteAdapter(Context context, ArrayList<Participante> participantes) {
        this.context = context;
        this.participantes = participantes;
    }

    @NonNull
    @Override
    public ParticipanteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_participante, parent, false);
        return new ParticipanteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipanteHolder holder, int position) {
        Participante participante = participantes.get(position);
        holder.setDetails(participante);
    }

    @Override
    public int getItemCount() {
        return participantes.size();
    }

    class ParticipanteHolder extends RecyclerView.ViewHolder{
        private TextView txtNome, txtEmail, txtRa, txtEntrada;
        ParticipanteHolder(View itemView){
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeParticipante_cardParticipante);
            txtEmail = itemView.findViewById(R.id.txtEmailParticipante_cardParticipante);
            txtRa = itemView.findViewById(R.id.txtRa_cardPArticipante);
            txtEntrada = itemView.findViewById(R.id.txtEntrada_cardPArticipante);
        }
        void setDetails(Participante participante){
            txtNome.setText(participante.getNome());
            txtEmail.setText("Email: " + participante.getEmail());
            txtRa.setText("RA: " + participante.getRa());
            txtEntrada.setText((participante.isSaida() ? "Saida Registrada" : "Presente"));
        }
    }
}
