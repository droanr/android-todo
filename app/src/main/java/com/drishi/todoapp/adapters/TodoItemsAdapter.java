package com.drishi.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.drishi.todoapp.R;
import com.drishi.todoapp.models.TodoTask;

import java.util.ArrayList;

/**
 * Created by drishi on 7/24/16.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoTask> {
    public TodoItemsAdapter(Context context, ArrayList<TodoTask> tasks) {
        super (context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoTask task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo_task, parent, false);
        }

        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);

        tvDescription.setText(task.taskDescription);
        tvPriority.setText(task.taskPriority);

        return convertView;
    }
}
