package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import ir.tdaapp.shaarpro.shaarpro.Adapter.EditMoshaverinAdapter;
import ir.tdaapp.shaarpro.shaarpro.Adapter.RecordMoshaverinAdapter;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Moshaverin;

/**
 * Created by Diako on 5/25/2019.
 */

public class Tab_Edit_Moshaverin extends Fragment implements IBase {
    public static final String TAG="Tab_Edit_Moshaverin";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    EditMoshaverinAdapter edit_moshaverin;
    Button btn_Reload;
    LinearLayout img_not_item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab_edit_moshaverin,container,false);

        FindItem(view);
        SetVal();
        OnClick();

        return view;
    }

    void FindItem(View view){
        recyclerView=view.findViewById(R.id.EditMoshaverin_Recycler);
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        img_not_item=view.findViewById(R.id.img_not_item);
        btn_Reload=view.findViewById(R.id.btn_Reload);
    }

    private void SetVal(){

        if (((MainActivity)getActivity()).internet.HaveNetworkConnection()){

            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال دریافت اطلاعات</font>")));
            progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();

            int Id = ((MainActivity) getActivity()).user.GetId();

            String url = Api + "User/GetListAllUser?id=" + Id;

            JsonArrayRequest arrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.length()>0){
                        List<VM_Moshaverin> lst = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject j = response.getJSONObject(i);

                                VM_Moshaverin moshaverin = new VM_Moshaverin();

                                moshaverin.setName(j.getString("FullName"));
                                moshaverin.setAdmin(Boolean.valueOf(j.getString("IsAdmin")));
                                moshaverin.setId(Integer.parseInt(j.getString("Id")));
                                moshaverin.setActive(Boolean.valueOf(j.getString("Status")));

                                lst.add(moshaverin);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        edit_moshaverin = new EditMoshaverinAdapter(lst,getActivity());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(edit_moshaverin);
                    }else{
                        img_not_item.setVisibility(View.VISIBLE);
                    }

                    progress.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();

                    new AlertDialog.Builder(getContext())
                            .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                            .setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")))
                            .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بارگیری مجدد</font>")), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                     getActivity().getSupportFragmentManager().
                                            beginTransaction().replace(R.id.Fragment_Main, new Fragment_Seting_Moshaverin()).commit();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            AppController.getInstance().addToRequestQueue(arrayRequest);

        }else{
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
    }
}
