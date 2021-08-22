
package com.jadblack;

import java.util.Date;

public class Comparer{
    public static boolean process(String a, String operator, String b){
        if ( operator.equals("=") )
            return a.equals(b);
        if ( operator.equals("<>") )
            return ! a.equals(b);
        if ( operator.equals("<") )
            return b.toUpperCase().indexOf(a.toUpperCase()) != -1 ? true : false ;
        return false;
    }

    public static boolean process(int a, String operator, int b){
        if ( operator.equals("=") )
            return a == (b);
        if ( operator.equals(">") )
            return a > b;
        if ( operator.equals("<") )
            return a < b;
        if ( operator.equals("<=") )
            return a <= b;
        if ( operator.equals(">=") )
            return a >= b;
        if ( operator.equals("<>") )
            return a != b;
        return false;
    }

    public static boolean process(float a, String operator, float b){
        if ( operator.equals("=") )
            return a == (b);
        if ( operator.equals(">") )
            return a > b;
        if ( operator.equals("<") )
            return a < b;
        if ( operator.equals("<=") )
            return a <= b;
        if ( operator.equals(">=") )
            return a >= b;
        if ( operator.equals("<>") )
            return a != b;
        return false;
    }
    
    public static boolean process(double a, String operator, double b){
        if ( operator.equals("=") )
            return a == (b);
        if ( operator.equals(">") )
            return a > b;
        if ( operator.equals("<") )
            return a < b;
        if ( operator.equals("<=") )
            return a <= b;
        if ( operator.equals(">=") )
            return a >= b;
        if ( operator.equals("<>") )
            return a != b;
        return false;
    }

    public static boolean process(short a, String operator, short b){
        if ( operator.equals("=") )
            return a == (b);
        if ( operator.equals(">") )
            return a > b;
        if ( operator.equals("<") )
            return a < b;
        if ( operator.equals("<=") )
            return a <= b;
        if ( operator.equals(">=") )
            return a >= b;
        if ( operator.equals("<>") )
            return a != b;
        return false;
    }

    public static boolean process(boolean a, String operator, boolean b){
        if ( operator.equals("=") )
            return a == b;
        if ( operator.equals("<>") )
            return a != b;
        return false;
    }

    //DATES
    public static boolean process(Date a, String operator, Date b){
        if ( operator.equals("<") )
            return DateFunctions.datediff(b, a) > 0;
        if ( operator.equals(">") )
            return DateFunctions.datediff(a, b) > 0;
        if ( operator.equals("<=") )
            return DateFunctions.datediff(b, a) >= 0;
        if ( operator.equals(">=") )
            return DateFunctions.datediff(a, b) >= 0;
        if ( operator.equals("=") )
            return DateFunctions.datediff(a, b) == 0;
        if ( operator.equals("<>") )
            return DateFunctions.datediff(a, b) != 0;
        return false;
    }
}
