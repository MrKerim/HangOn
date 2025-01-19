import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static String generateToken(String username,String password) {
        return BCrypt.hashpw(username+password,GameSettings.bcryptSalt);
    }
}
