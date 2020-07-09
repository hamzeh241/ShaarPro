package ir.tdaapp.shaarpro.shaarpro.ViewModel;

/**
 * Created by Diako on 8/3/2019.
 */

public class VM_Moshaverin {
    private String Name;
    private int Id;
    private boolean Active;
    private boolean IsAdmin;

    public VM_Moshaverin(){}
    public VM_Moshaverin(String Name,int Id,boolean Active){
        this.Name=Name;
        this.Id=Id;
        this.Active=Active;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }
}
