package com.example.santoriniapp.modules.aliquotelist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.santoriniapp.R;
import com.example.santoriniapp.dao.daoutils.DAOPaymentViewModel;
import com.example.santoriniapp.databinding.ActivityPaymentSummaryBinding;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.modules.payment.paymentheader.PaymentActivity;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.PaymentUtils;
import com.example.santoriniapp.utils.UrbanizationConstants;

public class PaymentSummaryActivity extends AppCompatActivity implements PaymentSummaryItemListener{

    public static String USER_ID = "USER_ID";
    public static String PARAMS = "parms";
    private String mUserId ;
    // First Load Flag.
    boolean firstLoad = false;
    // Menu Items
    MenuItem mRefreshAction;

    // Set the View Model
    PaymentSummaryActivityViewModel mViewModel;
    ActivityPaymentSummaryBinding mBinding;
    PaymentSummaryAdapter mAdapter;

    PaymentSummaryActivity mPaymentSummary;
    private DAOPaymentViewModel mPaymentViewModel;

    public static Intent launchIntent(Context context, String userId)
    {
        Intent intent = new Intent(context, PaymentSummaryActivity.class);
        Bundle parms = new Bundle();
        parms.putString(USER_ID, userId);
        intent.putExtra(PARAMS, parms);
        return intent;
    }

    public static void launch(Context context, String userId) {
        Intent intent = launchIntent(context, userId);
        context.startActivity(intent);
    }

    private void getParameters() {
        Bundle parms = getIntent().getBundleExtra(PARAMS);
        mUserId = parms.getString(USER_ID);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Binding.
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment_summary);

        // Set First Load.
        firstLoad = (savedInstanceState == null); // This is to avoid loading the Order info in the OnCreate and in the onResume the first time the screen loads.

        // Getting paramenters.
        getParameters();

        // Setting Toolbar.
        setSupportActionBar(mBinding.toolbar);

        mPaymentSummary = this;

        mViewModel = ViewModelProviders.of(this).get(PaymentSummaryActivityViewModel.class);

        // Recycler view setup.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.paymentsRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new PaymentSummaryAdapter(this, null,this);
        mBinding.paymentsRecyclerView.setAdapter(mAdapter);

        mViewModel.getCurrentPaymentSummaryListInformation(mUserId)
                .observe(this, new Observer<PaymentSummaryActivityViewModelResponse>() {
                    @Override
                    public void onChanged(@Nullable PaymentSummaryActivityViewModelResponse response) {

                        // Managing response.
                        if(response==null){
                            Toast.makeText(PaymentSummaryActivity.this, R.string.an_error_has_ocurred_please_try_again, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Set the Binding.
                        mBinding.setViewModel(response);
                        mBinding.toolbar.setSubtitle(response.userName);

                        // Set the Item List.
                        mAdapter.setItemList(response.paymentList);
                    }
                });

        mBinding.actionDownloadPaymentSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload(true);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aliquote_list_panel, menu);
        mRefreshAction             = menu.findItem(R.id.refresh_aliquote_from_ws);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(itemId == R.id.refresh_aliquote_from_ws)
        {
            reload(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reload(boolean downloadFromWS)
    {
        if(mViewModel == null) return;
        if(mViewModel.isDownloadingFromWS()) {
            Toast.makeText(this,"Por favor espere. Descarga en proceso...",Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel.reloadPanels(mUserId,downloadFromWS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mViewModel == null)return;
        if(!firstLoad)
           reload(false);
        firstLoad = false;
    }

    @Override
    public void onPaymentPayClick(PaymentSummaryItem item) {
        //Only Open panel if the month is pending to pay
        if(item.paymentMonthStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_MONTH_PENDING))
        {
            mPaymentViewModel = ViewModelProviders.of(mPaymentSummary).get(DAOPaymentViewModel.class);
            Payment payment = PaymentUtils.getNewPayment(mUserId);
            mPaymentViewModel.insertPayment(payment);
            startActivity(PaymentActivity.launchIntent(this,mUserId,DateFunctions.toDate(payment.getPaymentdate()),
                    UrbanizationConstants.PAYMENT_MODE_INSERT_FROM_PAYMENT_LIST,item.paymentYearToPay,item.paymentMonthCodeToPay));
        }
    }
}
