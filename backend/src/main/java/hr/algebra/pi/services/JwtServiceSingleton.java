package hr.algebra.pi.services;

public class JwtServiceSingleton {
    private static JwtService instance;
    private static final Object mutex = new Object();

    private JwtServiceSingleton() {}

    public static JwtService getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new JwtService();
                }
            }
        }
        return instance;
    }
}
