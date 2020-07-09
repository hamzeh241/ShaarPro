package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.content.DialogInterface;
import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.Published_Homes_Adapter;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Home;

/**
 * Created by Diako on 8/3/2019.
 */

public class Tab_Wait_Confirmation extends Fragment implements IBase {
    public static final String TAG = "Tab_Wait_Confirmation";

    RecyclerView Recycler;
    ProgressBar progressbar;
    LinearLayout img_not_item;

    Published_Homes_Adapter homesAdapter;
    LinearLayoutManager layoutManager;
    boolean loading;
    int CountItem;
    int Page;
    Button btn_Reload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_wait_confirmation, container, false);

        FindItem(view);
        setVal();
        Scroll();

        OnClick();


        return view;
    }

    void FindItem(View view) {
        Recycler = view.findViewById(R.id.Recycler);
        progressbar = view.findViewById(R.id.progressbar);
        img_not_item = view.findViewById(R.id.img_not_item);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        loading = true;
        Page = 0;
        CountItem = -1;

        btn_Reload = view.findViewById(R.id.btn_Reload);
    }

    void OnClick() {
        btn_Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_Reload.setVisibility(View.GONE);
                setVal();
            }
        });
    }

    void setVal() {

        progressbar.setVisibility(View.VISIBLE);

        int Id = ((MainActivity) getActivity()).user.GetId();

        String Url = Api + "Item/getUserProitem?Id=" + Id + "&Kind=0&Page=" + Page;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressbar.setVisibility(View.GONE);

                if (response.length() > 0) {

                    List<VM_Home> homes = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            VM_Home home = new VM_Home();

                            home.setId(Integer.parseInt(object.getString("Id")));
                            home.setAddress(object.getString("Address"));
                            home.setPrice(object.getString("Price"));
                            home.setCountRoom(object.getString("Room"));
                            home.setImage(ApiImage + "ImageSave/" + object.getString("Image"));
                            home.setDiscription(object.getString("Description"));
                            home.setArea(object.getString("Area"));
                            home.setTime(object.getString("DateInsert"));
                            home.setCountImage(object.getString("ImageCount"));
                            home.setSpecial(Boolean.valueOf(object.getString("Special")));

                            if (CountItem == -1) {
                                CountItem = Integer.parseInt(object.getString("Count"));
                            }

                            homes.add(home);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (Page == 0) {
                        homesAdapter = new Published_Homes_Adapter(homes, getActivity(), progressbar);
                        Recycler.setAdapter(homesAdapter);
                        Recycler.setLayoutManager(layoutManager);
                    } else {
                        homesAdapter.AddHomes(homes);
                        homesAdapter.notifyDataSetChanged();
                        loading = true;
                    }

                } else {
                    if (Page==0)
                    img_not_item.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbar.setVisibility(View.GONE);
                btn_Reload.setVisibility(View.VISIBLE);
            }
        });

        AppController.getInstance().addToRequestQueue(arrayRequest);
    }

    void Scroll() {
        Recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {

                    //تعداد آیتم های  که در صفحه نمایش داده می شوند
                    int visibleItemCount = layoutManager.getChildCount();
                    //تعداد کل آیتم ها
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (homesAdapter.getItemCount() < CountItem) {
                                Page += 1;
                                setVal();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });
    }
}
