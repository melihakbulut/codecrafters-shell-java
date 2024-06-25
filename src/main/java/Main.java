
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
        setPaths();
    }

    public static void main(String[] args) throws Exception {

        Set<String> shellBuiltin = Set.of("echo", "exit", "type", "pwd");

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
                    String[] arr = input.split("\\s+");
                    if (arr[0].equals("cd") && arr.length == 1) {
                        System.out.println(currentDir);
                    } else if (arr[1].equals("..")) {
                        String tempCurrentDir = currentDir;
                        String[] dirsInArr = tempCurrentDir.split("/");
                        for (int i = 0; i < dirsInArr.length - 1; i++) {
                            tempCurrentDir += dirsInArr[i];
                        }
                        currentDir = tempCurrentDir;

                    } else if (arr[1].equals("~")) {
                        currentDir = homeDir;
                    } else {
                        String tempCurrentDir = currentDir;
                        String givenPath = arr[1];
                        if (givenPath.startsWith("..")) {
                            int backCdCount = givenPath.split("/").length;
                            String[] tempCurrentDirAsArr = currentDir.split("/");
                            for (int i = 0; i < tempCurrentDirAsArr.length - backCdCount; i++) {
                                tempCurrentDir += tempCurrentDirAsArr[i];
                            }
                            currentDir = "/" + tempCurrentDir;
                        } else if (givenPath.startsWith(".")) {
                            tempCurrentDir += givenPath.substring(1, givenPath.length());
                            checkDirAndSetCurrent(tempCurrentDir);
                        } else {
                            checkDirAndSetCurrent(givenPath);
                        }
                    }

                } else {
                    String[] arr = input.split("\\s+");
                    String command = arr[0];
                    String binary = getBinary(command);
                    if (binary != null) {
                        InputStream is = Runtime.getRuntime().exec(arr).getInputStream();
                        System.out.print(new String(is.readAllBytes()));
                        is.close();
                    } else
                        System.out.println(input + ": command not found");
                }
            }

            System.out.print("$ ");
        }
        scanner.close();

    }

    private static void checkDirAndSetCurrent(String givenPath) {
        File cdFile = new File(givenPath);
        if (cdFile.exists()) {
            currentDir = givenPath;
        } else
            System.out.print("cd: " + givenPath + ": No such file or directory\n");
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
                return tmpFile.getAbsolutePath();
            }
        }
        return null;
    }
}
