package com.example.santoriniapp.modules.dashboard;

import java.util.ArrayList;

public class DashboardMenuActivityViewModelResponse {
    public String userName;
    public String taxpayerId;
    public boolean isLoading;
    public String errorMessage;
    public ArrayList<DashboardMenuItem> itemList;
    public String userPictureUrl;
    public String logoPictureUrl;

    public DashboardMenuActivityViewModelResponse()
    {
        this.userName = "";
        this.taxpayerId = "";
        this.isLoading = false;
        this.errorMessage = "";
        this.itemList = new ArrayList<>();
        this.userPictureUrl = "";
        this.logoPictureUrl = "";
    }

    public boolean showMenuList()
    {
        return !isLoading && itemList.size() > 0;
    }
}
