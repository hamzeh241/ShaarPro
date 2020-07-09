package ir.tdaapp.shaarpro.shaarpro.ViewModel;

/**
 * Created by Diako on 8/13/2019.
 */

public class VM_Id_FeaturesGroup {
    private int GroupId;
    private int FeaturesId;

    public VM_Id_FeaturesGroup(int Id,int FeaturesId){
        this.GroupId=Id;
        this.FeaturesId=FeaturesId;
    }


    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getFeaturesId() {
        return FeaturesId;
    }

    public void setFeaturesId(int featuresId) {
        FeaturesId = featuresId;
    }
}
