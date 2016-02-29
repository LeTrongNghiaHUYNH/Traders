package server.log;

/**
 * Created by warlof on 29/02/2016.
 */
public class ColorShell {

    public static String red(String message)
    {
        return String.format("\033[31m%s\033[39m", message);
    }

    public static String blue(String message)
    {
        return String.format("\033[34m%s\033[39m", message);
    }

    public static String yellow(String message)
    {
        return String.format("\033[93m%s\033[39m", message);
    }
}
