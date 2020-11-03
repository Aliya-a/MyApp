package com.example.myapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowActivity extends AppCompatActivity {

    private Button btnSave;
    private Button btnCancel;
    private TextView showTime;
    private EditText showContent;
    private EditText showTitle;

    private Values value;
    com.example.myapp.DBService myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        init();
    }

    public void init() {
        myDb = new com.example.myapp.DBService(this);
        btnCancel = findViewById(R.id.show_cancel);
        btnSave = findViewById(R.id.show_save);
        showTime = findViewById(R.id.show_time);
        showTitle = findViewById(R.id.show_title);
        showContent = findViewById(R.id.show_content);

        Intent intent = this.getIntent();
        if (intent != null) {
            value = new Values();

            value.setTime(intent.getStringExtra(com.example.myapp.DBService.TIME));
            value.setTitle(intent.getStringExtra(com.example.myapp.DBService.TITLE));
            value.setContent(intent.getStringExtra(com.example.myapp.DBService.CONTENT));
            value.setId(Integer.valueOf(intent.getStringExtra(com.example.myapp.DBService.ID)));

            showTime.setText(value.getTime());
            showTitle.setText(value.getTitle());
            showContent.setText(value.getContent());
        }

        //按钮点击事件
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDb.getWritableDatabase();
                ContentValues values = new ContentValues();
                String content = showContent.getText().toString();
                String title = showTitle.getText().toString();

                values.put(com.example.myapp.DBService.TIME, getTime());
                values.put(com.example.myapp.DBService.TITLE,title);
                values.put(com.example.myapp.DBService.CONTENT,content);

                db.update(com.example.myapp.DBService.TABLE,values, com.example.myapp.DBService.ID+"=?",new String[]{value.getId().toString()});
                Toast.makeText(com.example.myapp.ShowActivity.this,"修改成功", Toast.LENGTH_LONG).show();
                db.close();
                Intent intent = new Intent(com.example.myapp.ShowActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = showContent.getText().toString();
                final String title = showTitle.getText().toString();
                new AlertDialog.Builder(com.example.myapp.ShowActivity.this)
                        .setTitle("提示框")
                        .setMessage("是否保存当前内容?")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = myDb.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(com.example.myapp.DBService.TIME, getTime());
                                values.put(com.example.myapp.DBService.TITLE,title);
                                values.put(com.example.myapp.DBService.CONTENT,content);
                                db.update(com.example.myapp.DBService.TABLE,values, com.example.myapp.DBService.ID+"=?",new String[]{value.getId().toString()});
                                Toast.makeText(com.example.myapp.ShowActivity.this,"修改成功", Toast.LENGTH_LONG).show();
                                db.close();
                                Intent intent = new Intent(com.example.myapp.ShowActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(com.example.myapp.ShowActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }

    String getTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
