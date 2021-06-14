package ah.production.dileverybot.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CartItemsList implements Parcelable {

    @SerializedName("CartItemsData")
    @Expose
    private ArrayList<CartItemsData> cartItemsData;

    public CartItemsList(ArrayList<CartItemsData> cartItemsData) {
        this.cartItemsData = cartItemsData;
    }

    protected CartItemsList(Parcel in) {
    }

    public static final Creator<CartItemsList> CREATOR = new Creator<CartItemsList>() {
        @Override
        public CartItemsList createFromParcel(Parcel in) {
            return new CartItemsList(in);
        }

        @Override
        public CartItemsList[] newArray(int size) {
            return new CartItemsList[size];
        }
    };

    public ArrayList<CartItemsData> getCartItemsData() {
        return cartItemsData;
    }

    public void setCartItemsData(ArrayList<CartItemsData> cartItemsData) {
        this.cartItemsData = cartItemsData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(cartItemsData);
    }
}
