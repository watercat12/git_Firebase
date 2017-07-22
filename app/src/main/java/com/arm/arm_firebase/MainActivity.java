package com.arm.arm_firebase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.arm.adapter.NoteAdapter;
import com.arm.model.Note;
import com.arm.model.SaveValue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    ListView lvNote;
    Button btn;
    NoteAdapter adapter;
    public static String DATABASE_NAME = "Note.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    public static String edit_Note = "edit_Note";

    public static String arrayNgayTrongTuan [];
    SaveValue saveValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        addControl();
        addEvent();

        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                *//*Note value = dataSnapshot.getValue(Note.class);
                String a = dataSnapshot.getRef().getKey();
                Log.d("d", "Value is: " + value.getNoiDung());
                Log.d("d", "Value is: " + a);*//*
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("dd", "Failed to read value.", error.toException());
            }
        });*/
    }

    private void addEvent() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addControl() {

        lvNote = (ListView) findViewById(R.id.lvNote);
        btn = (Button) findViewById(R.id.btn);
        adapter = new NoteAdapter(this,R.layout.item);
        //adapter.add();
        lvNote.setAdapter(adapter);

        arrayNgayTrongTuan =
                getResources().getStringArray(R.array.array_ngay_trong_tuan);
        saveValue = new SaveValue(this);

        loadDataFireBase();
    }

    private void loadDataFireBase() {
        FirebaseDatabase databaseFireBase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef
                = databaseFireBase.getReference("dsNote");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!saveValue.getSaved_xoaLanDau_Boolean())
                {
                    database.delete("dsNote",null,null);
                    saveValue.setSaved_xoaLanDau_Boolean(true);
                }
                adapter.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    String snapshotKey = snapshot.getKey();
                    Log.d("key",snapshotKey);
                    adapter.add(note);
                    //nếu SQLite không có key giống thì add vào note mới
                    if (tongSoDongAnhHuong("SELECT * FROM dsNote WHERE key = ?"
                            ,new String[]{snapshotKey}) == 0)
                    {

                        addVaoSqlite(snapshotKey,note);
                    }
                    else
                    {
                        updateVaoSqlite(snapshotKey,note);
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("eo", "Failed to read value.", error.toException());
            }

        });


    }

    //--------------------trả về số dòng có kết quả khớp--------------------------
    public int tongSoDongAnhHuong(String sql,String []arr) {
        Cursor cursor = database.rawQuery(sql,arr);
        int totalRows = cursor.getCount();
        cursor.close();
        return totalRows;
    }

    private void updateVaoSqlite(String key,Note note) {
        ContentValues values = new ContentValues();

        values.put("tieu_de",note.getTieuDe());
        values.put("noi_dung",note.getNoiDung());

        values.put("ngay",note.getNgay());
        values.put("thang",note.getThang());
        values.put("nam",note.getNam());

        if (database.update("dsNote",values,"key = ? ",new String[]{key}) == -1)
        {

        }
        else
        {

        }
    }

    private void addVaoSqlite(String snapshotKey, Note note) {

        ContentValues values = new ContentValues();
        values.put("tieu_de",note.getTieuDe());
        values.put("noi_dung",note.getNoiDung());

        values.put("ngay",note.getNgay());
        values.put("thang",note.getThang());
        values.put("nam",note.getNam());
        values.put("key",snapshotKey);

        if (database.insert("dsNote",null,values) == -1)
        {
            //Toast.makeText(this, "không", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    private void hienThiToanBoNote(String sql,String []arr) {

        Cursor cursor = database.rawQuery(sql,arr);
        adapter.clear();
        while (cursor.moveToNext())
        {
            String tieuDe = cursor.getString(1);
            String noiDung = cursor.getString(2);
            int ngay = cursor.getInt(3);
            int thang = cursor.getInt(4);
            int nam = cursor.getInt(5);
            String key = cursor.getString(6);

            Note note = new Note();
            note.setTieuDe(tieuDe);
            note.setNoiDung(noiDung);
            note.setNgay(ngay);
            note.setThang(thang);
            note.setNam(nam);
            note.setKey(key);
            adapter.add(note);
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienThiToanBoNote("SELECT * FROM dsNote",null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.add_note:
                addNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        Intent intent = new Intent(this,EditActivity.class);
        startActivity(intent);
    }

    //----------------coppy database vào bộ nhớ máy------------------------------------
    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath1() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;

            myInput = getAssets().open(DATABASE_NAME);


            // Path to the just created empty db
            String outFileName = getDatabasePath1();

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }*/

    @Override
    public void onBackPressed() {
        nhanLuon();
    }

    private void nhanLuon() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("xác nhận tuyển ?");
        builder.setNeutralButton("thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, ":'(", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
}
