package com.example.sunwoo.fileio;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileIO extends AppCompatActivity {

    TextView readOutput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //FileOutput1();
        //FileInput1();
        RawFileInput1();
        //SDCardFileOutput1();
        //SDCardFileInput1();
    }

    public void FileOutput1() {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("filename.txt", Context.MODE_PRIVATE);
            fos.write(new String("test test").getBytes());
        }
        catch (FileNotFoundException e) {
            Log.e("CreateFile", e.getLocalizedMessage());
        }
        catch (IOException e) {
            Log.e("CreateFile", e.getLocalizedMessage());
        }
        finally {
            if(fos != null) {
                try {
                    fos.flush();
                    fos.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    public void FileInput1() {
        readOutput = (TextView) findViewById(R.id.readOutput);
        FileInputStream fis = null;

        try {
            fis = openFileInput("filename.txt");
            byte[] reader = new byte[fis.available()];

            while (fis.read(reader) != -1)
                this.readOutput.setText(new String(reader));
        }
        catch (IOException e) {
            Log.e("ReadFile", e.getLocalizedMessage(), e);
        }
        finally {
            if(fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    public void RawFileInput1() {
        readOutput = (TextView) findViewById(R.id.readOutput);
        InputStream is = null;
        Resources resources = getResources();

        try {
            is = resources.openRawResource(R.raw.rawdatafile);
            byte[] reader = new byte[is.available()];


            while (is.read(reader) != -1)
                this.readOutput.setText(new String(reader));
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
    }

    public void SDCardFileOutput1() {
        String fileName = "testfile-" + System.currentTimeMillis() + ".txt";
        File sdDir = new File("/sdcard/");

        if (sdDir.exists() && sdDir.canWrite()) {
            File uadDir = new File(sdDir.getAbsolutePath() + "/unlocking_android");

            uadDir.mkdir();

            if (uadDir.exists() && uadDir.canWrite()) {
                File file = new File(uadDir.getAbsolutePath() + "/" + fileName);

                try {
                    file.createNewFile();

                    if (file.exists() && file.canWrite()) {
                        FileOutputStream fos = null;
                        fos = new FileOutputStream(file);

                        fos.write("I fear you speak upon th rack.".getBytes());

                        if(fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    }
                }
                catch (IOException e) {
                    Log.e("ReadWritheSDCardFile", "ERROR", e);
                }
            }
        }
    }

    public void SDCardFileInput1() {
        readOutput = (TextView) findViewById(R.id.readOutput);

        File rfile = new File("/sdcard/unlocking_android/" + "testfile-1280042001515.txt");

        if (rfile.exists() && rfile.canRead()) {
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(rfile);
                byte[] reader = new byte[fis.available()];

                while (fis.read(reader) != -1) {}

                String str = new String(reader);

                if(fis != null) {
                    fis.close();
                }

                this.readOutput.setText(str);
            }
            catch (IOException e) {
                Log.e("ReadWhiteSDCardFile", "ERROR", e);
            }
        }
    }
}
