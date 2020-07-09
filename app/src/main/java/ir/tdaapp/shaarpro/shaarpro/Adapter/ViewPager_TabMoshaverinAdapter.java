package ir.tdaapp.shaarpro.shaarpro.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diako on 8/3/2019.
 */

public class ViewPager_TabMoshaverinAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;
    List<String> stringList;

    public ViewPager_TabMoshaverinAdapter(FragmentManager fm) {
        super(fm);
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
