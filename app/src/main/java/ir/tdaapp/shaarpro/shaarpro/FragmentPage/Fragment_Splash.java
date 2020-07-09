package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ir.tdaapp.shaarpro.shaarpro.DataBase.Setting;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.Utility.Internet;
import ir.tdaapp.shaarpro.shaarpro.Utility.Policy_Volley;

/**
 * Created by Diako on 8/4/2019.
 */

public class Fragment_Splash extends Fragment implements IBase {

    public static final String TAG = "Fragment_Splash";

    LinearLayout showItem;
    CountDownTimer timer;
    Button btn_Reload;
    Setting setting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        FindItem(view);

        OnClick();

        GetStatusUser();

        return view;
    }

    //برای پاک کردن حافظه دیتابیس
    public void clearApplicationData() {
        File cacheDirectory = getActivity().getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    void FindItem(View view) {
        showItem = view.findViewById(R.id.showItem);
        btn_Reload = view.findViewById(R.id.btn_Reload);
        setting=new Setting(((MainActivity)getActivity()).dbAdapter);
    }

    void GetStatusUser() {
        if (((MainActivity) getActivity()).internet.HaveNetworkConnection()) {

            if (((MainActivity) getActivity()).user.HasAccount()) {

                //در اینجا progressbar لودینگ نمایش داده می شود
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                String CellPhone = ((MainActivity) getActivity()).user.GetCellPhone();

                String url = Api + "User/PutUserProLastOn?Id=" + CellPhone;

                JsonArrayRequest
                        request
                        = new JsonArrayRequest(
                        Request.Method.PUT,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progress.dismiss();

                                JSONObject obj;

                                for (int i=0;i<response.length();i++){
                                    try {
                                        obj=response.getJSONObject(i);

                                        if (obj.getBoolean("Cleardate")){
                                            clearApplicationData();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                float versionSql=setting.GetVersionSql();
                                boolean Status=true;

                                JSONObject object;

                                for (int i=0;i<response.length();i++){
                                    try {
                                        object=response.getJSONObject(i);

                                        Status=object.getBoolean("Status");
                                        Float NewVersionSql = Float.valueOf(object.getString("Vesrsion"));

                                        if (NewVersionSql > versionSql) {
                                            try {
                                                String Query = object.getString("Query");
                                                ((MainActivity)getActivity()).dbAdapter.ExecuteQ(Query);
                                                ((MainActivity)getActivity()).dbAdapter.close();
                                                versionSql = NewVersionSql;
                                            }
                                            catch (Exception e){

                                            }

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (Status) {
                                    DateTime(3);
                                } else {
                                    DateTime(2);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.dismiss();

                                try {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                                            .setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")))
                                            .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بارگیری مجدد</font>")), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                     getActivity().getSupportFragmentManager().
                                                            beginTransaction().replace(R.id.Fragment_Main, new Fragment_Splash()).commit();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                } catch (Exception e) {

                                }
                            }
                        });

                AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley, request));

            } else {
                DateTime(1);
            }
        } else {
            try {

                btn_Reload.setVisibility(View.VISIBLE);

                new AlertDialog.Builder(getContext())
                        .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                        .setMessage((Html.fromHtml("<font color='#FF7F27'>" + getResources().getString(R.string.CheckConnectionInternet) + "</font>")))
                        .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                 getActivity().getSupportFragmentManager().
                                        beginTransaction().replace(R.id.Fragment_Main, new Fragment_Splash()).commit();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch (Exception e) {

            }
        }
    }

    void DateTime(final int Type) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation aniFade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_splash);
                showItem.startAnimation(aniFade);
            }
        }, 500);

        timer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (Type == 1) {
                     getActivity().getSupportFragmentManager().
                            beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.Fragment_Main, new Fragment_Login_Home()).commit();
                } else if (Type == 2) {
                     getActivity().getSupportFragmentManager().
                            beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.Fragment_Main, new Fragment_Wait_To_Confirm()).commit();
                } else {
                     getActivity().getSupportFragmentManager().
                            beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.Fragment_Main, new Fragment_Home()).commit();
                }
            }
        }.start();
    }

    void OnClick() {
        btn_Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().getSupportFragmentManager().
                        beginTransaction().replace(R.id.Fragment_Main, new Fragment_Splash()).commit();
            }
        });
    }
}
