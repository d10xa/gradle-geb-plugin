package ru.d10xa.geb

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import static ru.d10xa.geb.OsUtils.*

@CompileStatic
class DownloadChromeDriverTask extends DefaultTask {

    static final String NAME = 'downloadChromeDriver'
    final static String DRIVER_BASE_URL = "http://chromedriver.storage.googleapis.com"

    @TaskAction
    void run() {
        URL url = new URL("$DRIVER_BASE_URL/${geb.chromeDriverVersion}/chromedriver_${driverOsFilenamePart}.zip")
        FileUtils.copyURLToFile(url, outputFile)
    }

    @OutputFile
    File getOutputFile() {
        new File("${project.buildDir}/webdriver/chromedriver${geb.chromeDriverVersionWithUnderscores}.zip")
    }

    private GebExtension getGeb() {
        project.extensions.findByType(GebExtension)
    }

    @PackageScope
    static String getDriverOsFilenamePart() {
        def driverOsFilenamePart
        if (isWindows()) {
            driverOsFilenamePart = "win32"
        } else if (isMacOs()) {
            driverOsFilenamePart = "mac32"
        } else if (is32BitLinux()) {
            driverOsFilenamePart = "linux32"
        } else if (is64BitLinux()) {
            driverOsFilenamePart = "linux64"
        }
        return driverOsFilenamePart
    }

}
