package ru.d10xa.geb

import geb.Browser
import groovy.transform.ToString
import org.gradle.api.Project

@ToString
class GebExtension {

    static final NAME = 'geb'

    Browser browser

    Project project

    def chromeDriverVersion = '2.10'
    def groovyVersion = '2.4.3'
    def gebVersion = '0.10.0'
    def seleniumVersion = '2.46.0'
    def phantomJsVersion = '1.9.8'

    boolean usedBrowser = false

    GebExtension(Project project) {
        this.project = project
    }

    public Browser getBrowser() {
        if (!browser) {
//            System.setProperty "webdriver.chrome.driver", new ChromeConfig(project: project).driverPath
            def driver = Class.forName(driverClassName).newInstance()
            browser = new Browser(driver: driver)
            usedBrowser = true
        }
        browser
    }

    private def getDriverClassName() {
        System.getProperty('geb.driver') ?: ChromeConfig.PROPERTY_DRIVER_CLASSNAME
    }

    public void closeBrowser() {
        if (usedBrowser) {
            browser.quit()
        }
    }

}
