package br.com.testesbottomnav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    Context context;

    List<ItemNoticia> itemNoticiaList;

    OnClickListener onClickListener;

    public NewsAdapter(Context context, List<ItemNoticia> itemNoticiaList, OnClickListener onClickListener) {
        this.context = context;
        this.itemNoticiaList = itemNoticiaList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_view_noticias,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.noticiaTitulo.setText(itemNoticiaList.get(position).getTituloNoticia());
        holder.noticiaCorpo.setText(itemNoticiaList.get(position).getCorpoNoticia());

        Picasso.with(context)
                .load(itemNoticiaList.get(position).getImagemNoticia())
                .fit()
                .centerCrop()
                .into(holder.imgNoticia);

    }

    @Override
    public int getItemCount() {
        return itemNoticiaList.size();
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imgNoticia;
        private TextView noticiaTitulo, noticiaCorpo;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNoticia = itemView.findViewById(R.id.image_news);
            noticiaTitulo = itemView.findViewById(R.id.title_news);
            noticiaCorpo = itemView.findViewById(R.id.news_body);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

}
