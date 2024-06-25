
// Uncomment this block to pass the first stage
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {
        //        System.out.println(System.getenv("PATH"));
        //        example env
        //[your-program] /tmp/mango/pineapple/banana:/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

        // Uncomment this block to pass the first stage
        Set<String> shellBuiltin = Set.of("echo", "exit", "type");
        List<String> paths = Arrays.asList(System.getenv("PATH").split(":"));
        System.out.print("$ ");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit 0"))
                break;
            else if (input.startsWith("echo")) {
                System.out.println(input.substring(5, input.length()));
            } else if (input.startsWith("type")) {
                String[] arr = input.split("\\s+");
                String command = arr[1];
                if (shellBuiltin.contains(command)) {
                    System.out.println(command + " is a shell builtin");
                } else if (paths.contains(command)) {
                    System.out.println(command + " is /bin/" + command);
                } else {
                    System.out.println(command + ": not found");
                }

            } else
                System.out.println(input + ": command not found");
            System.out.print("$ ");
        }
        scanner.close();

    }
}
