package ir.tdaapp.shaarpro.shaarpro.FragmentPage.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.AddNewLocationAdapter;
import ir.tdaapp.shaarpro.shaarpro.DataBase.Location;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Edit_Location_User;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;

/**
 * Created by Diako on 8/15/2019.
 */

public class Add_New_Location extends BottomSheetDialogFragment implements IBase {

    public static final String TAG="Add_New_Location";

    RecyclerView Recycler;
    AddNewLocationAdapter adapter;
    LinearLayoutManager layoutManager;
    Location location;
    int UserId;
    Button btn_Add;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = View.inflate(getContext(), R.layout.add_new_location, null);

        FindItem(view);
        SetValues();

        Bundle bundle=getArguments();
        if (bundle==null){
            dialog.dismiss();
        }

        UserId=bundle.getInt("UserId");

        OnClick();

        dialog.setContentView(view);
    }

    void FindItem(View view){
        Recycler=view.findViewById(R.id.Recycler);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        location=((MainActivity)getActivity()).location;
        btn_Add=view.findViewById(R.id.btn_Add);
    }

    void SetValues(){
        adapter=new AddNewLocationAdapter(location.GetAll(),getContext());
        Recycler.setAdapter(adapter);
        Recycler.setLayoutManager(layoutManager);
    }

    void OnClick(){

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                List<Integer> Values=adapter.GetValues();

                if (Values.size()>0){

                    String url=Api+"User/AddUserProLocation";

                    JSONObject object=new JSONObject();

                    try {
                        object.put("UserProId",UserId);

                        JSONArray array=new JSONArray();

                        for (int i=0;i<Values.size();i++){
                            array.put(Values.get(i));
                        }

                        object.put("LocationId",array);

                        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progress.dismiss();

                                try {

                                    if (response.getString("Status").equalsIgnoreCase("true")){
                                        Toast.makeText(getActivity(), "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                                        Bundle bundle=new Bundle();
                                        bundle.putInt("Id",UserId);

                                        Fragment_Edit_Location_User locationUser=new Fragment_Edit_Location_User();
                                        locationUser.setArguments(bundle);

                                        getActivity().getSupportFragmentManager().
                                                beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, locationUser).commit();

                                    }else{
                                        Toast.makeText(getActivity(), response.getString("Titel"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.dismiss();
                                new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                            }
                        });

                        AppController.getInstance().addToRequestQueue(request);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), getResources().getString(R.string.PleaseAddLocation), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
