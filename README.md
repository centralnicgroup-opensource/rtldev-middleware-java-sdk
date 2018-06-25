# java-sdk

This module is a connector library for the insanely fast HEXONET Backend API. For further informations visit our [homepage](http://hexonet.net) and do not hesitate to contact us.

## How to use this module in your project

### Requirements
Having [Maven](https://maven.apache.org) installed on operating system side.
Feel free to let [us](https://github.com/hexonet/java-sdk/wiki/Help) know if there are further ways to integrate or if you have any trouble.

### Using Maven / jitpack.io

Standard way based on [jitpack.io](http://jitpack.io).
Add the following lines to your maven project's pom.xml:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
  <dependencies>
    <dependency>
        <groupId>com.github.hexonet</groupId>
        <artifactId>java-sdk</artifactId>
        <version>v1.3.12</version>
      </dependency>
  </dependencies>
```

Now `mvn install` will produce that package version and will make it available within your project.
See our demonstration app which you can find [here](https://github.com/hexonet/java-sdk-demo).

## Development

Have an eye on the [development guide](https://github.com/hexonet/java-sdk/wiki/Development-Guide) in our wiki documentation.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](https://github.com/hexonet/java-sdk/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Kai Schwarz** - *lead development* - [PapaKai](https://github.com/papakai)

See also the list of [contributors](https://github.com/hexonet/java-sdk/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## How-to-use Examples

### Session based API Communication

```java
    import net.hexonet.apiconnector.Client;
    import net.hexonet.apiconnector.ListResponse;
    import java.util.HashMap;
    import java.util.Map;

    public static void main(String[] args) {
        // perform an api login and create an api session
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("login", "test.user");
        cfg.put("pw", "test.passw0rd");
        cfg.put("entity", "1234");
        // --- use this for 2-Factor Auth ---
        // cfg.put("otp", "my_otp_code");
        // --- use this if you have active ip filter settings ---
        // cfg.put("remoteaddr", "client's remote ip address");
        Client cl = new Client();
        ListResponse r = cl.login(cfg);

        if (r.isSuccess()){
            System.out.println("Login succeeded.");
            // perform further api request reusing the generated api session
            Map<String, String> cmd = new HashMap<String, String>();
            cmd.put("COMMAND", "StatusAccount");
            r = cl.request(cmd);
            if (r.isSuccess()){
                System.out.println("Command succeeded.");
            }
            else {
                System.out.println("Command failed.");
            }
            // perform api logout and destroy api session
            r = cl.logout();
            if (r.isSuccess()){
                System.out.println("Logout succeeded.");
            }
            else {
                System.out.println("Logout failed.");
            }
        }
        else {
            System.out.println("Login failed.");
        }
    }
```

### Sessionless API Communication

```java
    import net.hexonet.apiconnector.Client;
    import net.hexonet.apiconnector.ListResponse;
    import java.util.HashMap;
    import java.util.Map;

    public static void main(String[] args) {
        // perform an api login and create an api session
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("login", "test.user");
        cfg.put("pw", "test.passw0rd");
        cfg.put("entity", "1234");
        // --- use this if you have active ip filter settings ---
        // cfg.put("remoteaddr", "client's remote ip address");
        Client cl = new Client();
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        ListResponse r = cl.request(cmd, cfg);
        if (r.isSuccess()){
            System.out.println("Command succeeded.");
        }
        else {
            System.out.println("Command failed.");
        }
    }
```
