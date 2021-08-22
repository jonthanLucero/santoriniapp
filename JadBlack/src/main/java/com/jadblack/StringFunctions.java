package com.jadblack;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

public class StringFunctions {
    //REPLICATE
    public static String replicate(String text, int times){
        String result = "";
        for (int i = 1; i <= times; i++){
            result += text;
        }
        return result;
    }
    
    //LEFT
    public static String left(String text, int length){
        String result;
        if (length < text.length())
            result = text.substring(0, length);
        else
            result = text;
        return result;
    }
    
    //RIGHT
    public static String right(String text, int length){
        int startAt;
        String result;
        startAt = text.length() - length;
        if (startAt > 0 || length > 0)
            result = substr(text, startAt, length);
        else 
            result = text;
        return result;
    }
    
    //toMoneyString
    public static String toMoneyString(double value) {
    	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
    	return "$ " + decimalFormat.format(value);
    }

    //toMoneyString
    public static String toMoneyString(double value, int numberDecimals) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');

        String decimalsSpaces = "";
        if (numberDecimals > 0) {
            for(int i=0;i<numberDecimals;i++)
                decimalsSpaces += "#";
        }else{
            decimalsSpaces = "##"; // Default 2
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###."+decimalsSpaces, decimalFormatSymbols);
        return "$ " + decimalFormat.format(value);
    }

    public static String toQuantityString(double value){
        DecimalFormat format = new DecimalFormat("##########.######");
        return format.format(value);
    }

    public static String toPercentageString(double value){
        DecimalFormat format = new DecimalFormat("##########.####");
        return format.format(value) + "%";
    }
    public static String toPercentageStringOneDecimal(double value){
        DecimalFormat format = new DecimalFormat("##########.#");
        return format.format(value) + "%";
    }
    //TO STRING FUNCTIONS
    //BOOLEAN TO STRING
    public static String toString(boolean value){
        return new Boolean(value).toString();
    }
    //BYTE TO STRING
    public static String toString(byte value){
        return new Byte(value).toString();
    }
    //SHORT TO STRING
    public static String toString(short value){
        return new Short(value).toString();
    }
    //INTEGER TO STRING
    public static String toString(int value){
        return new Integer(value).toString();
    }
    //LONG TO STRING
    public static String toString(long value){
    	return Long.toString(value);
    }
    //FLOAT TO STRING
    public static String toString(float value){
        /*
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        DecimalFormat format = new DecimalFormat("##########.######");
        return format.format(value);
        */

        return Float.toString(value);
    }
    //DOUBLE TO STRING
    public static String toString(double value){
        /*
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        DecimalFormat format = new DecimalFormat("##########.######", symbols);
        return format.format(value);
        */

        return Double.toString(value);
        //return String.format("%.2f", value);
    }

    public static String toStringWithTwoDecimals(double value)
    {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##", decimalFormatSymbols);
        return decimalFormat.format(value);
    }


    
    /*
    * String toString(int, int)
    * Returns string of a given int number formatted with spaces to the right
    */
    public static String toString(int value, int len){
        String result = "0";
        try{
            String valueString = toString(value);
            result = right( replicate(" ", len) + valueString, len );
        }
        catch (Exception ex){
            result = "0";
        }
        return result;
    }
    
    /*
    * String toString(float, int)
    * Returns string of a given float number formatted with spaces to the right
    */
    public static String toString(float value, int len){
        String result = "";
        try{
            String valueString = toString(value);
            result = right(replicate(" ", len) + valueString, len);
        }catch (Exception e){
            result = "0";
        }
        return result;
    }
    
    /*
    * String toString(short, int)
    * Returns string of a given short number formatted with spaces to the right
    */
    public static String toString(short value, int len){
        String result = "";
        try{
            String valueString = toString(value);
            result = right(replicate(" ", len) + valueString, len);
        }catch (Exception e){
            result = "0";
        }
        return result;
    }
    
    /*
    * String toString(long, int)
    * Returns string of a given long number formatted with spaces to the right
    */
    public static String toString(long value, int len){
        String result = "";
        try{
            String valueString = toString(value);
            result = right(replicate(" ", len) + valueString, len);
        }catch (Exception e){
            result = "0";
        }
        return result;
    }
    
    
    /*
    * String toString(double, int)
    * Returns string of a given double number formatted with spaces to the right
    */
    public static String toString(double value, int len){
        String result = "";
        try{
            String valueString = toString(value);
            result = right(replicate(" ", len) + valueString, len);
        }catch (Exception e){
            result = "0";
        }
        return result;
    }
    
    //SUBSTR
    public static String substr(String value, int start, int length){
        String result = "";
        try{
            result = value.substring(start, start + length);
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            result = value;
        }
        return result;
    }
    
