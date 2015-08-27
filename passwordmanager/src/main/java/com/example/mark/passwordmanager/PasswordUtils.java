package com.example.mark.passwordmanager;


import android.widget.EditText;

/**
 * Created by mark on 3/12/15.
 */
public class PasswordUtils {


    public static char[] getChars(EditText ed){
        int length = ed.length();
        char [] result = new char[length];
        ed.getText().getChars(0,length, result,0);

        return result;
    }

    public static char[] bytesToChar(byte [] input){

        char [] result = new char[input.length];
        for ( int i = 0; i < input.length; i++){
            result[i] = (char) input[i];
        }
        return result;
    }

    public static String byteToString(byte [] input){
        char [] buffer = bytesToChar(input);
        StringBuilder builder = new StringBuilder(input.length + 2);

        return  builder.append(buffer).toString();

    }


    public static byte [] stringToBytes(String input){
        return charToBytes(input.toCharArray());
    }

    public static byte [] charToBytes(char [] password){

        byte [] result = new byte[password.length];

        for( int i = 0; i < password.length; i++){
            result[i] = (byte)password[i];
        }
        return result;
    }



//    public static String asHex(byte buf[]) {
//        StringBuffer strbuf = new StringBuffer(buf.length * 2);
//        int i;
//
//        for (i = 0; i < buf.length; i++) {
//            if (((int) buf[i] & 0xff) < 0x10) {
//                strbuf.append("0");
//            }
//
//            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
//        }
//
//        return strbuf.toString();
//    }
//
//    public static byte[] fromHexString(String s) {
//        int len = s.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i+1), 16));
//        }
//        return data;
//    }


}
