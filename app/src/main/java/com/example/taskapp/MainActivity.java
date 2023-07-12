package com.example.taskapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView taskListView;
    private ArrayAdapter<String> adapter;
    public static ArrayList<String> taskList = new ArrayList<>();

    private static final int ADD_TASK_REQUEST_CODE = 1;
    private static final int SETTINGS_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListView = findViewById(R.id.taskListView);

        // Check if the taskList is empty before adding tasks
        if (taskList.isEmpty()) {
            taskList.add("Task 1");
            taskList.add("Task 2");
            taskList.add("Task 3");
        }

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Task list clicked");
                String name = taskList.get(position);
                makeToast(name);

            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                makeToast("Removed: " + taskList.get(position));
                removeItem(position);
                return true; // Return true to consume the long-click event
            }
        });




        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        taskListView.setAdapter(adapter);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTaskScreen();
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsScreen();
            }
        });

        Button markAsCompletedButton = findViewById(R.id.markAsCompletedButton);
        markAsCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markTaskAsCompleted();
            }
        });

        Button viewTaskDetailsButton = findViewById(R.id.viewTaskDetailsButton);
        viewTaskDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the clicked item
                int position = taskListView.getPositionForView(v);
                openTaskDetailsScreen(position);
            }
        });


    }

    private void openTaskDetailsScreen(int position) {
        Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
        startActivity(intent);
    }



    public void removeItem(int position) {
        String removedTask = taskList.get(position);
        taskList.remove(position);
        adapter.notifyDataSetChanged();

        makeToast("Removed: " + removedTask);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload or update the variables here
        adapter.notifyDataSetChanged();
    }

    private void openAddTaskScreen() {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
    }

    private void openSettingsScreen() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    private void markTaskAsCompleted() {
        Intent intent = new Intent(MainActivity.this, CompletedTasksActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            String newTask = data.getStringExtra("newTask");
            taskList.add(newTask);
            adapter.notifyDataSetChanged();
        } else if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean shouldClearTasks = data.getBooleanExtra("shouldClearTasks", false);
            if (shouldClearTasks) {
                taskList.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }


    Toast t;

    public void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}
