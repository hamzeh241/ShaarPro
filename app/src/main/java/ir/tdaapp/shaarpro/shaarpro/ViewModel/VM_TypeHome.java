package ir.tdaapp.shaarpro.shaarpro.ViewModel;

/**
 * Created by Diako on 8/10/2019.
 */

public class VM_TypeHome {
    private int Id;
    private String Text;

    public VM_TypeHome(String Text,int Id){
        this.Text=Text;
        this.Id=Id;
    }

    public void SetText(String Text) {
        this.Text = Text;
    }

    public String GetText() {
        return this.Text;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
