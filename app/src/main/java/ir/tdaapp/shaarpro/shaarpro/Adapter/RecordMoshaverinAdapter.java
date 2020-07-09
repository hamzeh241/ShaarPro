package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Seting_Moshaverin;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Moshaverin;

/**
 * Created by Diako on 8/3/2019.
 */

public class RecordMoshaverinAdapter extends RecyclerView.Adapter<RecordMoshaverinAdapter.MyViewHolder> implements IBase {
    Context context;
    List<VM_Moshaverin> list_moshaverins;

    public RecordMoshaverinAdapter(Context context, List<VM_Moshaverin> list_moshaverins) {
        this.context = context;
        this.list_moshaverins = list_moshaverins;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_record_moshaverin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        boolean temp = list_moshaverins.get(position).isActive();
        if (temp) {
            holder.btn.setBackground(context.getResources().getDrawable(R.color.colorBlue));
            holder.btn.setText("تایید شده");
        } else {
            holder.btn.setBackground(context.getResources().getDrawable(R.color.colorRed));
            holder.btn.setText("تایید نشده");
        }
        holder.txt.setText(list_moshaverins.get(position).getName());
        holder.btn.setTextColor(context.getResources().getColor(R.color.colorWhite));

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((MainActivity)context).internet.HaveNetworkConnection()){
                    final ProgressDialog progress = new ProgressDialog(context);
                    progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال دریافت اطلاعات</font>")));
                    progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    int Id=((MainActivity)context).user.GetId();
                    String Url=Api+"User/PutUserProActivate?id="+list_moshaverins.get(position).getId()+"&idpro="+Id+"&admin="+list_moshaverins.get(position).isAdmin()+"&status="+!list_moshaverins.get(position).isActive();

                    StringRequest request =new StringRequest(Request.Method.PUT, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("true")){

                                progress.dismiss();
                                if (list_moshaverins.get(position).isActive()){
                                    list_moshaverins.get(position).setActive(false);
                                    holder.btn.setBackground(context.getResources().getDrawable(R.color.colorRed));
                                    holder.btn.setText("تایید نشده");
                                }

                                else{
                                    list_moshaverins.get(position).setActive(true);
                                    holder.btn.setBackground(context.getResources().getDrawable(R.color.colorBlue));
                                    holder.btn.setText("تایید شده");
                                }
                            }else{
                                progress.dismiss();
                                new AlertDialog.Builder(context)
                                        .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                                        .setMessage((Html.fromHtml("<font color='#FF7F27'>شما سطح دسترسی این کار را ندارید</font>")))
                                        .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                            new AlertDialog.Builder(context)
                                    .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                                    .setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")))
                                    .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(request);

                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.CheckConnectionInternet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_moshaverins.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        Button btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.TextRecordMoshaverin);
            btn = itemView.findViewById(R.id.btnRecordMoshaverin);
        }
    }
}
