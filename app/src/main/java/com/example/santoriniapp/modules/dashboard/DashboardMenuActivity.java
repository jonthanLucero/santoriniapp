package com.example.santoriniapp.modules.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.santoriniapp.R;
import com.example.santoriniapp.databinding.ActivityDashboardMenuBinding;
import com.example.santoriniapp.utils.CircleTransform;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.squareup.picasso.Picasso;

public class DashboardMenuActivity extends AppCompatActivity
{
    ActivityDashboardMenuBinding mBinding;
    public DashboardMenuItem item;
    private DashboardMenuActivityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DashboardMenuActivityViewModel mViewModel;
    private static final String FIRST_LOAD = "FIRST_LOAD";
    private boolean firstLoad = true;
    public static String USER_ID = "USER_ID";
    public static String PARAMS = "parms";
    private String mUserId;

    MenuItem mRefreshAction;


    // --------------------------------------------------------
    // Launcher helpers
    // --------------------------------------------------------
    public static void launch(Context context,String userId) {
        context.startActivity(launchIntent(context,userId));
    }

    public static Intent launchIntent(Context context,String userId)
    {
        Intent intent = new Intent(context, DashboardMenuActivity.class);
        Bundle parms = new Bundle();
        parms.putString(USER_ID, userId);
        intent.putExtra(PARAMS, parms);
        return intent;
    }


    // --------------------------------------------------------
    // ONCREATE
    // --------------------------------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Binding.
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard_menu);

        // Toolbar setup.
        setSupportActionBar(mBinding.toolbar);

        // Getting paramenters.
        getParameters();

        // Get variable
        if(savedInstanceState != null && savedInstanceState.containsKey(FIRST_LOAD))
            firstLoad = savedInstanceState.getBoolean(FIRST_LOAD,true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        // Set the RecyclerView.
        setupMenuRecyclerView();

        // Init ViewModel.
        mViewModel = ViewModelProviders.of(this).get(DashboardMenuActivityViewModel.class);

        // ViewModel - Get Basic Panel Data (MenuItems, User Photo, Name, etc)
        mViewModel.getCurrentPanelData(mUserId)
                .observe(this, new Observer<DashboardMenuActivityViewModelResponse>() {


                    @Override
                    public void onChanged(@Nullable DashboardMenuActivityViewModelResponse response) {

                        // Managing response.
                        if(response==null){
                            Toast.makeText(getBaseContext(), R.string.an_error_has_ocurred_please_try_again, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Set the Binding.
                        mBinding.setViewModel(response);

                        // Set the User and Account Logo in the UI.
                        refreshUserAndAccountLogo(response);

                        //Refresh Indicators
                        mAdapter.setItemList(response.itemList);

                    }
                });


        // When the user is in this panel the user is logged in.
        // TODO: Check this behaviour.
        //TODO InalambrikSession.setLoggedIn(this,true);

        mBinding.actionSyncInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewModel == null)return;
                mViewModel.syncInformation(mUserId,true);
            }
        });
    }

    private void getParameters() {
        Bundle parms = getIntent().getBundleExtra(PARAMS);
        mUserId = parms.getString(USER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mViewModel == null)return;
        if(!firstLoad)
            mViewModel.forceRefreshPanelData(mUserId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard_panel, menu);
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
        if(itemId == R.id.refresh_payments_from_ws){
            syncInformation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Aplicación")
                .setMessage("¿Está seguro de que desea salir de la aplicación?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UrbanizationSessionUtils.setLoggedIn(getApplicationContext(),false);
                        finish();
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ----------------------------------------------------------
    //  RECYCLER VIEW SETUP
    // ----------------------------------------------------------
    public void setupMenuRecyclerView(){
        //Define number of objects Depending on device orientation and size
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int cardsPerRow = 2;

        //Large Device
        if ((getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

            //Portrait Mode
            if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
                cardsPerRow = 1;
            } else {
                //Landscape
                cardsPerRow = 2;
            }

        }else{ //Small Device (Phone)

            //Portrait Mode
            if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
                cardsPerRow = 2;
            } else {
                //Landscape
                cardsPerRow = 3;
            }
        }

        // ----------------------
        // Set Recycler View
        // ----------------------
        mLayoutManager = new GridLayoutManager(this,cardsPerRow);
        mBinding.menuRecyclerView.setLayoutManager(mLayoutManager);
        mBinding.menuRecyclerView.setHasFixedSize(true);
        mAdapter = new DashboardMenuActivityAdapter(null);
        mBinding.menuRecyclerView.setAdapter(mAdapter);
    }

    private void syncInformation() {
        if (mViewModel==null)return;
        mViewModel.syncInformation(mUserId,true);
    }

    private void refreshUserAndAccountLogo(DashboardMenuActivityViewModelResponse response){

        if(mViewModel == null || mBinding == null) return;

        Log.i("LOG_TAG","URL IMAGEN USER. URL=" + response.userPictureUrl.trim());

        // Painting the UserPictureUrl.
        if(response.userPictureUrl.trim().isEmpty()) {
            Log.d("LOG_TAG","Setting USER empty icon...");
            mBinding.userPictureImageView.setImageResource(R.drawable.profile);
        }
        else{
            Log.d("LOG_TAG","Setting USER valid icon...");
            try {
                Picasso.with(DashboardMenuActivity.this)
                        .load(response.userPictureUrl.trim())
                        .transform(new CircleTransform())
                        .into(mBinding.userPictureImageView);
            } catch (Exception e) {
                Log.d("LOG_TAG","URL IMAGEN no encontrada. URL=" + response.userPictureUrl.trim());
                e.printStackTrace();
            }
        }

    }

}
