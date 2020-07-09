package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.Utility.Base64Image;
import ir.tdaapp.shaarpro.shaarpro.Utility.Internet;
import ir.tdaapp.shaarpro.shaarpro.Utility.Policy_Volley;
import ir.tdaapp.shaarpro.shaarpro.Utility.PostError;
import ir.tdaapp.shaarpro.shaarpro.Utility.ReplaceData;

/**
 * Created by Diako on 8/3/2019.
 */

public class Fragment_Login_Home extends Fragment implements IBase {

    public static final String TAG = "Fragment_Login_Home";

    EditText txt_Mob, txt_Password;
    RelativeLayout NewAccount;
    Button btn_Register;
    Internet internet;
    ImageView close;
    ReplaceData replaceData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_home, container, false);

        FindItem(view);
        OnClick();

        return view;
    }

    void FindItem(View view) {
        txt_Mob = view.findViewById(R.id.txt_Mob);
        NewAccount = view.findViewById(R.id.NewAccount);
        txt_Password = view.findViewById(R.id.txt_Password);
        btn_Register = view.findViewById(R.id.btn_Register);
        internet = ((MainActivity) getActivity()).internet;
        close = view.findViewById(R.id.close);
        replaceData=new ReplaceData();
    }

    void OnClick() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle((Html.fromHtml("<font color='#FF7F27'>پیغام</font>")))
                        .setMessage((Html.fromHtml("<font color='#FF7F27'>" + getResources().getString(R.string.Exit_ToApplication) + "</font>")))
                        .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بله</font>")), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }).setNegativeButton((Html.fromHtml("<font color='#FF7F27'>خیر</font>")), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).show();
            }
        });

        txt_Mob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    txt_Mob.setGravity(Gravity.LEFT);
                    txt_Mob.setHint("");
                } else {
                    if (txt_Mob.getText().toString().equalsIgnoreCase("")) {
                        txt_Mob.setGravity(Gravity.RIGHT);
                        txt_Mob.setHint("شماره موبایل خود را وارد نمایید");
                    } else {
                        txt_Mob.setGravity(Gravity.LEFT);
                    }
                }
            }
        });

        NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Main, new Fragment_Add_Account()).addToBackStack(null).commit();
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_Register.setEnabled(false);

                if (internet.HaveNetworkConnection()) {

                    if (txt_Mob.getText().toString().equalsIgnoreCase("") || txt_Password.getText().toString().equalsIgnoreCase("")) {
                        new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا اطلاعات را کامل وارد نمایید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>متوجه شدم</font>")), null).show();
                        btn_Register.setEnabled(true);
                    } else {
                        //در اینجا progressbar لودینگ نمایش داده می شود
                        final ProgressDialog progress = new ProgressDialog(getActivity());
                        progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                        progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();

                        String Password=replaceData.Number_PersianToEnglish(txt_Password.getText().toString());

                        String url = Api + "User/" + replaceData.Number_PersianToEnglish(txt_Mob.getText().toString()) + "?password=" +Password;

                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {

                                    try {

                                        int Id = Integer.parseInt(response.getString("Id").toString());

                                        if (Id == 0) {
                                            btn_Register.setEnabled(true);
                                            progress.dismiss();
                                            Toast.makeText(getActivity(), "همچین کاربری یافت نشد", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String FullName = response.getString("FullName");
                                            String Email = response.getString("Email");
                                            String CellPhone = txt_Mob.getText().toString();

                                            ((MainActivity) getActivity()).user.AddUser(Id, FullName, Email, CellPhone);

                                            progress.dismiss();
                                            btn_Register.setEnabled(true);

                                            Toast.makeText(getActivity(), "عملیات با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                             getActivity().getSupportFragmentManager().
                                                    beginTransaction().replace(R.id.Fragment_Main, new Fragment_Splash()).commit();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                } else {
                                    progress.dismiss();
                                    btn_Register.setEnabled(true);
                                    new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>همچین کاربری یافت نشد</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                                String UrlError = Api + "Values";

                                JSONObject ObjectError = new JSONObject();

                                try {

                                    ObjectError.put("Title", "ShaarPro_AddAccount");
                                    ObjectError.put("ErrorMessage", new ReplaceData().ReplaceError(error.getMessage()));
                                    ObjectError.put("AndroidVersion", Build.VERSION.RELEASE + "");
                                    ObjectError.put("VersionApp", "1.0");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new PostError(UrlError, TAG, ObjectError, () -> {
                                    progress.dismiss();
                                    btn_Register.setEnabled(true);
                                    new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>در سرور خطای رخ داده است لطفا مجددا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();

                                });
                            }
                        });
                        AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley,objectRequest));
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.CheckConnectionInternet), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
