package com.example.santoriniapp.modules.dashboard;

import android.os.Bundle;
import com.example.santoriniapp.R;
import com.example.santoriniapp.modules.news.NewsListActivity;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentListActivity;
import com.example.santoriniapp.modules.payment.paymentsummary.PaymentSummaryActivity;
import com.example.santoriniapp.modules.socialclub.SocialClubActivity;
import com.example.santoriniapp.repository.LoginRepository;
import java.util.ArrayList;

public class DashboardMenuActivityViewModelHelper
{
    private static final String LOG_TAG = "SalesPersonPanel2Helper";

    //Set User general data
    public static DashboardMenuActivityViewModelResponse loadPanelData(String userId){

        // Init Response.
        DashboardMenuActivityViewModelResponse response = new DashboardMenuActivityViewModelResponse();

        String userName,taxpayerId;

        //Get UserName
        LoginRepository loginRepository = new LoginRepository();
        if(loginRepository.getLoginName(userId)== null)
            userName = "N/A";
        else
            userName =  loginRepository.getLoginName(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginName(userId).trim();

        if(loginRepository.getLoginTaxPayerId(userId) == null)
            taxpayerId = "N/A";
        else
            taxpayerId =  loginRepository.getLoginTaxPayerId(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginTaxPayerId(userId).trim();

        response.userName = userName;
        response.taxpayerId   = taxpayerId;

        // Load the "options" list.
        response.itemList = getMenuItemList();


        // Get User Picture URL.
        //TODO VERIIFY USER FILE PHOTO
        response.userPictureUrl = "";
        if(!response.userPictureUrl.trim().isEmpty())
            response.userPictureUrl = response.userPictureUrl.contains("pedidos.inalambrik.com.ec") ? "https://".concat(response.userPictureUrl.trim()) : "http://".concat(response.userPictureUrl.trim());

        return response;
    }

    public static ArrayList<DashboardMenuItem> getMenuItemList(){

        // ------------------------------------------------------------------------------
        // Defining the Options in the Menu.
        // ------------------------------------------------------------------------------
        ArrayList<DashboardMenuItem> items = new ArrayList<>();
        DashboardMenuItem item;
        int iconDrawable;
        Class className;
        String itemText;
        Bundle parms;

        //OPTION: SINGLE PAYMENT
        iconDrawable = R.drawable.payment_individual;
        className    = PaymentListActivity.class;
        itemText     = "Pagos";
        item         = new DashboardMenuItem(iconDrawable,className,itemText);
        items.add(item);

        // OPTION: ACCOUNT STATUS
        iconDrawable        = R.drawable.account_status;
        className           = PaymentSummaryActivity.class;
        itemText            = "Estado de Cuenta";
        item                = new DashboardMenuItem(iconDrawable, className, itemText);
        items.add(item);

        // OPTION: NEWS
        iconDrawable        = R.drawable.newspaper;
        className           = NewsListActivity.class;
        itemText            = "Noticias";
        item                = new DashboardMenuItem(iconDrawable, className, itemText);
        items.add(item);

        // OPTION: SOCIAL CLUB
        iconDrawable        = R.drawable.social_club;
        className           = SocialClubActivity.class;
        itemText            = "Club Social";
        item                = new DashboardMenuItem(iconDrawable, className, itemText);
        items.add(item);

        return items;
    }
}
