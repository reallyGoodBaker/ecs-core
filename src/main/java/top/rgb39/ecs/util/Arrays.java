package top.rgb39.ecs.util;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/util/Arrays.class */
public class Arrays {
    public static <T> String join(T[] tArr, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tArr.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(tArr[i].toString());
        }
        return sb.toString();
    }
}
