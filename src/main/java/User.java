import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class User {
    private static BufferedReader lineTypedFromKeyboard = new BufferedReader(new InputStreamReader(System.in));

    public static String getUserInput() throws IOException {
        return lineTypedFromKeyboard.readLine();
    }

    public static void closeUserInput() throws IOException {
        lineTypedFromKeyboard.close();
    }
}
