package ah.production.dileverybot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ah.production.dileverybot.R;
import ah.production.dileverybot.model.CartItemsData;

public class ConfrimAdapter extends RecyclerView.Adapter<ConfrimAdapter.VegViewHolder> {

    Context mContext;
    ArrayList<CartItemsData> mData;
    private OnItemClickListner onItemClickListner;

    public ConfrimAdapter(Context mContext, ArrayList<CartItemsData> mData, OnItemClickListner onItemClickListner) {
        this.mContext = mContext;
        this.mData = mData;
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public VegViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_scaler, viewGroup, false);
        return new VegViewHolder(layout, onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull VegViewHolder holder, int position) {

        holder.tv_item_name.setText(mData.get(position).getItem_name());
        holder.tv_scale.setText(mData.get(position).getItem_quanti() + " g");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class VegViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView tv_item_name;
        TextView tv_scale;
        OnItemClickListner onItemClickListner_;

        public VegViewHolder(@NonNull View itemView, OnItemClickListner onItemClickListner) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_scale = itemView.findViewById(R.id.tv_scale);

            this.onItemClickListner_ = onItemClickListner;

            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            onItemClickListner_.onItemClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position);
    }
}
