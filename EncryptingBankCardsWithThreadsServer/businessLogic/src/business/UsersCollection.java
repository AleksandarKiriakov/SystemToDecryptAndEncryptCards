package business;

import cards.Card;
import cards.NormalCard;
import exceptions.NoCorrectCardNumberException;
import exceptions.TwelveEncyptionsDone;
import exceptions.UserDoNotContainThatCard;
import person.User;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersCollection {
    Map<String, User> users;
    List<User> userList;

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public UsersCollection() {
        userList = new ArrayList<>();
        users = new HashMap<>();
    }

    public void saveToFile(XMLEncoder encoder) {
        encoder.writeObject(this);
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).saveToFile(encoder);
        }
    }
    public UsersCollection(Map<String, User> users) {
        this.users = users;
        userList = new ArrayList<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            userList.add(entry.getValue());
        }
    }
    public User getUser(String name) {
        return users.get(name);
    }
    public Map<String, User> getUsers() {
        return users;
    }

    public boolean checkUserCanCrypt(String name) throws UserDoNotContainThatCard {
        User check = users.get(name);
        return check.CanCrypt();
    }

    public String checkUserCanUseCard(String name,Card otherCard) throws UserDoNotContainThatCard, TwelveEncyptionsDone, NoCorrectCardNumberException {
        User check = users.get(name);
        check.checkIfUsersCard( (NormalCard) otherCard);
        return ((NormalCard) check.getCard()).getCardNumber();
    }

    public void addUser(String name, String pass, Card cards, boolean canCrypt) {
        User newUser = new User(name, pass, cards, canCrypt);
        if (!users.containsKey(name)) {
            userList.add(newUser);
        }
        users.putIfAbsent(name, newUser);

    }

    public void addUser(User user) {
        if (!users.containsKey(user.getName())) {
            userList.add(user);
        }
        users.putIfAbsent(user.getName(), user);

    }

    public boolean checkForUserByName(String name) {
        return users.containsKey(name);
    }

    public boolean checkForUserPass(String name, String pass) {
        if (users.containsKey(name)) {
            User check = users.get(name);

            if (check.getPass().equals(pass)) {
                return true;
            }
        }
        return false;
    }

    public void printSortedByCryptNumbers() {
        userList.stream().sorted(Comparator.comparing(i -> i.getCard().encryptedNumber().toString()));
        Path file = new File("SortedByCryptNumbers.txt").toPath();
        try (Writer fileWriter = Files.newBufferedWriter(file)) {
            for (int i = 0; i < userList.size(); i++) {
                userList.get(i).getCard().saveTxtByEncrypt(fileWriter);
            }
            fileWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    public void printSortedByBankNumbers() {
        userList.stream().sorted(Comparator.comparing(i -> i.getCard().getCardNumber()));
        Path file = new File("SortedByCardNumbers.txt").toPath();
        try (Writer fileWriter = Files.newBufferedWriter(file)) {
            for (int i = 0; i < userList.size(); i++) {
                userList.get(i).getCard().saveTxtByDecrypt(fileWriter);
            }
            fileWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }


}
