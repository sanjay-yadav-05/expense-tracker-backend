package authService.Util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])" +             // at least one digit
                    "(?=.*[a-z])" +              // at least one lowercase
                    "(?=.*[A-Z])" +              // at least one uppercase
                    "(?=.*[@#$%^&+=!])" +        // at least one special character
                    "(?=\\S+$).{8,}$";           // no whitespace, at least 8 characters

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static boolean validateCredentials(String email, String password) {
        boolean isEmailValid = EMAIL_PATTERN.matcher(email).matches();
        boolean isPasswordStrong = PASSWORD_PATTERN.matcher(password).matches();

        if (!isEmailValid && !isPasswordStrong) {
            System.out.println("❌ Both email and password are invalid.");
            return false;
        } else if (!isEmailValid) {
            System.out.println("❌ Invalid email format.");
            return false;
        } else if (!isPasswordStrong) {
            System.out.println("❌ Password is weak. It must contain at least:\n"
                    + "- One digit\n- One lowercase\n- One uppercase\n- One special character\n- Minimum 8 characters");
            return false;
        }

        System.out.println("✅ Both email and password are valid.");
        return true;
    }
}