    //STRSEARCH
    public static int strsearch(String text, String textToSearch){
        int result = -1;
        try{
            result = text.indexOf(textToSearch);
        }catch(NullPointerException e){
        }
        return result;
    }
    
    //STRSEARCH
    public static int strsearch(String text, String textToSearch, int startAt){
        int result = -1;
        try{
            result = text.indexOf(textToSearch, startAt);
        }catch(NullPointerException e){
        }
        return result;
    }
    
    //STRREPLACE
    public static String strreplace(String content, String thisString, String withthisString){
    	String s = content.replace(thisString, withthisString);
    	return s;    	
    }
    
    //UPPER
    public static String upper(String text){
        return text.toUpperCase();
    }
     
    //LOWER
    public static String lower(String text){
        return text.toLowerCase();
    }
     
    //TRIM
    public static String trim(String text){
        return text.trim();
    }
     
    //LEN
    public static int len(String text){
        return text.length();
    }
    
    //EXPLODE
    public static Vector<String> explode(String splitStr, String delimiter){
        StringBuffer token = new StringBuffer();  
        Vector<String> tokens = new Vector<String>();  
        // split  
        char[] chars = splitStr.toCharArray();
        for (int i=0; i < chars.length; i++) {  
            if (delimiter.indexOf(chars[i]) != -1) {  
                if (token.length() > 0) {  
                    tokens.addElement(token.toString());  
                    token.setLength(0);  
                }  
            } else {  
                token.append(chars[i]);  
            }  
        }          
        if (token.length() > 0) {  
            tokens.addElement(token.toString());  
        }
        return tokens;
    }

    public static List<String> explodeToStringList(String splitStr, String delimiter){
       List<String> stringList = new ArrayList<>();
       if(splitStr==null || splitStr.trim().isEmpty()) return stringList;
        stringList = Arrays.asList(splitStr.split(Pattern.quote(delimiter)));
        List<String> stringWithTrimList = new ArrayList<>();
        for (String oneItem : stringList) {
            oneItem = oneItem.trim();
            stringWithTrimList.add(oneItem);

        }
       return stringWithTrimList;
    }

    //EXPLODE
    public static Vector<Integer> explodeToIntegerVector(String splitStr, String delimiter){
        Vector<Integer> tokens = new Vector<>();

        if (splitStr==null || splitStr.trim().isEmpty()) return tokens; // Empty

        String[] valuesArray = splitStr.split(delimiter);
        if(valuesArray.length > 0){
            for(String value : valuesArray){
                tokens.add(NumericFunctions.toInteger(value.trim()));
            }
        }
        return tokens;
    }

    //EXPLODE
    public static String implode(Vector<String> strList, String delimiter){

        // Init implodede string
        String implodedString = "";

        // If there are elements in the str list.
        if (strList.size() > 0) {
            if (strList.size() == 1)
                implodedString = strList.get(0).trim();
            else {
                int count = 0;
                for (String stringItem : strList) {
                    count++;
                    if (count >= strList.size())
                        implodedString += stringItem.trim();
                    else
                        implodedString += stringItem.trim() + delimiter;
                }
            }
        }

        return implodedString;
    }

    //EXPLODE
    public static String implodeAnIntegerArrayList(Vector<Integer> strList, String delimiter){

        // Init implodede string
        String implodedString = "";

        // Just in case
        if(strList==null) return implodedString;

        // If there are elements in the str list.
        if (strList.size() > 0) {
            if (strList.size() == 1)
                implodedString = strList.get(0).toString().trim();
            else {
                int count = 0;
                for (Integer integerItem : strList) {
                    count++;
                    if (count >= strList.size())
                        implodedString += integerItem.toString().trim();
                    else
                        implodedString += integerItem.toString().trim() + delimiter;
                }
            }
        }

        return implodedString;
    }

    //EXPLODE
    public static String implodeStringWithWrapperInString(Vector<String> strList, String delimiter, String wrapper){

        // Init implodede string
        String implodedString = "";

        // If there are elements in the str list.
        if (strList.size() > 0) {
            if (strList.size() == 1)
                implodedString = String.format("'%s'",strList.get(0).trim());
            else {
                int count = 0;
                for (String stringItem : strList) {
                    count++;
                    if (count >= strList.size())
                        implodedString += String.format("'%s'",stringItem.trim());
                    else
                        implodedString += String.format("'%s'%s",stringItem.trim(),delimiter);
                }
            }
        }

        return implodedString;
    }

    // String list contains
    public static boolean listContainsIgnoreCase(List<String> stringList, String stringToBeSearch){
        for (String s :stringList) {
            if (stringToBeSearch.equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    public static String[] stringArrayListToStringArray(ArrayList<String> arrayList) {
        return arrayList.toArray(new String[arrayList.size()]);
    }
}
