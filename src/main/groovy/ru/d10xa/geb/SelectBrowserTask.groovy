package ru.d10xa.geb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SelectBrowserTask extends DefaultTask {

    private final String browser

    SelectBrowserTask(String browser) {
        this.outputs.upToDateWhen { false }
        this.finalizedBy 'test'
        this.browser = browser
    }

    @TaskAction
    void executeAction() {
        this.project
                .extensions.getByType(GebExtension)
                .defaultTestBrowser = this.browser
    }

    static class SelectFirefoxTask extends SelectBrowserTask {
        SelectFirefoxTask() { super("firefox") }
    }

    static class SelectChromeTask extends SelectBrowserTask {
        SelectChromeTask() { super("chrome") }
    }
}
