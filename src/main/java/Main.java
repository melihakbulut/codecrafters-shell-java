
// Uncomment this block to pass the first stage
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static List<File> paths = new ArrayList<File>();

    public static void main(String[] args) throws Exception {
        //        System.out.println(System.getenv("PATH"));
        //        example env
        //[your-program] /tmp/mango/pineapple/banana:/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

        // Uncomment this block to pass the first stage
        Set<String> shellBuiltin = Set.of("echo", "exit", "type");
        setPaths();
        System.out.println("$ ");

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
                } else {
                    String binary = getBinary(command);
                    if (binary != null) {
                        System.out.println(command + " is " + binary);
                    } else {
                        System.out.println(command + ": not found");
                    }
                }

            } else
                System.out.println(input + ": command not found");
            System.out.print("$ ");
        }
        scanner.close();

    }

    private static void setPaths() {
        String[] pathEnvArr = System.getenv("PATH").split(":");
        for (String path : pathEnvArr) {
            paths.add(new File(path));
        }
    }

    private static String getBinary(String command) {
        File tmpFile = null;
        for (File dir : paths) {
            tmpFile = new File(dir + "/" + command);
            if (tmpFile.exists()) {
                return tmpFile.getName();
            }
        }
        return null;
    }
}
