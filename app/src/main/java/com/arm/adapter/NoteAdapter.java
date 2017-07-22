package com.arm.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arm.arm_firebase.EditActivity;
import com.arm.arm_firebase.R;
import com.arm.model.Note;

import static com.arm.arm_firebase.MainActivity.edit_Note;

/**
 * Created by ARM on 21-Jul-17.
 */

public class NoteAdapter extends ArrayAdapter<Note> {
    Activity context;
    int resource;

    public NoteAdapter(@NonNull Activity context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);
        TextView txtTieuDe = (TextView) view.findViewById(R.id.txtTieuDe);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
        final Note note = getItem(position);
        txtTieuDe.setText(note.getTieuDe());
        txtDate.setText(note.getNgay()
                +"/"+note.getThang()
                +"/"+note.getNam());

        txtTieuDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XulyEdit(note);
            }
        });
        return view;
    }

    private void XulyEdit(Note note) {
        Intent intent = new Intent(this.context, EditActivity.class);
        intent.putExtra("edit",note);
        intent.setAction(edit_Note);
        this.context.startActivity(intent);
    }
}
