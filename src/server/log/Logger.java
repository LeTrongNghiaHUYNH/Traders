package server.log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by warlof on 29/02/2016.
 */
public class Logger {
    /**
     * Contains all logger instance buy log type
     */
    private static Hashtable<LogType, Logger> _instances;

    /**
     * Contains the logger attached log file
     */
    private File _file;

    /**
     * Create a new logger object with its associated log file and a shutdown hook
     * @param type The log type
     */
    private Logger(LogType type)
    {
        // Prepare the file path
        String filepath = String.format("./logs/%s.log", type.toString());

        // Check if the logs folder exists, else try to create it
        if (!Files.exists(Paths.get("./logs"))) {
            try {
                Files.createDirectory(Paths.get("./logs"));
            } catch (IOException e) {
                String errorMsg = String.format("Does the user %s have the write permission in the folder %s",
                        System.getProperty("user.name"),
                        Paths.get("."));

                System.out.println("Unable to create the logs folder");
                System.out.println(errorMsg);
            }
        }

        // Store the file object inside the logger
        this._file = new File(filepath);

        // Add an hook on shutdown event in order to move the file
        // Work even if the application's crashing or anything else is happening
        // ie: Exception or ^C
        if (Files.exists(this._file.toPath())) {
            moveLogFile();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                moveLogFile();
            }
        });
    }

    private void moveLogFile()
    {
        if (_file.canWrite()) {
            try {
                // get current log attribute in order to take back its creation date
                BasicFileAttributes attr = Files.readAttributes(_file.toPath(), BasicFileAttributes.class);
                DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");

                // prepare the new filename
                String filepath = String.format("./logs/%s_%s.log",
                        _file.getName().substring(0, _file.getName().lastIndexOf(".")),
                        df.format(attr.creationTime().toMillis()));
                File newFile = new File(filepath);

                // rename the file
                Files.move(_file.toPath(), newFile.toPath());
            } catch (IOException e) {
                String errorMsg = String.format("Does the user %s has write permission on file %s",
                        System.getProperty("user.name"),
                        _file.getAbsolutePath());

                System.out.println("Unable to move the log file");
                System.out.println(errorMsg);
            }
        }
    }

    /**
     * Enable to get a specific logger instance
     * @param type The log type
     * @return The associated logger instance
     */
    public static Logger getInstance(LogType type)
    {
        // check if the instances are set
        if (Logger._instances == null)
            Logger._instances = new Hashtable<LogType, Logger>();
        // check if the asked logger instance exists
        if (!Logger._instances.containsKey(type))
            Logger._instances.put(type, new Logger(type));

        // return the asked instance
        return Logger._instances.get(type);
    }

    /**
     * Enable to write a message inside the specified log file
     * @param type The log type
     * @param message The message to write
     */
    public static void write(LogType type, String message)
    {
        try {
            // prepare a buffer in order to write the message inside the attached file
            Writer output = new BufferedWriter(new FileWriter(Logger.getInstance(type)._file, true));
            // prepare a datetime string
            DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");
            // prepare the message as it should be wrote in the log file
            String logMessage = String.format("[%s] %s",
                    df.format(System.currentTimeMillis()),
                    message);
            // write the message into the file
            output.append(logMessage + "\r\n");
            output.close();

            switch (type){
                case error:
                    System.out.println("[" + ColorShell.red(type.toString()) + "] " + message);
                    break;
                case notice:
                    System.out.println("[" + ColorShell.blue(type.toString()) + "] " + message);
                    break;
                case warning:
                    System.out.println("[" + ColorShell.yellow(type.toString()) + "] " + message);
                    break;
                default:
                    System.out.println("[" + type.toString() + "] " + message);
            }
        } catch (IOException e) {
            String errorMsg = String.format("Does the user %s has write permission on file %s",
                    System.getProperty("user.name"),
                    Logger.getInstance(type)._file.getAbsolutePath());

            System.out.println("Unable to write in the log file");
            System.out.println(errorMsg);
        }
    }
}
