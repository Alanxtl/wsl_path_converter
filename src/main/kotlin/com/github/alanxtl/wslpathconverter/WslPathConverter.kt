package com.github.alanxtl.wslpathconverter

object WslPathConverter {

    fun isSupportedOperatingSystem(isWindows: Boolean): Boolean {
        return isWindows
    }

    fun fastConvertWinToWsl(windowsPath: String, mountRoot: String = "/mnt"): String? {
        val match = WINDOWS_DRIVE_PATH.find(windowsPath) ?: return null
        val drive = match.groupValues[1].lowercase()
        val rest = match.groupValues[2].replace('\\', '/')
        val root = normalizeMountRoot(mountRoot)
        return if (root == "/") "/$drive/$rest" else "$root/$drive/$rest"
    }

    fun normalizeMountRoot(mountRoot: String): String {
        val trimmed = mountRoot.trim()
        if (trimmed.isBlank()) return "/mnt"
        val normalized = trimmed
            .replace('\\', '/')
            .replace(Regex("/+"), "/")
            .trimEnd('/')
            .let { if (it.startsWith('/')) it else "/$it" }
        return normalized.ifBlank { "/" }
    }

    fun passthroughIfAlreadyWsl(path: String): String? {
        return when {
            path.startsWith("\\\\wsl$\\") -> path
            path.startsWith("/mnt/") -> path
            else -> null
        }
    }

    fun fastFallback(windowsPath: String): String {
        return fastConvertWinToWsl(windowsPath) ?: windowsPath
    }

    fun shouldNotifyFallbackFailure(originalPath: String, fallbackPath: String): Boolean {
        return originalPath == fallbackPath
    }

    private val WINDOWS_DRIVE_PATH = Regex("""^([A-Za-z]):[\\/](.*)$""")
}
