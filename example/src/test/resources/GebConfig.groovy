import org.openqa.selenium.chrome.ChromeDriver

import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

driver = { new ChromeDriver() }

baseUrl = "http://$InetAddress.localHost.hostAddress:${System.getProperty('gradleServerPort')}"

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
