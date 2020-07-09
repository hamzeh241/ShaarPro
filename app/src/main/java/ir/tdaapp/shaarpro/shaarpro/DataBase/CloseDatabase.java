package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;

/**
 * Created by Diako on 8/4/2019.
 */

//این کلاس برای بستن DbAdapter , Cursor می باشد
public class CloseDatabase {
    public static void CloseCursor(Cursor cursor){
        cursor.close();
    }

    public static void CloseDbAdapter(DBAdapter dbAdapter){
        dbAdapter.close();
    }

    public static void CloseCursor_DbAdapter(DBAdapter dbAdapter,Cursor cursor){
        cursor.close();
        dbAdapter.close();
    }
}
