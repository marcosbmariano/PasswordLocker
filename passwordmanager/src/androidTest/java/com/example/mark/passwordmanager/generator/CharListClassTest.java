package com.example.mark.passwordmanager.generator;

import android.test.AndroidTestCase;


/**
 * Created by mark on 3/4/15.
 */
public class CharListClassTest extends AndroidTestCase{
    private CharList mCharList;


    public void testObjectInstantiation1(){
        mCharList = new CharList(8);
        assertEquals("Size 8", 8, mCharList.getSize());

        mCharList = new CharList(10);
        assertEquals("Size 10", 10, mCharList.getSize());

        mCharList = new CharList(1);
        assertEquals("Size 1", 1, mCharList.getSize());

    }

    public void testIsLastType(){
        mCharList = new CharList(1);
        assertTrue( mCharList.isLastType());

        mCharList = new CharList(4);
        assertFalse(mCharList.isLastType());
    }

    public void testIsArrayEmpty(){
        mCharList = new CharList(0);
        assertTrue(mCharList.isArrayEmpty());
    }

    public void testPopType(){ //todo check this method
        int emptyType = 0;
        int type = 0;
        int index = -1;
        mCharList = new CharList(2);

        assertFalse("Not empty", mCharList.isArrayEmpty());


        for( int i = 0; i < 4; i++){
            index = mCharList.popType(i);

            if ( -1 == index){
                emptyType++;
            }else if( 0 <= index){
                type++;
            }
        }

        assertEquals("Empty", 2, emptyType);
        assertEquals("Has one", 2, type);
        assertTrue(mCharList.isArrayEmpty());
    }

    public void testGetLast(){
        mCharList = new CharList(1);
        int index = mCharList.getLastType();

        assertTrue(index >= 0);
        assertTrue(mCharList.isArrayEmpty());
    }

}
