package com.example.santoriniapp.modules.aliquotelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santoriniapp.R;
import com.example.santoriniapp.databinding.ListItemPaymentListSummaryBinding;

import java.util.List;

public class PaymentSummaryAdapter extends RecyclerView.Adapter<PaymentSummaryAdapter.ViewHolder>    {

    Context mContext;
    private List<PaymentSummaryItem> mItemList;
    PaymentSummaryItemListener mlistener;
    public PaymentSummaryAdapter(Context context,
                              List<PaymentSummaryItem> itemList, PaymentSummaryItemListener listener) {
        mContext        = context;
        mlistener       = listener;
        mItemList       = itemList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ListItemPaymentListSummaryBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_payment_list_summary, parent, false);

        return new ViewHolder(binding);
    }

    public void setItemList(List<PaymentSummaryItem> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // Get Item.
        PaymentSummaryItem item = mItemList.get(position);
        holder.binding.setViewModel(item);
        holder.binding.setListener(mlistener);
        holder.binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }


    /**
     * ViewHolders
     */

    public class ViewHolder extends RecyclerView.ViewHolder{
        ListItemPaymentListSummaryBinding binding;
        public ViewHolder(ListItemPaymentListSummaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
