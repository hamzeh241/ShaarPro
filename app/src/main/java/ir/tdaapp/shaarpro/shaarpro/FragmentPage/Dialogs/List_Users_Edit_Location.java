package ir.tdaapp.shaarpro.shaarpro.FragmentPage.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.Edit_List_Location_Adapter;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Seting_Moshaverin;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_EditLocationUser;

/**
 * Created by Diako on 8/7/2019.
 */

public class List_Users_Edit_Location extends BottomSheetDialogFragment implements IBase {

    RecyclerView Recycler;
    Edit_List_Location_Adapter locationAdapter;
    LinearLayoutManager layoutManager;
    int LocationId;
    int UserId;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = View.inflate(getContext(), R.layout.list_users_edit_location, null);

        Bundle bundle=getArguments();

        LocationId=bundle.getInt("LocationId");
        UserId=bundle.getInt("UserId");

        FindItem(view);

        SetUsers();

        dialog.setContentView(view);
    }

    void FindItem(View view) {
        Recycler = view.findViewById(R.id.Recycler);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    void SetUsers() {

        final int Id = ((MainActivity) getActivity()).user.GetId();

        String url = Api + "User/GetListAllUser?id=" + Id;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<VM_EditLocationUser> users = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject object = response.getJSONObject(i);

                        //در اینجا نام مربوط به خود کاربر نمایش داده نمی شود
                        if (Id!=Integer.parseInt(object.getString("Id"))&&UserId!=Integer.parseInt(object.getString("Id"))) {
                            VM_EditLocationUser user = new VM_EditLocationUser();
                            user.setTo_Id(Integer.parseInt(object.getString("Id")));
                            user.setName(object.getString("FullName"));
                            user.setLocationId(LocationId);
                            user.setFrom_Id(UserId);
                            users.add(user);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                locationAdapter = new Edit_List_Location_Adapter(users, getActivity());
                Recycler.setAdapter(locationAdapter);
                Recycler.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(arrayRequest);
    }
}
