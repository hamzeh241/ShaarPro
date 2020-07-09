package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ir.tdaapp.shaarpro.shaarpro.Adapter.TabHomesAdapter;
import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/3/2019.
 */

public class Fragment_Manegment_Homes extends Fragment{

    public static final String TAG="Fragment_Manegment_Homes";

    ViewPager viewPager;
    TabLayout tabLayout;
    RelativeLayout backall;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_manegment_homes,container,false);

        FindItem(view);
        OnClick();
        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        //در اینجا تعیین می شود که کاربر از طریق کدام دکمه وارد شده است اگر صفر باشد وارد تب منتشر شده می شود اگر یک باشد وارد تب انتظار تایید می شود و اگ دو باشد وارد تب آرشیو می شود
        Bundle bundle=getArguments();
        if (bundle!=null){
            viewPager.setCurrentItem(bundle.getInt("Page"));
        }

        return view;
    }

    public void FindItem(View view){
        viewPager=view.findViewById(R.id.ViewPager);
        tabLayout=view.findViewById(R.id.tab_layout);
        backall=view.findViewById(R.id.backall);
    }

    private void setViewPager(ViewPager viewPager){
        TabHomesAdapter adapter=new TabHomesAdapter( getActivity().getSupportFragmentManager(),getContext());
        adapter.addFragment(new Tab_Published_Homes(),getResources().getString(R.string.PublishedHome));
        adapter.addFragment(new Tab_Wait_Confirmation(),getResources().getString(R.string.WaitHome));
        adapter.addFragment(new Tab_Archive_Homes(),getResources().getString(R.string.ArchiveHome));
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
