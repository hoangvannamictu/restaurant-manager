package test1.com.quanlyquanlautrungkhanh.Model;

public class ItemFood {
    private String nameFood;
    private String idFood;
    private int amountFood;
    private int priceFood;
    private String tagFood;

    public ItemFood() {
    }

    public ItemFood(String idFood, String nameFood, int amountFood, int priceFood, String tagFood) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.amountFood = amountFood;
        this.priceFood = priceFood;
        this.tagFood = tagFood;
    }

    public String getTagFood() {
        return tagFood;
    }

    public void setTagFood(String tagFood) {
        this.tagFood = tagFood;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public int getAmountFood() {
        return amountFood;
    }

    public void setAmountFood(int amountFood) {
        this.amountFood = amountFood;
    }

    public int getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(int priceFood) {
        this.priceFood = priceFood;
    }
}
