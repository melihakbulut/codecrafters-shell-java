
// Uncomment this block to pass the first stage
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage

        System.out.print("$ ");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit 0"))
                break;
            else if (input.startsWith("echo")) {
                System.out.println(input.substring(input.indexOf("echo ", input.length())));
            }
            System.out.println(input + ": command not found");
            System.out.print("$ ");
        }
        scanner.close();

    }
}
