package com.mauricio.traktseries.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mauricio.traktseries.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mauricio on 22/03/16.
 */
public class SerieAdapter extends RecyclerView.Adapter<SerieViewHolder> {

    private Context context;
    private List<Serie> series;

    public SerieAdapter(Context context, List<Serie> series) {
        this.context = context;
        this.series = series;
    }

    /**
     * Método responsável por criar cada um dos itens de séries exibidos na tela.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SerieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_serie, parent, false);
        SerieViewHolder viewHolder = new SerieViewHolder(context, view);
        return viewHolder;
    }

    /**
     * Método responsável por carregar cada um dos itens de séries exibidos na tela.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(SerieViewHolder holder, int position) {
        Serie serie = series.get(position);
        holder.tvTitle.setText(serie.getTitle());
        Picasso.with(context).load(serie.getImages().getPoster().getThumb()).into(holder.ivPost);
    }

    @Override
    public int getItemCount() {
        return series.size();
    }
}
