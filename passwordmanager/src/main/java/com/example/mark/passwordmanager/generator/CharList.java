package com.example.mark.passwordmanager.generator;




import java.util.Random;

/**
 * Created by mark on 3/3/15.
 */
class CharList {

    //TODo implement ITERATOR!!!
    private final int CASE_TYPES = 4;
    final int [] mArray;
    //private final int mLength;
    private int mCurrentIndex;
    private int mSize;

    private Random mRandom;


    CharList(int length){
        //int length;
        mRandom = new Random();
        //length = length;
        mArray = fillTypeArray(length);
        mSize = length;
    }


    int getSize(){
        return  mSize;
    }

    int [] fillTypeArray(int size){
        int [] result = new int[CASE_TYPES];
        mCurrentIndex = mRandom.nextInt(CASE_TYPES);

        for( int i = 0; i < size; i++){
            result[mCurrentIndex]++;
            mCurrentIndex = getNextIndex();
        }
        return result;
    }

    int popType(){

        int lastIndex = mCurrentIndex;
        mCurrentIndex = getRandomIndex();
        int indexResult = -1;
        boolean loop = true;


        if ( isArrayEmpty()){
            indexResult = -1;
        }else {

            while ( loop ){

                if ( isTypeEmpty(mCurrentIndex) ) {
                    loop = true;
                    mCurrentIndex = getNextIndex();

                }else if (mCurrentIndex == lastIndex){

                    if ( isLastType() ){

                        indexResult = getLastType();
                        loop = false;

                    }else{
                        loop = true;
                        mCurrentIndex = getRandomIndex();  }

                }else {
                    loop = false;
                    indexResult = popType(mCurrentIndex);
                }
            }
        }

        return indexResult;
    }

    int popType(int index){

        if ( isTypeEmpty(index)){
            return -1;
        }else{
            mArray[index]--;
            mSize--;
            return index;
        }
    }

    boolean isLastType(){
        int result = 0;
        for ( int i = 0; i < mArray.length; i++){
            if( mArray[i] > 0){
                result++;
            }
        }
        return !(result > 1);
    }

    boolean isTypeEmpty(int type){
        return mArray[type] <= 0;
    }

    int getLastType(){
        int result = -1;
        for ( int i = 0; i < mArray.length; i++){
            if (mArray[i] > 0){
                result = popType(i);
            }
        }
        return result;
    }

    int getRandomIndex(){
        return mRandom.nextInt(CASE_TYPES);
    }

    boolean isArrayEmpty(){
        return mSize <= 0;
    }


    //get next loops infinitely
    int getNextIndex(){
        final int LAST_INDEX = 3;
        if ( mCurrentIndex == LAST_INDEX){
            return 0;
        }else{
            return ++mCurrentIndex;
        }
    }






}
