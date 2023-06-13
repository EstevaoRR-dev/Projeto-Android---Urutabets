package br.com.testesbottomnav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ApostasEmAbertoAdapter extends RecyclerView.Adapter<ApostasEmAbertoAdapter.ApostasViewHolder>{

    Context context;
    List<ItemApostasEmAberto> items;

    public ApostasEmAbertoAdapter(Context context, List<ItemApostasEmAberto> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ApostasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApostasViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_view_apostas_em_aberto,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ApostasViewHolder holder, int position) {

        holder.todasAsApostas.setText(items.get(position).getTodasAsApostas());
        holder.valor.setText(items.get(position).getValor().toString());
        holder.resultado.setText(items.get(position).getResultado().toString());

        Picasso.with(context)
                .load(items.get(position).getImgTimeA())
                .fit()
                .centerCrop()
                .into(holder.timeA);

        Picasso.with(context)
                .load(items.get(position).getImgTimeB())
                .fit()
                .centerCrop()
                .into(holder.timeB);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ApostasViewHolder extends RecyclerView.ViewHolder{

        ImageView timeA, timeB;
        TextInputEditText todasAsApostas;
        EditText valor, resultado;

        public ApostasViewHolder(@NonNull View itemView) {
            super(itemView);
            timeA = itemView.findViewById(R.id.logo_time_a_aposta);
            timeB = itemView.findViewById(R.id.logo_time_b_aposta);
            todasAsApostas = itemView.findViewById(R.id.text_todas_as_apostas);
            valor = itemView.findViewById(R.id.editText_valor_aposta);
            resultado = itemView.findViewById(R.id.editText_valor_apostaResultado);
        }
    }

}
