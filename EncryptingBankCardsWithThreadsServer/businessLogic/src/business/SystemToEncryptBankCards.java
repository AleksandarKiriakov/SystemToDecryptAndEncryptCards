package business;

import cards.Card;
import cards.NormalCard;
import exceptions.NoCorrectCardNumberException;
import person.User;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class SystemToEncryptBankCards implements EncryptingBankCards {
    private UsersCollection collection;

    public UsersCollection getCollection() {
        return collection;
    }

    public SystemToEncryptBankCards() {

        this.collection = new UsersCollection();
        collection = readFromFile().getCollection();
    }
    public SystemToEncryptBankCards(UsersCollection collection) {

        this.collection = collection;
    }


    public void saveToFile() {

        try {
            File file = new File("Users.xml");
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.setExceptionListener(e -> System.out.println("Exception! :" + e.toString()));
            collection.saveToFile(encoder);
            encoder.flush();
            encoder.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public boolean loginName(String name){
        return collection.checkForUserByName(name);
    }
    public boolean loginPass(String name,String pass){
       return collection.checkForUserPass(name,pass);
    }

    public SystemToEncryptBankCards readFromFile() {
       try {
           File file = new File("Users.xml");
           if(!file.exists()){
               file.createNewFile();
           }
           FileInputStream fis = new FileInputStream(file);
           XMLDecoder decoder = new XMLDecoder(fis);
           collection = (UsersCollection) decoder.readObject();
           decoder.close();
           fis.close();

       }catch (Exception e){

       }

        return new SystemToEncryptBankCards(collection);
    }
    public User getPersonByName(String name) {
        return collection.users.get(name);
    }
    public void register(String name, String pass, String cardNumber, boolean canCrypt) throws NoCorrectCardNumberException {
        NormalCard cards = new NormalCard(cardNumber);
        collection.addUser(name, pass, cards, canCrypt);
    }
    public void register(String name, String pass, Card cards, boolean canCrypt) throws NoCorrectCardNumberException {
        collection.addUser(name, pass, cards, canCrypt);
    }




}
