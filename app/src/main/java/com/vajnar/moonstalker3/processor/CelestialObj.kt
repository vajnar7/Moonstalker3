package com.vajnar.moonstalker3.processor

import com.google.gson.annotations.SerializedName
import java.util.Locale
import kotlin.math.abs

/**
 * Represents Right Ascension (RA) in Hours, Minutes, and Seconds.
 */
data class RaObj(
    @SerializedName("hours") val hours: Double = 0.0,
    @SerializedName("minutes") val minutes: Double = 0.0,
    @SerializedName("seconds") val seconds: Double = 0.0,
) {
    /** Total RA in decimal hours (0.0 .. 24.0) */
    val totalHours: Double
        get() = hours + (minutes / 60.0) + (seconds / 3600.0)

    /** Total RA converted to decimal degrees (0.0 .. 360.0) */
    val totalDegrees: Double
        get() = totalHours * 15.0

    /** Total RA in radians */
    val radians: Double
        get() = Math.toRadians(totalDegrees)

    /** Formats RA as "HHh MMm SS.s" */
    fun format(): String = String.format(Locale.US, "%02dh %02dm %04.1fs", hours.toInt(), minutes.toInt(), seconds)

    companion object {
        /** Creates an [RaObj] from decimal hours */
        fun fromDecimalHours(decimalHours: Double): RaObj {
            val h = decimalHours.toInt()
            val remMinutes = (decimalHours - h) * 60.0
            val m = remMinutes.toInt()
            val s = (remMinutes - m) * 60.0
            return RaObj(h.toDouble(), m.toDouble(), s)
        }
    }
}

/**
 * Represents Declination (Dec) in Degrees, Minutes, and Seconds.
 */
data class DecObj(
    @SerializedName("degrees") val degrees: Double = 0.0,
    @SerializedName("minutes") val minutes: Double = 0.0,
    @SerializedName("seconds") val seconds: Double = 0.0,
) {
    /** Total Declination in signed decimal degrees (-90.0 .. +90.0) */
    val totalDegrees: Double
        get() {
            val sign = if ((degrees < 0.0) || ((1.0 / degrees) < 0.0)) -1.0 else 1.0
            val absDeg = abs(degrees)
            return sign * (absDeg + (minutes / 60.0) + (seconds / 3600.0))
        }

    /** Total Declination in radians */
    val radians: Double
        get() = Math.toRadians(totalDegrees)

    /** Formats Dec as "+DD° MM' SS.s\"" or "-DD° MM' SS.s\"" */
    fun format(): String {
        val signChar = if (totalDegrees >= 0) '+' else '-'
        val absDeg = abs(degrees.toInt())
        return String.format(Locale.US, "%c%02d° %02d' %04.1f\"", signChar, absDeg, minutes.toInt(), seconds)
    }

    companion object {
        /** Creates a [DecObj] from decimal degrees */
        fun fromDecimalDegrees(decimalDegrees: Double): DecObj {
            val sign = if (decimalDegrees < 0.0) -1.0 else 1.0
            val absVal = abs(decimalDegrees)
            val d = absVal.toInt()
            val remMinutes = (absVal - d) * 60.0
            val m = remMinutes.toInt()
            val s = (remMinutes - m) * 60.0
            return DecObj(sign * d, m.toDouble(), s)
        }
    }
}

/**
 * Represents a Celestial Object's coordinates (RA and Dec).
 */
data class CelestialObj(
    @SerializedName("ra") val ra: RaObj = RaObj(),
    @SerializedName("dec") val dec: DecObj = DecObj(),
    @SerializedName("name") val name: String = "",
) {
    /** Backwards-compatible accessor for RA */
    val raObj: RaObj get() = ra
}
