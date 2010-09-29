package com.pivotallabs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class NamesActivity extends Activity {

    private static ArrayList<String> names = new ArrayList<String>();
    static {
        names.add("Lauren Conrad");
        names.add("Heidi Montag");
        names.add("Kim Kardashian");
        names.add("Lindsey Lohan");
        names.add("Justin Bieber");
        names.add("Sponge Bob Square Pants");
        names.add("Kai Lan");
        names.add("Polly Pockets");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.names_list_layout);

        NamesAdapter adapter = new NamesAdapter(names);

        ((ListView) findViewById(R.id.names_list)).setAdapter(adapter);
    }
}
