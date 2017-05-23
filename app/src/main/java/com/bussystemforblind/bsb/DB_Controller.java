package com.bussystemforblind.bsb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by HyeonJu on 2017-05-10.
 */

public class DB_Controller extends SQLiteOpenHelper {

    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "card.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CARD( CARDNUM TEXT PRIMARY KEY UNIQUE, EFFDATE TEXT NOT NULL, CVC INTEGER NOT NULL, PASSWORD INTEGER NOT NULL, UNAME TEXT NOT NULL, REGNUM TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CARD;");
        onCreate(db);
    }

    public void insert_card(String cardnum, String effdate, int cvc, int password, String uname, String regnum){
        ContentValues contentValues = new ContentValues();
        contentValues.put("CARDNUM", cardnum);
        contentValues.put("EFFDATE", effdate);
        contentValues.put("CVC", cvc);
        contentValues.put("PASSWORD", password);
        contentValues.put("UNAME", uname);
        contentValues.put("REGNUM", regnum);
        this.getWritableDatabase().insertOrThrow("CARD","",contentValues);
    }

    public void delete_card(){
        this.getWritableDatabase().delete("CARD", null, null);
    }

    public void show_card(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM CARD", null);
        textView.setText("");
        while(cursor.moveToNext()){
            textView.append(cursor.getString(0)+"  /  "+cursor.getString(1));
        }
    }
}
