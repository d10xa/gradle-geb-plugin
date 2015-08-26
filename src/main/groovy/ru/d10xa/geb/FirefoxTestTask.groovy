package ru.d10xa.geb

import org.gradle.api.tasks.testing.Test

class FirefoxTestTask extends Test {

    static final String NAME = 'firefoxTest'

    FirefoxTestTask() {
        outputs.upToDateWhen { false }

        project.with {
            reports {
                html.destination = reporting.file("$name/tests")
                junitXml.destination = new File("$buildDir/test-results/$name")
            }
            systemProperty "geb.build.reportsDir", reporting.file("$name/geb")
            systemProperty "geb.env", 'firefox'
        }
    }

}
