package com.example.noteapp;

import java.util.List;

import android.R.string;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteAdapter extends ArrayAdapter<Note>{

    private final Context context;
    private final List<Note> notes;
    public NoteAdapter(Context context, int textViewResourceId,
            List<Note> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.notes = objects;
    }
    
    static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View rowView = convertView;
        //reuse view
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_layout, null);
            //configure view holder
            ViewHolder viewHolder= new ViewHolder();
            viewHolder.textView = (TextView) rowView.findViewById(R.id.title);
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }
        
        //fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Note note = notes.get(position);
        
        holder.textView.setText(note.getTitle());
        return rowView;
    }
}
