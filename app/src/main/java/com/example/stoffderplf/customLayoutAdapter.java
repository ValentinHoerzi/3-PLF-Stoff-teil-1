package com.example.stoffderplf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class customLayoutAdapter extends BaseAdapter {

    private List<String> items;
    private List<String> itemHolder;
    private int layoutId;
    private LayoutInflater inflater;

    public customLayoutAdapter(Context context, int layoutId, List<String> data) {
        this.itemHolder = data;
        this.items = new ArrayList<>(itemHolder);
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void filter(String text) {
        if(text.equals(" ")){
            items.clear();
            items.addAll(itemHolder);
        }else{
            items.clear();
            for(String item : itemHolder){
                if(item.contains(text)){
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        String s = items.get(position);
        View listItem = (view == null)
                ?inflater.inflate(layoutId,null)
                :view;
        ((TextView)listItem.findViewById(R.id.textView1)).setText(s);
        ((TextView)listItem.findViewById(R.id.textView2)).setText(s);
        ((TextView)listItem.findViewById(R.id.textView3)).setText(s);
        ((TextView)listItem.findViewById(R.id.textView4)).setText(s);
        return  listItem;
    }
}
