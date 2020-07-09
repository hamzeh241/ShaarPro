package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;

public class Setting {
    DBAdapter dbAdapter;

    public Setting(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    public float GetVersionSql(){
        Cursor query=dbAdapter.ExecuteQ("select Version from TblSetting LIMIT 1");

        float version=Float.valueOf(query.getString(0));
        CloseDatabase.CloseCursor_DbAdapter(dbAdapter,query);
        return version;
    }
}
