package ru.d10xa.geb

import groovy.transform.ToString
import org.gradle.api.Project

@ToString
class GebExtension {

    static final NAME = 'geb'

    Project project

    def chromeDriverVersion = '2.10'
    def groovyVersion = '2.4.3'
    def gebVersion = '0.10.0'
    def seleniumVersion = '2.46.0'
    def phantomJsVersion = '1.9.8'

    GebExtension(Project project) {
        this.project = project
    }

}
