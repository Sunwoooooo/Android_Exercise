package com.example.sunwoo.cpuser;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

public class CPUser extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TextView contactView = (TextView) findViewById(R.id.textView);
        contactView.setText("");
        Cursor cursor = getContacts();

        while (cursor.moveToNext()) {
            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            contactView.append("Name: " + displayName + "\n");
        }

        return super.onKeyDown(keyCode, event);
    }

    private Cursor getContacts() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};

        return managedQuery(uri, projection, null, null, null);
    }
}
