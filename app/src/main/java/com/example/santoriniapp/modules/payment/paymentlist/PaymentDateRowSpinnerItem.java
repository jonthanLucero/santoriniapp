package com.example.santoriniapp.modules.payment.paymentlist;

import com.example.santoriniapp.utils.DateFunctions;

import java.util.Calendar;
import java.util.Date;

public class PaymentDateRowSpinnerItem
{
    public String timeCode;
    public String description;

    public PaymentDateRowSpinnerItem(String timeCode, String description) {
        this.timeCode = timeCode;
        this.description = description;
    }

    /*
    public String dateName(){
        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        switch(this.timeCode)
        {
            // TODAY
            case 1000 :
                dateString = DateFunctions.getDDMMMYYYYDateString(DateFunctions.today());

                break;
            // YESTERDAY
            case 2000 :
                Date yesterday = DateFunctions.addDays(DateFunctions.now(),-1);
                dateString = DateFunctions.getDDMMMYYYYDateString(yesterday);

                break;
            // Day before Yesterday
            case 3000 :

                Date dayBeforeYesterday = DateFunctions.addDays(DateFunctions.now(),-2);
                dateString = DateFunctions.getDDMMMYYYYDateString(dayBeforeYesterday);

                break;
            //Current Week
            case 4000 :
                calendar.setTime(DateFunctions.now());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-2;
                Date fromDate = DateFunctions.addDays(DateFunctions.now(),-dayOfWeek);
                Date toDate =  DateFunctions.addDays(fromDate,6);
                dateString = DateFunctions.getDDMMMYYYYDateString(fromDate) +" a "+DateFunctions.getDDMMMYYYYDateString(toDate);

                break;

            //Previous Week
            case 5000 :

                calendar.setTime(DateFunctions.now());
                int days = calendar.get(Calendar.DAY_OF_WEEK)-2+7;

                fromDate =   DateFunctions.addDays(DateFunctions.now(),-days);
                toDate =  DateFunctions.addDays(fromDate,6);
                dateString = DateFunctions.getDDMMMYYYYDateString(fromDate) +" a "+DateFunctions.getDDMMMYYYYDateString(toDate);
                break;

            //Current Month
            case 6000 :
                String currentMonth= DateFunctions.monthName(DateFunctions.now());
                String currentYear= DateFunctions.getYearString(DateFunctions.now());
                dateString = currentMonth + " "+currentYear;
                break;
            //Previous Month
            case 7000 :


                int currentYearInt= DateFunctions.getYear(DateFunctions.now());
                Date previusMonthDate = DateFunctions.addMonths(DateFunctions.now(),-1);
                dateString = DateFunctions.monthName(previusMonthDate) + " "+currentYearInt;
                break;
        }
        return  dateString;
    }
    public String dateTitle(){
        String dateTitleString = "";
        Calendar calendar = Calendar.getInstance();
        switch(this.timeCode)
        {
            // TODAY
            case 1000 :
                dateTitleString = "Hoy";

                break;
            // YESTERDAY
            case 2000 :

                dateTitleString = "Ayer";

                break;
            // Day before Yesterday
            case 3000 :


                dateTitleString = "Anteayer";

                break;
            //Current Week
            case 4000 :
                dateTitleString = "Semana Actual";

                break;

            //Previous Week
            case 5000 :


                dateTitleString = "Semana Anterior";
                break;

            //Current Month
            case 6000 :
                dateTitleString = "Mes Actual";
                break;
            //Previous Month
            case 7000 :
                dateTitleString = "Mes Anterior";
                break;
        }
        return  dateTitleString;
    }
    */
}
