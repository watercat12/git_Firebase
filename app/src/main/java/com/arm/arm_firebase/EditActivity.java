package com.arm.arm_firebase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.model.Note;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static com.arm.arm_firebase.MainActivity.arrayNgayTrongTuan;
import static com.arm.arm_firebase.MainActivity.database;
import static com.arm.arm_firebase.MainActivity.edit_Note;

public class EditActivity extends AppCompatActivity {

    TextView txtChonNgay;
    EditText edtTieuDe, edtNoiDung;
    Button btnSave;
    
    int ngayTrongLich;
    int thangTrongLich;
    int namTrongLich;

    int ngayHienTai;
    int thangHienTai;
    int namHienTai;
    
    int ngayTrongTuan;
    
    Calendar calendar;

    Intent intent;
    Note noteEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        addControl();
        addEvent();
    }



    private void addControl() {
        txtChonNgay = (TextView) findViewById(R.id.txtChonNgay);
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtNoiDung = (EditText) findViewById(R.id.edtNoiDung);
        btnSave = (Button) findViewById(R.id.btnSave);

        calendar = Calendar.getInstance();
        ngayHienTai = calendar.get(Calendar.DATE);
        thangHienTai = calendar.get((Calendar.MONTH))+1;
        namHienTai = calendar.get(Calendar.YEAR);

        intent = getIntent();
        // nếu là edit

            if (intent.getAction() == edit_Note)
            {
                noteEdit = (Note) intent.getSerializableExtra("edit");
                edtTieuDe.setText(noteEdit.getTieuDe());
                edtNoiDung.setText(noteEdit.getNoiDung());
                txtChonNgay.setText(noteEdit.getNgay()
                        +"/"+noteEdit.getThang()
                        +"/"+noteEdit.getNam());
            }
        }




    private void addEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.getAction() == null)
                {
                    xuLyAdd();
                }
                else
                {
                    xuLyEdit(noteEdit);
                }

            }
        });
        txtChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonNgay();
            }
        });
    }

    private void xuLyAdd() {
        String tieuDe = edtTieuDe.getText().toString();
        String noiDung = edtNoiDung.getText().toString();

        ContentValues values = new ContentValues();
        //values.put("date",date);
        values.put("tieu_de",tieuDe);
        values.put("noi_dung",noiDung);

        if (ngayTrongLich == 0)
        {
            values.put("ngay",ngayHienTai);
            values.put("thang",thangHienTai);
            values.put("nam",namHienTai);
        }
        else
        {
            values.put("ngay",ngayTrongLich);
            values.put("thang",thangTrongLich);
            values.put("nam",namTrongLich);
        }
        // Add lên firebase
        FirebaseDatabase databaseFire = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = databaseFire.getReference("dsNote");
        Note note = new Note();
        note.setTieuDe(tieuDe);
        note.setNoiDung(noiDung);
        if (ngayTrongLich == 0)
        {
            note.setNgay(ngayHienTai);
            note.setThang(thangHienTai);
            note.setNam(namHienTai);
        }
        else
        {
            note.setNgay(ngayTrongLich);
            note.setThang(thangTrongLich);
            note.setNam(namTrongLich);
        }

        //myRef.push().setValue(note);
        DatabaseReference bc = myRef.push();
        String key = bc.getKey();
        /*Map<String,Note> noteMap = new HashMap<String,Note>();
        noteMap.put("-Kpa2Wge8NJ5bsPYzozX",note);*/
        bc.setValue(note);

        //Toast.makeText(getBaseContext(), key, Toast.LENGTH_SHORT).show();

        values.put("key",key);
        // Add vào SQLite
        if (database.insert("dsNote",null,values) == -1)
        {
            Toast.makeText(this, "không dc", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void xuLyEdit(Note note) {
        String tieuDe = edtTieuDe.getText().toString();
        String noiDung = edtNoiDung.getText().toString();

        ContentValues values = new ContentValues();
        //values.put("date",date);
        values.put("tieu_de",tieuDe);
        values.put("noi_dung",noiDung);

        if (ngayTrongLich == 0)
        {
            values.put("ngay",note.getNgay());
            values.put("thang",note.getThang());
            values.put("nam",note.getNam());
        }
        else
        {
            values.put("ngay",ngayTrongLich);
            values.put("thang",thangTrongLich);
            values.put("nam",namTrongLich);
        }
        // Add lên firebase
        FirebaseDatabase databaseFire = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = databaseFire.getReference("dsNote");
        note.setTieuDe(tieuDe);
        note.setNoiDung(noiDung);
        if (ngayTrongLich == 0)
        {
            note.setNgay(note.getNgay());
            note.setThang(note.getThang());
            note.setNam(note.getNam());
        }
        else
        {
            note.setNgay(ngayTrongLich);
            note.setThang(thangTrongLich);
            note.setNam(namTrongLich);
        }

        //myRef.push().setValue(note);
        DatabaseReference bc = myRef.child(note.getKey()+"");
        //String key = bc.getKey();
        /*Map<String,Note> noteMap = new HashMap<String,Note>();
        noteMap.put("-Kpa2Wge8NJ5bsPYzozX",note);*/
        bc.setValue(note);

        //Toast.makeText(getBaseContext(), key, Toast.LENGTH_SHORT).show();

        //values.put("key",key);
        // Add vào SQLite
        if (database.update("dsNote",values,"key = ?",new String[]{note.getKey()}) == -1)
        {
            Toast.makeText(this, "không dc", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void xuLyChonNgay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_chon_ngay, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        Button btnOK = (Button) view.findViewById(R.id.btnOkLich);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancelLich);

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                ngayTrongLich = dayOfMonth;
                thangTrongLich = (month+1);
                namTrongLich = year;
                calendar.set(year,month,dayOfMonth);
                ngayTrongTuan = calendar.get(Calendar.DAY_OF_WEEK);


            }
        });

        builder.setView(view);
        final AlertDialog dialogChonNgay = builder.create();
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                txtChonNgay.setText(getResources().getString(R.string.hom_nay));
                if (intent.getAction() == edit_Note)
                {
                    noteEdit.setNgay(ngayHienTai);
                    noteEdit.setThang(thangHienTai);
                    noteEdit.setNam(namHienTai);
                }
                //so sánh để biết hôm nay, hôm qua, ngày mai
                if (ngayTrongLich == ngayHienTai
                        && namTrongLich == namHienTai
                        && thangTrongLich == thangHienTai)
                {
                    txtChonNgay.setText(getResources().getString(R.string.hom_nay));
                    dialogChonNgay.dismiss();

                    return;
                }
                else if (ngayTrongLich == (ngayHienTai + 1)
                        && namTrongLich == namHienTai
                        && thangTrongLich == thangHienTai)
                {
                    txtChonNgay.setText(getResources().getString(R.string.ngay_mai));
                    dialogChonNgay.dismiss();
                    return;
                }
                else if (ngayTrongLich == (ngayHienTai - 1)
                        && namTrongLich == namHienTai
                        && thangTrongLich == thangHienTai)
                {
                    txtChonNgay.setText(getResources().getString(R.string.hom_qua));
                    dialogChonNgay.dismiss();
                    return;
                }
                //gắn thêm thứ mấy vào
                switch (ngayTrongTuan)
                {
                    case 1:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[0]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                        break;
                    case 2:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[1]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                        break;
                    case 3:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[2]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                        break;
                    case 4:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[3]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                        break;
                    case 5:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[4]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                        break;
                    case 6:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[5]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                        break;
                    case 7:
                        txtChonNgay
                                .setText(arrayNgayTrongTuan[6]
                                        +", "+ngayTrongLich+"/"
                                        +thangTrongLich+"/"
                                        +namTrongLich);
                }
                dialogChonNgay.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogChonNgay.dismiss();
            }
        });
        dialogChonNgay.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (intent.getAction() != null)
        getMenuInflater().inflate(R.menu.delete_menu, menu);
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
            case R.id.delete_note:
                deleteNote(noteEdit.getKey());
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteNote(final String key) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("xóa hả?");
        builder.setNeutralButton("ừ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (database.delete("dsNote","key = ? ",new String[]{key}) == -1)
                {
                    Toast.makeText(getBaseContext()
                            , "có vẻ sai sai"
                            , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    xoaTrenFirebase(key);
                    Toast.makeText(getBaseContext()
                            , "đã xóa"
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        builder.setNegativeButton("thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void xoaTrenFirebase(String key) {
        FirebaseDatabase databaseFire = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = databaseFire.getReference("dsNote");
        myRef.child(key+"").removeValue();
    }
}
