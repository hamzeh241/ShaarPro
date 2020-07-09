package ir.tdaapp.shaarpro.shaarpro.ViewModel;

/**
 * Created by Diako on 8/12/2019.
 */

public class VM_Image {
    private int Id;
    private String ImageUrl;

    public VM_Image(){}
    public VM_Image(int Id,String Url){
        this.Id=Id;
        this.ImageUrl=Url;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUrl() {
        return ImageUrl;
    }

    public void setUrl(String url) {
        ImageUrl = url;
    }
}
