package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/4/2019.
 */

public class Fragment_Manegment_Profile extends Fragment {

    public static final String TAG="Fragment_Manegment_Profile";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_manegment_profile,container,false);
        return view;
    }
}
