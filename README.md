# java-sdk

This module is a connector library for the insanely fast HEXONET Backend API. For further informations visit our [homepage](http://hexonet.net) and do not hesitate to contact us.

## Requirements

Installed Maven on OS-side.
For developers: Visual Studio Code with installed plugins for Java Development described [here](https://code.visualstudio.com/docs/languages/java).

## Getting Started

Clone the git repository by `git clone https://github.com/hexonet/java-sdk`.

### For development purposes

Now you can already start working on the project.

### How to use this module in your project

Build the current stable JAR archive by executing `mvn package`. The archive can be found in subfolder "target".
Import the archive in your project as shown in the examples below.

*Standard way based on [jitpack.io](http://jitpack.io)*:

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

### Build current release

Build the JAR package of the current version by `mvn package`. Import that jar file into your project.
To only build the sources, use `mvn compile`.

You'll find anything you need in subfolder "target". Read [Maven - Getting started](https://maven.apache.org/guides/getting-started/index.html) for more details.

This does not generated things under deploy target path!

### Release a new Version and Deploy

In case your development has reached a status that can be released under a new version (SNAPSHOT or stable), merge your changes and then run the following steps:
`mvn release:prepare`
`mvn release:perform`

NOTE:
Run goal `release:perform` only in case goal `release:prepare` succeeds. If not you need to fix the issue and restart by `mvn release:prepare -Dresume=false` or `mvn release:clean release:prepare` instead.
In some cases this won't help and you need first to trigger goal `release:rollback`, then to fix the issue and then to start from scratch with the above goals.
If this runs still in an error, well rollback and set a new version manually by e.g. `mvn versions:set -DnewVersion=1.2.0-SNAPSHOT` and to commit that change before restarting the above goals.

Sounds weird, but issues only happened while we were setting up the pom.xml accordingly, we shouldn't have to get in contact with further issues from now on.

Packages can be found in the appropriate version subfolder in "/tmp/net/hexonet/apiconnector/" or in project's subfolder "target".
This includes JAR and md5/sha1 files for compiled files, source files, javadoc in the tmp folder.

### Snapshot Release

Nothing special. Use `mvn deploy` to get a development / snapshot version build.
Packages can be found in the appropriate version subfolder in "/tmp/net/hexonet/apiconnector/" or in project's subfolder "target".
This includes JAR and md5/sha1 files for compiled files, source files, javadoc in the tmp folder.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](https://github.com/hexonet/java-sdk/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://gihub.com/hexonet/java-sdk/tags).
Prepare and Release a new version can be done as described [here in the maven documentation](http://maven.apache.org/maven-release/maven-release-plugin/examples/prepare-release.html).

## Authors

* **Kai Schwarz** - *lead development* - [PapaKai](https://github.com/papakai)

See also the list of [contributors](https://github.com/hexonet/java-sdk/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## How-to-use Examples

### Session based API Communication

```java
    import net.hexonet.apiconnector.*;

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
```

### Sessionless API Communication

```java
    import net.hexonet.apiconnector.*;

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
```