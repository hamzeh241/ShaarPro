package ir.tdaapp.shaarpro.shaarpro.DataBase;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Id_FeaturesGroup;

/**
 * Created by Diako on 8/13/2019.
 */

public class TypeFeatures {
    DBAdapter dbAdapter;
    public TypeFeatures(DBAdapter dbAdapter){
        this.dbAdapter=dbAdapter;
    }

    public List<VM_Id_FeaturesGroup> InnerJoin_TblFeatures(int TypeHome){
        Cursor cursor=dbAdapter.ExecuteQ("SELECT TblFeatures.Id,TblFeatures.FkFeaturesGroup FROM TblTypeFeatures INNER JOIN TblFeatures on TblTypeFeatures.FkFeatures=TblFeatures.Id WHERE TblTypeFeatures.FkType ="+TypeHome);

        List<VM_Id_FeaturesGroup> featuresGroups=new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                featuresGroups.add(new VM_Id_FeaturesGroup(Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(0))));
                cursor.moveToNext();
            }
        }

        CloseDatabase.CloseCursor_DbAdapter(dbAdapter,cursor);
        return featuresGroups;
    }
}
