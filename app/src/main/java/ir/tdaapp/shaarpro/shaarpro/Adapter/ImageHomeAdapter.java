package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Data.DA_Add_Home;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Property_Home;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Show_Images;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/11/2019.
 */

public class ImageHomeAdapter extends RecyclerView.Adapter<ImageHomeAdapter.MyViewHolder> {

    Context context;
    List<Bitmap> val;

    public ImageHomeAdapter(Context context){
        this.context=context;
        val=new ArrayList<>();
    }

    public void Add(Bitmap bit){
        val.add(bit);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_list_add_image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.img.setImageBitmap(val.get(position));

        holder.img_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val.remove(position);
                DA_Add_Home.Images.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("Id",DA_Add_Home.ItemId);

                Fragment_Show_Images show_images=new Fragment_Show_Images();

                show_images.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager().
                        beginTransaction().replace(R.id.ThirdFragment, show_images).commit();

                ((MainActivity) context).ThirdFragment.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return val.size();
    }

    //برای ارسال لیست عکس ها
    public List<Bitmap>GetValues(){
        return val;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        ImageView img_Remove;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_add_image);
            img_Remove=itemView.findViewById(R.id.Remove);
        }
    }
}
