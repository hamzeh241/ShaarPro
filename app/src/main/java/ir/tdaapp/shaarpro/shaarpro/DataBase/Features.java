package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;

/**
 * Created by Diako on 8/13/2019.
 */

public class Features {
    DBAdapter dbAdapter;

    public Features(DBAdapter dbAdapter){
        this.dbAdapter=dbAdapter;
    }

    //در اینجا داده های که از نوع اسپینر هستند فیلد آپشن آن را برگشت می دهد
    public String[] GetOptions_Select(int Id){
        Cursor cursor=dbAdapter.ExecuteQ("select Option from TblFeatures where Id="+Id);

        String[] Data=cursor.getString(0).split(",");
        CloseDatabase.CloseCursor_DbAdapter(dbAdapter,cursor);
        return Data;
    }

    public Cursor GetBy_FkFormat(){
        Cursor cursor = dbAdapter.ExecuteQ("select * from TblFeatures where TblFeatures.FkFormat=4");
        CloseDatabase.CloseDbAdapter(dbAdapter);
        return cursor;
    }
}
