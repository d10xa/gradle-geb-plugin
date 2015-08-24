package ru.d10xa.geb

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project

class ChromeConfig {

    static final String PROPERTY_DRIVER_CLASSNAME = 'org.openqa.selenium.chrome.ChromeDriver'
    static final String PROPERTY_GEB_CLASSNAME = 'chrome'

    Project project;

    String getDriverPath() {
        def chromedriverFilename = Os.isFamily(Os.FAMILY_WINDOWS) ? "chromedriver.exe" : "chromedriver"
        def files = project.tasks.getByName('unzipChromeDriver').outputs.files
        def file = new File(files.singleFile, chromedriverFilename)
        def path = file.absolutePath
        path
    }

}
