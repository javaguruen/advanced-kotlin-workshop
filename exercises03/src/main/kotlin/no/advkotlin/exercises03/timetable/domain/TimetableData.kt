package no.advkotlin.exercises03.timetable.domain

data class DepartureWithLiveTime(
    val departure: Departure,
    val liveTime: DepartureTime?)