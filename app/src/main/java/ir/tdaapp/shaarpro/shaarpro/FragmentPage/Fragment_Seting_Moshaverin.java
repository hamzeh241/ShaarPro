package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ir.tdaapp.shaarpro.shaarpro.Adapter.ViewPager_TabMoshaverinAdapter;
import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/3/2019.
 */

public class Fragment_Seting_Moshaverin extends Fragment {

    public static final String TAG="Fragment_Seting_Moshaverin";

    ViewPager viewPager;
    TabLayout tabLayout;
    RelativeLayout backall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_seting_moshaverin,container,false);


        FindItem(view);

        OnClick();

        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }



    public void FindItem(View view){
        viewPager=view.findViewById(R.id.ViewPager);
        tabLayout=view.findViewById(R.id.tab_layout);
        backall=view.findViewById(R.id.backall);
    }

    private void setViewPager(ViewPager viewPager){
        ViewPager_TabMoshaverinAdapter adapter=new ViewPager_TabMoshaverinAdapter( getActivity().getSupportFragmentManager());
        adapter.addFragment(new Tab_Record_Moshaverin(),"ثبت");
        adapter.addFragment(new Tab_Edit_Moshaverin(),"ویرایش");
        viewPager.setAdapter(adapter);
    }

    void OnClick(){
        backall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Home()).commit();
            }
        });
    }

}
