package com.rankersprep.rankerspreplearning;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentsListAdapter extends BaseAdapter{
    ArrayList<String> mentorNameList;
    Context context;
    ArrayList<String> amountList;
    private static LayoutInflater inflater=null;
    public PaymentsListAdapter(Activity mainActivity, ArrayList<String> mentorNameList, ArrayList<String> amountList) {
        // TODO Auto-generated constructor stub
        this.mentorNameList=mentorNameList;
        context=mainActivity;
        this.amountList=amountList;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mentorNameList.size();
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
        TextView nameTV;
        TextView amountTV;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.payment_list, null);
        holder.nameTV=(TextView) rowView.findViewById(R.id.listNameTextView);
        holder.nameTV.setText(mentorNameList.get(position));
        holder.amountTV=(TextView) rowView.findViewById(R.id.amountTV);
        holder.amountTV.setText("₹ "+amountList.get(position));

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