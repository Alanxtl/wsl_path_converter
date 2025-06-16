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

        val presentableUrl = virtualFile.path
        val path = presentableUrl.substring(0, 2).lowercase() + presentableUrl.substring(2)

        try {
            // 1. 获取用户保存的设置
            val wslDistributionManager = WslDistributionManager.getInstance()
            val settings = WslPathSettingsService.instance.state
            val selectedDistroName = settings.selectedWslDistribution

            // 2. 查找用户选择的发行版
            val targetDistro: WSLDistribution? = if (selectedDistroName.isNotEmpty()) {
                wslDistributionManager.getOrCreateDistributionByMsId(selectedDistroName)
            } else {
                // 3. 如果找不到用户选择的（或用户从未设置过），则回退到默认发行版
                null
            }

            // 4. 使用目标发行版进行路径转换
            return if (targetDistro != null) {
                targetDistro.getWslPath(Path(virtualFile.path))
            } else {
                "/mnt/" + path.replace("\\", "/").replace(":", "")
            }

        } catch (e: Exception) {
            return "/mnt/" + path.replace("\\", "/").replace(":", "")
        }
    }
}