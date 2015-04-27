package com.example.mark.passwordmanager.generator;

import android.test.AndroidTestCase;
import android.util.Log;



/**
 * Created by mark on 3/3/15.
 */
public class CharListTest extends AndroidTestCase {

    CharList list = new CharList(12);
    int valueOfEmpties;
    int totalTypes;




    public void testTEst(){
        int popTimes = 17;
        totalTypes = 16;
        valueOfEmpties = popTimes - totalTypes;
        int manytime = 0;
        for(int i = 0; i < 1000; i++){
            list = new CharList(totalTypes);
            manytime++;
            int [] result = runCode(popTimes);
            goiaba(result);
        }
        Log.e("how many", "times "+ manytime);
    }

    public void goiaba(int [] result){

            assertEquals("0",4,result[0]);
            assertEquals("1",4,result[1]);
            assertEquals("2",4,result[2]);
            assertEquals("3",4,result[3]);
            assertEquals("-1",valueOfEmpties,result[4]);
    }


    public int [] runCode(int popTimes){
        int [] array = new int[5];
        for ( int i = 0; i < popTimes; i++){
            int type = list.popType();


            switch (type){
                case 0:
                    array[0]++;
                    break;

                case 1:
                    array[1]++;
                    break;

                case 2:
                    array[2]++;
                    break;

                case 3:
                    array[3]++;
                    break;
                default:
                    array[4]++;
            }

        }
        return array;
    }

}
