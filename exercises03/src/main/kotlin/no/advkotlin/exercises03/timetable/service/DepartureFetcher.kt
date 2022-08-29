package no.advkotlin.exercises03.timetable.service

import no.advkotlin.exercises03.timetable.domain.DepartureWithLiveTime
import java.util.concurrent.TimeoutException

class DepartureFetcher(val timetableService: TimetableService, val realTimeService: RealTimeService) {

    fun fetchDeparturesAndLiveTimes(max: Int): List<DepartureWithLiveTime> {
        return timetableService.departures(max).map {
            val liveTime = try {
                realTimeService.liveDepartureTime(it.id)
            } catch (e: TimeoutException) {
                null
            }
            DepartureWithLiveTime(it, liveTime)
        }
    }

}