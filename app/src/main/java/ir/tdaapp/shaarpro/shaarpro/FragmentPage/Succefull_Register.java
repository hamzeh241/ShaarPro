package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/5/2019.
 */

public class Succefull_Register extends Fragment {
    Button btn_Down;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.succefull_register, container, false);

        btn_Down = view.findViewById(R.id.btn_Done);

        btn_Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().getSupportFragmentManager().
                        beginTransaction().replace(R.id.Fragment_Main, new Fragment_Home()).commit();
            }
        });
        return view;
    }
}
