package ru.d10xa.geb

import org.gradle.api.Plugin
import org.gradle.api.Project

class GebPlugin implements Plugin<Project> {

    ChromeTestTask chromeTest
    FirefoxTestTask firefoxTest
    UnzipChromeDriverTask unzipChromeDriver
    DownloadChromeDriverTask downloadChromeDriver

    GebExtension geb

    @Override
    void apply(Project project) {
        geb = new GebExtension(project)
        project.extensions.add(GebExtension.NAME, geb)

        project.with {
            afterEvaluate {
                geb.with {
                    chromeDriverVersion = chromeDriverVersion ?: '2.10'
                    groovyVersion = groovyVersion ?: '2.4.4'
                    gebVersion = gebVersion ?: '0.10.0'
                    seleniumVersion = seleniumVersion ?: '2.46.0'
                    phantomJsVersion = phantomJsVersion ?: '1.9.8'
                }
                switch (geb.defaultTestBrowser) {
                    case 'firefox':
                        tasks.test.dependsOn firefoxTest
                        break
                    default:
                        tasks.test.dependsOn chromeTest
                        break
                }
                dependencies {
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
            }
        }

        project.with {
            downloadChromeDriver = project.task(type: DownloadChromeDriverTask, DownloadChromeDriverTask.NAME)

            unzipChromeDriver = project.task(type: UnzipChromeDriverTask, UnzipChromeDriverTask.NAME)
            chromeTest = project.task(type: ChromeTestTask, ChromeTestTask.NAME)
            firefoxTest = project.task(type: FirefoxTestTask, FirefoxTestTask.NAME)

            unzipChromeDriver.outputs.upToDateWhen { false }
            chromeTest.dependsOn unzipChromeDriver
            unzipChromeDriver.dependsOn downloadChromeDriver

            tasks.test.enabled = false

            tasks.withType(GebTask) {
                it.dependsOn unzipChromeDriver
            }

            groupTasks()

            gradle.buildFinished {
                geb.closeBrowser()
            }
        }

    }

    def groupTasks() {
        chromeTest.group = 'geb'
        unzipChromeDriver.group = 'geb'
        downloadChromeDriver.group = 'geb'
    }

}