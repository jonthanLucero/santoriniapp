package com.example.santoriniapp.modules.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santoriniapp.R;
import com.example.santoriniapp.modules.news.NewsListActivity;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentListActivity;
import com.example.santoriniapp.modules.payment.paymentsummary.PaymentSummaryActivity;
import com.example.santoriniapp.modules.socialclub.SocialClubActivity;

import java.util.ArrayList;

public class DashboardMenuActivityAdapter extends RecyclerView.Adapter<DashboardMenuActivityAdapter.ViewHolder>{

    private ArrayList<DashboardMenuItem> menu_items;
    Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DashboardMenuActivityAdapter(ArrayList<DashboardMenuItem> menu_items) {
        this.menu_items = menu_items;
    }

    public void setItemList(ArrayList<DashboardMenuItem> menu_items){
        this.menu_items = menu_items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;
        public TextView mTextView_Menu;
        public CardView mCardView;
        public ArrayList<DashboardMenuItem> menu_items;
        public Context mContext;

        public ViewHolder(View v, ArrayList<DashboardMenuItem> menu_items) {
            super(v);
            v.setOnClickListener(this);
            this.menu_items = menu_items;
            this.mContext = v.getContext();
            mTextView_Menu = v.findViewById(R.id.main_menu_item_text);
            mImageView = (ImageView) v.findViewById(R.id.main_menu_item_icon);
            mCardView = (CardView) v.findViewById(R.id.menu_item_card);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if(position == -1 )return;

            // Checking that the items list has the index (position) pressed. (Double check)
            if (this.menu_items != null && position < this.menu_items.size())
            {
                DashboardMenuItem menu_items = this.menu_items.get(position);
                Class menuClass            = menu_items.get_class_name();
                Bundle parameters          = menu_items.get_parms();

                Intent intent = new Intent(this.mContext, menu_items.get_class_name());
                intent.putExtra("parms", menu_items.get_parms());

                //TODO Set userID
                String userId = "1";

                //Specific to this class

                if (menu_items.get_class_name().equals(PaymentListActivity.class))
                    PaymentListActivity.launch(this.mContext, userId);

                else if (menu_items.get_class_name().equals(PaymentSummaryActivity.class))
                    PaymentSummaryActivity.launch(this.mContext, userId);

                else if(menu_items.get_class_name().equals(NewsListActivity.class))
                    Log.d("LOG_TAG","NewsListActivity");

                else if(menu_items.get_class_name().equals(SocialClubActivity.class))
                    Log.d("LOG_TAG","SocialClubActivity");
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DashboardMenuActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_menu, parent, false);
        ViewHolder vh = new ViewHolder(v, this.menu_items);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final DashboardMenuItem item = menu_items.get(position);
        holder.mImageView.setImageResource(item.get_icon_drawable());
        holder.mTextView_Menu.setText(item.get_item_text());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return menu_items!=null ? menu_items.size() : 0;
    }
}
