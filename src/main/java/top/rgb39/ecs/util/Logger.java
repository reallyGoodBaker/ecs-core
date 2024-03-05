package top.rgb39.ecs.util;

import java.io.PrintStream;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/Logger.class */
public class Logger {
    private static final PrintStream out = System.out;
    private static final PrintStream err = System.out;
    private static boolean debug = false;
    private static int fontColor = 37;
    private static int backgroundColor = 40;

    /* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/Logger$BackgroundColors.class */
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

    /* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/Logger$FontColors.class */
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

    public static void setDebug(boolean enable) {
        debug = enable;
    }

    public static void resetStyle() {
        fontColor = 37;
        backgroundColor = 40;
    }

    public static void setStyle(int color1, int color2) {
        fontColor = color1;
        backgroundColor = color2;
    }

    public static void write(String str, Object... args) {
        if (debug) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void write(int fontColor2, String str, Object... args) {
        if (debug) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void write(int fontColor2, int backgroundColor2, String str, Object... args) {
        if (debug) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor2), args);
        }
    }

    public static void info(String str, Object... args) {
        if (debug) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void info(int fontColor2, String str, Object... args) {
        if (debug) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor), args);
        }
    }

    public static void info(int fontColor2, int backgroundColor2, String str, Object... args) {
        if (debug) {
            out.printf("\u001b[%s;%sm" + str + "\u001b[0m\n", Integer.valueOf(fontColor2), Integer.valueOf(backgroundColor2), args);
        }
    }

    public static void err(String str, Object... args) {
        if (debug) {
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
