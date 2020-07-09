package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 7/31/2019.
 */

public class Fragment_Home extends Fragment {

    public static final String TAG="Fragment_Home";

    RelativeLayout ManegmentUser,Confirm_Home,Wait_Confirm,Arshive_Home;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        FindItem(view);
        OnClick();

        return view;
    }

    void FindItem(View view){
        ManegmentUser=view.findViewById(R.id.ManegmentUser);
        Confirm_Home=view.findViewById(R.id.Confirm_Home);
        Wait_Confirm=view.findViewById(R.id.Wait_Confirm);
        Arshive_Home=view.findViewById(R.id.Arshive_Home);
    }

    void OnClick(){
        ManegmentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Seting_Moshaverin()).commit();
            }
        });

        Confirm_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment_Manegment_Homes manegment_homes=new Fragment_Manegment_Homes();

                Bundle bundle=new Bundle();
                bundle.putInt("Page",0);
                manegment_homes.setArguments(bundle);

                 getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, manegment_homes).commit();
            }
        });

        Wait_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Manegment_Homes manegment_homes=new Fragment_Manegment_Homes();

                Bundle bundle=new Bundle();
                bundle.putInt("Page",1);
                manegment_homes.setArguments(bundle);

                 getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, manegment_homes).commit();
            }
        });

        Arshive_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Manegment_Homes manegment_homes=new Fragment_Manegment_Homes();

                Bundle bundle=new Bundle();
                bundle.putInt("Page",2);
                manegment_homes.setArguments(bundle);

                 getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, manegment_homes).commit();
            }
        });
    }
}
