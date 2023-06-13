package br.com.testesbottomnav;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder>{

    private List<SlideItem> items;
    private ViewPager2 viewPager2;

    public SlideAdapter(List<SlideItem> items, ViewPager2 viewPager2) {
        this.items = items;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImage(items.get(position));

        if (position == items.size() -1){
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;

        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SlideItem slideItem){
            imageView.setImageResource(slideItem.getImage());
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            items.addAll(items);
            notifyDataSetChanged();
        }
    };



}
