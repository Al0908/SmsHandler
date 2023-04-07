package com.example.smshandler;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SmsService {
    private final PhoneSmsService phoneSmsService;
    private final SmsFileService smsFileService;
    private final int countOfSms = 3; //count of latest sms

    public SmsService(PhoneSmsService phoneSmsService, SmsFileService smsFileService){
        this.phoneSmsService = phoneSmsService;
        this.smsFileService = smsFileService;
    }

    public Sms[] GetUnseenSms(int count){
        Sms[] phoneSms = this.phoneSmsService.ReadLatestSms(count);
        Sms[] seenSms = this.smsFileService.GetAllSmsFromFile();
        List<String> seenSmsIds = Arrays.stream(seenSms).map(x -> x.Id).collect(Collectors.toList());

        List<Sms> newSms = new ArrayList<>();

        for(int i =0; i < phoneSms.length; i++){
            if(!Seen(seenSmsIds, phoneSms[i])){
                newSms.add(phoneSms[i]);
            }
        }

        String content = new Gson().toJson(phoneSms);
        smsFileService.WriteToFile(content);


        Sms[] result = new Sms[newSms.size()];
        return newSms.toArray(result);
    }

    public Sms[] GetUnseenSms(){
       return GetUnseenSms(countOfSms);
    }

    public Sms[] GetLatestSms(int count){
        return this.phoneSmsService.ReadLatestSms(count);
    }

    private boolean Seen(List<String> seenSmsIds, Sms phoneSms){
        for(int i = 0; i< seenSmsIds.size(); i++){
            if(phoneSms.Id.equals(seenSmsIds.get(i))) {
              return true;
            }
        }
        return false;
    }
}
