package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.os.Bundle;
import io.reactivex.annotations.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.tdaapp.shaarpro.shaarpro.R;

/**
 * Created by Diako on 8/5/2019.
 */

public class Fragment_Wait_To_Confirm extends Fragment {

    Button btn_Done;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_wait_to_confirm,container,false);

        btn_Done=view.findViewById(R.id.btn_Done);

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        return view;
    }
}
