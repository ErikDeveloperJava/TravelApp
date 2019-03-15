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

    public int countPercent(int percentNumber,int fromNumber){
        double percent = (double) percentNumber * 100 / fromNumber;
        return parseDoubleToInt(percent);
    }

    private int parseDoubleToInt(double number){
        String[] strArray = String.valueOf(number).split("[.]");
        String nextStrNumber = String.valueOf(strArray[1].charAt(0));
        int percent = Integer.parseInt(strArray[0]);
        int nextNumber = Integer.parseInt(nextStrNumber);
        if(nextNumber > 5){
            return percent+1;
        }
        return percent;
    }
}