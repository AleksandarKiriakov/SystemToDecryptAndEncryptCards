package serverFX.command;


import business.SystemToEncryptBankCards;
import cards.NormalCard;
import exceptions.NoCorrectCardNumberException;
import exceptions.TwelveEncyptionsDone;
import exceptions.UserDoNotContainThatCard;
import person.User;

import java.util.List;


public class CommandExecutor {
    private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
            "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";

    private static final String Login = "login";
    private static final String Decrypt = "decrypt";
    private static final String Encrypt = "encrypt";
    private static final String Register = "register";
    private static final String DecryptedNumber = "numberDec";
    private static final String Quit = "quit";

    private SystemToEncryptBankCards storage = new SystemToEncryptBankCards();
    private User user;
    boolean logged = false;

    public CommandExecutor() {
        storage = storage.readFromFile();
    }

    public void save() {
        storage.saveToFile();
    }

    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case Login -> login(cmd.arguments());
            case Register -> register(cmd.arguments());
            case Encrypt -> encrypt(cmd.arguments());
            case Decrypt -> decrypt(cmd.arguments());
            case DecryptedNumber -> decrypedNumber(cmd.arguments());
            case Quit -> quit(cmd.arguments());
            default -> "Unknown command";
        };
    }

    private String login(String[] args) {
        if (args.length > 1 && storage.loginName(args[0])  && storage.loginPass(args[0], args[1])) {
            user = storage.getPersonByName(args[0]);
            logged = true;
            if (user.getCanCrypt()) {
                return "true";
            } else {
                return "false";
            }
        } else {
            return "close";
        }
    }

    private String quit(String[] args) {
        save();
        return "close";
    }

    private String register(String[] args) {
        try {
            boolean canCrypt = false;
            if (args[3].equals("True")) {
                canCrypt = true;
            }
            storage.register(args[0], args[1], new NormalCard(args[2]), canCrypt);
            storage.saveToFile();
            user = storage.getPersonByName(args[0]);
            logged = true;
            return args[0];
        } catch (NoCorrectCardNumberException e) {
            return "close";
        }

    }

    private String encrypt(String[] args) {
        try {
            List<User> userList = storage.getCollection().getUserList();
            try {
                User name = null;
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getCard().getCardNumber().equals(args[1])) {
                        name = userList.get(i);
                        break;
                    }
                }
                if (name != null) {
                    if (storage.getCollection().checkUserCanUseCard(args[0], name.getCard()).equals(args[1])) {
                        if (!storage.getCollection().checkUserCanCrypt(args[0])) {

                            return makeString(storage.getPersonByName(args[0]).getCard().encryptedNumber());
                        }
                    }
                } else {
                    return "not your card";
                }
            } catch (UserDoNotContainThatCard e) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getCard().getCardNumber().equals(args[1])) {
                        userList.get(i).getCard().encrypt();
                        userList.get(i).setFirstCrypt(userList.get(i).isFirstCrypt() + 1);
                        break;
                    }
                }
                return "not your card";
            } catch (NoCorrectCardNumberException e) {
                throw new RuntimeException(e);
            }

            return makeString(storage.getPersonByName(args[0]).getCard().encrypt());
        } catch (TwelveEncyptionsDone e) {
            return makeString(storage.getPersonByName(args[0]).getCard().encryptedNumber());
        }

    }

    private String decrypt(String[] args) {
        String[] res = args[1].split("/");

        return storage.getPersonByName(args[0]).getCard().decrypt();
    }

    private String decrypedNumber(String[] args) {
        return makeString(storage.getPersonByName(args[0]).getCard().encryptedNumber());
    }

    private String makeString(int[] number) {
        String result = new String();
        for (int i = 0; i < number.length; i++) {
            result += number[i];
            result += "/";
        }
        return result;
    }
}
