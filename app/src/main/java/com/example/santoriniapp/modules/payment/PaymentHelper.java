package com.example.santoriniapp.modules.payment;

import com.example.santoriniapp.modules.payment.paymentlist.PaymentListViewModelResponse;

public class PaymentHelper
{
    public static PaymentListViewModelResponse getPaymentList(String customerCode, String textToSearch, boolean loadFromWS){


        PaymentListViewModelResponse response = new PaymentListViewModelResponse();
        /*
        response.showAddPaymentButton = true; // By default true. It can change depending on preference.
        response.textToSearch = textToSearch;

        // Get Context
        final Context context = UrbanizationGlobalUtils.getInstance();

        // ------------------------------------------------------------------------
        //  Download Payment from WS (if needed).
        // ------------------------------------------------------------------------
        if(loadFromWS)
            response.errorMessageFromLoadingFromWS = downloadPaymentListWS(customerCode);

        // ------------------------------------------------------------------------
        //  Load Payment from DB
        // ------------------------------------------------------------------------

        // Getting Customer Info.
        response.userName = CustomerUtils.getCustomerName(context,customerCode);

        // FIRST Load the PENDING TO SEND Payment (Ordered by Date - Most Recent to oldest.)
        PaymentSelection pendingPaymentsSelection = new PaymentSelection().customercodeEquals(customerCode).and().paymentstatusEquals(PaymentUtils.PAYMENT_PENDING_STATUS);
        if(!textToSearch.isEmpty())
            pendingPaymentsSelection = getPaymentSelectionWithTextFilterIncluded(pendingPaymentsSelection,textToSearch);
        pendingPaymentsSelection = pendingPaymentsSelection.orderBy(PaymentContract.Indexes.INDEX_STATUSDATEINDEXDESC);

        // Adding all resultant list to the main list.
        response.itemList = Observable.from(pendingPaymentsSelection.get(context).all())
                .map(new Func1<payment_class, WWPaymentNativePanelItem>() {
                    @Override
                    public WWPaymentNativePanelItem call(payment_class paymentRecord) {
                        // Add to viewModel List.
                        WWPaymentNativePanelItem newItem = new WWPaymentNativePanelItem();
                        newItem.paymentDate                                     = paymentRecord.paymentdate;
                        newItem.paymentDetailAppliedAndUnappliedAmountCharacter = "Aplicado: "+ StringFunctions.toMoneyString(paymentRecord.paymentappliedamount) + " / No Aplicado: " + StringFunctions.toMoneyString(paymentRecord.paymentunappliedamount);
                        newItem.paymentAmount                                   = paymentRecord.paymentamount;
                        newItem.paymentNotes                                    = paymentRecord.paymentmemo;
                        newItem.paymentNumber                                   = paymentRecord.paymentnumber;
                        newItem.paymentStatus                                   = paymentRecord.paymentstatus;
                        newItem.valuesReceivedCount                             = PaymentDetailUtils.getPaymentDetailsList(context,paymentRecord.paymentdate).size();

                        return newItem;
                    }
                }).toList()
                .toBlocking()
                .first();

        // ---------------------------------------------
        // SECOND Load the SENT Payments
        // (Ordered by Date - Most Recent to oldest.)
        PaymentSelection sentPaymentSelection = new PaymentSelection()
                .customercodeEquals(customerCode)
                .and().paymentstatusEquals(PaymentUtils.PAYMENT_SENT_STATUS);
        if(!textToSearch.isEmpty())
            sentPaymentSelection = getPaymentSelectionWithTextFilterIncluded(sentPaymentSelection,textToSearch);
        sentPaymentSelection = sentPaymentSelection.orderBy(PaymentContract.Indexes.INDEX_STATUSDATEINDEXDESC);// Add Alfabetical Order in the Search.

        // Adding all resultant list to the main list.
        response.itemList.addAll(Observable.from(sentPaymentSelection.get(context).all())
                .map(new Func1<payment_class, WWPaymentNativePanelItem>() {
                    @Override
                    public WWPaymentNativePanelItem call(payment_class paymentRecord) {
                        // Add to viewModel List.
                        WWPaymentNativePanelItem newItem = new WWPaymentNativePanelItem();
                        newItem.paymentDate                                     = paymentRecord.paymentdate;
                        newItem.paymentDetailAppliedAndUnappliedAmountCharacter = "Aplicado: "+ StringFunctions.toMoneyString(paymentRecord.paymentappliedamount) + " / No Aplicado: " + StringFunctions.toMoneyString(paymentRecord.paymentunappliedamount);
                        newItem.paymentAmount                                   = paymentRecord.paymentamount;
                        newItem.paymentNotes                                    = paymentRecord.paymentmemo;
                        newItem.paymentNumber                                   = paymentRecord.paymentnumber;
                        newItem.paymentStatus                                   = paymentRecord.paymentstatus;
                        newItem.valuesReceivedCount                             = PaymentDetailUtils.getPaymentDetailsList(context,paymentRecord.paymentdate).size();

                        return newItem;
                    }
                }).toList().toBlocking().first()
        );


        // ---------------------------------------------------
        // Special Preferences
        // ---------------------------------------------------

        // Blocking Actions if the user has not marked IN EVENT yet and the agenda is not deferred.
        boolean markInEventIsRequiredToAddPayment = InalambrikGetAccountPreferenceHelper.getAccountPreferenceValueBoolean(context, CustomerAccountPreferenceDomain.ALLOW_ACTIVITY_CREATION_ON_MARKING_IN_EVENT_OR_AGENDA_REPROGRAMMATION);
        if(markInEventIsRequiredToAddPayment)
            response.showAddPaymentButton = CustomerUtils.agendedCustomerIsEnabledToGenerateActivities(context,customerCode);

        // Returning response.
         */
        return response;
    }

