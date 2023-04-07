package com.example.smshandler;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Extensions
{
    public static String Sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static String GetSmsText(Sms sms){
        Timestamp ts=new Timestamp(sms.TimeStamp);
        Date date=new Date(ts.getTime());

        return "From: " + sms.From + "\r\n" +
                "Date: " +  date     + "\r\n" +
                "Text: " + sms.Text + "\r\n";
    }

    public static String[] GetAllSmsText(Sms[] sms){
        List<String> smsList =  Arrays.stream(sms)
                .map(Extensions::GetSmsText)
                .collect(Collectors.toList());

        String[] arr = new String[smsList.size()];
        smsList.toArray(arr);
        return arr;
    }
}
