package com.example.sunwoo.dbsqlite2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DBSQLite2 extends AppCompatActivity {

    SQLiteDatabase myDB;
    SimpleAdapter myADT;
    ArrayList<String> aryMBRList;
    ArrayAdapter<String> adtMembers;
    TextView[] objTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        myDB = this.openOrCreateDatabase("PhoneBook", MODE_PRIVATE, null);
        myDB.execSQL("Drop table if exists members");

        myDB.execSQL("Create table members (" + "_id integer primary key autoincrement, " +
        "Name text not null, " + "Phone_No text not null);");

        myDB.execSQL("Insert into members " +
        " (Name, Phone_No) Values ('sunwoo', '010-6605-0703');");

        ContentValues insertValues = new ContentValues();
        insertValues.put("Name", "Juliet");
        insertValues.put("Phone_No", "010-123-1234");
        myDB.insert("members", null, insertValues);

        insertValues.put("Name", "Romio");
        insertValues.put("Phone_No", "010-100-1234");
        myDB.insert("members", null, insertValues);

        Cursor allRCD = myDB.query("members", null, null, null, null, null, null, null);

        aryMBRList = new ArrayList<String>();

        if (allRCD != null) {
            if (allRCD.moveToFirst()) {
                do {
                    aryMBRList.add(allRCD.getString(1));
                    aryMBRList.add(allRCD.getString(2));
                }while (allRCD.moveToNext());
            }
        }

        objTV = new TextView[8];
        objTV[0] = (TextView) findViewById(R.id.textview01);
        objTV[1] = (TextView) findViewById(R.id.textview02);
        objTV[2] = (TextView) findViewById(R.id.textview03);
        objTV[3] = (TextView) findViewById(R.id.textview04);
        objTV[4] = (TextView) findViewById(R.id.textview05);
        objTV[5] = (TextView) findViewById(R.id.textview06);
        objTV[6] = (TextView) findViewById(R.id.textview07);
        objTV[7] = (TextView) findViewById(R.id.textview08);

        for(int i=0; i<aryMBRList.size(); i++)
            objTV[i].setText(aryMBRList.get(i).toString());

        if(myDB != null)
            myDB.close();
    }
}
