package com.drishi.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditItem;
    TextView tvEditItem;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemBody = getIntent().getStringExtra("item_body");
        position = getIntent().getIntExtra("item_position", 1);
        etEditItem.setText(itemBody);

        // Set the cursor to the end
        etEditItem.setSelection(itemBody.length());
    }

    public void onSave(View view) {
        Intent data = new Intent();
        data.putExtra("item_body", etEditItem.getText().toString());
        data.putExtra("item_position", position);
        setResult(200, data);
        this.finish();
    }
}
