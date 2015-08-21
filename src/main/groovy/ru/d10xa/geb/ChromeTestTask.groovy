package ru.d10xa.geb

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.tasks.testing.Test

class ChromeTestTask extends Test {

    static final String NAME = 'chromeTest'
    static final String CHROME_DRIVER_CLASSNAME = 'org.openqa.selenium.chrome.ChromeDriver'

    ChromeTestTask() {
        outputs.upToDateWhen { false }

        project.with {
            reports {
                html.destination = reporting.file("$name/tests")
                junitXml.destination = new File("$buildDir/test-results/$name")
            }
            systemProperty "geb.build.reportsDir", reporting.file("$name/geb")
            systemProperty "geb.env", 'chrome'

            systemProperty "geb.driver", CHROME_DRIVER_CLASSNAME
            systemProperty "webdriver.chrome.driver", driverPath()
        }
    }

    String driverPath() {
        def chromedriverFilename = Os.isFamily(Os.FAMILY_WINDOWS) ? "chromedriver.exe" : "chromedriver"
        def files = project.tasks.getByName('unzipChromeDriver').outputs.files
        def file = new File(files.singleFile, chromedriverFilename)
        def path = file.absolutePath
        path
    }

}
