package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

@Database(
    entities = [
        CheckinEntity::class,
        UrgeEpisodeEntity::class,
        ThoughtEntity::class,
        RelationshipAuditEntity::class,
        IdealizationEntity::class,
        UnsentLetterEntity::class,
        MemoryBankEntity::class,
        ExperimentEntity::class,
        IdentityGoalEntity::class,
        RelapseEntity::class,
        TriggerEventEntity::class,
        RedFlagEntity::class,
        PeerSupportPostEntity::class,
        ThoughtLabEntity::class,
        AiMessageEntity::class,
        JournalEntryEntity::class,
        SoltarSettingsEntity::class,
        TimeCapsuleEntity::class,
        WisdomContributionEntity::class,
        RiskDateEntity::class,
        BeginnerLetterEntity::class,
        FavoriteWisdomCardEntity::class,
        UserPersistentMemoryEntity::class
    ],
    version = 32,
    exportSchema = false
)
abstract class SoltarDatabase : RoomDatabase() {
    abstract fun checkinDao(): CheckinDao
    abstract fun urgeEpisodeDao(): UrgeEpisodeDao
    abstract fun thoughtDao(): ThoughtDao
    abstract fun relationshipAuditDao(): RelationshipAuditDao
    abstract fun idealizationDao(): IdealizationDao
    abstract fun unsentLetterDao(): UnsentLetterDao
    abstract fun memoryBankDao(): MemoryBankDao
    abstract fun experimentDao(): ExperimentDao
    abstract fun identityGoalDao(): IdentityGoalDao
    abstract fun relapseDao(): RelapseDao
    abstract fun triggerEventDao(): TriggerEventDao
    abstract fun redFlagDao(): RedFlagDao
    abstract fun peerSupportDao(): PeerSupportDao
    abstract fun thoughtLabDao(): ThoughtLabDao
    abstract fun aiMessageDao(): AiMessageDao
    abstract fun journalDao(): JournalDao
    abstract fun soltarSettingsDao(): SoltarSettingsDao
    abstract fun timeCapsuleDao(): TimeCapsuleDao
    abstract fun wisdomContributionDao(): WisdomContributionDao
    abstract fun riskDateDao(): RiskDateDao
    abstract fun beginnerLetterDao(): BeginnerLetterDao
    abstract fun favoriteWisdomCardDao(): FavoriteWisdomCardDao
    abstract fun userPersistentMemoryDao(): UserPersistentMemoryDao

    companion object {
        val MIGRATION_30_31 = object : Migration(30, 31) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE soltar_settings ADD COLUMN exPartnerName TEXT NOT NULL DEFAULT ''")
            }
        }

        @Volatile
        private var INSTANCE: SoltarDatabase? = null

        fun getDatabase(context: Context): SoltarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoltarDatabase::class.java,
                    "adriana_database"
                )
                .addMigrations(MIGRATION_30_31)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialDataIfEmpty(database: SoltarDatabase) {
            try {
                val settings = database.soltarSettingsDao().getSettingsOnce()
                if (settings == null) {
                    populateCleanData(database)
                }
            } catch (_: Exception) {
            }
        }

        suspend fun populateCleanData(database: SoltarDatabase) {
            database.soltarSettingsDao().saveSettings(
                SoltarSettingsEntity(
                    id = 1,
                    memoryEnabled = true,
                    userName = "",
                    userEmail = "",
                    pinHash = "",
                    isLoggedIn = false,
                    breakupDateTimestamp = System.currentTimeMillis(),
                    initialStartDateTimestamp = System.currentTimeMillis(),
                    initialStartDateSet = false,
                    biometricLockEnabled = false,
                    soundEnabled = true,
                    onboardingCompleted = false,
                    preferredFramework = "PSICOLOGIA_MODERNA",
                    subscriptionTier = "FREE",
                    isTrialActive = false
                )
            )
            val currentMemory = database.userPersistentMemoryDao().getMemoryOnce()
            if (currentMemory == null) {
                database.userPersistentMemoryDao().saveMemory(
                    UserPersistentMemoryEntity(
                        id = 1L,
                        userProfileSummary = "Usuario en proceso de duelo y reconstrucción tras ruptura de pareja. Enfocado en superar la abstinencia afectiva y recuperar soberanía.",
                        currentDayOfContactZero = 0,
                        mainTriggers = "Soledad nocturna, revisar redes sociales, lugares compartidos, canciones significativas"
                    )
                )
            }
        }
    }
}
