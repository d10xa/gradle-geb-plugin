package ru.d10xa.geb

import geb.Browser
import groovy.transform.ToString
import org.gradle.api.Project
import org.openqa.selenium.chrome.ChromeDriver

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
            def driver = new ChromeDriver() ?: Class.forName(System.getProperty('geb.driver'))
            browser = new Browser(driver: driver)
            usedBrowser = true
        }
        browser
    }

    public void closeBrowser() {
        if (usedBrowser) {
            browser.quit()
        }
    }

}
