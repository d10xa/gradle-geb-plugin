package ru.d10xa.geb

import groovy.transform.CompileStatic
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CompileStatic
class DownloadChromeDriverTask extends DefaultTask {

    final static String NAME = 'downloadChromeDriver'
    final static String DRIVER_BASE_URL = "http://chromedriver.storage.googleapis.com"

    @TaskAction
    void run() {
        URL url = new ChromeDriverUrl(chromeDriverVersion: "$geb.chromeDriverVersion").toURL()
        FileUtils.copyURLToFile(url, outputFile)
    }

    @OutputFile
    File getOutputFile() {
        new File("${project.buildDir}/webdriver/chromedriver${geb.chromeDriverVersionWithUnderscores}.zip")
    }

    private GebExtension getGeb() {
        project.extensions.findByType(GebExtension)
    }

}
