package business;

import cards.Card;
import cards.NormalCard;
import exceptions.NoCorrectCardNumberException;
import exceptions.TwelveEncyptionsDone;
import person.User;

public class MainTest {
    public static void main(String[] args) throws NoCorrectCardNumberException, TwelveEncyptionsDone {
        UsersCollection test = new UsersCollection();
        SystemToEncryptBankCards t = new SystemToEncryptBankCards();
        Card card = new NormalCard("1563960122001999" );
        Card card1 = new NormalCard("1563960122001999" );
        User testu = new User("test", "test", card, true);
        t.register("test", "test", card, true);
        t.register("test1", "test1", card1, true);

        t.getCollection().printSortedByCryptNumbers();
        System.out.println();
        System.out.println(card.getCardNumber());
        System.out.println(card.decrypt());
        //System.out.println(t.getPersonByName("test1").getCard().decrypt());
        test.addUser(testu);
        t.getCollection().printSortedByCryptNumbers();
        t.getCollection().printSortedByBankNumbers();
    }
}
