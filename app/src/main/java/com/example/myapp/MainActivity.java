package com.example.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    com.example.myapp.DBService myDb;
    private Button mBtnAdd;
    private ListView lv_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new com.example.myapp.DBService(this);
        init();
    }
    public void init(){


        mBtnAdd = findViewById(R.id.btn_add);
        lv_note = findViewById(R.id.lv_note);
        List< com.example.myapp.Values > valuesList = new ArrayList<>();
        SQLiteDatabase db = myDb.getReadableDatabase();

        //查询数据库中的数据
        Cursor cursor = db.query(com.example.myapp.DBService.TABLE,null,null,
        null,null,null,null);
        if(cursor.moveToFirst()){
            com.example.myapp.Values values;
            while (!cursor.isAfterLast()){
                //实例化values对象
                values = new com.example.myapp.Values();

                //把数据库中的一个表中的数据赋值给values
                values.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(com.example.myapp.DBService.ID))));
                values.setTitle(cursor.getString(cursor.getColumnIndex(com.example.myapp.DBService.TITLE)));
                values.setContent(cursor.getString(cursor.getColumnIndex(com.example.myapp.DBService.CONTENT)));
                values.setTime(cursor.getString(cursor.getColumnIndex(com.example.myapp.DBService.TIME)));

                //将values对象存入list对象数组中
                valuesList.add(values);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        //设置list组件adapter
        final MyBaseAdapter myBaseAdapter = new MyBaseAdapter(valuesList,this,R.layout.note_item);
        lv_note.setAdapter(myBaseAdapter);

        //按钮点击事件
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.myapp.MainActivity.this, com.example.myapp.EditActivity.class);
                startActivity(intent);
            }
        });

        //单击查询
        lv_note.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(com.example.myapp.MainActivity.this, com.example.myapp.ShowActivity.class);
                com.example.myapp.Values values = (com.example.myapp.Values) lv_note.getItemAtPosition(position);
                intent.putExtra(com.example.myapp.DBService.TITLE,values.getTitle().trim());
                intent.putExtra(com.example.myapp.DBService.CONTENT,values.getContent().trim());
                intent.putExtra(com.example.myapp.DBService.TIME,values.getTime().trim());
                intent.putExtra(com.example.myapp.DBService.ID,values.getId().toString().trim());
                startActivity(intent);
            }
        });


        //双击删除
        lv_note.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final com.example.myapp.Values values = (com.example.myapp.Values) lv_note.getItemAtPosition(position);
                new AlertDialog.Builder(com.example.myapp.MainActivity.this)
                        .setTitle("提示框")
                        .setMessage("是否删除?")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SQLiteDatabase db = myDb.getWritableDatabase();
                                        db.delete(com.example.myapp.DBService.TABLE, com.example.myapp.DBService.ID+"=?",new String[]{String.valueOf(values.getId())});
                                        db.close();
                                        myBaseAdapter.removeItem(position);
                                        lv_note.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                myBaseAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        //MainActivity.this.onResume();
                                    }
                                })
                        .setNegativeButton("否",null).show();
                return true;
            }
        });
    }

    class MyBaseAdapter extends BaseAdapter {

        private List< com.example.myapp.Values > valuesList;
        private Context context;
        private int layoutId;

        public MyBaseAdapter(List< com.example.myapp.Values > valuesList, Context context, int layoutId) {
            this.valuesList = valuesList;
            this.context = context;
            this.layoutId = layoutId;
        }

        @Override
        public int getCount() {
            if (valuesList != null && valuesList.size() > 0)
                return valuesList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (valuesList != null && valuesList.size() > 0)
                return valuesList.get(position);
            else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.note_item, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.content = convertView.findViewById(R.id.tv_content);
                viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String title = valuesList.get(position).getTitle();
            String content = valuesList.get(position).getContent();
            viewHolder.title.setText(title);
            viewHolder.content.setText(content);
            viewHolder.time.setText(valuesList.get(position).getTime());
            return convertView;
        }

        public void removeItem(int position){
            this.valuesList.remove(position);
        }

    }
    class ViewHolder{
        TextView title;
        TextView content;
        TextView time;
    }
}


