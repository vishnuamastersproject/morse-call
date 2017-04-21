package com.project.vactionbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;


public class ImageAdapter extends ArrayAdapter<MemoryDTO>{
    Context context;
    int layoutResourceId;
    // BcardImage data[] = null;
    ArrayList<MemoryDTO> data=new ArrayList<MemoryDTO>();
    public ImageAdapter(Context context, int layoutResourceId, ArrayList<MemoryDTO> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ImageHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtDate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtDescription = (TextView)row.findViewById(R.id.txtDescription);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }

        MemoryDTO picture = data.get(position);
        holder.txtTitle.setText(picture._name);
        //convert byte to bitmap take from memoryDTO class
        byte[] outImage=picture._image;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.imgIcon.setImageBitmap(theImage);

        if(picture._memoryDate!=null){
            holder.txtTitle.setText(picture._memoryTitle);
            holder.txtDate.setText(picture._memoryDate);
            holder.txtDescription.setText(picture._memoryDescription);
        }
        return row;

    }

    static class ImageHolder
    {
        ImageView imgIcon;
        TextView txtTitle,txtDate,txtDescription;
    }
}