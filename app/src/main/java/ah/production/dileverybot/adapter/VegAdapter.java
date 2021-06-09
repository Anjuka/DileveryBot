package ah.production.dileverybot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ah.production.dileverybot.R;
import ah.production.dileverybot.model.ItemsData;

public class VegAdapter extends RecyclerView.Adapter<VegAdapter.VegViewHolder> {

    Context mContext;
    ArrayList<ItemsData> mData;
    private OnItemClickListner onItemClickListner;

    public VegAdapter(Context mContext, ArrayList<ItemsData> mData, OnItemClickListner onItemClickListner) {
        this.mContext = mContext;
        this.mData = mData;
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public VegViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_veg, viewGroup, false);
        return new VegViewHolder(layout, onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull VegViewHolder holder, int position) {

        holder.tv_item_name.setText(mData.get(position).getItem_name());
        if (mData.get(position).isIs_check()){
            holder.cb_select.setVisibility(View.VISIBLE);
        }
        else {
            holder.cb_select.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class VegViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_item_name;
        ImageView cb_select;
        OnItemClickListner onItemClickListner_;

        public VegViewHolder(@NonNull View itemView, OnItemClickListner onItemClickListner) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            cb_select = itemView.findViewById(R.id.cb_select);
            this.onItemClickListner_ = onItemClickListner;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClickListner_.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position);
    }
}
