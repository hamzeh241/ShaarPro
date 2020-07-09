package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;
import io.reactivex.annotations.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diako on 8/3/2019.
 */

public class TabHomesAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;
    List<String> stringList;
    Context context;

    public TabHomesAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context=context;
        fragmentList=new ArrayList<>();
        stringList=new ArrayList<>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        stringList.add(title);
    }

}
