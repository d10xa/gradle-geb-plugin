# gradle-geb-plugin

[ ![Download](https://api.bintray.com/packages/d10xa/maven/ru.d10xa%3Agradle-geb-plugin/images/download.svg) ](https://bintray.com/d10xa/maven/ru.d10xa%3Agradle-geb-plugin/_latestVersion)

```groovy
buildscript {
    repositories {
        maven { url 'https://dl.bintray.com/d10xa/maven' }
        jcenter()
    }
    dependencies {
        classpath "ru.d10xa:gradle-geb-plugin:1.0.3"
    }
}
apply plugin: 'groovy'
apply plugin: 'ru.d10xa.geb'

repositories {
    jcenter()
}
```

[read more at d10xa.ru](http://d10xa.ru/2015/09/geb-getting-started/)
