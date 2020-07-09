package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Edit_Location_User;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Seting_Moshaverin;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Moshaverin;

/**
 * Created by Diako on 8/3/2019.
 */

public class EditMoshaverinAdapter extends RecyclerView.Adapter<EditMoshaverinAdapter.MyViewHolder> implements IBase{

    List<VM_Moshaverin> list_moshaverins;
    Context context;

    public EditMoshaverinAdapter(List<VM_Moshaverin> list_moshaverins, Context context){
        this.list_moshaverins=list_moshaverins;
        this.context=context;
    }
    @Override

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_edit_moshaverin,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txt.setText(list_moshaverins.get(position).getName());
        holder.btnEdit.setText("ویرایش");
        boolean temp=list_moshaverins.get(position).isAdmin();
        if (temp){
            holder.btnActive.setBackground(context.getResources().getDrawable(R.color.colorBlue));
            holder.btnActive.setText("مدیر");
        }
        else {
            holder.btnActive.setBackground(context.getResources().getDrawable(R.color.colorRed));
            holder.btnActive.setText("عادی");
        }

        int Id=((MainActivity)context).user.GetId();

        holder.btnEdit.setVisibility(View.VISIBLE);
        if (Id==list_moshaverins.get(position).getId()){
            holder.btnEdit.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("Id",list_moshaverins.get(position).getId());

                Fragment_Edit_Location_User locationUser=new Fragment_Edit_Location_User();
                locationUser.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, locationUser).commit();
            }
        });

        holder.btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((MainActivity)context).internet.HaveNetworkConnection()){

                    final ProgressDialog progress = new ProgressDialog(context);
                    progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال دریافت اطلاعات</font>")));
                    progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    int Id=((MainActivity)context).user.GetId();
                    String Url=Api+"User/PutUserProActivate?id="+list_moshaverins.get(position).getId()+"&idpro="+Id+"&admin="+!list_moshaverins.get(position).isAdmin()+"&status="+list_moshaverins.get(position).isActive();

                    StringRequest request =new StringRequest(Request.Method.PUT, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("true")){

                                progress.dismiss();
                                if (list_moshaverins.get(position).isAdmin()){
                                    list_moshaverins.get(position).setAdmin(false);
                                    holder.btnActive.setBackground(context.getResources().getDrawable(R.color.colorRed));
                                    holder.btnActive.setText("عادی");
                                }
                                else{
                                    list_moshaverins.get(position).setAdmin(true);
                                    holder.btnActive.setBackground(context.getResources().getDrawable(R.color.colorBlue));
                                    holder.btnActive.setText("مدیر");
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
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_moshaverins.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Button btnEdit, btnActive;
        TextView txt;
        RelativeLayout Item;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnActive = itemView.findViewById(R.id.EditMoshaverin_btnActive);
            btnEdit = itemView.findViewById(R.id.EditMoshaverin_btnEdit);
            txt = itemView.findViewById(R.id.EditMoshaverin_txt);
            Item=itemView.findViewById(R.id.Item);
        }
    }

}
