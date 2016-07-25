package com.drishi.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.drishi.todoapp.R;
import com.drishi.todoapp.adapters.TodoItemsAdapter;
import com.drishi.todoapp.models.TodoTask;
import com.drishi.todoapp.utils.ToDoDatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoTask> todoItems = new ArrayList<TodoTask>();
    TodoItemsAdapter aToDoAdapter;
    ListView lvItems;
    EditText etEditText;

    private final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItem(position);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("item_body", todoItems.get(position).taskDescription);
                i.putExtra("item_position", position);
                i.putExtra("item_priority", todoItems.get(position).taskPriority);
                startActivityForResult(i, REQUEST_CODE);

            }
        });
    }

    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new TodoItemsAdapter(this, todoItems);
    }

    public void onAddItem(View view) {
        String todoText = etEditText.getText().toString().trim();
        String taskPriority = "NORMAL";
        if (todoText.length() != 0) {
            TodoTask task = new TodoTask(todoText, taskPriority);
            aToDoAdapter.add(task);
            ToDoDatabaseHelper helper = ToDoDatabaseHelper.getInstance(this);
            helper.addItem(task);
            etEditText.setText("");
        }
        else {
            etEditText.setText("");
        }
    }

    public void readItems() {
        ToDoDatabaseHelper helper = ToDoDatabaseHelper.getInstance(this);
        ArrayList<TodoTask> tasks = helper.getAllItems();
        for (TodoTask t: tasks) {
            todoItems.add(t);
        }
    }

    public void removeItem(int position) {
        TodoTask t = todoItems.remove(position);
        ToDoDatabaseHelper helper = ToDoDatabaseHelper.getInstance(this);
        helper.deleteTodoTask(t);
        aToDoAdapter.notifyDataSetChanged();
    }

    public void updateItem(int position, String taskDescription, String taskPriority) {
        ToDoDatabaseHelper helper = ToDoDatabaseHelper.getInstance(this);
        TodoTask todoItem = todoItems.get(position);
        todoItem.taskDescription = taskDescription;
        todoItem.taskPriority = taskPriority;
        helper.updateItem(todoItems.get(position));
        todoItems.set(position, todoItem);
        aToDoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 200) {
            String item_body = data.getExtras().getString("item_body").trim();
            String item_priority = data.getExtras().getString("item_priority");
            int position = data.getExtras().getInt("item_position");
            if (item_body.length() == 0) {
                removeItem(position);
            } else {
                updateItem(position, item_body, item_priority);
            }
        }

    }
}
