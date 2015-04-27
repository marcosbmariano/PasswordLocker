package com.marcos.autodatabases;

import android.test.AndroidTestCase;

import com.marcos.autodatabases.models.BoxedTypes;
import com.marcos.autodatabases.models.Model;

/**
 * Created by marcos on 12/4/14.
 */
public class XBoxedTypeTest extends AndroidTestCase {
    BoxedTypes b;
    @Override
    protected void setUp() throws Exception {
        b = new BoxedTypes();
        b.mBoolean = true;
        b.mByte = 1;
        b.mChar = 'a';
        b.mDouble = 2.2;
        b.mFloat = 5.3f;
        b.mInt = 3;
        b.mLong = 3333L;
        b.mShort = 2;
        b.save();

    }

    public void testBoolean(){
        BoxedTypes boxCheck = getModel();
        assertTrue(boxCheck.mBoolean);
    }

    public void testInteger(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mInt, boxCheck.mInt);

    }

    public void testDouble(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mDouble, boxCheck.mDouble, 0.001);

    }

    public void testByte(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mByte, boxCheck.mByte);
    }

    public void testChar(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mChar, boxCheck.mChar);
    }

    public void testFloat(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mFloat, boxCheck.mFloat);
    }

    public void testLong(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mLong, boxCheck.mLong);
    }

    public void testShort(){
        BoxedTypes boxCheck = getModel();
        assertEquals(b.mShort, boxCheck.mShort);
    }

    private BoxedTypes getModel(){
        return (BoxedTypes)Model.
                getModel(BoxedTypes.class, b.getId());
    }





    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        b.delete();
    }






}
