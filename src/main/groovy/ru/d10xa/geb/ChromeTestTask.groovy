package ru.d10xa.geb

import org.gradle.api.tasks.testing.Test

class ChromeTestTask extends Test {

    static final String NAME = 'chromeTest'

    ChromeTestTask() {
        outputs.upToDateWhen { false }
        project.with {
            reports {
                html.destination = reporting.file("$name/tests")
                junitXml.destination = new File("$buildDir/test-results/$name")
            }
            systemProperty "geb.build.reportsDir", reporting.file("$name/geb")
            systemProperty "geb.env", 'chrome'

            systemProperty "geb.driver", ChromeConfig.GEB_DRIVER
        }
    }

    @Override
    void executeTests() {
        systemProperty "webdriver.chrome.driver", new ChromeConfig(project: project).driverPath
        super.executeTests()
    }

}
