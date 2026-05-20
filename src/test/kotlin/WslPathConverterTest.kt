package com.github.alanxtl.wslpathconverter

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase

class WslPathConverterTest : TestCase() {

    fun testConvertsWindowsPathWithBackslashes() {
        assertEquals(
            "/mnt/c/Users/Alice/projects/demo.txt",
            WslPathConverter.fastConvertWinToWsl("C:\\Users\\Alice\\projects\\demo.txt", "/mnt"),
        )
    }

    fun testConvertsWindowsPathWithSlashes() {
        assertEquals(
            "/mnt/d/dev/code/src/main.kt",
            WslPathConverter.fastConvertWinToWsl("D:/dev/code/src/main.kt", "/mnt"),
        )
    }

    fun testLeavesNonDrivePathUnconverted() {
        assertNull(WslPathConverter.fastConvertWinToWsl("/home/alice/project", "/mnt"))
    }

    fun testConvertsWithCustomMountRoot() {
        assertEquals("/c/Users/a.txt", WslPathConverter.fastConvertWinToWsl("C:\\Users\\a.txt", "/"))
        assertEquals("/wsl/c/Users/a.txt", WslPathConverter.fastConvertWinToWsl("C:\\Users\\a.txt", "/wsl"))
        assertEquals("/wsl/c/Users/a.txt", WslPathConverter.fastConvertWinToWsl("C:\\Users\\a.txt", "wsl/"))
    }

    fun testNormalizeMountRoot() {
        assertEquals("/mnt", WslPathConverter.normalizeMountRoot(""))
        assertEquals("/mnt", WslPathConverter.normalizeMountRoot("mnt"))
        assertEquals("/mnt", WslPathConverter.normalizeMountRoot("/mnt/"))
        assertEquals("/", WslPathConverter.normalizeMountRoot("/"))
        assertEquals("/wsl", WslPathConverter.normalizeMountRoot("//wsl//"))
    }

    fun testPassthroughRecognizesWslPaths() {
        assertEquals("/mnt/e/work/logs/app.log", WslPathConverter.passthroughIfAlreadyWsl("/mnt/e/work/logs/app.log"))
        assertEquals(
            "\\\\wsl$\\Ubuntu\\home\\alice\\app.log",
            WslPathConverter.passthroughIfAlreadyWsl("\\\\wsl$\\Ubuntu\\home\\alice\\app.log"),
        )
    }

    fun testShouldNotifyOnlyWhenFallbackDidNotConvertPath() {
        assertEquals(
            true,
            WslPathConverter.shouldNotifyFallbackFailure(
                originalPath = "relative/path.txt",
                fallbackPath = "relative/path.txt",
            ),
        )
        assertEquals(
            false,
            WslPathConverter.shouldNotifyFallbackFailure(
                originalPath = "C:\\Users\\Alice\\demo.txt",
                fallbackPath = "/mnt/c/Users/Alice/demo.txt",
            ),
        )
    }

    fun testOnlyWindowsIsSupported() {
        assertEquals(true, WslPathConverter.isSupportedOperatingSystem(isWindows = true))
        assertEquals(false, WslPathConverter.isSupportedOperatingSystem(isWindows = false))
    }
}
