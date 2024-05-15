package cliente.utils;

public class StatusUtil {
    public static int getStatus(Object statusObject) {
        return switch (statusObject) {
            case Double v -> v.intValue();
            case String s -> Integer.parseInt(s);
            case Integer i -> i;
            case null, default -> throw new IllegalArgumentException("Formato de status inválido.");
        };
    }
}
