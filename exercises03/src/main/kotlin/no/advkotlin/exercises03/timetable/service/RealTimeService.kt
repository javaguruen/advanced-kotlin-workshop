package no.advkotlin.exercises03.timetable.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.advkotlin.exercises03.timetable.domain.DepartureTime
import no.advkotlin.exercises03.timetable.domain.Hours
import no.advkotlin.exercises03.timetable.domain.Minutes
import java.util.concurrent.TimeoutException
import kotlin.random.Random

class RealTimeService {
    fun liveDepartureTime(departureId: String) = runBlocking {
        delay(500L)

        if (Random.nextInt(0, 100) < 10) {
            throw TimeoutException()
        }
        realTime[departureId]
    }
}

val realTime =
    """
        7b5d0da5-4f5f-4145-afe7-0e4979023580, 10:10
        a247aa31-6b12-4a23-83c8-db814fc43243, 10:05
        0acc1625-d12f-4e56-9ef9-86d50ebffebb, 10:40
        d8b4ffc6-5269-4afe-8838-adc085b0dc0b, 10:40
        8fb4bd01-f88a-4a10-889f-45eb1c2a01cd, 10:50
        94b16ba4-5d0b-498e-8db6-954ee5c284ac, 11:00
    """.trimIndent()
        .lines()
        .associate { line ->
            line.split(", ").let { column ->
                val time = column[1].split(":").map { it.toByte() }
                column[0] to DepartureTime(Hours(time[0]), Minutes(time[1]))
            }
        }