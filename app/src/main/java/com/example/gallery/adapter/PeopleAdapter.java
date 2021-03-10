package com.example.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.ViewAlbumActivity;
import com.example.gallery.model.Album;
import com.example.gallery.R;

import java.util.ArrayList;

public class PeopleAdapter extends BaseAdapter {
    private ArrayList<Album> list = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    public PeopleAdapter(ArrayList<Album>  list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void addAlbum(Album album){
        list.add(album);
    }
    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = (View) layoutInflater.inflate(R.layout.people,parent, false);

        ImageView item = (ImageView) view.findViewById(R.id.item);
        TextView albumName = (TextView) view.findViewById(R.id.albumName);
        TextView countItems = (TextView) view.findViewById(R.id.countItems);

        item.setImageResource(list.get(position).getMainItem().getDrawableVal());
        albumName.setText(list.get(position).getName());
        countItems.setText(Integer.toString(list.get(position).getCount()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Activity) context, ViewAlbumActivity.class);
                intent.putExtra("people", list.get(position));
                ((Activity) context).startActivity(intent);
            }
        });

        return view;
    }

}
