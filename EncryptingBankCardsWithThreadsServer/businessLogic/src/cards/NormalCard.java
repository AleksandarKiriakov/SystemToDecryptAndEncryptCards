package cards;

import exceptions.NoCorrectCardNumberException;
import exceptions.TwelveEncyptionsDone;

import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class NormalCard implements Card {
    private String cardNumber;
    private int[] encryptedNumber;
    private int module;
    private int cypher;
    private ArrayList<String> allCryptNumbers;
    private int countCrypt;
    int encryptFirst = 0;

    public NormalCard() {
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }

    public NormalCard(String cardNumber) throws NoCorrectCardNumberException {
        if (!isValidCardNumber(cardNumber)) {
            throw new NoCorrectCardNumberException("Incorrect card number ");
        }
        allCryptNumbers = new ArrayList<>();
        cypher = 5;
        this.cardNumber = cardNumber;
        this.encryptedNumber = null;
        module = 10;
        countCrypt = 0;
    }

    public int getCypher() {
        return cypher;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int[] getEncryptedNumber() {
        return encryptedNumber;
    }

    public void setEncryptedNumber(int[] encryptedNumber) {
        this.encryptedNumber = encryptedNumber;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public ArrayList<String> getAllCryptNumbers() {
        return allCryptNumbers;
    }

    public void setAllCryptNumbers(ArrayList<String> allCryptNumbers) {
        this.allCryptNumbers = allCryptNumbers;
    }

    public int getCountCrypt() {
        return countCrypt;
    }

    public void setCountCrypt(int countCrypt) {
        this.countCrypt = countCrypt;
    }

    public void setCypher(int cypher) {
        this.cypher = cypher;
    }

    private long digitCount(long number) {
        long count = 0;

        while (number > 0) {
            count++;
            number /= 10;
        }

        return count;
    }

    private boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    public void save(XMLEncoder objectOutputStream) {
        objectOutputStream.writeObject(this);

    }

    public void saveTxtByEncrypt(Writer writer) throws IOException {

        allCryptNumbers.stream().sorted(Comparator.comparing(i ->{
            String save = "";
            for (int j = 0; j < encryptedNumber.length; j++) {
                save += encryptedNumber[j];
            }
            return save;
        }));
        writeToFile(writer);
    }

    public void saveTxtByDecrypt(Writer writer) throws IOException {

        allCryptNumbers.stream().sorted(Comparator.comparing(i -> cardNumber));
        writeToFile(writer);
    }

    private void writeToFile(Writer writer) throws IOException {
        writer.write("Card number: ");
        writer.write(cardNumber);
        writer.write("\nEncrypted numbers: ");

        for (int i = 0; i < allCryptNumbers.size(); i++) {
            writer.write(allCryptNumbers.get(i));
            writer.write(" ");
        }
        writer.write("\n------------\n");
    }

    private boolean isValidCardNumber(String cardNumber) {
        char ch = cardNumber.charAt(0);
        int rev = ch - '0';
        if ((rev == 3 || rev == 4 || rev == 5 || rev == 6)) {
            return false;
        }
        if (checkLuhn(cardNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public int[] encryptedNumber() {

        return encryptedNumber;
    }


    @Override
    public int[] encrypt() throws TwelveEncyptionsDone {
        if (countCrypt == 13) {
            throw new TwelveEncyptionsDone("You cant crypt more times this card");
        }

        encryptCardNumber(cardNumber);
        cypher++;
        countCrypt++;
        String save = "";
        return encryptedNumber;
    }

    @Override
    public String decrypt() {
        decryptCardNumber(encryptedNumber);
        return cardNumber;
    }

    private void encryptCardNumber(String number) {
        encryptFirst++;
        if (encryptFirst > 1) {
            module = 16;
        }
        if (encryptedNumber != null) {
            String save = "";
            for (int i = 0; i < encryptedNumber.length; i++) {
                save += encryptedNumber[i] + " ";
            }
            allCryptNumbers.add(save);
        }
        encryptedNumber = null;
        encryptedNumber = Arrays.stream(number.split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < number.length(); i++) {
            encryptedNumber[i] = ((encryptedNumber[i] + cypher) % module);
        }
       /* encryptedNumber = "";
        for (int i = 0; i < number.length(); i++) {
            int digit = number.charAt(i) - '0';
            encryptedNumber += ((((digit) + cypher) % module) % 10);
        }
*/
    }

    private void decryptCardNumber(int[] number) {
        for (int i = 0; i < number.length; i++) {
            number[i] = (number[i] - cypher + 1 + module) % module;
        }

    }

    @Override
    public boolean equals(Object o) {
        NormalCard normalCard= (NormalCard) o;
        return Objects.equals(this.cardNumber, normalCard.getCardNumber());
    }
}
