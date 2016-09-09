# gradle-geb-plugin

[![Download](https://api.bintray.com/packages/d10xa/maven/ru.d10xa%3Agradle-geb-plugin/images/download.svg) ](https://bintray.com/d10xa/maven/ru.d10xa%3Agradle-geb-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/d10xa/gradle-geb-plugin.svg?branch=master)](https://travis-ci.org/d10xa/gradle-geb-plugin)
[![Build Status](https://snap-ci.com/d10xa/gradle-geb-plugin/branch/master/build_image)](https://snap-ci.com/d10xa/gradle-geb-plugin/branch/master)
[![jitpack](https://jitpack.io/v/ru.d10xa/gradle-geb-plugin.svg)](https://jitpack.io/#ru.d10xa/gradle-geb-plugin)

## Getting Started

Step 1: Apply the plugin to your Gradle build script

```groovy
buildscript {
    repositories {
        maven { url 'https://dl.bintray.com/d10xa/maven' }
        jcenter()
    }
    dependencies {
        classpath "ru.d10xa:gradle-geb-plugin:2.0.1"
    }
}
apply plugin: 'groovy'
apply plugin: 'ru.d10xa.geb'

repositories {
    jcenter()
}
dependencies {
    testCompile "org.codehaus.groovy:groovy-all:2.4.7"
}
// Optional
geb {
    chromeDriverVersion = '2.24'
    gebVersion = '0.13.1'
    seleniumVersion = '2.53.1'
    gebEnv = 'firefox'
}
```

Step 2: Create file 'src/test/resources/GebConfig.groovy'

You can change baseUrl port in this snippet.
`InetAddress.getLocalHost().getHostAddress()` used for connect to localhost from docker

```groovy
import org.openqa.selenium.chrome.ChromeDriver

import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

driver = { new ChromeDriver() }

baseUrl = "http://${InetAddress.localHost.hostAddress}:8080"

environments {

    chrome_docker {
        driver = {
            def remoteWebDriverServerUrl = new URL("http://localhost:4444/wd/hub")
            new RemoteWebDriver(remoteWebDriverServerUrl, DesiredCapabilities.chrome())
        }
    }

    firefox_docker {
        driver = {
            def remoteWebDriverServerUrl = new URL("http://localhost:4444/wd/hub")
            new RemoteWebDriver(remoteWebDriverServerUrl, DesiredCapabilities.firefox())
        }
    }

}
```

## Configuration

- `chromeDriverVersion` https://sites.google.com/a/chromium.org/chromedriver/downloads
- `gebVersion` http://mvnrepository.com/artifact/org.gebish/geb-core
- `seleniumVersion` http://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-api
- `dockerStandaloneChromeVersion` https://hub.docker.com/r/selenium/standalone-chrome/
- `dockerStandaloneFirefoxVersion` https://hub.docker.com/r/selenium/standalone-firefox/
- `seleniumPort` (integer) default 4444. Selenium docker exposed port.
- `gebEnv` (string) system property `geb.env` value. 
Can be overridden by GebEnvironmentTask. Available values: chrome, firefox

## Tasks

### chromeTest

Runs a test with geb.env = chrome

Depends on: downloadChromeDriver, unzipChromeDriver

### firefoxTest

Runs a test with geb.env = firefox

### chromeDockerTest

Runs a test with geb.env = chrome_docker.
Starts [selenium/standalone-chrome](https://hub.docker.com/r/selenium/standalone-chrome/)

### firefoxDockerTest

Runs a test with geb.env = firefox_docker
Starts [selenium/standalone-firefox](https://hub.docker.com/r/selenium/standalone-firefox/)

## Custom geb.env

If you need connection to your own selenium, you can define your own environment

```groovy
import org.openqa.selenium.remote.RemoteWebDriver
baseUrl = "http://$InetAddress.localHost.hostAddress:8080"
environments {

    // ... some environments here

    my_custom_remote_firefox {
        driver = {
            def remoteWebDriverServerUrl = new URL("http://localhost:5555/wd/hub")
            new RemoteWebDriver(remoteWebDriverServerUrl, DesiredCapabilities.firefox())
        }
    }
}

```

Then create new gradle task

```gradle
task myCustomRemoteFirefoxTest(type: ru.d10xa.geb.GebEnvironmentTask) {
    gebEnv = "my_custom_remote_firefox"
    seleniumPort = 5555
}
```

## Deprecations from 1.0.5

### `groovyVersion` configuration property has been removed. 

You need to explicitly add groovy dependency. 

```gradle
dependencies {
    testCompile "org.codehaus.groovy:groovy-all:2.4.7"
}
```

### Changed the logic of task execution

Early tasks chromeTest, firefoxTest was inherited from Test. 
Now it is configuration tasks only. Tests executes in `test` task.
This was done for compatibility with IDE debugging.

### Renamed property `defaultTestBrowser` to `gebEnv`

Now gebEnv used to set the system property `geb.env` in tests.
