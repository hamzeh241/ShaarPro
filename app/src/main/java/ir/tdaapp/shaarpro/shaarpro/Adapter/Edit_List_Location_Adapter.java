package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import io.reactivex.annotations.NonNull;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Edit_Location_User;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_EditLocationUser;

/**
 * Created by Diako on 8/7/2019.
 */

public class Edit_List_Location_Adapter extends RecyclerView.Adapter<Edit_List_Location_Adapter.MyViewHolder> implements IBase {

    List<VM_EditLocationUser> locationUsers;
    Context context;

    public Edit_List_Location_Adapter(List<VM_EditLocationUser> locationUsers,Context context){
        this.locationUsers=locationUsers;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_users_edit_location,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.Title.setText(locationUsers.get(position).getName());

        holder.btn_Replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btn_Replace.setText(context.getResources().getString(R.string.Wait));

                String url=Api+"User/RemoveUserProLocation?locId="+locationUsers.get(position).getLocationId()+"&userProId="+locationUsers.get(position).getFrom_Id()+"&userProIdNew="+locationUsers.get(position).getTo_Id();

                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        holder.btn_Replace.setText(context.getResources().getString(R.string.Replace));

                        if (response.equalsIgnoreCase("true")){
                            Toast.makeText(context, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            Bundle bundle=new Bundle();
                            bundle.putInt("Id",locationUsers.get(position).getFrom_Id());

                            Fragment_Edit_Location_User locationUser=new Fragment_Edit_Location_User();
                            locationUser.setArguments(bundle);

                            ((AppCompatActivity) context).getSupportFragmentManager().
                                    beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, locationUser).commit();

                        }else{
                            Toast.makeText(context, "عملیات انجام نشد", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.btn_Replace.setText(context.getResources().getString(R.string.Replace));
                        Toast.makeText(context, "اینترنت شما ضعیف است لطفا مجددا امتحان کنید", Toast.LENGTH_SHORT).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationUsers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Title;
        Button btn_Replace;

        public MyViewHolder(View itemView) {
            super(itemView);

            Title=itemView.findViewById(R.id.Title);
            btn_Replace=itemView.findViewById(R.id.btn_Replace);
        }
    }
}
