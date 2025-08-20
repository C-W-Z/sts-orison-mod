package orison.utils;

public class GeneralUtils {
    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static String removePrefix(String ID) {
        return ID.substring(ID.indexOf(":") + 1);
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(v, max));
    }

    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(v, max));
    }
}
