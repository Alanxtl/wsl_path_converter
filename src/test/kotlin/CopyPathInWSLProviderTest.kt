package com.github.alanxtl.wslpathconverter

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CopyPathInWSLProviderTest : BasePlatformTestCase() {

    private fun vfWithPath(path: String): VirtualFile {
        // VirtualFile 是抽象类，用 Mockito 直接桩掉 path / fileSystem 即可
        val fs = mock<com.intellij.openapi.vfs.VirtualFileSystem>()
        return mock {
            on { this.path } doReturn path
            on { fileSystem } doReturn fs
        }
    }

    fun testNullVirtualFile() {
        val provider = CopyPathInWSLProvider()
        val result = provider.getPathToElement(project, null, null as Editor?)
        assertNull(result)
    }

    fun testFastPathBackslash() {
        if (!SystemInfo.isWindows) return
        val provider = CopyPathInWSLProvider()
        val vf = vfWithPath("C:\\Users\\Alice\\projects\\demo.txt")
        val result = provider.getPathToElement(project, vf, null)
        assertEquals("/mnt/c/Users/Alice/projects/demo.txt", result)
    }

    fun testFastPathSlash() {
        if (!SystemInfo.isWindows) return
        val provider = CopyPathInWSLProvider()
        val vf = vfWithPath("D:/dev/code/src/main.kt")
        val result = provider.getPathToElement(project, vf, null)
        assertEquals("/mnt/d/dev/code/src/main.kt", result)
    }

    fun testAlreadyMnt() {
        if (!SystemInfo.isWindows) return
        val provider = CopyPathInWSLProvider()
        val already = "/mnt/e/work/logs/app.log"
        val vf = vfWithPath(already)
        val result = provider.getPathToElement(project, vf, null)
        assertEquals(already, result)
    }

    fun testWslUnc() {
        if (!SystemInfo.isWindows) return
        val provider = CopyPathInWSLProvider()
        val unc = "\\\\wsl$\\Ubuntu\\home\\alice\\app.log"
        val vf = vfWithPath(unc)
        val result = provider.getPathToElement(project, vf, null)
        assertEquals(unc, result)
    }
}
