package com.example.user.energysoft;

/**
 * Created by user on 12/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import static com.example.user.energysoft.R.id.news_title;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    NewsMain main;
    private List<News> newsList;
    private Context context;
    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        newsList = new ArrayList<>();
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        final RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.newslist_layout, parent, false);
        viewHolder = new NewsVH(v1);
        final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News news = newsList.get(viewHolder.getAdapterPosition());
                System.out.println("CLICKed"+news.getId());
//                news.setPage("FullNews");
            }
        };
        v1.setOnClickListener(mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Movie movie = movies.get(position);
        News news = newsList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                NewsVH newsVH = (NewsVH) holder;

                newsVH.news_title.setText(news.getTitle());
//                newsVH.news_description.setText("Description");
                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == newsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(News mc) {
        newsList.add(mc);
        notifyItemInserted(newsList.size() - 1);
    }

    public void addAll(List<News> mcList) {
        for (News mc : mcList) {
            add(mc);
        }
    }

    public void remove(News city) {
        int position = newsList.indexOf(city);
        if (position > -1) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new News());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = newsList.size() - 1;
        News item = getItem(position);

        if (item != null) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public News getItem(int position) {
        return newsList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class NewsVH extends RecyclerView.ViewHolder {
            public TextView news_title;
        private TextView news_description;
//        ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
        public NewsVH(View itemView) {
            super(itemView);
//            ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
//            if (convertView == null) {
//                LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.newslist_layout, null);

//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.code = (TextView) convertView.findViewById(R.id.code);
                news_title = (TextView) itemView.findViewById(R.id.news_title);
//                news_description = (TextView) itemView.findViewById(R.id.news_description);
            System.out.println(itemView);
//                news_image = (ImageView) convertView.findViewById(R.id.news_image);
//            TextView news = (TextView) convertView.findViewById(R.id.news_title);
//                convertView.setTag(holder);
//            System.out.println("View "+this.main.news.get(position).news_title);
//            }
//            else
//            {
//                holder = (ListAdapter.ViewHolderItem) convertView.getTag();
//            }

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}

