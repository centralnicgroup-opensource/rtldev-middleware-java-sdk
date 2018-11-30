# java-sdk

![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/net/hexonet/apiconnector/java-sdk/maven-metadata.xml.svg)
[![Release](https://jitpack.io/v/hexonet/java-sdk.svg)](https://jitpack.io/#hexonet/java-sdk)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/hexonet/java-sdk/blob/master/CONTRIBUTING.md)
[![Slack Widget](https://camo.githubusercontent.com/984828c0b020357921853f59eaaa65aaee755542/68747470733a2f2f73332e65752d63656e7472616c2d312e616d617a6f6e6177732e636f6d2f6e6774756e612f6a6f696e2d75732d6f6e2d736c61636b2e706e67)](https://hexonet-sdk.slack.com/messages/CBF4K9E1F)

This module is a connector library for the insanely fast HEXONET Backend API. For further informations visit our [homepage](http://hexonet.net) and do not hesitate to [contact us](https://www.hexonet.net/contact).

## Resources

* [Usage Guide](https://github.com/hexonet/java-sdk/blob/master/README.md#how-to-use-this-module-in-your-project)
* [SDK Documenation](https://hexonet.github.io/java-sdk/target/apidocs/net/hexonet/apiconnector/package-summary.html)
* [HEXONET Backend API Documentation](https://github.com/hexonet/hexonet-api-documentation/tree/master/API)
* [Release Notes](https://github.com/hexonet/java-sdk/releases)
* [Development Guide](https://github.com/hexonet/java-sdk/wiki/Development-Guide)

## How to use this module in your project

We have also a demo app available showing how to integrate and use our SDK. See [here](https://github.com/hexonet/java-sdk-demo).

### Download from OSSRH

This module is available on the [Maven Central Registry](https://github.com/hexonet/java-sdk/wiki/Development-Guide#ossrh-paths) (OSSRH).

### Using Maven

#### Requirements

Having [Maven](https://maven.apache.org) installed on operating system side. For ubuntu this can be achieved in ease by

```bash
sudo apt install maven
```

Feel free to let [us](https://github.com/hexonet/java-sdk/wiki/Help) know if there are further ways to integrate or if you have any trouble.
If you have trouble with JAVA_HOME variable when using Maven, create ~/.mavenrc with the following contents:

```bash
export JAVA_HOME=<path to your java installation>
#e.g. export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
```

#### Using Maven standalone

As our package is available on the OSSRH as mentioned above, simply use:

```xml
<dependencies>
  <dependency>
    <groupId>net.hexonet.apiconnector</groupId>
    <artifactId>java-sdk</artifactId>
    <version>2.1.3</version>
  </dependency>
</dependencies>
```

#### Using Maven w/ jitpack.io

Standard way based on [jitpack.io](http://jitpack.io).
Add the following lines to your maven project's pom.xml:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

 <dependencies>
    <dependency>
        <groupId>com.github.hexonet</groupId>
        <artifactId>java-sdk</artifactId>
        <version>v2.1.3</version>
      </dependency>
  </dependencies>
```

Now `mvn install` will produce that package version and will make it available within your project.

### Usage Examples

Please have an eye on our [HEXONET Backend API documentation](https://github.com/hexonet/hexonet-api-documentation/tree/master/API). Here you can find information on available Commands and their response data.

#### Session based API Communication

```java
    import net.hexonet.apiconnector.APIClient;
    import net.hexonet.apiconnector.Response;
    import java.util.HashMap;
    import java.util.Map;

    public static void main(String[] args) {
        // perform an api login and create an api session
        APIClient cl = new APIClient();
        cl.useOTESystem()
          .setCredentials("test.user", "test.passw0rd")
        // --- use this if you have active ip filter settings ---
          .setRemoteIPAddress("1.2.3.4");
        // ------------------------------------------------------

        Response r = cl.login();
        // --- use this for 2-Factor Auth ---
        // Response r = cl.login("... provide otp code here ...");
        // ----------------------------------

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

#### Sessionless API Communication

```java
    import net.hexonet.apiconnector.APIClient;
    import net.hexonet.apiconnector.Response;
    import java.util.HashMap;
    import java.util.Map;

    public static void main(String[] args) {
        // perform an api login and create an api session
        APIClient cl = new APIClient();
        cl.useOTESystem()
          .setCredentials("test.user", "test.passw0rd")
        // --- use this if you have active ip filter settings ---
          .setRemoteIPAddress("1.2.3.4");
        // ------------------------------------------------------

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response r = cl.request(cmd);
        if (r.isSuccess()){
            System.out.println("Command succeeded.");
        }
        else {
            System.out.println("Command failed.");
        }
    }
```

## Contributing

Please read [our development guide](https://github.com/hexonet/java-sdk/wiki/Development-Guide) for details on our code of conduct, and the process for submitting pull requests to us.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Kai Schwarz** - *lead development* - [PapaKai](https://github.com/papakai)

See also the list of [contributors](https://github.com/hexonet/java-sdk/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
