package com.rankersprep.rankerspreplearning;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AnnouncementAdapter extends BaseAdapter {

    ArrayList<String> title,date,description;
    ArrayList<Boolean> isUrgent;
    Context context;
    private static LayoutInflater inflater=null;
    public AnnouncementAdapter(Activity mainActivity, ArrayList<String> title, ArrayList<String> date, ArrayList<String> description,ArrayList<Boolean> isUrgent) {
        // TODO Auto-generated constructor stub
        this.title  =title;
        context=mainActivity;
        this.date=date;
        this.description = description;
        this.isUrgent = isUrgent;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView titleTV;
        TextView dateTV,descriptionTV;
        View urgent;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.announcement_list, null);
        holder.titleTV=(TextView) rowView.findViewById(R.id.titleTV);
        holder.titleTV.setText(title.get(position));
        holder.dateTV=(TextView) rowView.findViewById(R.id.date);
        holder.dateTV.setText(date.get(position));
        holder.descriptionTV = rowView.findViewById(R.id.descriptionTV);
        holder.descriptionTV.setText(description.get(position));
        holder.urgent = rowView.findViewById(R.id.urgent);
        if(!isUrgent.get(position)){
           holder.urgent.setVisibility(View.GONE);
        }

//        rowView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "You Clicked "+mentorNameList.get(position), Toast.LENGTH_LONG).show();
//            }
//        });
        rowView.setClickable(false);
        rowView.setFocusable(false);
        return rowView;
    }

}
