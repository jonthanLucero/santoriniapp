package com.example.santoriniapp.modules.payment.paymentprintreceipt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.santoriniapp.R;
import com.example.santoriniapp.databinding.ActivityPaymentPrintReceiptBinding;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.NumericFunctions;

import java.util.Date;

public class PaymentPrintReceiptActivity extends AppCompatActivity
{

    public static final String LOG_TAG = PaymentPrintReceiptActivity.class.getSimpleName();
    public static final String PAYMENT_DATE = "PAYMENT_DATE";
    public static final String USER_ID = "USER_ID";

    ActivityPaymentPrintReceiptBinding mBinding;

    // First Load Flag.
    boolean firstLoad = false;
    Date mPaymentDate;
    String mUserId;

    PaymentPrintReceiptActivityViewModel mViewModel;

    public static Intent launchIntent(Context context, String userId, Date paymentDate) {
        Intent intent = new Intent(context, PaymentPrintReceiptActivity.class);
        Bundle parms = new Bundle();
        parms.putString(USER_ID,userId);
        parms.putLong(PAYMENT_DATE, NumericFunctions.toLong(paymentDate));
        intent.putExtra("parms", parms);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment_print_receipt);

        // Set First Load.
        firstLoad = (savedInstanceState == null);

        getParameters();

        // Setting Toolbar.
        setSupportActionBar(mBinding.toolbar);

        // --------------------------------------------------------------------------------------------------
        // Setting MAIN View Model observer.
        // --------------------------------------------------------------------------------------------------
        mViewModel = ViewModelProviders.of(this).get(PaymentPrintReceiptActivityViewModel.class);

        mViewModel.getCurrentPaymentPrintInformation(mUserId,mPaymentDate)
                .observe(this, new Observer<PaymentPrintReceiptViewModelReponse>() {
                    @Override
                    public void onChanged(@Nullable PaymentPrintReceiptViewModelReponse response) {

                        // Managing response.
                        if (response == null) {
                            Toast.makeText(PaymentPrintReceiptActivity.this, R.string.an_error_has_ocurred_please_try_again, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Set the Binding.
                        mBinding.setViewModel(response);
                    }
                });


        //Accept Payment
        mBinding.actionAcceptPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getParameters() {
        Bundle parms = getIntent().getBundleExtra("parms");
        setVariablesFromBundle(parms);
    }

    public synchronized void setVariablesFromBundle(Bundle data) {
        if (data == null) return;
        if (data.containsKey(PAYMENT_DATE))
            mPaymentDate = DateFunctions.toDate(data.getLong(PAYMENT_DATE));
        if(data.containsKey(USER_ID))
            mUserId = data.getString(USER_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
