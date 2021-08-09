package com.example.santoriniapp.modules.payment.paymentlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.santoriniapp.R;

import java.util.List;

public class PaymentDateRowArrayAdapterDropDown extends ArrayAdapter<PaymentDateRowSpinnerItem>
{
    Context context;
    List<PaymentDateRowSpinnerItem> itemList;
    LayoutInflater inflater;
    int resourceID;
    int resourceDropDownID;

    public PaymentDateRowArrayAdapterDropDown(Context applicationContext, List<PaymentDateRowSpinnerItem> itemsList
            ,int resourceID,int resourceDropDownID) {
        super(applicationContext,resourceID,resourceDropDownID,itemsList);

        this.context    = applicationContext;
        this.itemList   = itemsList;
        this.inflater   = (LayoutInflater.from(applicationContext));
        this.resourceID   = resourceID;
        this.resourceDropDownID   = resourceDropDownID;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        convertView = inflater.inflate(resourceDropDownID, null);

        // Get the Views references.
        TextView periodDescriptionLabel     = convertView.findViewById(R.id.period_description_label);
        PaymentDateRowSpinnerItem item= itemList.get(position);
        periodDescriptionLabel.setText(item.description);
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(resourceID, null);

        // Get the Views references.
        TextView periodDescriptionLabel     = convertView.findViewById(R.id.period_description_label);
        PaymentDateRowSpinnerItem item= itemList.get(position);
        periodDescriptionLabel.setText(item.description);
        return convertView;
    }
}
