package com.github.alanxtl.wslpathconverter

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

// 定义需要存储的数据结构
data class WslPathState(
    var selectedWslDistribution: String = "" // 存储用户选择的WSL发行版名称
)

// 使用@State注解来定义存储的位置和名称
@State(
    name = "com.github.alanxtl.wslpath.settings.WslPathSettingsService",
    storages = [Storage("WslPathSettings.xml")]
)
class WslPathSettingsService : PersistentStateComponent<WslPathState> {

    private var myState = WslPathState()

    // 获取当前状态
    override fun getState(): WslPathState {
        return myState
    }

    // 从XML文件加载状态
    override fun loadState(state: WslPathState) {
        myState = state
    }

    companion object {
        // 提供一个方便的静态方法来获取服务实例
        val instance: WslPathSettingsService
            get() = ApplicationManager.getApplication().getService(WslPathSettingsService::class.java)
    }
}