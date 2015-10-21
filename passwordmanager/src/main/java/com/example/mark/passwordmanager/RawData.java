package com.example.mark.passwordmanager;

/**
 * Created by mark on 3/5/15.
 */
public final class RawData {

    private final byte [] mRawData;

    public RawData(char[] data){
        this(PasswordUtils.charToBytes(data));
    }
    private RawData(byte[] data){
        try{
            mRawData = data.clone();
        } catch (NullPointerException e){
            throw new NullPointerException(
                    "The Password argument cannot be null! ");
        }
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
