package com.drishi.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.drishi.todoapp.R;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditItem;
    TextView tvEditItem;
    Spinner spTaskPriority;
    int position;

    public ArrayList<String> getPriorities() {
        ArrayList<String> ret = new ArrayList<String>();
        ret.add("HIGH");
        ret.add("NORMAL");
        ret.add("LOW");
        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemBody = getIntent().getStringExtra("item_body");
        spTaskPriority = (Spinner) findViewById(R.id.spTaskPriority);
        String itemPriority = getIntent().getStringExtra("item_priority");
        spTaskPriority.setSelection(getPriorities().indexOf(itemPriority));
        position = getIntent().getIntExtra("item_position", 1);
        etEditItem.setText(itemBody);

        // Set the cursor to the end
        if (itemBody.length() != 0){
            etEditItem.setSelection(itemBody.length());
        }
    }

    public void onSave(View view) {
        Intent data = new Intent();
        data.putExtra("item_body", etEditItem.getText().toString());
        data.putExtra("item_position", position);
        data.putExtra("item_priority", spTaskPriority.getSelectedItem().toString());
        setResult(200, data);
        this.finish();
    }
}
