package com.vdart.example.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman19 on 10/22/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ImageModel> data = new ArrayList<>();

    public GalleryAdapter(Context context, List<ImageModel> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageModel text = data.get(position);
        Glide.with(context).load(data.get(position).getUrl())
                .thumbnail(0.5f)
                .override(200,200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((MyItemHolder) holder).mImg);
//        Picasso.with(context).load(data.get(position).getUrl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
//                .into(((MyItemHolder) holder).mImg, new com.squareup.picasso.Callback(){
//
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });
        ((MyItemHolder) holder).mText.setText(text.getName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mText;

        public MyItemHolder(View itemView) {
            super(itemView);

            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            mText = (TextView) itemView.findViewById(R.id.item_text);
        }

    }


}

//public class GalleryAdapter extends BaseAdapter {
//    Context context;
//    int logos[];
//    LayoutInflater inflter;
//
//    public GalleryAdapter(Context applicationContext, int[] logos) {
//        this.context = applicationContext;
//        this.logos = logos;
//        inflter = (LayoutInflater.from(applicationContext));
//    }
//
//    @Override
//    public int getCount() {
//        return logos.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        view = inflter.inflate(R.layout.activity_gridgallery, null); // inflate the layout
//        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
//        System.out.println(logos[i]);
//        icon.setImageResource(logos[i]); // set logo images
//        return view;
//    }
//}
