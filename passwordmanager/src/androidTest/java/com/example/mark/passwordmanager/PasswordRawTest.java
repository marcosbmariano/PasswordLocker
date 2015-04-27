package com.example.mark.passwordmanager;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by mark on 3/6/15.
 */
public class PasswordRawTest extends AndroidTestCase {
    final char [] pass = {'0','1','2','3','4','5','6','7'};
    RawData password = new RawData(pass);


    public void testSecurity1(){
        password.getDataCharArray()[2] = 5;
        assertTrue(Arrays.equals(pass, password.getDataCharArray()));
    }

    public void testSecurity2(){
        pass [3] = 5;
        assertFalse(Arrays.equals(pass, password.getDataCharArray()));
    }

    public void testSecurity3(){
        char [] test = null;
        try{
            password = new RawData(test);
            fail();
        } catch (NullPointerException e){
            assertTrue(true);
        }
    }

    public void testInitialization1(){
        String data = "xchashegeds";
        RawData rawData = new RawData(data);
        Log.e("Inside PAsswordRawTest", rawData.toString());
        assertEquals(data, rawData.toString());
    }



}
