package net.travel.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberUtilCountPercentTest {

    private NumberUtil numberUtil;

    @Before
    public void before(){
        this.numberUtil = new NumberUtil();
    }

    @Test
    public void testIntegerNumber(){
        int result = numberUtil.countPercent(20, 25);
        assertEquals(80,result);
    }

    @Test
    public void testFloatNumber(){
        int result = numberUtil.countPercent(20, 27);
        assertEquals(74,result);
    }

    @Test
    public void testFloatNumber2(){
        int result = numberUtil.countPercent(20, 29);
        assertEquals(69,result);
    }
}