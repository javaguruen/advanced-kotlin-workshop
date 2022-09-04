package no.advkotlin.exercises03.timetable.service

import org.fusesource.jansi.Ansi.ansi

class DepartureDisplay(val timetableService: TimetableService, val realTimeService: RealTimeService) {

    val departureFetcher = DepartureFetcher(timetableService, realTimeService)

    fun update() {
        val header = StringBuilder()
        header.append(ansi().eraseScreen())
        header.append("                  DEPARTURES\n\n")
        header.append("====================================================\n")
        header.append("  #    To                   Gate     Departure\n")
        header.append("----------------------------------------------------\n")
        print(header)
        val table = StringBuilder()

        departureFetcher.fetchDeparturesAndLiveTimes(10).forEach { departureAndTime ->
            val timeString = if (departureAndTime.liveTime != null) {
                "@|red ${departureAndTime.liveTime} (delayed)|@"
            } else {
                "@|green ${departureAndTime.departure.time}|@"
            }
            table.append(ansi().render(String.format("%-6s %-20s %-5s    %-15s\n", departureAndTime.departure.code, departureAndTime.departure.destination, departureAndTime.departure.gate, timeString)))
        }
        table.append("----------------------------------------------------\n")
        print(table)

    }

}


