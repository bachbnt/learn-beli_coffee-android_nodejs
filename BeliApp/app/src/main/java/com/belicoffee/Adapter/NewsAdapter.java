package com.belicoffee.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.belicoffee.Model.News;
import com.belicoffee.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    ArrayList<News> newsArrayList;
    Context context;
    OnItemClickListener listener;

    public NewsAdapter(ArrayList<News> newsArrayList, Context context, OnItemClickListener listener) {
        this.newsArrayList = newsArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_news, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final News news = newsArrayList.get(i);
        viewHolder.tvTitle.setText(news.getTitle());
        viewHolder.tvContent.setText(news.getContent());
        Log.i("tag", "convertByte2Bitmap " + news.getImage());
        if (news.getImage() != null)
            Glide.with(context).load(Uri.parse(news.getImage())).into(viewHolder.ivImage);
        else viewHolder.ivImage.setImageResource(R.drawable.ic_belicoffee);
        viewHolder.ibUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdateClick(news);
            }
        });
        viewHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        ImageView ivImage;
        ImageButton ibUpdate;
        ImageButton ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_news_item);
            tvContent = itemView.findViewById(R.id.tv_content_news_item);
            ivImage = itemView.findViewById(R.id.iv_image_news_item);
            ibUpdate = itemView.findViewById(R.id.ib_update_news_item);
            ibDelete = itemView.findViewById(R.id.ib_delete_news_item);
        }
    }

    public interface OnItemClickListener {
        void onUpdateClick(News news);

        void onDeleteClick(News news);
    }
}
