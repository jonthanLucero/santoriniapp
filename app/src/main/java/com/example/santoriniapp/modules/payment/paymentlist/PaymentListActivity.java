package com.example.santoriniapp.modules.payment.paymentlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.santoriniapp.R;
import com.example.santoriniapp.dao.daoutils.DAOPaymentViewModel;
import com.example.santoriniapp.databinding.ActivityPaymentListBinding;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.modules.payment.paymentheader.PaymentActivity;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.PaymentUtils;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import java.util.ArrayList;
import rx.Subscriber;


public class PaymentListActivity extends AppCompatActivity implements PaymentItemListener
{
    public static String USER_ID = "USER_ID";
    public static String PARAMS = "parms";
    private String mUserId ;
    // First Load Flag.
    boolean firstLoad = false;
    // Menu Items
    MenuItem mRefreshAction;

    // Set the View Model
    PaymentListViewModel mViewModel;
    ActivityPaymentListBinding mBinding;
    PaymentListAdapter mAdapter;

    private DAOPaymentViewModel mPaymentViewModel;
    private PaymentListActivity mPaymentListActivity;

    public static Intent launchIntent(Context context, String userId)
    {
        Intent intent = new Intent(context, PaymentListActivity.class);
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
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment_list);

        // Set First Load.
        firstLoad = (savedInstanceState == null); // This is to avoid loading the Order info in the OnCreate and in the onResume the first time the screen loads.

        // Getting paramenters.
        getParameters();

        mPaymentListActivity = this;


        // Setting Toolbar.
        setSupportActionBar(mBinding.toolbar);



        mViewModel = ViewModelProviders.of(this).get(PaymentListViewModel.class);

        // Recycler view setup.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.paymentsRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new PaymentListAdapter(this, null,this);
        mBinding.paymentsRecyclerView.setAdapter(mAdapter);

        // Set "Create Payment Action".

        mBinding.addPaymentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mPaymentViewModel = ViewModelProviders.of(mPaymentListActivity).get(DAOPaymentViewModel.class);
                Payment payment = PaymentUtils.getNewPayment(mUserId);
                mPaymentViewModel.insertPayment(payment);
                startActivity(PaymentActivity.launchIntent(PaymentListActivity.this,mUserId,
                        DateFunctions.toDate(payment.getPaymentdate()), UrbanizationConstants.PAYMENT_MODE_INSERT,""));
            }
        });

        String requestCodeThisMonth= UrbanizationUtils.getTodayMonthRequestCode();
        mViewModel.getCurrentPaymentListInformation(mUserId,requestCodeThisMonth)
                .observe(this, new Observer<PaymentListViewModelResponse>() {
                    @Override
                    public void onChanged(@Nullable PaymentListViewModelResponse response) {

                        // Managing response.
                        if(response==null){
                            Toast.makeText(PaymentListActivity.this, R.string.an_error_has_ocurred_please_try_again, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Set the Binding.
                        mBinding.setViewModel(response);

                        setupPaymentDatePickerSpinner(response.paymentListSpinnerList,response.currentTimeSpinnerPosition);
                        mBinding.toolbar.setSubtitle(response.userName);

                        // Set the Item List.
                        mAdapter.setItemList(response.paymentList);

                        // Show any error message.
                        if(!response.errorMessage.trim().isEmpty()) {
                            Toast.makeText(PaymentListActivity.this, response.errorMessage.trim(), Toast.LENGTH_SHORT).show();
                            mViewModel.resetErrorMessage();
                        }
                        else if(!response.errorMessage.trim().isEmpty()) {
                            Toast.makeText(PaymentListActivity.this, response.errorMessage.trim(), Toast.LENGTH_SHORT).show();
                            mViewModel.resetErrorMessage();
                        }


                    }
                });

    }


    private void setupPaymentDatePickerSpinner(ArrayList<PaymentDateRowSpinnerItem> paymentDateSpinnerList, int selectedPosition) {
        int resourceID = R.layout.payment_date_time_spinner_item;
        int resourceDropDownID = R.layout.payment_date_time_spinner_item_drop_down;
        mBinding.salesPersonaTimeSpinner.setAdapter(new PaymentDateRowArrayAdapterDropDown(this,paymentDateSpinnerList,resourceID,resourceDropDownID));
        mBinding.salesPersonaTimeSpinner.setSelection(selectedPosition);
        System.out.println("selectedPosition "+selectedPosition);
        RxAdapterView.itemSelections(mBinding.salesPersonaTimeSpinner).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("LOG_TAG","Error loaded=>"+e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                if(mViewModel == null) return;
                int selectedPosition = integer;
                mViewModel.setSelectedPositionSpinner(selectedPosition);
                if (mViewModel.isReload())
                    reload(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_payment_list_panel, menu);
        mRefreshAction             = menu.findItem(R.id.refresh_payments_from_ws);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(itemId == R.id.refresh_payments_from_ws)
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
        mViewModel.reloadPanels(mUserId,mViewModel.getSelectedPositionItemRequestCode(),downloadFromWS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mViewModel == null)return;
        if(!firstLoad)
            mViewModel.reloadPanels(mUserId,mViewModel.getSelectedPositionItemRequestCode(),false);
        firstLoad = false;
    }

    @Override
    public void onPaymentItemClick(PaymentItem payment) {
        startActivity(PaymentActivity.launchIntent(this,mUserId,payment.paymentDate,
                UrbanizationConstants.PAYMENT_MODE_UPDATE,""));
    }
}
