package com.rankersprep.rankerspreplearning;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ButtonListAdapter extends BaseAdapter {

    ArrayList<String> mentorNameList,UIDs,collegeNameList;
    Context context;
    ArrayList<Bitmap> mentorImages;
    private static LayoutInflater inflater=null;
    FragmentManager manager;
    boolean b=false;

    public ButtonListAdapter(Activity mainActivity, ArrayList<String> mentorNameList,ArrayList<String> UIDs, ArrayList<Bitmap> mentorImages, ArrayList<String> collegeNameList, FragmentManager manager){
        this.mentorNameList=mentorNameList;
        context=mainActivity;
        this.mentorImages=mentorImages;
        this.collegeNameList=collegeNameList;
        this.UIDs = UIDs;
        this.manager = manager;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ButtonListAdapter(Activity mainActivity, ArrayList<String> mentorNameList,ArrayList<String> UIDs, ArrayList<Bitmap> mentorImages, ArrayList<String> collegeNameList, FragmentManager manager,boolean b){
        this.mentorNameList=mentorNameList;
        context=mainActivity;
        this.mentorImages=mentorImages;
        this.collegeNameList=collegeNameList;
        this.UIDs = UIDs;
        this.manager = manager;
        this.b=b;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mentorNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView nameTV;
        TextView collegeName;
        ImageView img;
        TextView button;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.other_mentors_list, null);
        holder.nameTV = rowView.findViewById(R.id.mentorNameTextViewList);
        holder.collegeName =rowView.findViewById(R.id.collegeNameTextViewList);
        holder.img = rowView.findViewById(R.id.mentorImageView2);
        holder.button = rowView.findViewById(R.id.viewMentor);
        holder.button.setTag(position);

        holder.nameTV.setText(mentorNameList.get(position));
        holder.collegeName.setText(collegeNameList.get(position));
        holder.img.setImageBitmap(mentorImages.get(position));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("UID", UIDs.get((int)v.getTag()));
                if(!b) {
                    MentorProfileFragment nextFrag = new MentorProfileFragment();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_mentor, nextFrag ); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                }else{
                    MenteeProfileFragment nextFrag = new MenteeProfileFragment();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_mentor, nextFrag ); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                }
            }
        });

        return rowView;
    }
}
