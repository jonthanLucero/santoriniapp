package com.example.santoriniapp.utils;

public class ErrorHandler {
    private static ErrorHandler _instance;
    private ErrorHandlerItem errorHandlerItem;

    public ErrorHandler(){
        this._initGlobals();
    }

    private void _initGlobals(){
        errorHandlerItem = new ErrorHandlerItem();
    }

    public static synchronized ErrorHandler getInstance(){
        if ( _instance == null ) _instance = new ErrorHandler();
        return _instance;
    }

    public void clean(){
        errorHandlerItem = new ErrorHandlerItem();
    }

    public void setErrorMessage(String errorMessage){
        ErrorHandlerItem aux = new ErrorHandlerItem();
        aux.setErrorMessage(errorMessage);
        errorHandlerItem = aux;
    }

    public String getErrorMessage(){
        return errorHandlerItem.getErrorMessage();
    }

    public boolean hasErrorOccurred(){
        String errorMessage = getErrorMessage();
        return ! errorMessage.equals("") ? true : false ;
    }

    private class ErrorHandlerItem{
        private String errorMessage;

        public ErrorHandlerItem(){
            errorMessage = "";
        }

        public void setErrorMessage(String value){
            errorMessage = value;
        }

        public String getErrorMessage(){
            return errorMessage;
        }
    };
}

