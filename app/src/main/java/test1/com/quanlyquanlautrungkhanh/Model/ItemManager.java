package test1.com.quanlyquanlautrungkhanh.Model;

public class ItemManager {
    int resourceImage;
    String nameItem;

    public ItemManager() {
    }

    public ItemManager(int resourceImage, String nameOption) {
        this.resourceImage = resourceImage;
        this.nameItem = nameOption;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(int resourceImage) {
        this.resourceImage = resourceImage;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameOption) {
        this.nameItem = nameOption;
    }
}