    /*
    private static PaymentSelection getPaymentSelectionWithTextFilterIncluded(PaymentSelection paymentSelection, String textToSearch){



        paymentSelection = paymentSelection.and();
        paymentSelection = paymentSelection.openParenthesis();
        if(NumericFunctions.toInteger(textToSearch) > 0)
            paymentSelection = paymentSelection.paymentnumberLike(NumericFunctions.toInteger(Integer.parseInt(textToSearch))).or();
        paymentSelection = paymentSelection.paymentmemoLike(textToSearch).or();
        paymentSelection = paymentSelection.paymentpaymenttypecodeLike(textToSearch);
        paymentSelection = paymentSelection.closeParenthesis();
        return paymentSelection;
    }

    @SuppressWarnings("unchecked")
    private static String downloadPaymentListWS(String customerCode){

        // -----------------------------------
        // Set WS Request.
        // -----------------------------------
        PaymentListWSV2Request request = new PaymentListWSV2Request();
        request.setUserId(sfaplusGlobals.getInstance().getuserid());
        request.setUserLogin(sfaplusGlobals.getInstance().getuserlogin());
        request.setUserPassword(sfaplusGlobals.getInstance().getuserpassword());
        request.setCustomerCode(customerCode);

        try {
            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<PaymentListWSV2Response> call = apiInterface.paymentListWSV2(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            PaymentListWSV2Response wsResponse = call.execute().body();

            // If there is no response...
            if(wsResponse==null)
                return "No se pudo contactar Servidor. Por favor intentar nuevamente.\n\nNOTA: Visita ha sido guardado como pendiente de envío.";

            // If server returned an error...
            if(!wsResponse.getErrorMessage().trim().isEmpty())
                return wsResponse.getErrorMessage().trim();

            //  -----------------------------------------------------------------------------------
            //  Getting Payment Structures.
            //  -----------------------------------------------------------------------------------

            // Payment Header
            ArrayList<PaymentListWSV2Response_PaymentListSDT>
                    paymentListSDT = (ArrayList<PaymentListWSV2Response_PaymentListSDT>)
                    PaymentUtils.fromJson(wsResponse.getPayments(),new TypeToken<ArrayList<PaymentListWSV2Response_PaymentListSDT>>(){}.getType());

            // If there are NO payments headers, then return error message.
            if(paymentListSDT.size() == 0)
                return  "No se encontraron Pagos anteriores para el cliente.";


            // Parsing detail structures.
            ArrayList<PaymentListWSV2Response_PaymentDetailListSDT>
                    paymentDetailListSDT = (ArrayList<PaymentListWSV2Response_PaymentDetailListSDT>)
                    PaymentUtils.fromJson(wsResponse.getPaymentDetails(),new TypeToken<ArrayList<PaymentListWSV2Response_PaymentDetailListSDT>>(){}.getType());

            ArrayList<PaymentListWSV2Response_PaymentDistributionListSDT>
                    paymentDistributionListSDT = (ArrayList<PaymentListWSV2Response_PaymentDistributionListSDT>)
                    PaymentUtils.fromJson(wsResponse.getPaymentDistributions(),new TypeToken<ArrayList<PaymentListWSV2Response_PaymentDistributionListSDT>>(){}.getType());

            ArrayList<PaymentListWSV2Response_PaymentDistributionDetailListSDT>
                    paymentDistributionDetailListSDT = (ArrayList<PaymentListWSV2Response_PaymentDistributionDetailListSDT>)
                    PaymentUtils.fromJson(wsResponse.getPaymentDistributionDetails(),new TypeToken<ArrayList<PaymentListWSV2Response_PaymentDistributionDetailListSDT>>(){}.getType());

            WWPaymentNativeDownloadedStructures downloadedStructures =
                    new WWPaymentNativeDownloadedStructures(paymentListSDT,paymentDetailListSDT,paymentDistributionListSDT,paymentDistributionDetailListSDT);

            //  -----------------------------------------------------------------------------------
            //  Save the payment information downloaded in the Payments Structures
            //  -----------------------------------------------------------------------------------
            boolean paymentInformationSavedFromWS = PaymentUtils.savePaymentInformationDownloaded(downloadedStructures);
            if(!paymentInformationSavedFromWS)
                return  "Existió un error al guardar los pagos descargados. Por favor intente nuevamente.";

            return "";

        }catch (Exception e) {
            e.printStackTrace();
            return  "No se pudo contactar Servidor. Por favor intentar nuevamente.";
        }
    }

     */
}
