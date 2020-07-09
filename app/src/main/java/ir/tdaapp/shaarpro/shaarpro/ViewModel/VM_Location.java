package ir.tdaapp.shaarpro.shaarpro.ViewModel;

/**
 * Created by Diako on 8/4/2019.
 */

public class VM_Location {
    private int Id;
    private String Title;

    public VM_Location(){}
    public VM_Location(int Id,String Title){
        this.Id=Id;
        this.Title=Title;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String toString() {
        return Title;
    }
}
