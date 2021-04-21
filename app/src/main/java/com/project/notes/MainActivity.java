package com.proyecto.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button saveButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing variables
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        // Finding elements by its id
        listView = findViewById(R.id.listView);
        saveButtom = findViewById(R.id.saveButtom);

        // Setting up adapter
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();

        saveButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote(v);
            }
        });


    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item has been removed", Toast.LENGTH_LONG).show();

//                Removing note from list view
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void addNote(View v) {
        EditText titleInput = findViewById(R.id.titleTextField);
        String titleText = titleInput.getText().toString();

        if (!titleText.equals(""))
        {
            itemsAdapter.add(titleText);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "The title can't be empty", Toast.LENGTH_LONG).show();
        }
    }
}