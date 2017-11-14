package com.example.sunwoo.makelist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {

    ArrayList<Itemlist> aryImageList;
    ListArrayAdapter adtImages;
    ListView imagelist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagelist);

        getImageStore();
        imagelist = (ListView) findViewById(R.id.listView_image);
        imagelist.setAdapter(adtImages);

        imagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImageListActivity.this, MyImage.class);

                String tv = (String)parent.getAdapter().getItem(position);
                Log.i("image_path", tv);
                Toast.makeText(getApplicationContext(), tv + " 실행", Toast.LENGTH_SHORT).show();

                intent.putExtra("image_path", tv);
                startActivity(intent);
            }
        });
    }

    private void getImageStore() {
        aryImageList = new ArrayList<>();
        adtImages = new ListArrayAdapter(this, R.layout.imagelist_item, aryImageList);

        String[] filedata = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
        };

        //getApplicationContext().grantUriPermission(getCallingPackage(),
        //        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                filedata, "", null, "");

        if(cursor.getCount() > 0) {
            cursor.moveToLast();

            int data = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);

            for(int i=0; i<cursor.getCount(); i++) {
                aryImageList.add(new Itemlist(cursor.getString(data), cursor.getInt(id)));
                cursor.moveToPrevious();
            }
        }
    }

    class Itemlist {
        String Image_Path;
        int Image_ID;

        public Itemlist (String Image_Path, int Image_ID) {
            this.Image_Path = Image_Path;
            this.Image_ID = Image_ID;
        }

        public String getImagePath() {
            return Image_Path;
        }

        public int getImageID() {
            return Image_ID;
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
            return itemlist.get(position).getImagePath();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ImageView list_thumbnail;
            TextView list_imagename;

            if(v == null) {
                LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(this.layoutID, null);
            }

            Itemlist item = itemlist.get(position);
            list_thumbnail = (ImageView) v.findViewById(R.id.itemlist_image);
            list_imagename = (TextView) v.findViewById(R.id.itemlist_text);

            if(item != null) {
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                        itemlist.get(position).getImageID(),
                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
                list_thumbnail.setImageBitmap(bitmap);
                list_imagename.setText(itemlist.get(position).getImagePath());
            }

            return v;
        }
    }
}

