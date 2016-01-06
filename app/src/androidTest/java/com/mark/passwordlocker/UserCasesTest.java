package com.mark.passwordlocker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.mark.passwordlocker.account.AccountRecord;
import com.mark.passwordlocker.activities.PLMainActivity;
import static com.mark.passwordlocker.helpers.ApplicationPasswordTest.PASSWORD;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;



import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.action.ViewActions.*;

/**
 * Created by mark on 9/21/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class UserCasesTest extends ActivityInstrumentationTestCase2<PLMainActivity>{


    public UserCasesTest(){
        super(PLMainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }


    public void test1UnlockApplication(){
        lockApplication();

        //first try a invalid password
        tryPassword("garbage");
        assertTrue(getAppState().isApplicationLocked());

        onView(withId(R.id.btn_enter_pass_show_hint))
                .check(matches(isDisplayed()))
                .perform(click());
        //show hint
        onView(withText("Your Hint"))
                .check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //now try a valid password
        tryPassword(PASSWORD);
        assertFalse(getAppState().isApplicationLocked());
    }

    public void test2AddNewAccount(){
        addNewAccount();
    }

    private void addNewAccount(){
        unlockPassword();
        //try all fields with correct values
        tryToAddNewAccount("newAccount", "123456", "123456");
        //check if form for new account is still visible
        onView(withId(R.id.et_new_account_account)).check(doesNotExist());
    }


    public void test3InvalidAccount(){
        unlockPassword();
        //try invalid account information, valid password and confirmation
        tryToAddNewAccount("", "shjshufuew", "shjshufuew");
        //check if form for new account is still visible
        onView(withId(R.id.et_new_account_account)).check(matches(isDisplayed()));
        //cancel new account
        onView(withId(R.id.btn_new_pass_cancel)).perform(click());
    }

    public void test4EmptyPassword(){
        unlockPassword();
        //try a valid account , empty valid password and confirmation
        tryToAddNewAccount("anyAccount", "", "");
        //check if form for new account is still visible
        onView(withId(R.id.et_new_account_account)).check(matches(isDisplayed()));
        //cancel new account
        onView(withId(R.id.btn_new_pass_cancel)).perform(click());
    }

    public void test5PasswordMismatch(){
        unlockPassword();
        //try a valid account and a mismatch password confirmation
        tryToAddNewAccount("anyAccount", "123456", "12345");
        //check if form for new account is still visible
        onView(withId(R.id.et_new_account_account)).check(matches(isDisplayed()));
        //cancel new account
        onView(withId(R.id.btn_new_pass_cancel)).perform(click());
    }

    public void test6DeleteAnAccount(){
        unlockPassword();
        int accounts = AccountRecord.getAllAccounts().size();
        Log.e("this is the size", "this is the size " + accounts);

        for ( int i = 0; i < accounts; i++){
            onView(withId(R.id.rvAccounts)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0,longClick()));
            onView(withText("OK")).perform(click());
        }

        int accountsAfterDeletion = AccountRecord.getAllAccounts().size();
        assertTrue( 0 == accountsAfterDeletion );
    }



    private void tryToAddNewAccount(String account, String password, String confirmationPass){
        //press add account button
        //onView(withId(R.id.action_add_account)).perform(click());

        onView(withId(R.id.et_new_account_account)).perform(typeText(account));
        //type password
        onView(withId(R.id.eT_app_pass_crea_pass)).perform(typeText(password));
        //pass confirm field
        onView(withId(R.id.eT_app_pass_crea_confirm)).perform(typeText(confirmationPass));
        //try to save
        onView(withId(R.id.btn_new_pass_acc_save)).perform(click());
    }

    private void unlockPassword(){
        if ( getAppState().isApplicationLocked()){
            tryPassword(PASSWORD);
        }
    }

    private void tryPassword(String password){
        onView(withId(R.id.et_app_enter_pass_pass)).perform(typeText(password));
        onView(withId(R.id.bt_app_enter_pass_enter)).perform(click());
    }

    private void lockApplication(){
        getAppState().lockApplication();
    }

    private ApplicationState getAppState(){
        return ApplicationState.getInstance();
    }


}
