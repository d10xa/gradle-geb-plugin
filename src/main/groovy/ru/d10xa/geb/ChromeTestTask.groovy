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
            systemProperty "geb.env", ChromeConfig.PROPERTY_GEB_CLASSNAME

            systemProperty "geb.driver", ChromeConfig.PROPERTY_DRIVER_CLASSNAME
            systemProperty "webdriver.chrome.driver", new ChromeConfig(project: project).driverPath
        }
    }

}
