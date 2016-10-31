package com.example.admin.wifihelper.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.admin.wifihelper.Main.GridView.addDataToGridView;
import com.example.admin.wifihelper.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class mainActivity extends Activity {
           addDataToGridView addDataToGridView;
           GridView          main_view;
           Button            button;
    static Context           context;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        main_view = (GridView)findViewById(R.id.gridView);

        addDataToGridView = new addDataToGridView(this.getBaseContext(),main_view);

        addDataToGridView.show();


    }

    public static void makeTaost(String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}

