package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import io.reactivex.annotations.NonNull;

import androidx.fragment.app.Fragment;
import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.EditLocationUserAdapter;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Dialogs.Add_New_Location;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Location;

/**
 * Created by Diako on 8/4/2019.
 */

public class Fragment_Edit_Location_User extends Fragment implements IBase {

    public static final String TAG="Fragment_Edit_Location_User";

    EditLocationUserAdapter editLocationUserAdapter;
    GridLayoutManager gridLayoutManager;
    Button btn_Reload,btn_NewLocation;
    LinearLayout img_not_item;
    int UserId;
    Spinner cmb_Location;

    RecyclerView recycler;
    RelativeLayout backall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit_location_user,container,false);

        FindItem(view);
        SetVal();
        OnClick();

        return view;
    }

    void FindItem(View view){
        recycler=view.findViewById(R.id.Recycler);
        btn_Reload=view.findViewById(R.id.btn_Reload);
        img_not_item=view.findViewById(R.id.img_not_item);
        cmb_Location=view.findViewById(R.id.cmb_Location);
        btn_NewLocation=view.findViewById(R.id.btn_NewLocation);
        backall=view.findViewById(R.id.backall);
    }

    void SetVal(){

        if (((MainActivity)getActivity()).internet.HaveNetworkConnection()){

            int Id=0;
            Bundle bundle=getArguments();
            if (bundle!=null){
                Id=bundle.getInt("Id");
            }

            UserId=Id;

            String url=Api+"User/getUserProLocation?Id="+Id;

            final int finalId = Id;
            JsonArrayRequest arrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    List<VM_Location> locations=new ArrayList<>();

                    if (response.length()>0){
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject object=response.getJSONObject(i);

                                int Id=Integer.parseInt(object.getString("FkLocation"));
                                String Title=object.getString("TblLocationTitle");

                                locations.add(new VM_Location(Id,Title));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        editLocationUserAdapter=new EditLocationUserAdapter(locations,getContext(),UserId);
                        gridLayoutManager=new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
                        recycler.setLayoutManager(gridLayoutManager);
                        recycler.setAdapter(editLocationUserAdapter);
                    }else{
                        img_not_item.setVisibility(View.VISIBLE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(getContext())
                            .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                            .setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")))
                            .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بارگیری مجدد</font>")), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("Id", finalId);

                                    Fragment_Edit_Location_User locationUser=new Fragment_Edit_Location_User();
                                    locationUser.setArguments(bundle);

                                     getActivity().getSupportFragmentManager().
                                            beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, locationUser).commit();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            AppController.getInstance().addToRequestQueue(arrayRequest);
        }else{
            btn_Reload.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getResources().getString(R.string.CheckConnectionInternet), Toast.LENGTH_SHORT).show();
        }
    }

    void OnClick(){
        btn_Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_Reload.setVisibility(View.GONE);
                SetVal();
            }
        });

        btn_NewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("UserId",UserId);

                Add_New_Location add_new_location=new Add_New_Location();
                add_new_location.setArguments(bundle);

                add_new_location.show(getActivity().getSupportFragmentManager(),add_new_location.getTag());
            }
        });

        backall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Seting_Moshaverin()).commit();
            }
        });
    }
}
