package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.reactivex.annotations.NonNull;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Dialogs.List_Users_Edit_Location;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Location;

/**
 * Created by Diako on 8/4/2019.
 */

public class EditLocationUserAdapter extends RecyclerView.Adapter<EditLocationUserAdapter.MyViewHolder> {

    List<VM_Location> locations;
    Context context;
    int UserId;

    public EditLocationUserAdapter(List<VM_Location> locations,Context context,int UserId){
        this.locations=locations;
        this.context=context;
        this.UserId=UserId;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_edit_location_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.Title.setText(locations.get(position).getTitle());

        holder.Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putInt("LocationId",locations.get(position).getId());
                bundle.putInt("UserId",UserId);

                List_Users_Edit_Location list_users_edit_location=new List_Users_Edit_Location();

                list_users_edit_location.setArguments(bundle);

                list_users_edit_location.show(((AppCompatActivity)context).getSupportFragmentManager(),list_users_edit_location.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Title;
        RelativeLayout Remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            Title=itemView.findViewById(R.id.Title);
            Remove=itemView.findViewById(R.id.Remove);
        }
    }
}
