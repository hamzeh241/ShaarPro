package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;

/**
 * Created by Diako on 8/4/2019.
 */

public class ETC {

    DBAdapter dbAdapter;

    public ETC(DBAdapter dbAdapter){
        this.dbAdapter=dbAdapter;
    }

    //در اینجا نام یک جدول را گرفته و Id آن را پاس می دهد
    public int GetId(String TableName){
        try{
            Cursor GetId = dbAdapter.ExecuteQ("select Max(Id) from "+TableName);
            int Id;

            if (GetId.getString(0) != null)
                Id = Integer.parseInt(GetId.getString(0)) + 1;
            else
                Id = 1;

            CloseDatabase.CloseCursor_DbAdapter(dbAdapter,GetId);
            return Id;
        }catch (Exception e){
            return 0;
        }

    }
}
