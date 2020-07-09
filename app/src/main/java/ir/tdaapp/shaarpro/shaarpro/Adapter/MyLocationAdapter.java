package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;
import io.reactivex.annotations.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/4/2019.
 */

public class MyLocationAdapter extends RecyclerView.Adapter<MyLocationAdapter.MyViewHolder> {

    Context context;
    String[]values;

    public MyLocationAdapter(Context context,String[]values){
        this.context=context;
        this.values=values;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_my_location,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Text.setText(values[position]);
    }

    @Override
    public int getItemCount() {
        return values.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Text;

        public MyViewHolder(View itemView) {
            super(itemView);

            Text=itemView.findViewById(R.id.Text);
        }
    }
}
