package ru.d10xa.geb

import groovy.transform.CompileStatic

@CompileStatic
class OsUtils {

    static boolean isMacOs() {
        String osString = System.getProperty('os.name').toLowerCase()
        osString.contains("mac os x") || osString.contains("darwin")
    }

    static boolean isWindows() {
        System.properties['os.name'] ==~ '[W|w]indows.*'
    }

    static boolean is32BitLinux() {
        System.properties['os.arch'] ==~ '.*x86.*'
    }

    static boolean is64BitLinux() {
        System.properties['os.arch'] ==~ '.*64.*'
    }

}