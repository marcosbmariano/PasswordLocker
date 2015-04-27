package com.example.mark.passwordmanager;

/**
 * Created by mark on 3/5/15.
 */
public final class RawData {

    private final byte [] mRawData;

    public RawData(char[] data){

        try{
            mRawData = PasswordUtils.charToBytes(data);
        } catch (NullPointerException e){
            throw new NullPointerException(
                    "The Password argument cannot be null! I must be a char [].");
        }
    }
    public RawData(byte[] data){
        mRawData = data.clone();
    }

    public RawData(String data){
        this( data.toCharArray() );
    }

    public char [] getDataCharArray(){
        return PasswordUtils.bytesToChar(mRawData);
    }

    public byte [] getDataByteArray(){
        return mRawData.clone();
    }

    public int length(){
        return mRawData.length;
    }

    public boolean isEmpty(){
        return mRawData.length == 0;
    }

    @Override
    public String toString() {
        return PasswordUtils.byteToString(mRawData);
    }


}
