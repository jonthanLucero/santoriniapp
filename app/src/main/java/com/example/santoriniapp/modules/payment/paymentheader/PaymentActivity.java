package com.example.santoriniapp.modules.payment.paymentheader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.santoriniapp.R;
import com.example.santoriniapp.databinding.ActivityPaymentBinding;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentDateRowSpinnerItem;
import com.example.santoriniapp.modules.payment.paymentprintreceipt.PaymentPrintReceiptActivity;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.NumericFunctions;
import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationProcessEndPopupMessageDialogFragment;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class PaymentActivity extends AppCompatActivity implements UrbanizationProcessEndPopupMessageDialogFragment.UrbanizationProcessEndPopupListener {


    public static final String LOG_TAG = PaymentActivity.class.getSimpleName();
    public static final String PAYMENT_DATE = "PAYMENT_DATE";
    public static final String USER_ID = "USER_ID";
    public static final String MONTH_CODE = "MONTH_CODE";
    public static final String PAYMENT_MODE = "PAYMENT_MODE";

    ActivityPaymentBinding mBinding;
    MenuItem mSavePaymentAction;
    MenuItem mDeletePaymentAction;

    // First Load Flag.
    boolean firstLoad = false;
    Date mPaymentDate;
    String mUserId;
    String mMonthCode;
    String mMode;
    PaymentActivityViewModel mViewModel;
    Context context;

    UrbanizationProcessEndPopupMessageDialogFragment processDialogFragment;

    String processDialogFragmentTag = "processDialog";

    public static Intent launchIntent(Context context,String userId, Date paymentDate, String mode,String monthCode) {
        Intent intent = new Intent(context, PaymentActivity.class);
        Bundle parms = new Bundle();
        parms.putString(USER_ID,userId);
        parms.putLong(PAYMENT_DATE, NumericFunctions.toLong(paymentDate));
        parms.putString(MONTH_CODE,monthCode);
        parms.putString(PAYMENT_MODE,mode);
        intent.putExtra("parms", parms);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        // Set First Load.
        firstLoad = (savedInstanceState == null);

        context = this;

        // Setting Toolbar.
        setSupportActionBar(mBinding.toolbar);

        getParameters();

        if(savedInstanceState!=null)
            processDialogFragment = (UrbanizationProcessEndPopupMessageDialogFragment) getSupportFragmentManager().findFragmentByTag(processDialogFragmentTag);

        // Set a Maximum of 5 photos.
        mBinding.addPhotoGalleryControl.initialize(this);
        mBinding.addPhotoGalleryControl.setMaxPhotosNum(5);

        // --------------------------------------------------------------------------------------------------
        // Setting MAIN View Model observer.
        // --------------------------------------------------------------------------------------------------
        mViewModel = ViewModelProviders.of(this).get(PaymentActivityViewModel.class);

        mViewModel.getCurrentPaymentInformation(mUserId,mMode,mPaymentDate,mMonthCode)
                .observe(this, new Observer<PaymentActivityViewModelResponse>() {
                    @Override
                    public void onChanged(@Nullable PaymentActivityViewModelResponse response) {

                        // Managing response.
                        if (response == null)
                            showErrorDialog(String.valueOf(R.string.an_error_has_ocurred_please_try_again));

                        // Set the Binding.
                        mBinding.setViewModel(response);

                        // -----------------------------------------------------------------------
                        //  Payment SENT successfully.
                        // ----------------------------------------l-------------------------------
                        if(response.isPaymentSent)
                            showSuccessDialog(response.paymentNumber, response.serverMessage);

                        if(response.loadDataFromDB)
                        {
                            if(!mMode.equalsIgnoreCase(UrbanizationConstants.PAYMENT_MODE_INSERT))
                                mBinding.paymentAmountEditText.setText(StringFunctions.toString(response.paymentAmount));

                            mBinding.addPhotoGalleryControl.setDisplayMode(!response.isDisplayMode);
                            setupPaymentDatePickerSpinner(response.paymentListSpinnerList, response.paymentTimeSpinnerPosition);
                            setupPaymentTypePickerSpinner(response.paymentTypeListSpinnerList, response.paymentTypeSpinnerPosition);
                            mBinding.paymentCommentaryEdittext.setText(response.paymentCommentary);
                            mBinding.addPhotoGalleryControl.setInitialImageList(response.paymentPhotoList, response.isDisplayMode);
                        }

                        // Show any error message.
                        if (!response.errorMessage.trim().isEmpty()) {
                            showErrorDialog(response.errorMessage.trim());
                            mViewModel.resetErrorMessage();
                        }

                        checkMenuOptions(response);
                    }
                });

        mBinding.actionPrintReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PaymentPrintReceiptActivity.launchIntent(context,mUserId,mPaymentDate));
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
        if(data.containsKey(PAYMENT_MODE))
            mMode = data.getString(PAYMENT_MODE);
        if(data.containsKey(MONTH_CODE))
            mMonthCode = data.getString(MONTH_CODE);
    }

    // -----------------------------------------------------------------------------------------------
    //  Methods for  Dialog fragment.
    // -----------------------------------------------------------------------------------------------
    private void showSuccessDialog(int generatedNumber, String serverMessage){
        // If dialog is showing...
        if (processDialogFragment != null
                && processDialogFragment.getDialog() != null
                && processDialogFragment.getDialog().isShowing()
                && !processDialogFragment.isRemoving())
        {
            Log.i("DialogFragment","Success Dialog Fragment is been showing. Nothing to do...");
        }
        else
        {
            processDialogFragment = UrbanizationProcessEndPopupMessageDialogFragment.newInstanceWithInfoMessage(R.drawable.ic_material_ok_green,
                        "Pago #"+generatedNumber,serverMessage,
                        serverMessage);
            processDialogFragment.show(getSupportFragmentManager(), processDialogFragmentTag);
        }
    }

    private void showErrorDialog(String errorMessage)
    {
        // If dialog is showing...
        if (processDialogFragment != null
                && processDialogFragment.getDialog() != null
                && processDialogFragment.getDialog().isShowing()
                && !processDialogFragment.isRemoving())
        {
            Log.i("DialogFragment","Success Dialog Fragment is been showing. Nothing to do...");
        }
        else
        {
            processDialogFragment = UrbanizationProcessEndPopupMessageDialogFragment.newInstanceWithInfoMessage(R.drawable.ic_material_warning_yellow,
                    "Mensaje de Error",errorMessage,"");
            processDialogFragment.show(getSupportFragmentManager(), processDialogFragmentTag);
        }
    }


    private void setupPaymentDatePickerSpinner(final ArrayList<PaymentDateRowSpinnerItem> paymentDateSpinnerList, int selectedPosition)
    {
        List<String> paymentDateRowSpinnerList =   Observable.from(paymentDateSpinnerList)
                .map(new Func1<PaymentDateRowSpinnerItem, String>() {
                    @Override
                    public String call(PaymentDateRowSpinnerItem oneitem) {
                        return oneitem.description;
                    }
                })
                .toList().toBlocking().first();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.small_spinner_item,new ArrayList<>(paymentDateRowSpinnerList));
        mBinding.paymentmonth.setAdapter(adapter);
        mBinding.paymentmonth.setSelection(selectedPosition);
        RxAdapterView.itemSelections(mBinding.paymentmonth).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer integer) {
                if(mViewModel == null) return;
                int selectedPosition = integer;
                mViewModel.setSelectedPaymentMonthPositionSpinner(selectedPosition);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_payment_panel, menu);
        mSavePaymentAction             = menu.findItem(R.id.action_send_payment);
        mDeletePaymentAction           = menu.findItem(R.id.action_delete_payment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(itemId == R.id.action_send_payment)
        {
            sendPayment();
            return true;
        }

        if(itemId == R.id.action_delete_payment)
        {
            //TODO delete payment;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkMenuOptions(PaymentActivityViewModelResponse response)
    {
        if(response == null)return;
        if(mSavePaymentAction == null)return;
        if(mDeletePaymentAction == null)return;

        mSavePaymentAction.setVisible(true);
        mDeletePaymentAction.setVisible(false);
        if(response.isPaymentSent || response.isSendingPayment || response.isDisplayMode)
        {
            mSavePaymentAction.setVisible(false);
            mDeletePaymentAction.setVisible(false);
        }
    }

    private void setupPaymentTypePickerSpinner(final ArrayList<PaymentTypeItem> paymentTypeSpinnerList, int selectedPosition)
    {
        List<String> paymentTypeRowSpinnerList =   Observable.from(paymentTypeSpinnerList)
                .map(new Func1<PaymentTypeItem, String>() {
                    @Override
                    public String call(PaymentTypeItem oneitem) {
                        return oneitem.paymentTypeDescription;
                    }
                })
                .toList().toBlocking().first();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.small_spinner_item,new ArrayList<>(paymentTypeRowSpinnerList));
        mBinding.paymenttype.setAdapter(adapter);
        mBinding.paymenttype.setSelection(selectedPosition);
        RxAdapterView.itemSelections(mBinding.paymenttype).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer integer) {
                if(mViewModel == null) return;
                int selectedPosition = integer;
                mViewModel.setSelectedPaymentTypePositionSpinner(selectedPosition);
            }
        });
    }

    // -----------------------------------------------------------------------------------------------
    //  Method to update/get values in the form and then try to send to server (through ViewModel).
    // -----------------------------------------------------------------------------------------------
    private void sendPayment()
    {
        String paymentAmount = mBinding.paymentAmountEditText.getText().toString().trim();
        double amount = 0;
        try {
            amount = Double.parseDouble(paymentAmount);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            amount = 0;
        }

        //Validate that is registered value to pay
        if(amount == 0)
            UrbanizationUtils.showMessage(this,"Favor ingrese el monto a pagar");
        else
        {
            // Update Payment Fields.
            try
            {
                // Get values in the Form and set it in the ViewModel
                mViewModel.updatePaymentAmount(Double.parseDouble(paymentAmount));
                mViewModel.setSelectedPaymentMonthPositionSpinner(mBinding.paymentmonth.getSelectedItemPosition());
                mViewModel.setSelectedPaymentTypePositionSpinner(mBinding.paymenttype.getSelectedItemPosition());
                mViewModel.updatePaymentCommentary(mBinding.paymentCommentaryEdittext.getText().toString().trim());
                mViewModel.updatePaymentPhotos(mBinding.addPhotoGalleryControl.getFinalImageList());

                // Send to Server
                mViewModel.saveAndSendPaymentToServer(mPaymentDate);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this,R.string.an_error_has_ocurred_please_try_again,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mBinding.addPhotoGalleryControl.getAddPhotoDialogFragment()!=null)
            mBinding.addPhotoGalleryControl.getAddPhotoDialogFragment().onActivityResult( requestCode,  resultCode,  data);
    }

    @Override
    public void onExtraActionClick(int extraActionCode) {

    }

    @Override
    public void onAcceptOrCancelClick() {
        //finish();
        if(processDialogFragment == null)return;
        processDialogFragment.dismiss();
    }
}

