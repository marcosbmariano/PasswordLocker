package com.example.mark.passwordmanager;

import android.test.AndroidTestCase;

import java.util.Arrays;

/**
 * Created by mark on 3/12/15.
 */
public class PasswordUtilsTest extends AndroidTestCase {
    char [] charArray = {'ￏ', '6', 'ﾫ',  '{', 'ￗ', 'd', 'ﾃ',' ', 'ﾡ', '￞', 's','ﾓ', 'z', '￞', 'g', 'a', 'ﾹ', '.',
     '￾', '�', 'c', 'ﾎ', '.', '￠', 'j', 'y',  '￞', '￀', 'H', '['};
    byte [] bytesArray = {-49, 54, -85, 0, 123, -41, 100, -125, 7, -95, -34, 115, -109, 122, -34, 103, 97, -71,
            46, -2, -3, 99, -114, 46, -32, 106, 121, 92, -34, -64, 72, 91 };

    public void testCharToBytesToChar(){
        byte [] result = PasswordUtils.charToBytes(charArray);
        char [] test = PasswordUtils.bytesToChar(result);

        assertTrue(Arrays.equals(test, charArray));
    }

    public void test2BytesToCharToBytes(){
        char [] test = PasswordUtils.bytesToChar(bytesArray);
        byte [] result = PasswordUtils.charToBytes(test);
        assertTrue(Arrays.equals(result, bytesArray));
    }

    public void testByteToStringToBytes(){
        String test = PasswordUtils.byteToString(bytesArray);
        byte [] bytes = PasswordUtils.stringToBytes(test);

        assertTrue(Arrays.equals(bytesArray, bytes));

    }

    public void testStringToBytesToString(){
        String expected = "XksjrnxJDHJHScnc123";
        byte [] test = PasswordUtils.stringToBytes("XksjrnxJDHJHScnc123");
        String result = PasswordUtils.byteToString(test);
        assertTrue( expected.equals(result));

    }

}
