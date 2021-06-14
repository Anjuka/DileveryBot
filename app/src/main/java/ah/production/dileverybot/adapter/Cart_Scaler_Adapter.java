package ah.production.dileverybot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ah.production.dileverybot.R;
import ah.production.dileverybot.model.CartItemsData;

public class Cart_Scaler_Adapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<CartItemsData> cartItemsData;

    public Cart_Scaler_Adapter(Context context, ArrayList<CartItemsData> cartItemsData) {
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
            convertView = inflater.inflate(R.layout.item_cart_scaler, null);
        }

        TextView tv_item_name = convertView.findViewById(R.id.tv_item_name);
        TextView tv_conti = convertView.findViewById(R.id.tv_conti);

        tv_item_name.setText("◯  " + cartItemsData.get(position).getItem_name());

        tv_conti.setVisibility(View.VISIBLE);
        tv_conti.setText(cartItemsData.get(position).getItem_quanti());

        return convertView;
    }

}
