package net.travel.util;

import org.springframework.stereotype.Component;

@Component
public class NumberUtil {

    public int parseStrToInteger(String str){
        try {
            int number = Integer.parseInt(str);
            if(number <= 0){
                throw new NumberFormatException();
            }
            return number;
        }catch (NumberFormatException e){
            return -1;
        }
    }
    
    public int countRating(Integer sumRating,int count){
        if(sumRating == null){
            return 0;
        }
        int mod = sumRating % count;
        if(mod > 2){
            return sumRating/count+1;
        }
        return sumRating/count;
    }
}