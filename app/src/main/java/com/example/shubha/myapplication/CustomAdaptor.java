package com.example.shubha.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdaptor extends BaseAdapter {

    TextView Name1,Reg1,Trx1,Money1,DateSent1;
    Context context;
    ArrayList<CustomClass> arr;

    public CustomAdaptor(Context context, ArrayList<CustomClass> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false);
        Name1=(TextView) convertView.findViewById(R.id.customName);
        Reg1=(TextView) convertView.findViewById(R.id.customReg);
        Trx1=(TextView) convertView.findViewById(R.id.customTrx);
        Money1=(TextView) convertView.findViewById(R.id.customMoney);
        DateSent1=(TextView) convertView.findViewById(R.id.customDate);

        Name1.setText(arr.get(position).getName());
        Reg1.setText(arr.get(position).getReg());
        Trx1.setText(arr.get(position).getTrx());
        Money1.setText(arr.get(position).getMoney());
        DateSent1.setText(arr.get(position).getDate());

        return convertView;
    }
}
