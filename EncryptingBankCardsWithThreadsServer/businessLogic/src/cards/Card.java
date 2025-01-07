package cards;

import exceptions.TwelveEncyptionsDone;

import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.Writer;

public interface Card {
    //get set
    //cardNumber
    int[] encryptedNumber();
    String getCardNumber();
    int[] encrypt() throws TwelveEncyptionsDone;
    String decrypt();

    void save(XMLEncoder objectOutputStream);
     void saveTxtByEncrypt(Writer objectOutputStream) throws IOException;

    void saveTxtByDecrypt(Writer writer) throws IOException;


    //encrypted spisuk ot takiva
    //kodiraniq na tazi karta
    //int index na suotvetnata kodirovka
}
