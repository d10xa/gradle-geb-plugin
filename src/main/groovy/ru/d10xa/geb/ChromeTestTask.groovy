package ru.d10xa.geb

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.testing.Test

class ChromeTestTask extends Test {

    static final String NAME = 'chromeTest'

    ChromeTestTask() {
        outputs.upToDateWhen { false }  // Always run tests
    }

    @TaskAction
    void run() {
        project.with {
            reports {
                html.destination = reporting.file("$name/tests")
                junitXml.destination = file("$buildDir/test-results/$name")
            }
            systemProperty "geb.build.reportsDir", reporting.file("$name/geb")
            systemProperty "geb.env", 'chrome'
        }
    }

}
