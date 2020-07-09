package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Location;

/**
 * Created by Diako on 8/15/2019.
 */

public class AddNewLocationAdapter extends RecyclerView.Adapter<AddNewLocationAdapter.MyViewHolder> {

    List<VM_Location> locations;
    Context context;
    List<Integer> values;

    public List<Integer> GetValues(){
        return values;
    }

    public AddNewLocationAdapter(List<VM_Location> locations,Context context){
        this.locations=locations;
        this.context=context;
        values=new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_add_new_location,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.checkBox.setText(locations.get(position).getTitle());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=GetPositionValue(locations.get(position).getId());

                if (val==-1){
                    values.add(locations.get(position).getId());
                }else{
                    values.remove(val);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    int GetPositionValue(int id){

        int position=-1;

        for (int i=0;i<values.size();i++){
            int val=values.get(i);

            if (val==id)
                position=i;
        }
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);

            checkBox=itemView.findViewById(R.id.CheckBox);
        }
    }
}
