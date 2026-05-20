package com.github.alanxtl.wslpathconverter

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import junit.framework.TestCase

class WslCachedDistroServiceTest : TestCase() {

    fun testCachedDistroServiceDoesNotPersistSettings() {
        assertFalse(PersistentStateComponent::class.java.isAssignableFrom(WslCachedDistroService::class.java))
        assertNull(WslCachedDistroService::class.java.getAnnotation(State::class.java))
    }
}
