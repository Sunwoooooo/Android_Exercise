package com.example.sunwoo.makelist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AudioListActivity extends AppCompatActivity {

    ListArrayAdapter adtAudio;
    ArrayList<Itemlist> aryAudioList;
    //ArrayAdapter<String> adtAudio;
    //ArrayList<String> aryAudioList;
    ListView audiolist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiolist);

        getMediaStore();
        audiolist = (ListView) findViewById(R.id.listView_audio);
        audiolist.setAdapter(adtAudio);

        audiolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AudioListActivity.this, MyAudio.class);

                String tv = (String)parent.getAdapter().getItem(position);
                Log.i("tv : ", tv);
                Toast.makeText(getApplicationContext(), tv + " 실행", Toast.LENGTH_SHORT).show();

                intent.putExtra("audio_path", tv);
                startActivity(intent);
            }
        });
    }

    private void getMediaStore() {
        aryAudioList = new ArrayList<>();
        adtAudio = new ListArrayAdapter(this, R.layout.audiolist_item, aryAudioList);

        String[] requestedColumns = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };

        Cursor cur = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                requestedColumns, null, null, null);

        int name = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int data = cur.getColumnIndex(MediaStore.Audio.Media.DATA);

        cur.moveToFirst();

        while (!cur.isAfterLast()) {
            aryAudioList.add(new Itemlist(cur.getString(data), cur.getString(name)));
            Log.i("audio_name", cur.getString(name));
            Log.i("audio_path", cur.getString(data));
            cur.moveToNext();
        }
    }

    class Itemlist {
        String Audio_Path;
        String Audio_Name;

        public Itemlist (String Audio_Path, String Audio_Name) {
            this.Audio_Path = Audio_Path;
            this.Audio_Name = Audio_Name;
        }

        public String getAudioPath() {
            return Audio_Path;
        }

        public String getAudioName() {
            return Audio_Name;
        }
    }

    class ListArrayAdapter extends BaseAdapter {

        private ArrayList<Itemlist> itemlist = null;
        private int layoutID;

        public ListArrayAdapter(Context context, int layoutID, ArrayList<Itemlist> dataSet) {
            this.itemlist = dataSet;
            this.layoutID = layoutID;
        }

        @Override
        public int getCount() {
            return itemlist.size();
        }

        @Override
        public Object getItem(int position) {
            return itemlist.get(position).Audio_Path;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            TextView list_audiopath;
            TextView list_audioname;

            if(v == null) {
                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(this.layoutID, null);
            }

            Itemlist item = itemlist.get(position);

            list_audiopath = (TextView) v.findViewById(R.id.itemlist_audiopath);
            list_audioname = (TextView) v.findViewById(R.id.itemlist_audioname);

            if(item != null) {
                list_audiopath.setText(item.getAudioPath());
                list_audioname.setText(item.getAudioName());
            }

            return v;
        }
    }
}
