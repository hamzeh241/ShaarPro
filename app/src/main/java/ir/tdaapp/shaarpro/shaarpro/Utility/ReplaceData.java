package ir.tdaapp.shaarpro.shaarpro.Utility;

/**
 * Created by Diako on 8/26/2019.
 */

public class ReplaceData {

    public String Number_PersianToEnglish(String Number) {
        Number = Number.replace("۰", "0");
        Number = Number.replace("۱", "1");
        Number = Number.replace("۲", "2");
        Number = Number.replace("۳", "3");
        Number = Number.replace("۴", "4");
        Number = Number.replace("۵", "5");
        Number = Number.replace("۶", "6");
        Number = Number.replace("۷", "7");
        Number = Number.replace("۸", "8");
        Number = Number.replace("۹", "9");
        return Number;
    }

    public String ReplaceError(String Error){

        String e=Error;
        e=e.replace("!","");
        e=e.replace("@","");
        e=e.replace("#","");
        e=e.replace("$","");
        e=e.replace("%","");
        e=e.replace("^","");
        e=e.replace("&","");
        e=e.replace("*","");
        e=e.replace("(","");
        e=e.replace(")","");
        e=e.replace("-","");
        e=e.replace("_","");
        e=e.replace("=","");
        e=e.replace("+","");
        e=e.replace("/","");

        return e;
    }

}
