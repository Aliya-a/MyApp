package com.example.myapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    com.example.myapp.DBService myDb;
    private Button btnCancel;
    private Button btnSave;
    private EditText titleEditText;
    private EditText contentEditText;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        init();
        if(timeTextView.getText().length()==0)
            timeTextView.setText(getTime());

    }

    private void init() {

        myDb = new com.example.myapp.DBService(this);
        SQLiteDatabase db = myDb.getReadableDatabase();
        titleEditText = findViewById(R.id.et_title);
        contentEditText = findViewById(R.id.et_content);
        timeTextView = findViewById(R.id.edit_time);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        //按钮点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.myapp.EditActivity.this, com.example.myapp.MainActivity.class);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = myDb.getWritableDatabase();
                ContentValues values = new ContentValues();

                String title= titleEditText.getText().toString();
                String content=contentEditText.getText().toString();
                String time= timeTextView.getText().toString();

               if("".equals(titleEditText.getText().toString())){
                    Toast.makeText(com.example.myapp.EditActivity.this,"标题不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if("".equals(contentEditText.getText().toString())) {
                    Toast.makeText(com.example.myapp.EditActivity.this,"内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                values.put(com.example.myapp.DBService.TITLE,title);
                values.put(com.example.myapp.DBService.CONTENT,content);
                values.put(com.example.myapp.DBService.TIME,time);
                db.insert(com.example.myapp.DBService.TABLE,null,values);
                Toast.makeText(com.example.myapp.EditActivity.this,"保存成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(com.example.myapp.EditActivity.this,com.example.myapp.MainActivity.class);
                startActivity(intent);
                db.close();
            }
        });
    }

    //获取当前时间
    public static final String getTime(){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date=new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }
}
