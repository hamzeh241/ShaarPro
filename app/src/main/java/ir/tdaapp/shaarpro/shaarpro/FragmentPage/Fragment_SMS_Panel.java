package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;

/**
 * Created by Diako on 8/5/2019.
 */

public class Fragment_SMS_Panel extends Fragment implements IBase {
    String CellPhone;

    TextView txt_Discription, txt_Timer;
    Button btn_Done, btn_Resend;
    EditText txt_Code;
    CountDownTimer timer;
    RelativeLayout back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_panel, container, false);

        FindItem(view);

        //در اینجا از صفحه لاگین اطلاعات مربوطه را دریافت می کند
        Bundle bundle = getArguments();
        CellPhone = bundle.getString("CellPhone");

        txt_Discription.setText("کد تایید به شماره " + CellPhone+" "+ "ارسال شده است لطفا آن را وارد نمایید");
        DateTime();
        OnClick();

        return view;
    }

    void FindItem(View view) {
        txt_Discription = view.findViewById(R.id.Discription);
        btn_Done = view.findViewById(R.id.btn_Done);
        txt_Timer = view.findViewById(R.id.txtTimer);
        txt_Code = view.findViewById(R.id.txt_Code);
        btn_Resend = view.findViewById(R.id.btn_Resend);
        back=view.findViewById(R.id.backall);

    }

    void OnClick() {

        //دکمه ارسال
        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_Code.getText().toString().equalsIgnoreCase("")) {
                    new AlertDialog.Builder(getActivity()).setTitle("اخطار").setMessage("لطفا کد را وارد نماببد").setPositiveButton("باشه", null).show();
                } else {

                    //آدرس api که با فرستادن اطلاعات به سمت سرور که نتیجه بولین ارسال می شود
                    String url = Api+"User/" + CellPhone + "?sms=" + txt_Code.getText().toString();

                    //در اینجا progressbar لودینگ نمایش داده می شود
                    final ProgressDialog progress = new ProgressDialog(getActivity());
                    progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                    progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    StringRequest
                            stringRequest
                            = new StringRequest(
                            Request.Method.GET,
                            url,
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {

                                    //در این خط لودیگ مخفی می شود
                                    progress.dismiss();

                                    //در اینجا چک می شود که عملیات در سرور به درستی انجام شده است
                                    if (response.toString().equalsIgnoreCase("true")) {
                                        AddUser();
                                    } else {
                                        new android.app.AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا کد را درست وارد نمایید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>متوجه شدم</font>")), null).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progress.dismiss();
                                    new android.app.AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>در سرور خطای رخ داده است لطفا بعدا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                                }
                            });

                    AppController.getInstance().addToRequestQueue(stringRequest);
                }
            }
        });


        //دکمه ارسال مجدد
        btn_Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTime();

                //آدرس api که با فرستادن اطلاعات به سمت سرور که نتیجه بولین ارسال می شود
                String url = Api+"api/User/" + CellPhone;

                //در اینجا progressbar لودینگ نمایش داده می شود
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                StringRequest
                        stringRequest
                        = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                                //در این خط لودیگ مخفی می شود
                                progress.dismiss();

                                //در اینجا چک می شود که عملیات در سرور به درستی انجام شده است
                                if (response.toString().equalsIgnoreCase("true")) {
                                    Toast.makeText(getActivity(), "پیامک دوباره ارسال شد", Toast.LENGTH_SHORT).show();
                                } else {
                                    new android.app.AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>خطای در عملیات رخ داده است</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.dismiss();
                                new android.app.AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>در سرور خطای رخ داده است لطفا بعدا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                            }
                        });

                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });
    }

    void DateTime() {
        btn_Resend.setEnabled(false);
        btn_Resend.setBackgroundColor(getResources().getColor(R.color.colorGray));

        timer=new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_Timer.setText("ارسال مجدد: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                txt_Timer.setText("ارسال مجدد: " + "0");
                btn_Resend.setEnabled(true);
                btn_Resend.setBackground(getResources().getDrawable(R.drawable.change_color_btn));
            }
        }.start();
    }

    void AddUser() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle((Html.fromHtml("<font color='#FF7F27'>درحال ارسال</font>")));
        progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        String url = Api+"User/UserInfo?id=" + CellPhone;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ((MainActivity)getActivity()).user.AddUser(Integer.parseInt(response.getString("Id")),response.getString("FullName"),response.getString("Email"),CellPhone);
                            timer.cancel();
                            progress.dismiss();
                             getActivity().getSupportFragmentManager().
                                    beginTransaction().replace(R.id.Fragment_Main, new Succefull_Register()).commit();

                        } catch (JSONException e) {

                            progress.dismiss();
                            new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>مشکلی در عملیات رخ داده است لطفا بعدا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>در سرور خطای رخ داده است لطفا بعدا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }
}
