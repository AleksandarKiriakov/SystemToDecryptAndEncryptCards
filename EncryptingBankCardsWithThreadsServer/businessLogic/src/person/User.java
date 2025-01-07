package person;

import cards.Card;
import cards.NormalCard;
import exceptions.TwelveEncyptionsDone;
import exceptions.UserDoNotContainThatCard;

import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class User implements Person, java.io.Serializable {
    private String name;
    private String pass;
    private Card cards;
    private boolean canCrypt;
    private int firstCrypt = 0;

    public User() {
    }

    public User(User user) {
        this.name = user.name;
        this.pass = user.pass;
        this.cards = user.cards;
        this.canCrypt = user.canCrypt;
    }

    public User(String name, String pass, Card cards, boolean canCrypt) {
        this.name = name;
        this.pass = pass;
        this.cards = cards;
        this.canCrypt = canCrypt;
    }

    public void checkIfUsersCard(NormalCard card) throws UserDoNotContainThatCard, TwelveEncyptionsDone {
        if (!this.cards.equals(card)) {
            throw new UserDoNotContainThatCard("User not own that card");
        }
    }

    @Override
    public void addCard(Card card) {
        this.cards = card;
    }


    @Override
    public Card getCard() {
        return cards;
    }

    public String getName() {
        return name;
    }

    public void saveToFile(XMLEncoder objectOutputStream) {
        objectOutputStream.writeObject(this);
        saveCards(objectOutputStream);

    }

    public Object saveToTxt(ObjectOutputStream objectOutputStream) throws IOException {
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isFirstCrypt() {
        return firstCrypt;
    }

    public void setFirstCrypt(int firstCrypt) {
        this.firstCrypt = firstCrypt;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Card getCards() {
        return cards;
    }

    public void setCards(Card cards) {
        this.cards = cards;
    }

    public boolean isCanCrypt() {
        return canCrypt;
    }

    public void setCanCrypt(boolean canCrypt) {
        this.canCrypt = canCrypt;
    }

    public void saveCards(XMLEncoder objectOutputStream) {
        cards.save(objectOutputStream);
    }

    public boolean getCanCrypt() {
        return canCrypt;
    }

    public boolean CanCrypt() {
        if (firstCrypt == 0) {
            firstCrypt = 1;
            return true;
        }
        return canCrypt;
    }

    public String getPass() {
        return pass;
    }

}
