package com.arm.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ARM on 22-Jul-17.
 */

public class SaveValue {
    private String firstDelete = "firstDelete";

    private String save = "SaveValue";
    Context context;

    public SaveValue(Context context) {
        this.context = context;
    }

    //------------------------get và set date hiện tại----------------------------------------
    public void setSaved_xoaLanDau_Boolean(Boolean is)
    {
        SharedPreferences.Editor localEditor = this.context
                .getSharedPreferences(this.save, 0).edit();
        localEditor.putBoolean(this.firstDelete, is);
        localEditor.commit();
    }

    public Boolean getSaved_xoaLanDau_Boolean()
    {
        return this.context.getSharedPreferences(this.save, 0)
                .getBoolean(this.firstDelete,false);
    }

}
