package no.advkotlin.exercises03.timetable.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.advkotlin.exercises03.timetable.domain.Departure
import no.advkotlin.exercises03.timetable.domain.DepartureTime
import no.advkotlin.exercises03.timetable.domain.Hours
import no.advkotlin.exercises03.timetable.domain.Minutes
import kotlin.math.min

class TimetableService {

    fun departures(max: Int = 10): List<Departure> {
        return runBlocking {
            delay(500L)
            timetable.subList(0, min(timetable.size, max))
        }
    }
}

val timetable =
    """
        7b5d0da5-4f5f-4145-afe7-0e4979023580, 22, Bergen, 10:00, A
        a247aa31-6b12-4a23-83c8-db814fc43243, 22, Bergen, 10:00, A
        f46fc26f-6dc5-4fc3-b9ce-f38663cccd60, 22, Bergen, 10:00, A
        0acc1625-d12f-4e56-9ef9-86d50ebffebb, 22, Bergen, 10:00, A
        d8b4ffc6-5269-4afe-8838-adc085b0dc0b, 22, Bergen, 10:00, A
        c3c6b949-0910-4a28-ad07-93da2bc9035a, 22, Bergen, 10:00, A
        8fb4bd01-f88a-4a10-889f-45eb1c2a01cd, 22, Bergen, 10:00, A
        94b16ba4-5d0b-498e-8db6-954ee5c284ac, 22, Bergen, 10:00, A
        c7a6ad45-6c8e-4d74-9f9b-9ec19ac6ceee, 22, Bergen, 10:00, A
        9d0e5f77-b676-480a-b131-ef618adc34db, 22, Bergen, 10:00, A
        c9fd4525-f36e-43dc-ab67-e0288b7fa256, 22, Bergen, 10:00, A
        60c3052c-9758-46ba-9d42-6b5aa5adc65c, 22, Bergen, 10:00, A
        36953214-9566-4c98-8a89-adf0625f5623, 22, Bergen, 10:00, A
        b1bb241f-c6bc-4b17-b374-e921929ca4ef, 22, Bergen, 10:00, A
        4b644834-8424-463a-9f7f-ec7aad1c9768, 22, Bergen, 10:00, A
        70d01efd-6b6b-4344-a8cc-7b8d1768d53d, 22, Bergen, 10:00, A
        b47b97f8-c099-4585-a39c-0e4f1467f794, 22, Bergen, 10:00, A
        02465e9d-9228-4722-8cab-5aa090d4dc98, 22, Bergen, 10:00, A
        ec2df1fb-5ab6-457f-be8b-088dc1cc3d08, 22, Bergen, 10:00, A
        7d0bcd7b-6f92-4bb4-9232-a5ba365be874, 22, Bergen, 10:00, A
    """.trimIndent()
        .lines()
        .map { line ->
            line.split(", ").let { column ->
                val time = column[3].split(":").map { it.toByte() }
                Departure(column[0], column[1],column[2], DepartureTime(Hours(time[0]), Minutes(time[1])), column[4])
            }
        }
