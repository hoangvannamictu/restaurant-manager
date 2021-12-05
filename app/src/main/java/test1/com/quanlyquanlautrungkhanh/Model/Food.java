package test1.com.quanlyquanlautrungkhanh.Model;

import java.io.Serializable;

public class Food implements Serializable {
    private String idFood;
    private String nameFood;
    private int amountFood;
    private int priceFood;
    private String tagFood;
    private int statusFood;
    private String imgFood;
    private String nameImgFood;

    public Food() {
    }

    public Food(String idFood, String nameFood, int amountFood, int priceFood,String imgFood, String tagFood, int statusFood) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.amountFood = amountFood;
        this.priceFood = priceFood;
        this.imgFood = imgFood;
        this.tagFood = tagFood;
        this.statusFood = statusFood;
    }

    public String getImgFood() {
        return imgFood;
    }

    public void setImgFood(String imgFood) {
        this.imgFood = imgFood;
    }

    public String getNameImgFood() {
        return nameImgFood;
    }

    public void setNameImgFood(String nameImgFood) {
        this.nameImgFood = nameImgFood;
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

    public String getTagFood() {
        return tagFood;
    }

    public void setTagFood(String tagFood) {
        this.tagFood = tagFood;
    }

    public int getStatusFood() {
        return statusFood;
    }

    public void setStatusFood(int statusFood) {
        this.statusFood = statusFood;
    }
}
