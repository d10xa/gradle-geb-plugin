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
            System.setProperty ChromeConfig.PROPERTY_WEBDRIVER_PATH, config.driverPath
            def driver = Class.forName(driverClassName).newInstance()
            browser = new Browser(driver: driver)
            usedBrowser = true
        }
        browser
    }

    private static def getDriverClassName() {
        System.getProperty('geb.driver') ?: ChromeConfig.GEB_DRIVER
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
