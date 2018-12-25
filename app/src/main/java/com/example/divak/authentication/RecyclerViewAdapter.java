package com.example.divak.authentication;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//import com.example.divak.authentication.R

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    String[] item;

    public RecyclerViewAdapter(Context context, String[] item){
        this.context=context;
        this.item=item;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.items_layout,parent,false );
        Items i = new Items(row);
        return i;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Items)holder).textView.setText(item[position]);

    }

    @Override
    public int getItemCount() {
        return item.length;
    }

    class Items extends RecyclerView.ViewHolder{
        TextView textView;
        public Items(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.rectxtview);
        }
    }
}
