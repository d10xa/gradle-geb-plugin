package ru.d10xa.geb

import groovy.transform.CompileStatic
import org.gradle.api.Project

@CompileStatic
class ChromeConfig {

    static final String GEB_DRIVER = 'org.openqa.selenium.chrome.ChromeDriver'
    static final String PROPERTY_WEBDRIVER_PATH = 'webdriver.chrome.driver'

    Project project

    String getDriverPath() {
        def chromedriverFilename = OsUtils.isWindows() ? "chromedriver.exe" : "chromedriver"
        def files = project.tasks.getByName(UnzipChromeDriverTask.NAME).outputs.files
        def file = new File(files.singleFile, chromedriverFilename)
        file.absolutePath
    }

}
