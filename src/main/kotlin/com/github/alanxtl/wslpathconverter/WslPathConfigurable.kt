package com.github.alanxtl.wslpathconverter

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.WslDistributionManager
import com.intellij.execution.wsl.ui.WslDistributionComboBox
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.options.Configurable
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.components.JBTextField
import javax.swing.JComponent
import javax.swing.JPanel


@com.intellij.openapi.components.Service(Service.Level.APP)
class WslCachedDistroService {

    @Volatile
    private var cachedSelected: WSLDistribution? = null

    @Volatile
    private var cachedDefault: WSLDistribution? = null

    fun invalidate() {
        cachedSelected = null; cachedDefault = null
    }

    fun selectedOrNull(): WSLDistribution? {
        val id = WslPathSettingsService.instance.state.selectedWslDistribution
        if (id.isBlank()) return null
        return cachedSelected ?: run {
            val d = WslDistributionManager.getInstance().getOrCreateDistributionByMsId(id)
            cachedSelected = d
            d
        }
    }

    fun defaultOrNull(): WSLDistribution? {
        return cachedDefault ?: run {
            // 注意：尽量避免在 UI 线程调用，如果担心阻塞，可不提供默认分发返回
            val d = WslDistributionManager.getInstance().installedDistributions.firstOrNull()
            cachedDefault = d
            d
        }
    }

    companion object {
        val instance: WslCachedDistroService
            get() = ApplicationManager.getApplication().getService(WslCachedDistroService::class.java)
    }
}


class WslPathConfigurable : Configurable {

    private lateinit var mainPanel: JPanel

    // 直接使用官方的 WslDistributionComboBox 组件
    private lateinit var wslDistributionComboBox: WslDistributionComboBox
    private lateinit var mountRootTextField: JBTextField

    override fun getDisplayName(): String = MyBundle.message("settings.displayName")

    override fun createComponent(): JComponent {
        // 初始化官方组件，它会自己处理所有加载逻辑
        wslDistributionComboBox = WslDistributionComboBox(null, true)
        mountRootTextField = JBTextField()

        mainPanel = panel {
            row(MyBundle.message("settings.wslDistribution.label")) {
                cell(wslDistributionComboBox)
            }
            row(MyBundle.message("settings.mountRoot.label")) {
                cell(mountRootTextField)
            }
            row {
                comment(MyBundle.message("settings.mountRoot.examples"))
            }
        }
        return mainPanel
    }

    override fun isModified(): Boolean {
        val settings = WslPathSettingsService.instance.state
        // 从组件获取当前选中的发行版，然后获取其ID。注意处理null的情况。
        val selectedId = wslDistributionComboBox.selected?.msId ?: ""
        val mountRoot = WslPathConverter.normalizeMountRoot(mountRootTextField.text)
        return selectedId != settings.selectedWslDistribution || mountRoot != settings.mountRoot
    }

    override fun apply() {
        val settingsService = WslPathSettingsService.instance
        val cached = WslCachedDistroService.instance
        settingsService.state.selectedWslDistribution = wslDistributionComboBox.selected?.msId ?: ""
        settingsService.state.mountRoot = WslPathConverter.normalizeMountRoot(mountRootTextField.text)
        mountRootTextField.text = settingsService.state.mountRoot
        cached.invalidate()
    }

    override fun reset() {
        val settings = WslPathSettingsService.instance.state
        val savedDistroName = settings.selectedWslDistribution
        mountRootTextField.text = WslPathConverter.normalizeMountRoot(settings.mountRoot)
        if (savedDistroName.isNotEmpty()) {
            // 从 WslDistributionManager 找到对应的发行版对象
            val distribution = WslDistributionManager.getInstance().getOrCreateDistributionByMsId(savedDistroName)
            // 在下拉框中选中它
            wslDistributionComboBox.selected = distribution
        } else {
            // 如果没有保存过设置，则不选中任何项或保持默认
            wslDistributionComboBox.selected = null
        }
    }

}
