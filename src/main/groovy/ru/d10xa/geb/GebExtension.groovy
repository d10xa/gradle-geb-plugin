package ru.d10xa.geb

import geb.Browser
import groovy.transform.ToString
import org.gradle.api.Project

@ToString
class GebExtension {

    static final NAME = 'geb'

    Browser browser

    Project project

    def chromeDriverVersion
    def groovyVersion
    def gebVersion
    def seleniumVersion
    def phantomJsVersion

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

    private def getDriverClassName() {
        System.getProperty('geb.driver') ?: ChromeConfig.PROPERTY_DRIVER_CLASSNAME
    }

    public void closeBrowser() {
        if (usedBrowser) {
            browser.quit()
        }
    }

}
