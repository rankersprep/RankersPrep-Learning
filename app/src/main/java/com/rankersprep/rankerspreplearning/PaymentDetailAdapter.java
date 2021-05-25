package com.rankersprep.rankerspreplearning;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PaymentDetailAdapter extends BaseAdapter {

    ArrayList<String> menteeNamesList,planNameList;
    Context context;
    ArrayList<Integer> amountList;
    private static LayoutInflater inflater=null;
    public PaymentDetailAdapter(Activity mainActivity, ArrayList<String> menteeNameList, ArrayList<Integer> amountList,ArrayList<String> planName) {
        // TODO Auto-generated constructor stub
        this.menteeNamesList=menteeNameList;
        context=mainActivity;
        this.amountList=amountList;
        this.planNameList = planName;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return planNameList.size();
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
        TextView amountTV,planName;
    }
     @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.mentee_payment_list, null);
        holder.nameTV=(TextView) rowView.findViewById(R.id.nameMentee);
        holder.nameTV.setText(menteeNamesList.get(position));
        holder.amountTV=(TextView) rowView.findViewById(R.id.amount);
        holder.amountTV.setText("₹ "+amountList.get(position));
        holder.planName = rowView.findViewById(R.id.planNameTV);
        holder.planName.setText(planNameList.get(position));

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
