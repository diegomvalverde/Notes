package com.project.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    String url ="http://192.168.0.3:3080/";
    Button deleteButton;
    EditText titleInput;

    private Note note;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing variables
        notes = new ArrayList<>();

        // Finding elements by its id
        listView = findViewById(R.id.listView);
        Button saveButton = findViewById(R.id.saveButtom);

        deleteButton = findViewById(R.id.deleteButton);
        this.titleInput = findViewById(R.id.titileTextField);

        // Getting all notes from backend
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, (url + "getAllNotes"), null,
                response -> {
                    System.out.println(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject note = response.getJSONObject(i);
                            String description = note.getString("description");
                            int id = note.getInt("id");
                            notes.add(new Note(description, id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    reloadItems();

                    notesReady();
                }, System.out::println);

        queue.add(req);

        setUpListViewListener();

        saveButton.setOnClickListener(this::addNote);
        this.deleteButton.setOnClickListener(this::deleteNote);
    }

    private void notesReady()
    {
        // Setting adapter
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        // Setting up adapter
        listView.setAdapter(itemsAdapter);
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Context context = getApplicationContext();

            System.out.println(items.get(position));
            System.out.println(notes.get(position).getDescription());

            note = notes.get(position);
            pos = position;
            editNote();

//                Removing note from list view
            items.remove(position);
            itemsAdapter.notifyDataSetChanged();

            return true;
        });


    }

    private void reloadItems()
    {
        items = new ArrayList<>();
        for (Note note:
                notes)
        {
            items.add(note.getDescription());
        }
    }

    private void deleteNote(View v)
    {
        System.out.println(note.getId());
        // Getting all notes from backend
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, (url + "deleteNote" + "/" + note.getId()), null,
                response -> {
                    this.note = null;
                    this.notes.remove(pos);
//                    this.pos = Integer.parseInt(null);
                    reloadItems();
                    notesReady();
                }, System.out::println);
        deleteButton.setVisibility(View.GONE);

        queue.add(req);

        this.titleInput.setText("");
    }

    private void addNote(View v) {

        String titleText = this.titleInput.getText().toString();

        if (this.note != null)
        {
            this.deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setVisibility(View.GONE);

            String text = titleInput.getText().toString();

            // Getting all notes from backend
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                    (url + "modifyNote" + "/" + note.getId() + "/" + text),
                    null,
                    response -> {

                        Note tmp = new Note(text, this.note.getId());
                        this.notes.remove(this.pos);
                        this.notes.add(tmp);
                        this.note = null;

                        reloadItems();
                        notesReady();
                    }, System.out::println);
            deleteButton.setVisibility(View.GONE);

            queue.add(req);

            this.titleInput.setText("");
        }
        else
        {
            System.out.println(titleText);
            if (!titleText.equals(""))
            {
//            itemsAdapter.add(titleText);
                // Getting all notes from backend
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, (url + "addNote" + "/" + titleText), null,
                        response -> {
                            System.out.println(response);
                            try {
                                int id = response.getInt("insertId");
                                notes.add(new Note(titleText, id));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            items = new ArrayList<>();
                            for (Note note:
                                    notes)
                            {
                                items.add(note.getDescription());
                            }

                            notesReady();
                        }, System.out::println){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<>();
                        params.put("description", titleText);
                        return params;
                    }
                };
                queue.add(req);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "The title can't be empty", Toast.LENGTH_LONG).show();
            }

        }
        titleInput.setText("");

    }

    private void editNote()
    {

        this.titleInput.setText(this.note.getDescription());

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);
    }
}