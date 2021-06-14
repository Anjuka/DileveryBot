package ah.production.dileverybot.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartItemsData implements Parcelable {


    @SerializedName("item_name")
    @Expose
    private String item_name;

    @SerializedName("is_check")
    @Expose
    private boolean is_check;

    @SerializedName("position")
    @Expose
    private int position;

    @SerializedName("item_quanti")
    @Expose
    private String item_quanti;

    public CartItemsData(String item_name, boolean is_check, int position, String item_quanti) {
        this.item_name = item_name;
        this.is_check = is_check;
        this.position = position;
        this.item_quanti = item_quanti;
    }

    protected CartItemsData(Parcel in) {
        item_name = in.readString();
        is_check = in.readByte() != 0;
        position = in.readInt();
        item_quanti = in.readString();
    }

    public static final Creator<CartItemsData> CREATOR = new Creator<CartItemsData>() {
        @Override
        public CartItemsData createFromParcel(Parcel in) {
            return new CartItemsData(in);
        }

        @Override
        public CartItemsData[] newArray(int size) {
            return new CartItemsData[size];
        }
    };

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

    public String getItem_quanti() {
        return item_quanti;
    }

    public void setItem_quanti(String item_quanti) {
        this.item_quanti = item_quanti;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_name);
        dest.writeByte((byte) (is_check ? 1 : 0));
        dest.writeInt(position);
        dest.writeString(item_quanti);
    }
}
