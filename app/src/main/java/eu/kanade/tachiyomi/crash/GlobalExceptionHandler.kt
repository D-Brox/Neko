package eu.kanade.tachiyomi.crash

import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.nekomanga.logging.TimberKt

class GlobalExceptionHandler
private constructor(
    private val applicationContext: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler,
    private val activityToBeLaunched: Class<*>,
) : Thread.UncaughtExceptionHandler {

    object ThrowableSerializer : KSerializer<Throwable> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Throwable", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Throwable =
            Throwable(message = decoder.decodeString())

        override fun serialize(encoder: Encoder, value: Throwable) =
            encoder.encodeString(value.stackTraceToString())
    }

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        try {
            TimberKt.e(exception) { "Uncaught Exception" }
            launchActivity(applicationContext, activityToBeLaunched, exception)
            exitProcess(0)
        } catch (_: Exception) {
            defaultHandler.uncaughtException(thread, exception)
        }
    }

    private fun launchActivity(
        applicationContext: Context,
        activity: Class<*>,
        exception: Throwable,
    ) {
        val intent =
            Intent(applicationContext, activity).apply {
                putExtra(INTENT_EXTRA, Json.encodeToString(ThrowableSerializer, exception))
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        applicationContext.startActivity(intent)
    }

    companion object {
        private const val INTENT_EXTRA = "Throwable"

        fun initialize(applicationContext: Context, activityToBeLaunched: Class<*>) {
            val handler =
                GlobalExceptionHandler(
                    applicationContext,
                    Thread.getDefaultUncaughtExceptionHandler() as Thread.UncaughtExceptionHandler,
                    activityToBeLaunched,
                )
            Thread.setDefaultUncaughtExceptionHandler(handler)
        }

        fun getThrowableFromIntent(intent: Intent): Throwable? {
            return try {
                Json.decodeFromString(ThrowableSerializer, intent.getStringExtra(INTENT_EXTRA)!!)
            } catch (e: Exception) {
                TimberKt.e(e) { "Wasn't able to retrive throwable from intent" }
                null
            }
        }
    }
}
