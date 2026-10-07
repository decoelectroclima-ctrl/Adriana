package com.example

import android.content.Context
import android.content.Intent
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AdrianaDatabase
import com.example.data.SoltarSettingsEntity
import com.example.notifications.NotificationScheduler
import com.example.notifications.SoltarAlarmReceiver
import com.example.notifications.SoltarNotificationHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NotificationSchedulerTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testCalculateNextTrigger_sameDayWhenLater() {
        // Fijamos un tiempo base: 10:00 AM del día actual
        val baseCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val now = baseCal.timeInMillis

        // Queremos programar para las 21:00 (mismo día más tarde)
        val trigger = NotificationScheduler.calculateNextTriggerMillis(21, 0, now)

        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }
        assertEquals(baseCal.get(Calendar.DAY_OF_YEAR), triggerCal.get(Calendar.DAY_OF_YEAR))
        assertEquals(21, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, triggerCal.get(Calendar.MINUTE))
        assertTrue(trigger > now)
    }

    @Test
    fun testCalculateNextTrigger_nextDayWhenEarlierOrEqual() {
        // Fijamos un tiempo base: 21:30 PM
        val baseCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val now = baseCal.timeInMillis

        // Queremos programar para las 21:00 (hora que ya pasó hoy)
        val trigger = NotificationScheduler.calculateNextTriggerMillis(21, 0, now)

        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }
        val expectedDay = baseCal.apply { add(Calendar.DAY_OF_YEAR, 1) }.get(Calendar.DAY_OF_YEAR)

        assertEquals(expectedDay, triggerCal.get(Calendar.DAY_OF_YEAR))
        assertEquals(21, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, triggerCal.get(Calendar.MINUTE))
        assertTrue(trigger > now)
    }

    @Test
    fun testCalculateNextTrigger_midnightHandling() {
        // Fijamos las 23:59 del 31 de diciembre
        val baseCal = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val now = baseCal.timeInMillis

        // Programar para las 08:00 de la mañana
        val trigger = NotificationScheduler.calculateNextTriggerMillis(8, 0, now)

        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }
        // Debe ser 1 de enero del año siguiente
        assertEquals(Calendar.JANUARY, triggerCal.get(Calendar.MONTH))
        assertEquals(1, triggerCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(8, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertTrue(trigger > now)
    }

    @Test
    fun testCalculateNextTrigger_monthTransition() {
        // Fijamos 28 de febrero a las 22:00
        val baseCal = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DAY_OF_MONTH, 28)
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val now = baseCal.timeInMillis

        // Programar a las 20:00 (ya pasó)
        val trigger = NotificationScheduler.calculateNextTriggerMillis(20, 0, now)
        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }

        assertTrue(trigger > now)
        assertEquals(20, triggerCal.get(Calendar.HOUR_OF_DAY))
    }

    @Test
    fun testReceiverExecutesSafelyWithRoomDataAndNoViewModel() {
        // Verificamos que el receiver procesa el intent con datos en Room sin fallar
        // y sin ninguna dependencia en ViewModels o servicios de IA pesados.
        val db = Room.inMemoryDatabaseBuilder(context, AdrianaDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        runBlocking {
            db.soltarSettingsDao().saveSettings(
                SoltarSettingsEntity(
                    id = 1,
                    userName = "TestUser",
                    notificationsEnabled = true,
                    reminderHour = 21,
                    reminderMinute = 0
                )
            )
        }

        val receiver = SoltarAlarmReceiver()
        val intent = Intent(NotificationScheduler.ACTION_DAILY_REMINDER)

        // No debe lanzar ninguna excepción
        receiver.onReceive(context, intent)

        db.close()
    }

    @Test
    fun testContinuousProtectionToggleAndPreferences() {
        assertFalse(NotificationScheduler.isContinuousProtectionEnabled(context))

        NotificationScheduler.setContinuousProtectionEnabled(context, true)
        assertTrue(NotificationScheduler.isContinuousProtectionEnabled(context))

        NotificationScheduler.setContinuousProtectionEnabled(context, false)
        assertFalse(NotificationScheduler.isContinuousProtectionEnabled(context))
    }
}
