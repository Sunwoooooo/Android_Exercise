package com.example.sunwoo.dbsqlite1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;

public class DBSQLite1 extends AppCompatActivity {

    SQLiteDatabase myDB;
    SimpleAdapter myADT;
    ArrayList<String> aryMBRList;
    ArrayAdapter<String> adtMembers;
    ListView firstView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String strRecord = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbsqlite1);

        //DB 생성
        myDB = this.openOrCreateDatabase("PhoneBook", MODE_PRIVATE, null);
        myDB.execSQL("Drop table if exists members");

        //Table 생성 (Table 이름 : members)
        myDB.execSQL("Create table members (" +
        " _id integer primary key autoincrement, " +
        "Name text not null, " + "Phone_No text not null);" );

        //Data 저장 ("sunwoo", "010-6605-0703")
        myDB.execSQL("Insert into members " +
        " (Name, Phone_No) values ('sunwoo', '010-6605-0703');");

        //members 테이블에서 Data 저장
        ContentValues insertValue = new ContentValues();
        ContentValues insertValue2 = new ContentValues();
        insertValue.put("Name", "Juliet");
        insertValue.put("Phone_No", "010-1122-3344");
        insertValue2.put("Name", "Romio");
        insertValue2.put("Phone_No", "010-110-1234");
        myDB.insert("members", null, insertValue);
        myDB.insert("members", null, insertValue2);

        //members 테이블에서 모든 Record Data 가져오기
        Cursor allRCD = myDB.query("members", null, null, null, null, null, null, null);

        //ArrayList 생성
        aryMBRList = new ArrayList<String>();
        if (allRCD != null) {
            if(allRCD.moveToFirst()) {
                do{
                    strRecord = allRCD.getString(1) + "\t\t" + allRCD.getString(2);
                    aryMBRList.add(strRecord);
                }while(allRCD.moveToNext());
            }
        }
        adtMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryMBRList);

        //ListView 생성
        firstView = (ListView) findViewById(R.id.firstmember);
        firstView.setAdapter(adtMembers);
        firstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //DB 연결 해제
        if(myDB != null)
            myDB.close();
    }
}
