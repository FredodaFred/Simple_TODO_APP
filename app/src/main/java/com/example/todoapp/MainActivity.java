package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdaptor adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.add_button);
        etItem = findViewById(R.id.inputText);
        rvItems = findViewById(R.id.recyclerView);

        loadItems();

        ItemsAdaptor.OnLongClickListener onLongClickListener = new ItemsAdaptor.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position){
                //Delete the item from the data
                items.remove(position);
                //notify the adaptor
                adaptor.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdaptor.OnClickListener clickListener = new ItemsAdaptor.OnClickListener(){
            @Override
            public void onItemClicked(int position){
                Log.d("MainActivity", "Single click at position " +position);
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass data being edited to the intent
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display activity
                startActivityForResult(i, EDIT_TEXT_CODE);
                adaptor.notifyItemChanged(position);
                Toast.makeText(getApplicationContext(), "Item was updated", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        adaptor = new ItemsAdaptor(items, onLongClickListener, clickListener);
        rvItems.setAdapter(adaptor);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //ad item to the data
                items.add(todoItem);
                //notify adapter that there is a new item
                adaptor.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //return the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //update the model at the right position with new item text
            items.set(position, itemText);
            //notify adapter
            adaptor.notifyItemChanged(position);
            //persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }

    }

    /**
     * Gets the file where the app's data is stored
     * @return that file (data.txt)
     */
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    /**
     * Loads items by reading each line of the data file
     */
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch(IOException e){
            Log.e("MainActivity", "error reading items");
            items = new ArrayList<>();
        }
    }
    /**
     * saves the items when changes are made
     * adding and removing an item
     */
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}