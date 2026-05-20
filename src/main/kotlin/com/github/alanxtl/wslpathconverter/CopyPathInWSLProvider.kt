package com.github.alanxtl.wslpathconverter


import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.ide.actions.DumbAwareCopyPathProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import kotlin.io.path.Path


class CopyPathInWSLProvider : DumbAwareCopyPathProvider() {

    override fun getPathToElement(project: Project, virtualFile: VirtualFile?, editor: Editor?): String? {
        if (virtualFile == null || !WslPathConverter.isSupportedOperatingSystem(SystemInfo.isWindows)) return null

        val p = virtualFile.path
        val mountRoot = WslPathSettingsService.instance.state.mountRoot
        // 先快速转换（不触发任何昂贵调用）
        WslPathConverter.fastConvertWinToWsl(p, mountRoot)?.let { return it }

        // 已经是 \\wsl$ 或 /mnt/* 的路径就直接返回（避免重复转换）
        WslPathConverter.passthroughIfAlreadyWsl(p)?.let { return it }

        // 如必须使用发行版做精确转换，尽量只使用缓存；如果缓存为空，直接走回退而不是去枚举系统发行版
        val distro = WslCachedDistroService.instance.selectedOrNull()
            ?: WslCachedDistroService.instance.defaultOrNull()

        return try {
            // 这里依然可能代价高，但由于我们不再为了“找一个默认发行版”去枚举，
            // 而是仅当缓存已建立时才用它，实际触发概率会极低。
            if (distro != null) distro.getWslPath(Path(p)) else fallbackWithNotification(project, p)
        } catch (_: Exception) {
            fallbackWithNotification(project, p)
        }
    }

    private fun fallbackWithNotification(project: Project, path: String): String {
        val fallback = WslPathConverter.fastFallback(path)
        if (WslPathConverter.shouldNotifyFallbackFailure(path, fallback)) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("WSL Path Converter")
                .createNotification("Could not convert path to WSL format: $path", NotificationType.WARNING)
                .notify(project)
        }
        return fallback
    }

}
