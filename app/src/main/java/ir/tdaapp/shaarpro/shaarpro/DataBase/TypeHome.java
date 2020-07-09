package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_TypeHome;

/**
 * Created by Diako on 8/10/2019.
 */

public class TypeHome {

    DBAdapter dbAdapter;

    public TypeHome(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    //در اینجا یک مقدار از نوع اینتیجر از ورودی گرفته و مقادیری که پرنت آن ها با مقدار ورودی برابر باشند را پاس می دهد
    public List<VM_TypeHome> GetDataByParentId(int type) {
        List<VM_TypeHome> lst = new ArrayList<>();
        int Id;
        if (type == 1) {
            Cursor cursor = dbAdapter.ExecuteQ("select * from TblType where ParentId=1");
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Id = Integer.parseInt(cursor.getString(0));

                    if (Id != 37 && Id != 29 && Id != 38 && Id != 18)
                        lst.add(new VM_TypeHome(cursor.getString(1), Id));
                    cursor.moveToNext();
                }
            }
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter, cursor);
            return lst;
        } else if (type == 2) {
            Cursor cursor = dbAdapter.ExecuteQ("select * from TblType where ParentId=2");
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Id = Integer.parseInt(cursor.getString(0));

                    if (Id != 40 && Id != 24 && Id != 41)
                        lst.add(new VM_TypeHome(cursor.getString(1), Id));
                    cursor.moveToNext();
                }
            }
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter, cursor);
            return lst;
        } else {
            Cursor cursor = dbAdapter.ExecuteQ("select * from TblType where ParentId=32");
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Id = Integer.parseInt(cursor.getString(0));

                    if (Id != 36)
                        lst.add(new VM_TypeHome(cursor.getString(1), Id));
                    cursor.moveToNext();
                }
            }
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter, cursor);
            return lst;
        }
    }

    //در اینجا یک مقدار را از ورودی گرفته و پرنت آن را پاس می دهد 1 به معنای مسکونی 2 به معنای تجاری و 32 به معنای صنعتی
    public int GetParent_TypeHome(int id) {

        Cursor cursor = dbAdapter.ExecuteQ("select ParentId,Titel from TblType where Id=" + id);

        int parentId = 0;
        if (cursor.getString(0) != null) {
            parentId = Integer.parseInt(cursor.getString(0));

            if (parentId == 0) {
                if (cursor.getString(1) != null) {
                    if (cursor.getString(1).equalsIgnoreCase("مسکونی")) {
                        parentId = 1;
                    } else if (cursor.getString(1).equalsIgnoreCase("اداری-تجاری")) {
                        parentId = 2;
                    } else if (cursor.getString(1).equalsIgnoreCase("صنعتی")) {
                        parentId = 32;
                    }
                }
            }
        }
        CloseDatabase.CloseCursor_DbAdapter(dbAdapter, cursor);
        return parentId;
    }
}
