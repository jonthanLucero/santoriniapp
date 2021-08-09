package com.example.santoriniapp.modules.payment.paymentlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santoriniapp.R;
import com.example.santoriniapp.databinding.ListItemWwPaymentPanelBinding;

import java.util.List;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder>    {

    Context mContext;
    private List<PaymentItem> mItemList;
    PaymentItemListener mlistener;
    public PaymentListAdapter(Context context,
                          List<PaymentItem> itemList, PaymentItemListener listener) {
        mContext        = context;
        mlistener       = listener;
        mItemList       = itemList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ListItemWwPaymentPanelBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_ww_payment_panel, parent, false);

        return new ViewHolder(binding);
    }

    public void setItemList(List<PaymentItem> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // Get Item.
        PaymentItem item = mItemList.get(position);
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
        ListItemWwPaymentPanelBinding binding;
        public ViewHolder(ListItemWwPaymentPanelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
