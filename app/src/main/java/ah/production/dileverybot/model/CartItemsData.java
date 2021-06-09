package ah.production.dileverybot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartItemsData {


    @SerializedName("item_name")
    @Expose
    private String item_name;

    @SerializedName("is_check")
    @Expose
    private boolean is_check;

    @SerializedName("position")
    @Expose
    private int position;

    public CartItemsData(String item_name, boolean is_check, int position) {
        this.item_name = item_name;
        this.is_check = is_check;
        this.position = position;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public boolean isIs_check() {
        return is_check;
    }

    public void setIs_check(boolean is_check) {
        this.is_check = is_check;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
