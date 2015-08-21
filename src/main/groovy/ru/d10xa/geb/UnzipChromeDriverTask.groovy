package ru.d10xa.geb

import org.gradle.api.tasks.Copy

class UnzipChromeDriverTask extends Copy {

    static final String NAME = 'unzipChromeDriver'

    UnzipChromeDriverTask() {
        def singleFile = project.tasks.getByName(DownloadChromeDriverTask.NAME).outputs.files.singleFile
        from(project.zipTree(singleFile))
        into(new File("${project.buildDir}/webdriver/chromedriver"))
    }

}