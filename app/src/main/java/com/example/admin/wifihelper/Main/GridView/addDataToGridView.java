package com.example.admin.wifihelper.Main.GridView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.admin.wifihelper.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/17.
 */

public class addDataToGridView{
    GridView                          gridView;
    SimpleAdapter                     adapter;
    ArrayList<HashMap<String,String>> list;
    HashMap<String,String>            map;

    int[] to ={
            R.id.WifiName,
            R.id.number
    };

    String[] from = {
        "WifeName",
            "number"
    };

    String[] number = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
    };

    Context context;


    public addDataToGridView(Context context, GridView gridView){
        this.context = context;
        this.gridView = gridView;
    }


    public List<HashMap<String,String>> getDataSource(){
        list = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            map = new HashMap<>();
            map.put("WifiName", " ");
            map.put("number", number[i]);
            list.add(map);
        }

        return list;
    }

    public SimpleAdapter setAdapter(){
        adapter = new SimpleAdapter(context,getDataSource(),R.layout.item,from,to);
        return adapter;
    }

    public void show(){ //show the gridview
        setAdapter();
        gridView.setAdapter(adapter);
    }

    public void getNewData(int from, int to){// 改变list里元素的位置从from变到to(from插入to的位置)
        list.add(to,list.get(from));
        if(from > to){
            list.remove(from+1);
        }
        if(from < to){
            list.remove(from);
        }

        adapter.notifyDataSetChanged();
    }
}

