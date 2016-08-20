package ru.d10xa.geb

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.testing.Test

class GebPlugin implements Plugin<Project> {

    Task chromeTest
    Task chromeDockerTest
    Task firefoxDockerTest
    Task firefoxTest
    Task unzipChromeDriver
    Task downloadChromeDriver
    Task stopDockerSeleniumChrome
    Task stopDockerSeleniumFirefox
    Task startDockerSeleniumChrome
    Task startDockerSeleniumFirefox

    GebExtension geb

    @Override
    void apply(Project project) {
        geb = project.extensions.create(GebExtension.NAME, GebExtension, project)
        project.with {
            tasks.getByName('test').outputs.upToDateWhen { false }
            gradle.buildFinished {
                geb.closeBrowser()
            }
            afterEvaluate {
                downloadChromeDriver = project.task(type: DownloadChromeDriverTask, DownloadChromeDriverTask.NAME)
                unzipChromeDriver = project.task(type: UnzipChromeDriverTask, UnzipChromeDriverTask.NAME)
                chromeTest = project.task(type: GebEnvironmentTask.ChromeEnvironmentTask, 'chromeTest')
                firefoxTest = project.task(type: GebEnvironmentTask.FirefoxEnvironmentTask, 'firefoxTest')
                chromeDockerTest = project.task(type: GebEnvironmentTask.ChromeDockerEnvironmentTask, 'chromeDockerTest')
                firefoxDockerTest = project.task(type: GebEnvironmentTask.FirefoxDockerEnvironmentTask, 'firefoxDockerTest')
                startDockerSeleniumChrome = project.task(type: Exec, 'startDockerSeleniumChrome') {
                    def image = "selenium/standalone-chrome:$geb.dockerStandaloneChromeVersion"
                    def port = geb.dockerStandaloneChromePort
                    executable "sh"
                    args "-c", "docker run -p $port:4444 -d --name gradle-selenium-chrome $image"
                }
                stopDockerSeleniumChrome = project.task(type: Exec, 'stopDockerSeleniumChrome') {
                    executable "sh"
                    args "-c", "docker stop gradle-selenium-chrome && docker rm gradle-selenium-chrome"
                }
                startDockerSeleniumFirefox = project.task(type: Exec, 'startDockerSeleniumFirefox') {
                    def image = "selenium/standalone-firefox:$geb.dockerStandaloneFirefoxVersion"
                    def port = geb.dockerStandaloneFirefoxPort
                    executable "sh"
                    args "-c", "docker run -p $port:4444 -d --name gradle-selenium-firefox $image"
                }
                stopDockerSeleniumFirefox = project.task(type: Exec, 'stopDockerSeleniumFirefox') {
                    executable "sh"
                    args "-c", "docker stop gradle-selenium-firefox && docker rm gradle-selenium-firefox"
                }
                tasks.getByName('test').mustRunAfter 'startDockerSeleniumChrome', 'startDockerSeleniumFirefox'
                stopDockerSeleniumChrome.mustRunAfter 'test'
                stopDockerSeleniumFirefox.mustRunAfter 'test'
                startDockerSeleniumChrome.finalizedBy 'stopDockerSeleniumChrome'
                startDockerSeleniumFirefox.finalizedBy 'stopDockerSeleniumFirefox'
                chromeDockerTest.dependsOn startDockerSeleniumChrome
                firefoxDockerTest.dependsOn startDockerSeleniumFirefox

                unzipChromeDriver.outputs.upToDateWhen { false }
                chromeTest.dependsOn unzipChromeDriver
                unzipChromeDriver.dependsOn downloadChromeDriver

                tasks.withType(GebTask) {
                    it.dependsOn unzipChromeDriver
                }

                groupTasks()

                tasks.getByName('test').dependsOn 'unzipChromeDriver'
                tasks.getByName('test') {
                    doFirst {
                        String selectedBrowser = project.extensions.getByType(GebExtension).gebEnv
                        reports {
                            html.destination = reporting.file("$name/tests")
                            junitXml.destination = new File("$buildDir/test-results/$name")
                        }

                        systemProperty "geb.build.reportsDir", reporting.file("$name/geb")

                        GebEnvironmentTask gebEnvTask = tasks
                                .withType(GebEnvironmentTask)
                                .find { it.gebEnv == selectedBrowser } ?: firefoxTest as GebEnvironmentTask

                        def testTask = tasks.getByName('test') as Test
                        testTask.systemProperties(gebEnvTask.systemProperties)
                    }
                }

                dependencies {
                    testCompile "org.gebish:geb-spock:${geb.gebVersion}"
                    testCompile("org.spockframework:spock-core:1.0-groovy-2.4") {
                        exclude group: "org.codehaus.groovy"
                    }
                    // Drivers
                    testCompile "org.seleniumhq.selenium:selenium-support:${geb.seleniumVersion}"//for <select/>
                    testCompile "org.seleniumhq.selenium:selenium-chrome-driver:${geb.seleniumVersion}"
                    testCompile "org.seleniumhq.selenium:selenium-firefox-driver:${geb.seleniumVersion}"
                }
            }
        }
    }

    def groupTasks() {
        def gebTasks = [
                chromeTest,
                chromeDockerTest,
                firefoxDockerTest,
                firefoxTest,
                unzipChromeDriver,
                downloadChromeDriver,
                stopDockerSeleniumChrome,
                stopDockerSeleniumFirefox,
                startDockerSeleniumChrome,
                startDockerSeleniumFirefox
        ]
        gebTasks*.group = 'geb'
    }

}
