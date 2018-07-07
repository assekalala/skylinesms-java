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

	public static void main (String args[]) throws Exception {
      
        if (args.length > 4 && args[1].equals("send")) {
            String key = args[0]; 
            String number = args[2]; 
            String message = args[3];
            
            SkylineSms skylinesms = new SkylineSms(key);
            
            if (args.length > 6) {
                log(skylinesms.send_message(number, message, args[6]));
            } else {
                log(skylinesms.send_message(number, message, "sklylinesms"));
            }

        } else if (args.length > 2 && args[2].equals("status")) {
            String key = args[1];
            String message_id = args[3];

            SkylineSms skylinesms = new SkylineSms(key);

            log(skylinesms.check_status(message_id));

        } else if (args.length > 2 && args[2].equals("balance")) { 
            String key = args[1];

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

    public static void print_usage(String args[]) {
        log("usage: <application key> send <number> <message> <from_number>");
        log("       <application key> status <message_id>");
        log("       <application key> balance");
    }

    public static String send_message(String phoneNumber, String msg, String _from) throws Exception  {
        /*  
        Send a message to the specified number and return a response dictionary.

        The numbers must be specified in international format starting without a '+'.
        Returns a dictionary that contains a 'reference' key with the sent message id value or
        contains 'status' and 'message' on error.
        */


        String url = sms_url + "?token=" + api_key + "&to=" + phoneNumber + "&message=" + msg;
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