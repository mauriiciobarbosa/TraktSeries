package com.mauricio.traktseries.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mauricio.traktseries.R;

/**
 * Created by mauricio on 22/03/16.
 */
public class SerieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    public ImageView ivPost;
    public TextView tvTitle;

    public SerieViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        itemView.setOnClickListener(this);
        tvTitle = (TextView) itemView.findViewById(R.id.tvSerieName);
        ivPost = (ImageView) itemView.findViewById(R.id.ivSeriePost);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, tvTitle.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
