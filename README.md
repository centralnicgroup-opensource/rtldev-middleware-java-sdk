# java-sdk

[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)
[![Build Status](https://travis-ci.com/hexonet/java-sdk.svg?branch=master)](https://travis-ci.com/hexonet/java-sdk)
![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/net/hexonet/apiconnector/java-sdk/maven-metadata.xml.svg)
[![Release](https://jitpack.io/v/hexonet/java-sdk.svg)](https://jitpack.io/#hexonet/java-sdk)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/hexonet/java-sdk/blob/master/CONTRIBUTING.md)

This module is a connector library for the insanely fast HEXONET Backend API. For further informations visit our [homepage](http://hexonet.net) and do not hesitate to [contact us](https://www.hexonet.net/contact).

## Resources

* [Usage Guide](https://github.com/hexonet/java-sdk/blob/master/README.md#how-to-use-this-module-in-your-project)
* [SDK Documenation](https://hexonet.github.io/java-sdk/target/apidocs/net/hexonet/apiconnector/package-summary.html)
* [HEXONET Backend API Documentation](https://github.com/hexonet/hexonet-api-documentation/tree/master/API)
* [Release Notes](https://github.com/hexonet/java-sdk/releases)
* [Development Guide](https://github.com/hexonet/java-sdk/wiki/Development-Guide)

## Features

* Automatic IDN Domain name conversion to punycode (our API accepts only punycode format in commands)
* Allows nested associative arrays in API commands to improve for bulk parameters
* Connecting and communication with our API
* Possibility to use a custom mechanism for debug mode
* Several ways to access and deal with response data
* Getting the command again returned together with the response
* Sessionless communication
* Session based communication
* Possibility to save API session identifier in session
* Configure a Proxy for API communication
* Configure a Referer for API communication
* High Performance Proxy Setup

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

### High Performance Proxy Setup

Long distances to our main data center in Germany may result in high network latencies. If you encounter such problems, we highly recommend to use this setup, as it uses persistent connections to our API server and the overhead for connection establishments is omitted.

#### Step 1: Required Apache2 packages / modules

*At least Apache version 2.2.9* is required.

The following Apache2 modules must be installed and activated:

```bash
proxy.conf
proxy.load
proxy_http.load
ssl.conf # for HTTPs connection to our API server
ssl.load # for HTTPs connection to our API server
```

#### Step 2: Apache configuration

An example Apache configuration with binding to localhost:

```bash
<VirtualHost 127.0.0.1:80>
    ServerAdmin webmaster@localhost
    ServerSignature Off
    SSLProxyEngine on
    ProxyPass /api/call.cgi https://api.ispapi.net/api/call.cgi min=1 max=2
    <Proxy *>
        Order Deny,Allow
        Deny from none
        Allow from all
    </Proxy>
</VirtualHost>
```

After saving your configuration changes please restart the Apache webserver.

#### Step 3: Using this setup

```java
    import net.hexonet.apiconnector.APIClient;
    import net.hexonet.apiconnector.Response;
    import java.util.HashMap;
    import java.util.Map;

    public static void main(String[] args) {
        APIClient cl = new APIClient();
        cl.useOTESystem()//LIVE System would be used otherwise by default
          .useHighPerformanceConnectionSetup()//Default Connection Setup would be used otherwise by default
          .setCredentials("test.user", "test.passw0rd");

        Map<String, Object> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response r = cl.request(cmd);
    }
```

So, what happens in code behind the scenes? We communicate with localhost (so our proxy setup) that passes the requests to the HEXONET API.
Of course we can't activate this setup by default as it is based on Steps 1 and 2. Otherwise connecting to our API wouldn't work.

Just in case the above port or ip address can't be used, use function setURL instead to set a different URL / Port.
`http://127.0.0.1/api/call.cgi` is the default URL for the High Performance Proxy Setup.
e.g. `$cl->setURL("http://127.0.0.1:8765/api/call.cgi");` would change the port. Configure that port also in the Apache Configuration (-> Step 2)!

Don't use `https` for that setup as it leads to slowing things down as of the https `overhead` of securing the connection. In this setup we just connect to localhost, so no direct outgoing network traffic using `http`. The apache configuration finally takes care passing it to `https` for the final communication to the HEXONET API.

### Customize Logging / Outputs

When having the debug mode activated \HEXONET\Logger will be used for doing outputs.
Of course it could be of interest for integrators to look for a way of getting this replaced by a custom mechanism like forwarding things to a 3rd-party software, logging into file or whatever.

```java
    import net.hexonet.apiconnector.APIClient;
    import net.hexonet.apiconnector.Response;
    import java.util.HashMap;
    import java.util.Map;

    public static void main(String[] args) {
        APIClient cl = new APIClient();
        cl.useOTESystem()
          .enableDebugMode() // activate debug outputs
          .setCustomLogger(new MyCustomerLogger()) // provide your mechanism here
          .setCredentials("test.user", "test.passw0rd");
        Map<String, Object> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response r = cl.request(cmd);
    }
```

NOTE: Find an example for a custom logger class implementation in `src/main/java/net/hexonet/apiconnector/CustomLogger.java`. If you have questions, feel free to open a github issue.

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
            Map<String, Object> cmd = new HashMap<String, String>();
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

        Map<String, Object> cmd = new HashMap<String, String>();
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
