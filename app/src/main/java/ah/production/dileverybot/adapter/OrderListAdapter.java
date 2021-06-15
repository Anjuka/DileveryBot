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
import ah.production.dileverybot.model.OrderData;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.VegViewHolder> {

    Context mContext;
    ArrayList<OrderData> mData;
    private OnItemClickListner onItemClickListner;

    public OrderListAdapter(Context mContext, ArrayList<OrderData> mData, OnItemClickListner onItemClickListner) {
        this.mContext = mContext;
        this.mData = mData;
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public VegViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_orderlist, viewGroup, false);
        return new VegViewHolder(layout, onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull VegViewHolder holder, int position) {

        holder.tv_orderDate.setText(mData.get(position).getOrder_date());
        holder.tv_ordernumber.setText(String.valueOf(mData.get(position).getOrder_number()));
        holder.tv_orderTime.setText(mData.get(position).getOrder_time());
        holder.tv_orderItems.setText(String.valueOf(mData.size()));
        holder.tv_status.setText(mData.get(position).getStatus());

        switch (mData.get(position).getStatus()){
            case "inprogress":
                holder.iv_status.setBackgroundResource(R.color.orange);
                break;

            case "accept":
                holder.iv_status.setBackgroundResource(R.color.yellow);
                break;

            case "delivering":
                holder.iv_status.setBackgroundResource(R.color.blue);
                break;

            case "completed":
                holder.iv_status.setBackgroundResource(R.color.green);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class VegViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_ordernumber;
        TextView tv_orderDate;
        TextView tv_orderTime;
        TextView tv_orderItems;
        TextView tv_status;
        View iv_status;
        OnItemClickListner onItemClickListner_;

        public VegViewHolder(@NonNull View itemView, OnItemClickListner onItemClickListner) {
            super(itemView);
            tv_ordernumber = itemView.findViewById(R.id.tv_ordernumber);
            tv_orderDate = itemView.findViewById(R.id.tv_orderDate);
            tv_orderTime = itemView.findViewById(R.id.tv_orderTime);
            tv_orderItems = itemView.findViewById(R.id.tv_orderItems);
            iv_status = itemView.findViewById(R.id.iv_status);
            tv_status = itemView.findViewById(R.id.tv_status);

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
