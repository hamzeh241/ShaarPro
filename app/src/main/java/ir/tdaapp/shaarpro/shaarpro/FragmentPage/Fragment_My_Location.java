package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.tdaapp.shaarpro.shaarpro.Adapter.MyLocationAdapter;
import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/4/2019.
 */

public class Fragment_My_Location extends Fragment {

    RecyclerView Recycler;
    MyLocationAdapter myLocationAdapter;
    GridLayoutManager gridLayoutManager;

    public static final String TAG="Fragment_My_Location";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_location,container,false);

        FindItem(view);

        SetVal();

        return view;
    }

    void FindItem(View view){
        Recycler=view.findViewById(R.id.Recycler);
    }

    //در اینجا داده ها در Recycler view set می شوند
    void SetVal(){
        myLocationAdapter=new MyLocationAdapter(getContext(),GetVal());
        gridLayoutManager=new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false);
        Recycler.setAdapter(myLocationAdapter);
        Recycler.setLayoutManager(gridLayoutManager);
    }

    String[] GetVal(){
        String[]val=new String[5];

        for (int i=0;i<val.length;i++){
            val[i]="بهاران "+i;
        }
        return val;
    }
}
