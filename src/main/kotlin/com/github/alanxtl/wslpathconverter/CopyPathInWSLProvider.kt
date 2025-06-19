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
        if (virtualFile == null || !SystemInfo.isWindows) {
            return null
        }

        val windowsPath = virtualFile.path
        val wslDistributionManager = WslDistributionManager.getInstance()

        // 1. 获取用户在设置里选择的发行版
        val settings = WslPathSettingsService.instance.state
        val selectedDistroName = settings.selectedWslDistribution

        var targetDistro: WSLDistribution? = if (selectedDistroName.isNotEmpty()) {
            wslDistributionManager.getOrCreateDistributionByMsId(selectedDistroName)
        } else {
            null
        }

        // 2. 如果用户没有选择，或者选择的发行版找不到了，尝试获取系统默认的发行版
        if (targetDistro == null) {
            targetDistro = wslDistributionManager.installedDistributions.firstOrNull()
        }

        // 3. 如果找到了目标发行版 (用户选择的或默认的)，用它来转换路径
        if (targetDistro != null) {
            try {
                // 使用官方API进行转换，这是最可靠的方式
                return targetDistro.getWslPath(Path(windowsPath))
            } catch (e: Exception) {
                // 如果API转换失败，可以记录日志，然后执行最终回退
            }
        }

        // 4. 万不得已的最后回退方案：手动拼接路径
        val drive = windowsPath.substring(0, 1).lowercase()
        val restOfPath = windowsPath.substring(3).replace("\\", "/")
        return "/mnt/$drive/$restOfPath"
    }
}