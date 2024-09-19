package uz.pdp.dreamexpressbot.util;

public class ValidationUtil {

    public static boolean checkFirstName(String firstname) {
        if (firstname == null || firstname.trim().isEmpty()) {
            return false;
        }
        if (firstname.length() < 3) {
            return false;
        }
        if (!firstname.matches("[A-Z][a-zA-Z]*")) {
            return false;
        }
        return !firstname.contains(" ");
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        String cleanedPhoneNumber = phoneNumber.replaceAll(" ", "");

        // Regular expressions for Uzbek numbers
        String regexUzbekWithCode = "^\\+998\\d{9}$"; // +998 followed by 9 digits
        String regexUzbekWithoutCode = "^\\d{9}$"; // 9 digits without country code

        // Regular expressions for Korean numbers
        String regexKoreanWithCode = "^\\+82\\d{8,10}$"; // +82 followed by 8 to 10 digits
        String regexKoreanWithoutCode = "^010\\d{7,8}$"; // 010 followed by 7 or 8 digits

        // Check if the phone number matches any of the valid Uzbek or Korean patterns
        return cleanedPhoneNumber.matches(regexUzbekWithCode) || cleanedPhoneNumber.matches(regexUzbekWithoutCode) ||
               cleanedPhoneNumber.matches(regexKoreanWithCode) || cleanedPhoneNumber.matches(regexKoreanWithoutCode);
    }


}
