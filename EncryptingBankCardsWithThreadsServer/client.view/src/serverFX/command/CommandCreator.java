package serverFX.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandCreator {
    private static List<String> getCommandArguments(String input) {

        String[] res = input.split(" ");

        return new ArrayList<>(Arrays.asList(res));
    }

    public static Command newCommand(String clientInput) {
        List<String> tokens = CommandCreator.getCommandArguments(clientInput);
        String[] args = tokens.subList(1, tokens.size()).toArray(new String[0]);

        return new Command(tokens.get(0), args);
    }
}
