package no.advkotlin.exercises03.timetable.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import no.advkotlin.exercises03.timetable.domain.DepartureWithLiveTime
import java.util.concurrent.TimeoutException

class DepartureFetcher(val timetableService: TimetableService, val realTimeService: RealTimeService) {

    suspend fun fetchDeparturesAndLiveTimes(max: Int): List<DepartureWithLiveTime> = coroutineScope {
        timetableService.departures(max).map {
            async {
                val liveTime = try {
                    realTimeService.liveDepartureTime(it.id)
                } catch (e: TimeoutException) {
                    null
                }
                DepartureWithLiveTime(it, liveTime)
            }
        }.awaitAll()
    }
}