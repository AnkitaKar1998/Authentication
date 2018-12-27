package com.example.divak.authentication;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.divak.authentication.R.id.rectxtview;
//import com.example.divak.authentication.R

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Items> {
    Context context;
    String[] item;
    ClickListner clickListner;

    public RecyclerViewAdapter( String[] item, Context context, ClickListner clickListner ){
        this.context=context;
        this.item=item;
        this.clickListner = clickListner;
    }

    interface ClickListner{
        public void onClick(int position);
    }
    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.items_layout,parent,false );
        Items i = new Items(row);
        return i;
    }

    @Override
    public void onBindViewHolder(Items holder, final int position) {
        holder.textView.setText(item[position]);
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context,TeacherChatActivity.class);
//                i.putExtra("Department",item[position]);
//                context.startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return item.length;
    }

    public class Items extends RecyclerView.ViewHolder{
        TextView textView;
        LinearLayout linearLayout;
        //RecyclerView parentLayout;
        public Items(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.rectxtview);
            linearLayout = itemView.findViewById(R.id.sectionteacher);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListner.onClick(getAdapterPosition());
                }
            });
        }


    }
}
