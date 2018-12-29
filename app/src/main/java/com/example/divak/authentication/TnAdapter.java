package com.example.divak.authentication;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TnAdapter extends RecyclerView.Adapter<TnAdapter.ViewHolder>{

    public ArrayList<Listitem> ListItems;
    public Context context;
    ClickListner clickListner;

    public TnAdapter(ArrayList<Listitem> listItems, Context context,ClickListner clickListner) {
        ListItems = listItems;
        this.context = context;
        this.clickListner=clickListner;
    }

    interface ClickListner{
        void onChatClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v =LayoutInflater.from(parent.getContext())
               .inflate(R.layout.list_items,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Listitem listItem= ListItems.get(position);
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return ListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHead;
        public TextView textViewDesc;
        LinearLayout nameRow;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewHead = (TextView)itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView)itemView.findViewById(R.id.textViewDesc);
            nameRow=itemView.findViewById(R.id.nameLayout);
            nameRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListner.onChatClick(getAdapterPosition());
                }
            });
        }
    }
}
