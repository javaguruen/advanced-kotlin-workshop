package no.advkotlin.exercises03.timetable

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.debug.DebugProbes
import kotlinx.coroutines.delay
import no.advkotlin.exercises03.timetable.service.DepartureDisplay
import no.advkotlin.exercises03.timetable.service.RealTimeService
import no.advkotlin.exercises03.timetable.service.TimetableService
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
suspend fun main() = coroutineScope {

    val departureDisplay = DepartureDisplay(TimetableService(), RealTimeService())
    while (true) {
        val time = measureTime {
            departureDisplay.update()
        }
        println("(update took $time")
       delay(5000L)
    }
}
