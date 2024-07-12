package Service;

public class Validation {
    public static boolean validCustomerName(String name){
        String pattern = "^(Mr|Mrs)\\.?[A-Z][a-z]*$";
        return name.matches(pattern);
    }

    public static boolean validCustomerPhone(String phone){
        String pattern = "^\\+?[0-9]{9}$";
        return phone.matches(pattern);
    }

    public static boolean validIdCusAndTrans(String id){
        String pattern = "^[1-9]{1}$";
        return id.matches(pattern);
    }

    public static boolean validIdAcc(String id){
        String pattern = "^[1-9]{8}$";
        return id.matches(pattern);
    }

}
