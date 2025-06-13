package com.github.alanxtl.wslpathconverter

import com.intellij.ide.actions.DumbAwareCopyPathProvider
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import java.util.concurrent.TimeUnit

class CopyPathInWSLProvider : DumbAwareCopyPathProvider() {

    /**
     * 指定该 Action 的 update 方法在后台线程（BGT）执行，避免阻塞 UI。
     * 这是 IntelliJ Platform 2022.3 及以后版本推荐的做法。
     */
//    override fun getActionUpdateThread(): ActionUpdateThread {
//        return ActionUpdateThread.BGT
//    }

    override fun getPathToElement(project: Project, virtualFile: VirtualFile?, editor: Editor?): String? {
        // 首先，确保 virtualFile 存在且当前系统是 Windows
        if (virtualFile == null || !SystemInfo.isWindows) {
            return null
        }

        // 使用 virtualFile.path 获取标准的、系统相关的绝对路径。
        // 这比 presentableUrl 更适合用于程序处理。
        val windowsPath = virtualFile.path

        // 调用 wsl.exe 的 wslpath 工具来转换路径。
        // 会遵循用户自己的 WSL 配置（例如不同的挂载点）。
        // 命令: wsl wslpath -a 'C:/Users/user/file.txt'
        try {
            val process = ProcessBuilder("wsl.exe", "wslpath", "-a", windowsPath).start()
            // 等待命令执行完成，设置一个超时时间（例如 3 秒）
            if (process.waitFor(3, TimeUnit.SECONDS)) {
                // 读取命令的输出
                val wslPath = process.inputStream.bufferedReader().readLine()
                if (process.exitValue() == 0 && !wslPath.isNullOrBlank()) {
                    return wslPath
                }
            } else {
                // 如果超时，销毁进程
                process.destroy()
            }
        } catch (e: IOException) {
//             e.printStackTrace() // 仅用于调试
        }

        // 如果调用 wsl.exe 失败，返回规则转换
        return "/mnt/" + virtualFile.path.replace("\\", "/").replace(":", "")
    }
}