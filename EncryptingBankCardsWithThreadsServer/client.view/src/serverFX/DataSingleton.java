package serverFX;

import business.SystemToEncryptBankCards;
import business.UsersCollection;

public class DataSingleton {
    private static DataSingleton instance = new DataSingleton();
      SystemToEncryptBankCards system = new SystemToEncryptBankCards();
      UsersCollection collection = system.getCollection();
      String name;

    // host server for this application


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SystemToEncryptBankCards getSystem() {
        return system;
    }

    public UsersCollection getCollection() {
        return collection;
    }

    private DataSingleton(){
    }

    public static DataSingleton getInstance() {
        return instance;
    }

}
