package com.example.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.activity.MainActivity;
import com.example.gallery.activity.ViewAlbumActivity;
import com.example.gallery.activity.ViewItemActivity;
import com.example.gallery.model.Album;
import com.example.gallery.model.Item;


import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter {
    private String date;
    private ArrayList<Item> items = new ArrayList<>();
    private Context context;
    private final int IMAGE_TYPE = 0;
    private final int VIDEO_TYPE = 1;
    public DateAdapter(String date) {
        this.date = date;
    }
    public DateAdapter(String date, ArrayList<Item> items, Context context) {
        this.date = date;
        this.context = context;
        this.items = items;
    }

    public ArrayList<Item> getImages() {
        return items;
    }

    public void setItems() {
        this.items = items;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }



    RecyclerView mRecyclerView;


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        if (viewType== IMAGE_TYPE){
            view = inflater.inflate(R.layout.image, parent, false);
            return new DateAdapter.ImageViewHolder(view);
        }
        view = inflater.inflate(R.layout.video_item_layout, parent, false);
        return new DateAdapter.VideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == 0){
            float k = (float) (1.0 * items.size() / (MainActivity.numCol + MainActivity.padding));
            int tmp = (int)k;
            int numRows;
            if (tmp <k)
                numRows = tmp + 1;
            else
                numRows = tmp;
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
                    params.height = numRows * holder.itemView.getHeight();
                    mRecyclerView.setLayoutParams(params);
                }
            });
        }

        if (items.get(position).isImage())
        {
            ImageViewHolder imageViewHolder = (ImageViewHolder)holder;

            ImageView imageView = imageViewHolder.imageView;
            Glide.with(context).load(items.get(position).getFilePath()).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            CheckBox check = imageViewHolder.view.findViewById(R.id.check);
            if (AlbumDetailAdapter.delMode == true)
                check.setVisibility(CheckBox.VISIBLE);
            else
                check.setVisibility(CheckBox.INVISIBLE);
            if (MainActivity.buffer.contains(items.get(position)))
                check.setChecked(true);
            else
                check.setChecked(false);
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if (!MainActivity.buffer.contains(items.get(position))) {

                                MainActivity.buffer.add(items.get(position));
                            }
                    }
                    else {
                            if (MainActivity.buffer.contains(items.get(position))) {
                                {
                                    MainActivity.buffer.remove(items.get(position));
                                }

                            }
                        }



                    if (MainActivity.mainMode){
                        CheckBox checkBox = (CheckBox)MainActivity.checkAll.getActionView();
                        if(MainActivity.buffer.size() == MainActivity.items.size()){
                            checkBox.setChecked(true);
                        }
                        else{
                            checkBox.setChecked(false);
                        }
                    }
                    else {
                        CheckBox checkBox = (CheckBox)ViewAlbumActivity.checkAll.getActionView();
                        if(MainActivity.buffer.size() == MainActivity.curAlbum.getImages().size()){
                            checkBox.setChecked(true);
                        }
                        else{
                            checkBox.setChecked(false);
                        }
                    }

                }
            });
            imageViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Do some work here
                    if (AlbumDetailAdapter.delMode == true){
                        if (MainActivity.buffer.contains(items.get(position))){
                            check.setChecked(false);
                            MainActivity.buffer.remove(items.get(position));

                        }
                        else{
                            check.setChecked(true);
                            if (!MainActivity.buffer.contains(items.get(position)))
                                MainActivity.buffer.add(items.get(position));

                        }

                        return;
                    }
                    Intent intent = new Intent((Activity) context, ViewItemActivity.class);
                    intent.putExtra("item", items.get(position));
                    ((Activity) context).startActivityForResult(intent, MainActivity.UPDATED_CODE);
                }
            });

            imageViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (AlbumDetailAdapter.delMode == false)
                        AlbumDetailAdapter.delMode = true;
                    else
                        AlbumDetailAdapter.delMode = false;
                    if (AlbumDetailAdapter.delMode == true){
                        check.setChecked(true);
                        if (!MainActivity.buffer.contains(items.get(position)))
                            MainActivity.buffer.add(items.get(position));
                        if(MainActivity.mainMode)
                            MainActivity.showMenu();
                        else
                            ViewAlbumActivity.showMenu();
                    }
                    else{
                        AlbumDetailAdapter.unCheckAll();
                        if(MainActivity.mainMode)
                            MainActivity.hideMenu();
                        else
                            ViewAlbumActivity.hideMenu();
                    }
                    if(MainActivity.mainMode)
                        MainActivity.fragment1.getAlbumDetailAdapter().notifyDataSetChanged();
                    else
                        ViewAlbumActivity.albumDetailAdapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
        else{
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            ImageView thumbnail = videoViewHolder.thumnail;
            Glide.with(context).load(items.get(position).getFilePath()).into(thumbnail);
            thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TextView textView = videoViewHolder.duration;
            if (items.get(position).getDurationLong()!=0)
                textView.setText(items.get(position).getDuration());
            CheckBox check = videoViewHolder.view.findViewById(R.id.check);
            if (AlbumDetailAdapter.delMode == true)
                check.setVisibility(CheckBox.VISIBLE);
            else
                check.setVisibility(CheckBox.INVISIBLE);
            if (MainActivity.buffer.contains(items.get(position)))
                check.setChecked(true);
            else
                check.setChecked(false);

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if (!MainActivity.buffer.contains(items.get(position))){

                                MainActivity.buffer.add(items.get(position));
                            }

                        }
                        else {
                            if (MainActivity.buffer.contains(items.get(position))){
                                {
                                    MainActivity.buffer.remove(items.get(position));
                                }

                            }

                    }

                    if (MainActivity.mainMode){
                        CheckBox checkBox = (CheckBox)MainActivity.checkAll.getActionView();
                        if(MainActivity.buffer.size() == MainActivity.items.size()){
                            checkBox.setChecked(true);
                        }
                        else{
                            checkBox.setChecked(false);
                        }
                    }
                    else {
                        CheckBox checkBox = (CheckBox)ViewAlbumActivity.checkAll.getActionView();
                        if(MainActivity.buffer.size() == MainActivity.curAlbum.getImages().size()){
                            checkBox.setChecked(true);
                        }
                        else{
                            checkBox.setChecked(false);
                        }
                    }
                }
            });


            videoViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Do some work here
                    if (AlbumDetailAdapter.delMode == true){
                        if (MainActivity.buffer.contains(items.get(position))){
                            check.setChecked(false);
                            MainActivity.buffer.remove(items.get(position));

                        }
                        else{
                            check.setChecked(true);
                            if (!MainActivity.buffer.contains(items.get(position)))
                                MainActivity.buffer.add(items.get(position));

                        }

                        return;
                    }
                    Intent intent = new Intent((Activity) context, ViewItemActivity.class);
                    intent.putExtra("item", items.get(position));
                    ((Activity) context).startActivity(intent);
                    ((Activity) context).startActivityForResult(intent, MainActivity.UPDATED_CODE);
                }
            });

            videoViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (AlbumDetailAdapter.delMode == false)
                        AlbumDetailAdapter.delMode = true;
                    else
                        AlbumDetailAdapter.delMode = false;
                    if (AlbumDetailAdapter.delMode == true){
                        check.setChecked(true);
                        if (!MainActivity.buffer.contains(items.get(position)))
                            MainActivity.buffer.add(items.get(position));
                        if(MainActivity.mainMode)
                            MainActivity.showMenu();
                        else
                            ViewAlbumActivity.showMenu();
                    }
                    else{
                        AlbumDetailAdapter.unCheckAll();
                        if(MainActivity.mainMode)
                            MainActivity.hideMenu();
                        else
                            ViewAlbumActivity.hideMenu();
                    }
                    if(MainActivity.mainMode)
                        MainActivity.fragment1.getAlbumDetailAdapter().notifyDataSetChanged();
                    else
                        ViewAlbumActivity.albumDetailAdapter.notifyDataSetChanged();
                    return true;
                }
            });
        }



    }

    @Override
    public int getItemViewType(int position) {
        return Item.isImageFile((items.get(position).getFilePath())) ? IMAGE_TYPE:VIDEO_TYPE;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        View view;
        public ImageViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.image);
        }
    }
    private class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView thumnail;
        TextView duration;
        View view;
        public VideoViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            thumnail = itemView.findViewById(R.id.thumbnail);
            duration = itemView.findViewById(R.id.text_duration);
        }
    }
}
