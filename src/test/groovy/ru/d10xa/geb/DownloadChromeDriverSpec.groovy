package ru.d10xa.geb

import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

import static ru.d10xa.geb.DownloadChromeDriverTask.DRIVER_BASE_URL
import static ru.d10xa.geb.DownloadChromeDriverTask.driverOsFilenamePart

class DownloadChromeDriverSpec extends Specification {

    @Shared
    Project project

    def setup() {
        project = ProjectBuilder.builder().build();
        project.apply plugin: "groovy"
        project.apply plugin: "ru.d10xa.geb"
        project.evaluate()
    }

    def 'chrome driver archive size'() {
        when:
        project.downloadChromeDriver.execute()
        File chromeDriverZip = project.downloadChromeDriver.outputs.files.singleFile

        then:
        expectedZipSize != null
        expectedZipSize > 1000000
        chromeDriverZip.exists()
        chromeDriverZip.length() == expectedZipSize
    }

    int getExpectedZipSize() {
        GPathResult xml = new XmlSlurper().parse(DRIVER_BASE_URL)
        def node = xml.Contents.find {
            it.Key.text() == expectedChromeDriverKey
        }
        node.Size.text() as Integer
    }

    String getChromeDriverVersion() {
        project.extensions.getByName(GebExtension.NAME).chromeDriverVersion
    }

    String getExpectedChromeDriverKey() {
        "$chromeDriverVersion/chromedriver_${driverOsFilenamePart}.zip"
    }

}
