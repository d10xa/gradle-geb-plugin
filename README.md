# gradle-geb-plugin

[![Download](https://api.bintray.com/packages/d10xa/maven/ru.d10xa%3Agradle-geb-plugin/images/download.svg) ](https://bintray.com/d10xa/maven/ru.d10xa%3Agradle-geb-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/d10xa/gradle-geb-plugin.svg?branch=master)](https://travis-ci.org/d10xa/gradle-geb-plugin)
[![Build Status](https://snap-ci.com/d10xa/gradle-geb-plugin/branch/master/build_image)](https://snap-ci.com/d10xa/gradle-geb-plugin/branch/master)

```groovy
buildscript {
    repositories {
        maven { url 'https://dl.bintray.com/d10xa/maven' }
        jcenter()
    }
    dependencies {
        classpath "ru.d10xa:gradle-geb-plugin:1.0.5"
    }
}
apply plugin: 'groovy'
apply plugin: 'ru.d10xa.geb'

repositories {
    jcenter()
}

// Optional
geb {
    chromeDriverVersion = '2.22'
    groovyVersion = '2.4.7'
    gebVersion = '0.13.1'
    seleniumVersion = '2.53.1'
    defaultTestBrowser = 'firefox'
//  defaultTestBrowser = 'chrome'
}
```

[read more at d10xa.ru](http://d10xa.ru/2015/09/geb-getting-started)
