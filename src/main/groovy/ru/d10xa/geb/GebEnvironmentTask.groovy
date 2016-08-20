package ru.d10xa.geb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GebEnvironmentTask extends DefaultTask {

    String gebEnv

    Map<String, String> systemProperties = new LinkedHashMap<>()

    GebEnvironmentTask() {
        this.outputs.upToDateWhen { false }
        this.finalizedBy 'test'
    }

    @TaskAction
    void executeAction() {
        this.project
                .extensions.getByType(GebExtension)
                .gebEnv = this.gebEnv
    }

    void systemProperty(String k, String v) {
        systemProperties.put(k, v)
    }

    static class FirefoxEnvironmentTask extends GebEnvironmentTask {
        FirefoxEnvironmentTask() {
            gebEnv = "firefox"
            systemProperties = [
                    "geb.env"   : 'firefox',
                    "geb.driver": 'org.openqa.selenium.firefox.FirefoxDriver'
            ]
        }
    }

    static class ChromeEnvironmentTask extends GebEnvironmentTask {
        ChromeEnvironmentTask() {
            gebEnv = "chrome"
            systemProperties = [
                    "geb.env"                : 'chrome',
                    "geb.driver"             : 'org.openqa.selenium.chrome.ChromeDriver',
                    "webdriver.chrome.driver": new ChromeConfig(project: project).driverPath

            ]
        }
    }

    static class ChromeDockerEnvironmentTask extends GebEnvironmentTask {
        ChromeDockerEnvironmentTask() {
            gebEnv = "chrome_docker"
            systemProperties = [
                    "geb.env": 'chrome_docker'
            ]
        }
    }

    static class FirefoxDockerEnvironmentTask extends GebEnvironmentTask {
        FirefoxDockerEnvironmentTask() {
            gebEnv = "firefox_docker"
            systemProperties = [
                    "geb.env": 'firefox_docker'
            ]
        }
    }

}
