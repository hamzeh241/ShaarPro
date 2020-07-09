package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Location;

/**
 * Created by Diako on 8/11/2019.
 */

public class Location {
    DBAdapter dbAdapter;

    public Location(DBAdapter dbAdapter){
        this.dbAdapter=dbAdapter;
    }

    public List<VM_Location> GetAll(){

        List<VM_Location> locations=new ArrayList<>();
        Cursor cursor = dbAdapter.ExecuteQ("select * from TblLocation ORDER BY Title");

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                locations.add(new VM_Location(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
                cursor.moveToNext();
            }
        }
        CloseDatabase.CloseCursor_DbAdapter(dbAdapter,cursor);
        return locations;
    }

    public ArrayList<VM_Location> GetAll_TypeArrayList(){

        ArrayList<VM_Location> locations=new ArrayList();
        Cursor cursor = dbAdapter.ExecuteQ("select * from TblLocation ORDER BY Title");

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                locations.add(new VM_Location(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
                cursor.moveToNext();
            }
        }
        CloseDatabase.CloseCursor_DbAdapter(dbAdapter,cursor);
        return locations;
    }

    public int GetPosition(int id){
        ArrayList<VM_Location> locations=new ArrayList();
        Cursor cursor = dbAdapter.ExecuteQ("select * from TblLocation ORDER BY Title");
        int Position=0;

        while (!cursor.isAfterLast()) {
            int Id=Integer.parseInt(cursor.getString(0));

            if (Id==id){
                return Position;
            }
            Position++;
        }
        return Position;
    }
}
