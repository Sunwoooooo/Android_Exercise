package com.example.sunwoo.databaseexercise;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase myDB;
    SimpleAdapter myADT;
    ArrayList<String> aryMBRList;
    ArrayAdapter<String> adtMembers;
    EditText input_name;
    EditText input_phoneno;
    Button button_insert;
    ListView listView;
    Cursor allRCD;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //DB 생성
        myDB = this.openOrCreateDatabase("PhoneBook", MODE_PRIVATE, null);
        myDB.execSQL("Drop table if exists members");

        //Table 생성
        myDB.execSQL(RawFileInput1());

        //Data 저장
        myDB.execSQL(RawFileInput2());
        myDB.execSQL(RawFileInput3());

        getAllRecord();

        aryMBRList = new ArrayList<String>();
        makeArrayList();

        adtMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryMBRList);

        makeListView();

        button_insert = (Button) findViewById(R.id.insertbutton);
        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_name = (EditText) findViewById(R.id.input_Name);
                input_phoneno = (EditText) findViewById(R.id.input_PhoneNo);

                ContentValues values = new ContentValues();
                values.put("Name", input_name.getText().toString());
                values.put("PhoneNo", input_phoneno.getText().toString());
                myDB.insert("members", null, values);

                getAllRecord();
                addArrayList();
                adtMembers.notifyDataSetChanged();

                Toast.makeText(getBaseContext(), "Name : " +
                        values.getAsString("Name") + " PhoneNo : " +
                        input_phoneno.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String RawFileInput1() {
        String createTable = "";

        InputStream is = null;
        Resources resources = getResources();

        try {
            is = resources.openRawResource(R.raw.rawcreatetable);
            byte[] reader = new byte[is.available()];


            while (is.read(reader) != -1)
                createTable = new String(reader);
        }
        catch (IOException e) {
            Log.e("ReadFile", e.getLocalizedMessage(), e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
            }
        }

        return createTable;
    }

    public String RawFileInput2() {
        String insertValues = "";

        InputStream is = null;
        Resources resources = getResources();

        try {
            is = resources.openRawResource(R.raw.rawinsertvalues1);
            byte[] reader = new byte[is.available()];


            while (is.read(reader) != -1)
                insertValues = new String(reader);
        }
        catch (IOException e) {
            Log.e("ReadFile", e.getLocalizedMessage(), e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return insertValues;
    }

    public String RawFileInput3() {
        String insertValues = "";

        InputStream is = null;
        Resources resources = getResources();

        try {
            is = resources.openRawResource(R.raw.rawinsertvalues2);
            byte[] reader = new byte[is.available()];


            while (is.read(reader) != -1)
                insertValues = new String(reader);
        }
        catch (IOException e) {
            Log.e("ReadFile", e.getLocalizedMessage(), e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return insertValues;
    }

    public void getAllRecord() {
        allRCD = myDB.query("members", null, null, null, null, null, null, null);
    }

    public void makeArrayList() {
        String strRecord = null;

        if (allRCD != null) {
            if(allRCD.moveToFirst()) {
                do{
                    strRecord = allRCD.getString(1) + "\t\t" + allRCD.getString(2);
                    aryMBRList.add(strRecord);
                }while(allRCD.moveToNext());
            }
        }
    }

    public void addArrayList() {
        String strRecord = null;

        if(allRCD != null) {
            if(allRCD.moveToLast()) {
                strRecord = allRCD.getString(1) + "\t\t" + allRCD.getString(2);
                aryMBRList.add(strRecord);
            }
        }
    }

    public void makeListView() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adtMembers);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
