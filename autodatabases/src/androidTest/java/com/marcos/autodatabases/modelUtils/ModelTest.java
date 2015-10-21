package com.marcos.autodatabases.modelUtils;

import android.test.AndroidTestCase;

import com.marcos.autodatabases.models.Address;
import com.marcos.autodatabases.models.Costumer;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.models.User;

import java.util.List;
import java.util.Random;


/**
 * Created by marcos on 12/2/14.
 */
public class ModelTest extends AndroidTestCase {
    private User user;
    private Costumer costumer;
    private Address address;
    private Random rand;


    @Override
    protected void setUp() {


        rand = new Random();
        int n = rand.nextInt(10000);
        user = new User();
        user.setName("Maria" + n);
        user.setNumber(rand.nextInt(100));
        user.setmBoolean(rand.nextBoolean());
        user.setmByte((byte)rand.nextInt(10));
        user.setmChar((char)(65 + rand.nextInt(25)));
        user.setmDouble(rand.nextDouble());
        user.setmFloat(rand.nextFloat());
        user.setmLong(rand.nextLong());
        user.setmShort((short) 22);
        user.save();

        costumer = new Costumer();
        costumer.setName("John");
        costumer.save();

        address = new Address();
        address.setStreet("First");
        address.setNumber(333);
        address.save();

        user.addChildModel(costumer);
        costumer.addChildModel(address);

    }



    public void testHashCode(){
        User user1 = (User)Model.getModel(User.class, user.getId());
        assertEquals(user.hashCode(), user1.hashCode());
        user1.setId(123456789);
        assertNotSame( user.hashCode(), user1.hashCode());
    }

    public void testGetChildren(){
        List<Costumer> list = user.getCostumersChildren();
        assertTrue(list.contains(costumer));
    }

    public void testEquals(){
        User user1 = (User)Model.getModel(User.class, user.getId());
        User user2 = (User)Model.getModel(User.class, user.getId());
        User user3 = null;

        //noinspection ConstantConditions
        assertFalse(user.equals(user3));
        //noinspection EqualsWithItself
        assertTrue(user.equals(user));
        assertTrue(user.equals(user1));
        assertTrue(user1.equals(user));
        assertTrue(user.equals(user2));
        assertTrue(user2.equals(user1));
        user2.setId(123456789);
        assertFalse(user.equals(user2));
    }

    public void testFieldNotNull(){
        User user1 = new User();
        assertEquals(0, user1.getId());
        user1.save();
        assertEquals(-1, user1.getId());
    }

    public void testAllButMustFieldsNull(){
        User user1 = new User();
        user1.setName("Paul" + rand.nextInt(99999));
        user1.save();
        long n = user1.getId();
        assertTrue("Value is " + n, n > 0);
        assertNull(user1.getLastName());
        user1.delete();
    }

    public void testNotUnique(){
        User user1 = new User();
        user1.setName("John");
        user1.save();
        User user2 = new User();
        user2.setName("John");
        user2.save();
        assertEquals(-1,user2.getId());
        user1.delete();
    }

    public void testUserCreation() {
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertNotNull(userCheck);
    }

    public void testUserInteger(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals("int",user.getNumber(), userCheck.getNumber());
    }

    public void testUserString(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals(userCheck.getName(), user.getName());
    }

    public void testUserBoolean(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals(userCheck.ismBoolean(), user.ismBoolean());
    }

    public void testUserByte(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals( user.getmByte(),userCheck.getmByte());
    }

    public void testUserChar(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals(userCheck.getmChar(), user.getmChar());
    }

    public void testUserDouble(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals(userCheck.getmDouble(), user.getmDouble());
    }

    public void testUserFloat(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals(userCheck.getmFloat(), user.getmFloat());
    }

    public void testUserShort(){
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertEquals( user.getmShort(), userCheck.getmShort());
    }

    public void testUserUpdate() {
        int n = new Random().nextInt(10000);
        user.setName("Joanita" + n);
        //check before save, before update
        User userCheck = (User) Model.getModel(User.class, user.getId());
        assertFalse(user.getName().equals(userCheck.getName()));
        //save, if the object already exists in database, it will update
        user.save();
        User userCheck2 = (User)Model.getModel(User.class, user.getId());
        assertEquals(user.getName(), userCheck2.getName());
    }

    public void testCostumer() {
        Costumer costumerCheck = (Costumer) Model.
                getModel(Costumer.class, costumer.getId());
        assertEquals(costumer.getName(), costumerCheck.getName());

    }

    public void testAddress() {

        Address addressCheck = (Address) Model.
                getModel(Address.class, address.getId());
        assertEquals(address.getStreet(), addressCheck.getStreet());
        assertEquals(address.getNumber(), addressCheck.getNumber());
    }

    public void testHasRelationsWith() {
        assertFalse(user.hasRelationWith(address));
        assertTrue(user.hasRelationWith(costumer));
        assertTrue(costumer.hasRelationWith(address));
    }


    public void testDeleteWithoutRelation(){

        User us = new User();
        us.setName("kate");
        us.save();

        Costumer cos = new Costumer();
        cos.setName("mike");
        cos.save();

        Address add = new Address();
        add.setStreet("dddddd");
        add.save();

        us.delete();
        assertNull(Model.getModel(User.class, us.getId()));
        //after user being deleted costumer and address should not be null
        assertNotNull(Model.getModel(Costumer.class, cos.getId()));
        assertNotNull(Model.getModel(Address.class, add.getId()));
        cos.delete();
        assertNull(Model.getModel(Costumer.class, cos.getId()));
        add.delete();
        assertNull(Model.getModel(Address.class, add.getId()));
    }

    public void testDeleteWithRelation() {

        user.delete();
        assertNull(Model.getModel(Costumer.class, costumer.getId()));
        assertNull(Model.getModel(Address.class, address.getId()));

    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();



        User userCheck = (User) Model.getModel(User.class, user.getId());
        if (userCheck != null){
            userCheck.delete();
        }

        Costumer costumerCheck = (Costumer) Model.
                getModel(Costumer.class, costumer.getId());
        if (costumerCheck != null) {
            costumerCheck.delete();
        }


        Address addressCheck = (Address) Model.
                getModel(Address.class, address.getId());
        if (addressCheck != null){
            addressCheck.delete();
        }

    }
}
