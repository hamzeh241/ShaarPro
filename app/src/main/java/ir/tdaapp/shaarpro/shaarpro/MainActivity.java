package ir.tdaapp.shaarpro.shaarpro;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.circularreveal.CircularRevealFrameLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import ir.tdaapp.shaarpro.shaarpro.Adapter.DBAdapter;
import ir.tdaapp.shaarpro.shaarpro.Data.DA_Add_Home;
import ir.tdaapp.shaarpro.shaarpro.DataBase.ETC;
import ir.tdaapp.shaarpro.shaarpro.DataBase.Location;
import ir.tdaapp.shaarpro.shaarpro.DataBase.TypeHome;
import ir.tdaapp.shaarpro.shaarpro.DataBase.User;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Add_Account;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Edit_Home;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Edit_Location_User;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Home;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Login_Home;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Manegment_Homes;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Property_Home;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Seting_Moshaverin;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Show_Images;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Splash;
import ir.tdaapp.shaarpro.shaarpro.Utility.Internet;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public DBAdapter dbAdapter;
    public User user;
    public Internet internet;
    public ETC etc;
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment;
    public FrameLayout Fragment_Main, SecondFragment,ThirdFragment;
    public TypeHome typeHome;
    public Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "6R4FG3PCJPYJ4TR84RCP");

        FindItem();

        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Main, new Fragment_Splash()).commit();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void FindItem() {
        dbAdapter = new DBAdapter(this);
        user = new User(dbAdapter);
        internet = new Internet(this);
        etc = new ETC(dbAdapter);
        Fragment_Main = findViewById(R.id.Fragment_Main);
        SecondFragment = findViewById(R.id.SecondFragment);
        typeHome=new TypeHome(dbAdapter);
        location=new Location(dbAdapter);
        ThirdFragment=findViewById(R.id.ThirdFragment);
    }

    @Override
    public void onBackPressed() {

        if (Fragment_Main.getVisibility() == View.VISIBLE) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.Fragment_Main);
        } else if (SecondFragment.getVisibility()==View.VISIBLE){
            fragment = getSupportFragmentManager().findFragmentById(R.id.SecondFragment);
        }else if (ThirdFragment.getVisibility()==View.VISIBLE){
            fragment = getSupportFragmentManager().findFragmentById(R.id.ThirdFragment);
        }

        if (fragment instanceof Fragment_Home || fragment instanceof Fragment_Login_Home) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        }

        if (fragment instanceof Fragment_Home || fragment instanceof Fragment_Login_Home) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "لطفا یک بار دیگر کلیک کنید", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (fragment instanceof Fragment_Seting_Moshaverin) {
            ((AppCompatActivity) this).getSupportFragmentManager().
                    beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Home()).commit();
        } else if (fragment instanceof Fragment_Manegment_Homes) {
            ((AppCompatActivity) this).getSupportFragmentManager().
                    beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Home()).commit();
        } else if (fragment instanceof Fragment_Edit_Location_User) {
            ((AppCompatActivity) this).getSupportFragmentManager().
                    beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Seting_Moshaverin()).commit();
        }else if (fragment instanceof Fragment_Edit_Home){
            DA_Add_Home.ClearDA_AddHome();
            Fragment_Main.setVisibility(View.VISIBLE);
            SecondFragment.setVisibility(View.GONE);
            SecondFragment.removeAllViews();
        }else if (fragment instanceof Fragment_Property_Home){
            SecondFragment.setVisibility(View.VISIBLE);
            ThirdFragment.setVisibility(View.GONE);
            ThirdFragment.removeAllViews();
        }else if (fragment instanceof Fragment_Add_Account){
            ((AppCompatActivity) this).getSupportFragmentManager().
                    beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Login_Home()).commit();
        }else if (fragment instanceof Fragment_Show_Images){
            SecondFragment.setVisibility(View.VISIBLE);
            ThirdFragment.setVisibility(View.GONE);
            ThirdFragment.removeAllViews();
        }

    }
}
