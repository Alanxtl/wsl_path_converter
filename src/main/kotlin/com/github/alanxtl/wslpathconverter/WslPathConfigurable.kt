package com.github.alanxtl.wslpathconverter

import com.intellij.execution.wsl.ui.WslDistributionComboBox
import com.intellij.execution.wsl.WslDistributionManager

import com.intellij.openapi.options.Configurable
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import javax.swing.JPanel


class WslPathConfigurable : Configurable {

    private lateinit var mainPanel: JPanel
    // 直接使用官方的 WslDistributionComboBox 组件
    private lateinit var wslDistributionComboBox: WslDistributionComboBox

    override fun getDisplayName(): String = "WSL Path Converter Settings"

    override fun createComponent(): JComponent {
        // 初始化官方组件，它会自己处理所有加载逻辑
        wslDistributionComboBox = WslDistributionComboBox(null, true)

        mainPanel = panel {
            row("Select WSL Distribution:") {
                cell(wslDistributionComboBox)
            }
        }
        return mainPanel
    }

    override fun isModified(): Boolean {
        val settings = WslPathSettingsService.instance.state
        // 从组件获取当前选中的发行版，然后获取其ID。注意处理null的情况。
        val selectedId = wslDistributionComboBox.selected?.msId ?: ""
        return selectedId != settings.selectedWslDistribution
    }

    override fun apply() {
        val settings = WslPathSettingsService.instance.state
        // 保存当前选中发行版的ID。
        settings.selectedWslDistribution = wslDistributionComboBox.selected?.msId ?: ""
    }
    override fun reset() {
        val settings = WslPathSettingsService.instance.state
        val savedDistroName = settings.selectedWslDistribution
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