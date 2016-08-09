package ru.d10xa.geb

import geb.Browser
import groovy.transform.ToString
import org.gradle.api.Project

@ToString
class GebExtension {

    static final String NAME = 'geb'

    Browser browser

    Project project

    /**
     * https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    def chromeDriverVersion = '2.22'

    /**
     * http://mvnrepository.com/artifact/org.codehaus.groovy/groovy-all
     */
    def groovyVersion = '2.4.7'

    /**
     * http://mvnrepository.com/artifact/org.gebish/geb-core
     */
    def gebVersion = '0.13.1'

    /**
     * http://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-api
     */
    def seleniumVersion = '2.53.1'

    /**
     * not implemented
     */
    def phantomJsVersion = '1.9.8'

    def defaultTestBrowser

    boolean usedBrowser = false

    GebExtension(Project project) {
        this.project = project
    }

    public Browser getBrowser() {
        if (!browser) {
            def config = new ChromeConfig(project: project)
            def driver = null
            switch (project.extensions.getByType(ru.d10xa.geb.GebExtension).defaultTestBrowser){
                case 'chrome':
                    System.setProperty "geb.env", 'chrome'
                    System.setProperty "geb.driver", 'org.openqa.selenium.chrome.ChromeDriver'
                    System.setProperty "webdriver.chrome.driver", new ChromeConfig(project:project).driverPath
                    driver = Class.forName('org.openqa.selenium.chrome.ChromeDriver').newInstance()
                    break;
                case 'firefox':
                default:
                    System.setProperty "geb.env", 'firefox'
                    System.setProperty "geb.driver", 'org.openqa.selenium.firefox.FirefoxDriver'
                    driver = Class.forName('org.openqa.selenium.firefox.FirefoxDriver').newInstance()
                    break
            }
            browser = new Browser(driver: driver)
            if(browser){
                usedBrowser = true
            }
        }
        browser
    }

    public void closeBrowser() {
        if (usedBrowser) {
            browser.quit()
        }
    }

    String getChromeDriverVersionWithUnderscores() {
        "$chromeDriverVersion".replace(".", "_")
    }

}
