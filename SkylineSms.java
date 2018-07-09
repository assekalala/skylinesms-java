import java.io.*;
import java.net.*;

public class SkylineSms {

    private static final String base_url = "http://skylinesms.com/api/v2/json/";
    private static final String sms_url =  base_url + "messages";
    private static final String check_status_url = base_url + "delivery";
    private static final String balance_url = base_url + "balance";

    private static String api_key;

    public SkylineSms(String api_key) {
        this.api_key = api_key;
    }

	public static void main (String[] args) throws Exception {
        if (args.length != 33) {
            log("Ivalid API key");
            System.exit(1);
        }

        if (args.length > 4 && args[1].equals("send")) {
            String key = args[0]; 
            String number = args[2]; 
            String message = args[3];
            String _from = (args.length > 6) ? args[4] : "skylinesms";
            
            ValidationResult validation = is_valid_number(number);

            if(validation.status == true) {
                SkylineSms skylinesms = new SkylineSms(key);
            
                log(skylinesms.send_message(number, message, _from));
            } else {
                log(validation.msg);
            }

        } else if (args.length > 2 && args[1].equals("status")) {
            String key = args[0];
            String message_id = args[2];

            SkylineSms skylinesms = new SkylineSms(key);

            log(skylinesms.check_status(message_id));

        } else if (args.length > 1 && args[1].equals("balance")) { 
            String key = args[0];

            SkylineSms skylinesms = new SkylineSms(key);
            log(skylinesms.balance());

        } else {
            print_usage(args);
            System.exit(1);
        }
        
        System.exit(0);
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static ValidationResult is_valid_number(String number) {
        ValidationResult result;

        if (number.matches("\\d+") == false) {
            result = new ValidationResult(false, "Phone number must be digits only");
        } else if (number.length() < 10) {
            result = new ValidationResult(false, "Phone number is too short");
        } else {
            result = new ValidationResult(true, null);
        }

        return result;
    }

    public static void print_usage(String args[]) {
        log("usage: <application key> send <number> <message> <from_number>");
        log("       <application key> status <message_id>");
        log("       <application key> balance");
    }

    public static String send_message(String phoneNumber, String message, String _from) throws Exception  {
        /*  
        Send a message to the specified number and return a response dictionary.

        The numbers must be specified in international format starting without a '+'.
        Returns a dictionary that contains a 'reference' key with the sent message id value or
        contains 'status' and 'message' on error.
        */

        String msg = URLEncoder.encode(message, "UTF-8");
        String url = sms_url + "?token=" + api_key + "&to=" + phoneNumber + "&message=" + msg + "&from=" + _from;
        return _request(url);
    }

    public static String check_status(String message_id) throws Exception {
        /*
        Request the status of a message with the provided id and return a response dictionary.

        Returns a dictionary that contains a 'delivery' key with the status value string or
        contains 'errorCode' and 'message' on error.
        */

        String url = check_status_url + "?token=" + api_key + "&reference=" + message_id;
        return _request(url);
    }



    public static String balance() throws Exception {
        String url =  balance_url + "?token=" + api_key;
        return _request(url);
    }


    private static String _request(String _url) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        return(result.toString());
    }
}

final class ValidationResult {
    boolean status;
    String msg;

    public ValidationResult(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}