package com.github.alanxtl.wslpathconverter

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.ide.actions.DumbAwareCopyPathProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile


import com.intellij.execution.wsl.WslDistributionManager
import kotlin.io.path.Path


class CopyPathInWSLProvider : DumbAwareCopyPathProvider() {

    override fun getPathToElement(project: Project, virtualFile: VirtualFile?, editor: Editor?): String? {
        if (virtualFile == null || !SystemInfo.isWindows) return null

        val p = virtualFile.path
        // 先快速转换（不触发任何昂贵调用）
        fastConvertWinToWsl(p)?.let { return it }

        // 已经是 \\wsl$ 或 /mnt/* 的路径就直接返回（避免重复转换）
        if (p.startsWith("\\\\wsl$\\") || p.startsWith("/mnt/")) return p

        // 如必须使用发行版做精确转换，尽量只使用缓存；如果缓存为空，直接走回退而不是去枚举系统发行版
        val distro = WslCachedDistroService.instance.selectedOrNull()
            ?: WslCachedDistroService.instance.defaultOrNull()

        return try {
            // 这里依然可能代价高，但由于我们不再为了“找一个默认发行版”去枚举，
            // 而是仅当缓存已建立时才用它，实际触发概率会极低。
            if (distro != null) distro.getWslPath(Path(p)) else fastFallback(p)
        } catch (_: Exception) {
            fastFallback(p)
        }
    }

    private fun fastFallback(windowsPath: String): String {
        // 与 fastConvert 保持一致（这里假定之前 fastConvert 失败，仍兜个底）
        return fastConvertWinToWsl(windowsPath) ?: windowsPath
    }

    private fun fastConvertWinToWsl(windowsPath: String): String? {
        // 典型的 "C:\Users\..." 或 "C:/Users/..." -> "/mnt/c/Users/..."
        // VirtualFile.path 在 Windows 通常使用 '/', 但也容忍 '\'
        val regex = Regex("""^([A-Za-z]):[\\/](.*)$""")
        val m = regex.find(windowsPath) ?: return null
        val drive = m.groupValues[1].lowercase()
        val rest = m.groupValues[2].replace('\\', '/')
        return "/mnt/$drive/$rest"
    }

}