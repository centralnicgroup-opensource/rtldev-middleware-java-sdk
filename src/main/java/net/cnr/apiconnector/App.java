package net.cnr.apiconnector;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        // // Example usage with a single domain
        // String domainName = "faß.de";
        // IDNAConverter singleResult = IDNAConverter.convert(domainName);
        // System.out.println("Single IDN: " + singleResult.getIDN());
        // System.out.println("Single PC: " + singleResult.getPC());

        // // Example usage with a list of domains
        // List<String> domainNames = new ArrayList<>();
        // domainNames.add("faß.de");
        // domainNames.add("faßelous.com");

        // IDNAConverter listResult = IDNAConverter.convert(domainNames, false);
        // System.out.println("List IDN: " + listResult.getIDNList());
        // System.out.println("List PC: " + listResult.getPCList());

        // perform an api login and create an api session
        APIClient cl = new APIClient();
        cl.useLIVESystem().setCredentials("qmtest:middleware", "#RSRMIDftw123#");
        cl.enableDebugMode();
        Response r = cl.login();
        // --- use this for 2-Factor Auth ---
        // Response r = cl.login("1234567");

        if (r.isSuccess()) {
            System.out.println("Login succeeded.");
            // perform further api request reusing the generated api session
            Map<String, Object> cmd = new HashMap<String, Object>();
            cmd.put("COMMAND", "StatusAccount");
            r = cl.request(cmd);
            if (r.isSuccess()) {
                System.out.println("Command succeeded.");
            } else {
                System.out.println("Command failed.");
            }
            // perform api logout and destroy api session
            r = cl.logout();
            if (r.isSuccess()) {
                System.out.println("Logout succeeded.");
            } else {
                System.out.println("Logout failed.");
            }
        } else {
            Map<String, Object> cmd = new HashMap<String, Object>();
            cmd.put("COMMAND", "CheckDomains");
            cmd.put("DOMAIN0", "𑀓.com");
            cmd.put("DOMAIN1", "faß.net");
            cmd.put("DOMAIN2", "faß.de");
            r = cl.request(cmd);
            System.out.println("Login failed.");
        }
    }
}
