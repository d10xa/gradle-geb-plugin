package ru.d10xa.geb

import groovy.transform.CompileStatic
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory

@CompileStatic
class UnzipChromeDriverTask extends Copy {

    static final String NAME = 'unzipChromeDriver'

    UnzipChromeDriverTask() {
        from({ project.zipTree(chromeDriverArchive) })
        into({ project.file(chromeDriverExtractToDirectory) })
    }

    @OutputDirectory
    def getChromeDriverExtractToDirectory() {
        project.file(
                "${project.buildDir}/webdriver/chromedriver${geb.chromeDriverVersionWithUnderscores}")
    }

    @Input
    def getChromeDriverArchive() {
        project.tasks.getByName(DownloadChromeDriverTask.NAME).outputs.files.singleFile
    }

    private GebExtension getGeb() {
        project.extensions.getByType(GebExtension)
    }

}
