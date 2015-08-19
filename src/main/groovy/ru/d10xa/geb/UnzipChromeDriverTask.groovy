package ru.d10xa.geb

import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction

class UnzipChromeDriverTask extends Copy {

    static final String NAME = 'unzipChromeDriver'

    @TaskAction
    void run() {
        def outputDir = new File("${project.buildDir}/webdriver/chromedriver")
        outputs.dir(outputDir)
        from(project.zipTree(project.task('downloadChromeDriver').outputs.files.singleFile))
        into(outputDir)
    }

}