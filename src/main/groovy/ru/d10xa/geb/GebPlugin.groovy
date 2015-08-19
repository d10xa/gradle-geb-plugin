package ru.d10xa.geb

import org.gradle.api.Plugin
import org.gradle.api.Project

class GebPlugin implements Plugin<Project> {

    ChromeTestTask chromeTest
    UnzipChromeDriverTask unzipChromeDriver
    DownloadChromeDriverTask downloadChromeDriver

    GebExtension geb = new GebExtension()

    @Override
    void apply(Project project) {
        project.extensions.add('geb', geb)

        project.with {
            afterEvaluate {

                geb.with {
                    chromeDriverVersion = chromeDriverVersion ?: '2.10'
                    groovyVersion = groovyVersion ?: '2.4.4'
                    gebVersion = gebVersion ?: '0.10.0'
                    seleniumVersion = seleniumVersion ?: '2.46.0'
                    phantomJsVersion = phantomJsVersion ?: '1.9.8'
                }

                dependencies {
                    testCompile "org.gebish:geb-spock:${geb.gebVersion}"
                    testCompile("org.spockframework:spock-core:1.0-groovy-2.4") {
                        exclude group: "org.codehaus.groovy"
                    }
                    testCompile "org.codehaus.groovy:groovy-all:${geb.groovyVersion}"

                    // If using JUnit, need to depend on geb-junit (3 or 4)
                    testCompile "org.gebish:geb-junit4:${geb.gebVersion}"

                    // Drivers
                    testCompile "org.seleniumhq.selenium:selenium-support:${geb.seleniumVersion}"//for <select/>
                    testCompile "org.seleniumhq.selenium:selenium-chrome-driver:${geb.seleniumVersion}"
                    testCompile "org.seleniumhq.selenium:selenium-firefox-driver:${geb.seleniumVersion}"
                    testCompile("com.github.detro.ghostdriver:phantomjsdriver:1.1.0") {
                        // phantomjs driver pulls in a different selenium version
                        transitive = false
                    }
                }

            }

            downloadChromeDriver = project.task(type: DownloadChromeDriverTask, DownloadChromeDriverTask.NAME)
            unzipChromeDriver = project.task(type: UnzipChromeDriverTask, UnzipChromeDriverTask.NAME)
            chromeTest = project.task(type: ChromeTestTask, ChromeTestTask.NAME)

            chromeTest.dependsOn 'unzipChromeDriver'
            unzipChromeDriver.dependsOn 'downloadChromeDriver'
            groupTasks()
        }
    }

    def groupTasks() {
        chromeTest.group = 'geb'
        unzipChromeDriver.group = 'geb'
        downloadChromeDriver.group = 'geb'
    }

}