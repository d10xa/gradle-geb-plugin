package ru.d10xa.geb

import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

import static ru.d10xa.geb.DownloadChromeDriverTask.DRIVER_BASE_URL

class DownloadChromeDriverSpec extends Specification {

    @Shared
    Project project

    def setup() {
        project = ProjectBuilder.builder().build();
        project.apply plugin: "groovy"
        project.apply plugin: "ru.d10xa.geb"
        project.evaluate()
    }

    def 'chrome driver for mac 2.22 only 32bit'(){
        when:
        def url = new ChromeDriverUrl(os: ChromeDriverUrl.Os.mac, chromeDriverVersion: "2.22")

        then:
        url.toString() == "http://chromedriver.storage.googleapis.com/2.22/chromedriver_mac32.zip"
        url.key == "2.22/chromedriver_mac32.zip"
    }

    def 'chrome driver for mac 2.23 only 64bit'(){
        when:
        def url = new ChromeDriverUrl(os: ChromeDriverUrl.Os.mac, chromeDriverVersion: "2.23")

        then:
        url.toString() == "http://chromedriver.storage.googleapis.com/2.23/chromedriver_mac64.zip"
        url.key == "2.23/chromedriver_mac64.zip"
    }

    def 'chrome driver for windows'(){
        when:
        def url = new ChromeDriverUrl(os: ChromeDriverUrl.Os.windows, chromeDriverVersion: "2.22")

        then:
        url.toString() == "http://chromedriver.storage.googleapis.com/2.22/chromedriver_win32.zip"
        url.key == "2.22/chromedriver_win32.zip"
    }

    def 'chrome driver for linux32'(){
        when:
        def url = new ChromeDriverUrl(os: ChromeDriverUrl.Os.linux32, chromeDriverVersion: "2.22")

        then:
        url.toString() == "http://chromedriver.storage.googleapis.com/2.22/chromedriver_linux32.zip"
        url.key == "2.22/chromedriver_linux32.zip"
    }

    def 'chrome driver for linux64'(){
        when:
        def url = new ChromeDriverUrl(os: ChromeDriverUrl.Os.linux64, chromeDriverVersion: "2.22")

        then:
        url.toString() == "http://chromedriver.storage.googleapis.com/2.22/chromedriver_linux64.zip"
        url.key == "2.22/chromedriver_linux64.zip"
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
            it.Key.text() ==
                    new ChromeDriverUrl(chromeDriverVersion: "2.24").key
        }
        node.Size.text() as Integer
    }

    String getChromeDriverVersion() {
        project.extensions.getByName(GebExtension.NAME).chromeDriverVersion
    }

}
