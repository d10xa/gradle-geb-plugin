package ru.d10xa.geb

import geb.Browser
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask

@CompileStatic
class GebTask extends DefaultTask {

    public Browser drive(Closure closure) {
        GebExtension extension = project.extensions.getByName(GebExtension.NAME) as GebExtension
        Browser browser = extension.browser
        closure.delegate = browser
        closure.call()
        browser
    }

}