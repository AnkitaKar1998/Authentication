package com.example.divak.authentication;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//import com.example.divak.authentication.R

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Items> {
    Context context;
    String[] item;
    ClickListner clickListner;

    public RecyclerViewAdapter(Context context, String[] item){
        this.context=context;
        this.item=item;
    }

    interface ClickListner{
        public void onClick(int position);
    }
    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.items_layout,parent,false );
        Items i = new Items(row);
        return i;
    }

    @Override
    public void onBindViewHolder(Items holder, int position) {
        holder.textView.setText(item[position]);
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
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    clickListner.onClick(0);
//                }
//            });
        }

    }
}
