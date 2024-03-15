package top.rgb39.ecs.util;

import java.io.PrintStream;
import java.util.Set;
import java.util.HashSet;

public class Logger {
    private static final PrintStream out = System.out;
    private static final PrintStream err = System.out;
    private static int fontColor = 37;
    private static int backgroundColor = 40;
    private static Set<String> enabledLoggers = new HashSet<>();

    public final static Logger DEBUG = getLogger("debug");
    public final static Logger ECS = getLogger("ecs");

    private String loggerName;
    private Logger(String loggerName) {
        this.loggerName = loggerName;
    }

    public static Logger getLogger(String loggerName) {
        return new Logger(loggerName);
    }

    public interface BackgroundColors {
        public static final int BLACK = 40;
        public static final int RED = 41;
        public static final int GREEN = 42;
        public static final int YELLOW = 43;
        public static final int BLUE = 44;
        public static final int PURPLE = 45;
        public static final int CYAN = 46;
        public static final int WHITE = 47;
    }

    public interface FontColors {
        public static final int BLACK = 30;
        public static final int RED = 31;
        public static final int GREEN = 32;
        public static final int YELLOW = 33;
        public static final int BLUE = 34;
        public static final int PURPLE = 35;
        public static final int CYAN = 36;
        public static final int WHITE = 37;
    }

    public static void enableDebugger(String name) {
        enabledLoggers.add(name);
    }

    public static void disableDebugger(String name) {
        enabledLoggers.remove(name);
    }

    public static void enableDebugger(Logger logger) {
        enabledLoggers.add(logger.loggerName);
    }

    public static void disableDebugger(Logger logger) {
        enabledLoggers.remove(logger.loggerName);
    }

    public static void resetStyle() {
        fontColor = 37;
        backgroundColor = 40;
    }

    public static void setStyle(int color1, int color2) {
        fontColor = color1;
        backgroundColor = color2;
    }

    private static Boolean isEnabled(String name) {
        return enabledLoggers.contains(name);
    }

    public static void write(String name, String str, Object... args) {
        if (isEnabled(name)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void write(String name, int fontColor2, String str, Object... args) {
        if (isEnabled(name)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void write(String name, int fontColor2, int backgroundColor2, String str, Object... args) {
        if (isEnabled(name)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor2), args);
        }
    }

    public void write(String str, Object... args) {
        if (isEnabled(loggerName)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor), Integer.valueOf(backgroundColor), args);
        }
    }

    public void write(int fontColor2, String str, Object... args) {
        if (isEnabled(loggerName)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor), args);
        }
    }

    public void write(int fontColor2, int backgroundColor2, String str, Object... args) {
        if (isEnabled(loggerName)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor2), args);
        }
    }

    public static void info(String name, String str, Object... args) {
        if (isEnabled(name)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void info(String name, int fontColor2, String str, Object... args) {
        if (isEnabled(name)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void info(String name, int fontColor2, int backgroundColor2, String str, Object... args) {
        if (isEnabled(name)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor2), args);
        }
    }

    public static void err(String name, String str, Object... args) {
        if (isEnabled(name)) {
            err.printf(str + "\n", args);
        }
    }

    public void info(String str, Object... args) {
        if (isEnabled(loggerName)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor), Integer.valueOf(backgroundColor), args);
        }
    }

    public void info(int fontColor2, String str, Object... args) {
        if (isEnabled(loggerName)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor), args);
        }
    }

    public void info(int fontColor2, int backgroundColor2, String str, Object... args) {
        if (isEnabled(loggerName)) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor2), args);
        }
    }

    public void err(String str, Object... args) {
        if (isEnabled(loggerName)) {
            err.printf(str + "\n", args);
        }
    }

    public static final int light(int color) {
        return color + 60;
    }

    public static final String color(Integer... color) {
        String[] strs = new String[color.length];
        for (int i = 0; i < color.length; i++) {
            strs[i] = Integer.toString(color[i].intValue());
        }
        return "\u001b[" + Arrays.join(color, ";") + "m";
    }

    public static final String clear(int color) {
        return "\u001b[0m";
    }
}
