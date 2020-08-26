package ru.lookzbs;

import com.blade.Blade;
import ru.lookzbs.sql.SqlConnector;
import ru.lookzbs.util.VKUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class Core {

    public static String jarPath = "/home/lookzbs/";

    private static Scanner scanner;
    private CallbackHandler callbackHandler;
    public static VKUtil vkUtil = new VKUtil();
    public static SqlConnector sqlConnector = new SqlConnector();

    public static void main(String[] args) {
        new Core().start(args);
    }

    private void start(String[] args) {
        System.out.println(jarPath);

        callbackHandler = new CallbackHandler();
        scanner = new Scanner(System.in);

        Blade.of()
                .listen(80)
                .appName("LookZbs")
                .devMode(true)
                .post("/callback", callbackHandler)
                .start();

        while (true) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("stop")) {
                break;
            } else {
                //Test command

            }
        }

        Blade.of().stop();
        System.exit(0);
    }

    public static Properties loadConfig() {
        try(InputStream in = Files.newInputStream(Paths.get( "/home/lookzbs/config.properties"))){
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
