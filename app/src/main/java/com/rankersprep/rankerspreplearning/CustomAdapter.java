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

public class CustomAdapter extends BaseAdapter{
    ArrayList<String> mentorNameList;
    Context context;
    ArrayList<Bitmap> mentorImages;
    ArrayList<String> menteeNoList;
    ArrayList<String> fieldList;
    ArrayList<String> approvalsList;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Activity mainActivity, ArrayList<String> mentorNameList, ArrayList<Bitmap> mentorImages, ArrayList<String> menteeNoList, ArrayList<String> fieldList, ArrayList<String> approvals) {
        // TODO Auto-generated constructor stub
        this.mentorNameList=mentorNameList;
        context=mainActivity;
        this.mentorImages=mentorImages;
        this.menteeNoList=menteeNoList;
        this.fieldList=fieldList;
        this.approvalsList = approvals;
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
        TextView fieldTV;
        TextView noMenteeTV;
        View approval;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        holder.nameTV=(TextView) rowView.findViewById(R.id.listNameTextView);
        holder.img=(ImageView) rowView.findViewById(R.id.mentorImageView);
        holder.nameTV.setText(mentorNameList.get(position));
        holder.img.setImageBitmap(mentorImages.get(position));
        holder.fieldTV=(TextView) rowView.findViewById(R.id.listFieldTextView);
        holder.fieldTV.setText(fieldList.get(position));
        holder.approval=rowView.findViewById(R.id.approval);
        if(!approvalsList.get(position).matches("approved")){
            holder.approval.setBackgroundResource(R.drawable.approval_not_done);
        }

        holder.noMenteeTV=(TextView) rowView.findViewById(R.id.menteesNoTextView);
        holder.noMenteeTV.setText(menteeNoList.get(position));
//        rowView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked "+mentorNameList.get(position), Toast.LENGTH_LONG).show();
//            }
//        });
        rowView.setClickable(false);
        rowView.setFocusable(false);
        return rowView;
    }

}