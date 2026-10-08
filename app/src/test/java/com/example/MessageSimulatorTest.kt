package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.SoltarDatabase
import com.example.data.SoltarSettingsEntity
import com.example.ui.SoltarViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MessageSimulatorTest {

    private lateinit var application: Application
    private lateinit var viewModel: SoltarViewModel

    @Before
    fun setup() = runBlocking {
        application = ApplicationProvider.getApplicationContext()
        val db = SoltarDatabase.getDatabase(application)
        db.soltarSettingsDao().saveSettings(
            SoltarSettingsEntity(
                id = 1,
                userName = "UsuarioTest",
                exPartnerName = "Carlos",
                onboardingCompleted = true
            )
        )
        viewModel = SoltarViewModel(application)
        ShadowLooper.idleMainLooper()
    }

    @Test
    fun testOpenAndCloseMessageSimulator() {
        assertFalse(viewModel.uiState.value.isMessageSimulatorVisible)

        viewModel.openMessageSimulator()
        assertTrue(viewModel.uiState.value.isMessageSimulatorVisible)

        viewModel.closeMessageSimulator()
        assertFalse(viewModel.uiState.value.isMessageSimulatorVisible)
    }

    @Test
    fun testOpenPriorityToolRoutesToMessageSimulator() {
        assertFalse(viewModel.uiState.value.isMessageSimulatorVisible)

        viewModel.openPriorityTool("Simulacro de Mensaje")
        assertTrue(viewModel.uiState.value.isMessageSimulatorVisible)
    }

    @Test
    fun testEncounterSimulatorIsDeactivated() {
        viewModel.toggleEncounterSimulator(true)
        assertFalse(viewModel.uiState.value.isEncounterSimulatorVisible)
    }
}
