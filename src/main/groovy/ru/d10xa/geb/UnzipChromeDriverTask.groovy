package ru.d10xa.geb

import groovy.transform.CompileStatic
import org.gradle.api.tasks.Copy

@CompileStatic
class UnzipChromeDriverTask extends Copy {

    static final String NAME = 'unzipChromeDriver'

    UnzipChromeDriverTask() {
        File singleFile = project.tasks.getByName(DownloadChromeDriverTask.NAME).outputs.files.singleFile
        from(project.zipTree(singleFile))
        into(new File("${project.buildDir}/webdriver/chromedriver"))
    }

}
