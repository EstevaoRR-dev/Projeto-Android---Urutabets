package br.com.testesbottomnav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.TimesViewHolder> {

    Context context;
    List<Item> items;

    OnClickListener onClickListener;

    public Adapter(Context context, List<Item> items, OnClickListener onClickListener) {
        this.context = context;
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TimesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
        return new TimesViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_view,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TimesViewHolder holder, int position) {
        holder.nomeA.setText(items.get(position).getNomeClubeUm());
        holder.nomeB.setText(items.get(position).getNomeClubeDois());
        holder.data.setText(items.get(position).getData());
        holder.horario.setText(items.get(position).getHorario());

        Picasso.with(context)
                .load(items.get(position).getImagemClubeUm())
                .fit()
                .centerCrop()
                .into(holder.timeA);

        Picasso.with(context)
                .load(items.get(position).getImagemClubeDois())
                .fit()
                .centerCrop()
                .into(holder.timeB);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }

    class TimesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView timeA, timeB;
        TextView nomeA, nomeB, data, horario;

        public TimesViewHolder(@NonNull View itemView) {
            super(itemView);

            timeA = itemView.findViewById(R.id.imagemTimeUm);
            timeB = itemView.findViewById(R.id.imagemTimeDois);
            data = itemView.findViewById(R.id.data);
            horario = itemView.findViewById(R.id.horario);
            nomeA = itemView.findViewById(R.id.nomeClubeUm);
            nomeB = itemView.findViewById(R.id.nomeClubeDois);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

}
