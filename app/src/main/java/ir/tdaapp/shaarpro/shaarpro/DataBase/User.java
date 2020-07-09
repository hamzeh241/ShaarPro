package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;

/**
 * Created by Diako on 8/4/2019.
 */

public class User {

    DBAdapter dbAdapter;

    public User(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    //در اینجا چک می شود که آیا این کاربر قبلا لاگین کرده است یا خیر
    public boolean HasAccount() {
        try {
            Cursor cursor = dbAdapter.ExecuteQ("select * from TblUserPro");

            boolean res = cursor.getCount() > 0;
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter, cursor);

            if (res)
                return true;
            return false;

        } catch (Exception ex) {
            return false;
        }
    }

    public boolean AddUser(int Id, String FullName, String Email, String CellPhone) {
        try{
            Cursor cursor = dbAdapter.ExecuteQ("insert into TblUserPro (Id,IsAdmin,FullName,Email,CellPhone,DateRegister) values (" + Id + ",0,'" + FullName + "','" + Email + "','" + CellPhone + "',1999-5-4)");
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter, cursor);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String GetCellPhone(){
        Cursor cursor=dbAdapter.ExecuteQ("select CellPhone from TblUserPro");
        if (cursor.getString(0)!=null){
            String Cell=cursor.getString(0);
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter,cursor);
            return Cell;
        }
        return "";
    }

    public int GetId(){
        Cursor cursor=dbAdapter.ExecuteQ("select Id from TblUserPro");
        if (cursor.getString(0)!=null){
            int id=Integer.parseInt(cursor.getString(0).toString());
            CloseDatabase.CloseCursor_DbAdapter(dbAdapter,cursor);
            return id;
        }
        return 0;
    }
}
