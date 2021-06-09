package ah.production.dileverybot.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ah.production.dileverybot.R;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<CartItemsData> cartItemsData;

    public CartAdapter(Context context, ArrayList<CartItemsData> cartItemsData) {
        this.context = context;
        this.cartItemsData = cartItemsData;
    }

    @Override
    public int getCount() {
        return cartItemsData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cart, null);
        }

        TextView tv_item_name = convertView.findViewById(R.id.tv_item_name);

        tv_item_name.setText("◯  " + cartItemsData.get(position).getItem_name());

        return convertView;
    }

}
