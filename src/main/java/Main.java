
// Uncomment this block to pass the first stage
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static List<File> paths = new ArrayList<File>();
    private static String currentDir = System.getProperty("user.dir");
    private static String homeDir = System.getenv("HOME");

    static {
        convertPathVariable();
    }

    private static void convertPathVariable() {
        String[] pathEnvArr = System.getenv("PATH").split(":");
        for (String path : pathEnvArr) {
            paths.add(new File(path));
        }
    }

    public static void main(String[] args) throws Exception {

        Set<String> shellBuiltin = Set.of("echo", "exit", "type", "pwd");

        System.out.print("$ ");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            String[] inputParams = input.split("\\s+");
            if (input.equals("exit 0"))
                break;
            else if (input.startsWith("echo")) {
                System.out.println(input.substring(5, input.length()));
            } else if (input.startsWith("type")) {
                String command = inputParams[1];
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

            } else {
                if (input.equals("pwd")) {
                    System.out.println(currentDir);
                } else if (input.startsWith("cd")) {
                    if (inputParams[0].equals("cd") && inputParams.length == 1) {
                        System.out.println(currentDir);
                    } else if (inputParams[1].equals("~")) {
                        setCurrentDir(homeDir);
                    } else {
                        String tempCurrentDir = currentDir;
                        String givenPath = inputParams[1];
                        if (givenPath.startsWith("..")) {
                            int backCdCount = givenPath.split("/").length;
                            String[] tempCurrentDirAsArr = currentDir.split("/");
                            for (int i = 0; i < tempCurrentDirAsArr.length - backCdCount; i++) {
                                tempCurrentDir = tempCurrentDirAsArr[i];
                            }
                            setCurrentDir("/" + tempCurrentDir);
                        } else if (givenPath.startsWith(".")) {
                            tempCurrentDir += givenPath.substring(1, givenPath.length());
                            checkDirAndSetCurrent(tempCurrentDir);
                        } else {
                            checkDirAndSetCurrent(givenPath);
                        }
                    }

                } else {
                    executeBinary(input);
                }
            }

            System.out.print("$ ");
        }
        scanner.close();

    }

    private static void setCurrentDir(String givenDir) {
        currentDir = givenDir;
    }

    private static void executeBinary(String input) {
        try {
            InputStream is = Runtime.getRuntime().exec(input.split("\\s+")).getInputStream();
            System.out.print(new String(is.readAllBytes()));
            is.close();
        } catch (Exception e) {
            System.out.println(input + ": command not found");
        }
    }

    private static void checkDirAndSetCurrent(String givenPath) {
        File cdFile = new File(givenPath);
        if (cdFile.exists()) {
            setCurrentDir(givenPath);
        } else
            System.out.print("cd: " + givenPath + ": No such file or directory\n");
    }

    private static String getBinary(String command) {
        File tmpFile = null;
        for (File dir : paths) {
            tmpFile = new File(dir + "/" + command);
            if (tmpFile.exists()) {
                return tmpFile.getAbsolutePath();
            }
        }
        return null;
    }
}
