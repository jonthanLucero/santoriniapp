package com.example.santoriniapp.modules.dashboard;

import java.util.ArrayList;

public class DashboardMenuActivityViewModelResponse {
    public String userName;
    public String taxpayerId;
    public boolean isLoading;
    public boolean isDownloading;
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
        return !isLoading && !isDownloading && errorMessage.trim().isEmpty() && itemList.size() > 0;
    }

    public String showUserNameTitle()
    {
        if(!userName.trim().isEmpty())
            return "Hola "+userName;
        return "Hola Invitado";
    }

    public boolean showDashboard()
    {
        return !isDownloading && !isLoading;
    }

    public boolean showErrorMessage()
    {
        return !this.errorMessage.trim().isEmpty();
    }
}
