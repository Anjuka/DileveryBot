package ah.production.dileverybot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderData {

    @SerializedName("order_time")
    @Expose
    private String order_time;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("order_date")
    @Expose
    private String order_date;

    @SerializedName("item_name")
    @Expose
    private int order_number;

    @SerializedName("order_list")
    @Expose
    private ArrayList<CartItemsData> order_list;

    public OrderData() {
    }

    public OrderData(String order_time, String status, String order_date, int order_number, ArrayList<CartItemsData> order_list) {
        this.order_time = order_time;
        this.status = status;
        this.order_date = order_date;
        this.order_number = order_number;
        this.order_list = order_list;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public ArrayList<CartItemsData> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<CartItemsData> order_list) {
        this.order_list = order_list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
