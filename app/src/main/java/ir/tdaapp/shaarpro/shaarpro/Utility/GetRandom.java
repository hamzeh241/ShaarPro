package ir.tdaapp.shaarpro.shaarpro.Utility;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Diako on 7/9/2019.
 */

public class GetRandom {

    public static long GetLong(){

        Random random=new Random();
        int A=random.nextInt(100);
        int B=random.nextInt(110);

        return A*B;
    }
}
