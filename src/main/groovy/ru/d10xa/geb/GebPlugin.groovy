package ru.d10xa.geb

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GebPlugin implements Plugin<Project> {

    Task chromeTest
    Task firefoxTest
    Task unzipChromeDriver
    Task downloadChromeDriver

    GebExtension geb

    @Override
    void apply(Project project) {
        geb = project.extensions.create(GebExtension.NAME, GebExtension, project)

        project.with {
            afterEvaluate {
                tasks.getByName('test') {
                    doFirst {
                        reports {
                            html.destination = reporting.file("$name/tests")
                            junitXml.destination = new File("$buildDir/test-results/$name")
                        }
                        systemProperty "geb.build.reportsDir", reporting.file("$name/geb")

                        String selectedBrowser = project.extensions.getByType(GebExtension).defaultTestBrowser
                        logger.info("selected browser $selectedBrowser")

                        switch (selectedBrowser) {
                            case 'chrome':
                                systemProperty "geb.env", 'chrome'
                                systemProperty "geb.driver", 'org.openqa.selenium.chrome.ChromeDriver'
                                systemProperty "webdriver.chrome.driver", new ChromeConfig(project: project).driverPath
                                break;
                            case 'firefox':
                            default:
                                systemProperty "geb.env", 'firefox'
                                systemProperty "geb.driver", 'org.openqa.selenium.firefox.FirefoxDriver'
                                break
                        }
                    }

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
        }

        project.with {
            test.outputs.upToDateWhen { false }

            downloadChromeDriver = project.task(type: DownloadChromeDriverTask, DownloadChromeDriverTask.NAME)
            unzipChromeDriver = project.task(type: UnzipChromeDriverTask, UnzipChromeDriverTask.NAME)
            chromeTest = project.task(type: SelectBrowserTask.SelectChromeTask, 'chromeTest')
            firefoxTest = project.task(type: SelectBrowserTask.SelectFirefoxTask, 'firefoxTest')
            unzipChromeDriver.outputs.upToDateWhen { false }
            chromeTest.dependsOn unzipChromeDriver
            unzipChromeDriver.dependsOn downloadChromeDriver


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
        def gebTasks = [chromeTest, firefoxTest, unzipChromeDriver, downloadChromeDriver]
        gebTasks*.group = 'geb'
    }

}
