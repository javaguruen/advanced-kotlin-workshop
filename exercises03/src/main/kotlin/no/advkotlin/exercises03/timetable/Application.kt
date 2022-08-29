package no.advkotlin.exercises03.timetable

import kotlinx.coroutines.debug.DebugProbes
import no.advkotlin.exercises03.timetable.service.DepartureDisplay
import no.advkotlin.exercises03.timetable.service.RealTimeService
import no.advkotlin.exercises03.timetable.service.TimetableService
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {

    val departureDisplay = DepartureDisplay(TimetableService(), RealTimeService())
    while (true) {
        val time = measureTime {
            departureDisplay.update()
        }
        println("(update took $time")
       Thread.sleep(5000L)
    }
}
