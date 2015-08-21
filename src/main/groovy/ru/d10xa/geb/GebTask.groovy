package ru.d10xa.geb

import geb.Browser
import org.gradle.api.DefaultTask

class GebTask extends DefaultTask {

    public Browser drive(Closure closure) {
        Browser browser = project.extensions.getByName(GebExtension.NAME).browser
        closure.delegate = browser
        closure.call()
        browser
    }

}
