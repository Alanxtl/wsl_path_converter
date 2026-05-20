package com.github.alanxtl.wslpathconverter

import junit.framework.TestCase

class WslPathSettingsServiceTest : TestCase() {

    fun testLoadStateKeepsSelectedDistributionId() {
        val service = WslPathSettingsService()

        service.loadState(WslPathState(selectedWslDistribution = "Ubuntu-24.04", mountRoot = "/wsl"))

        assertEquals("Ubuntu-24.04", service.state.selectedWslDistribution)
        assertEquals("/wsl", service.state.mountRoot)
    }
}
