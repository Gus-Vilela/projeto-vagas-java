package server.utils;

public class ConversionUtil {
    public static int convertToInteger(Object valueObject) {
        return switch (valueObject) {
            case Double d -> d.intValue();
            case String s -> {
                try {
                    double doubleValue = Double.parseDouble(s);
                    yield (int) doubleValue;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid format for conversion to integer.");
                }
            }
            case Integer i -> i;
            case null, default -> throw new IllegalArgumentException("Null or unsupported type for conversion to integer.");
        };
    }
}