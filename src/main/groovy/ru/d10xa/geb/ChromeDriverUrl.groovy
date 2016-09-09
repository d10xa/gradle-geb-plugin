package ru.d10xa.geb

import static ru.d10xa.geb.OsUtils.*

class ChromeDriverUrl {

    final static String DRIVER_BASE_URL = "http://chromedriver.storage.googleapis.com"

    String chromeDriverVersion

    Os os = getCurrentOs()

    @Override
    String toString() {
        "$DRIVER_BASE_URL/$key"
    }

    public String getKey(){
        "${chromeDriverVersion}/chromedriver_${driverOsFilenamePart}.zip"
    }

    private String getDriverOsFilenamePart() {
        def split = chromeDriverVersion.split("\\.")
        assert split.size() == 2

        String driverOsFilenamePart
        switch (os) {
            case Os.mac:
                if ((split[1] as Integer) >= 23) {
                    driverOsFilenamePart = "mac64"
                } else {
                    driverOsFilenamePart = "mac32"
                }
                break
            case Os.windows:
                driverOsFilenamePart = "win32"
                break
            case Os.linux32:
                driverOsFilenamePart = "linux32"
                break
            case Os.linux64:
                driverOsFilenamePart = "linux64"
                break
        }
        return driverOsFilenamePart
    }

    static Os getCurrentOs() {
        if (isWindows()) {
            return Os.windows
        } else if (isMacOs()) {
            return Os.mac
        } else if (is32BitLinux()) {
            return Os.linux32
        } else if (is64BitLinux()) {
            return Os.linux64
        }
        throw new RuntimeException("Unable to determine operating system")
    }

    URL toURL(){
        new URL(this.toString())
    }

    static enum Os {
        mac, windows, linux32, linux64
    }
}
