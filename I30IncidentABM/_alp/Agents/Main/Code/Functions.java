double exportVehicle(Driver vehicle)
{/*ALCODESTART::1794016766667*/
double comparisonDistance;

if (vehicle.exportRoute.equals("A")) {
    comparisonDistance =
        rtRegular1.length()
        + rtRegular2.length();

} else if (vehicle.exportRoute.equals("B")) {
    comparisonDistance =
        rtDiversion1.length()
        + rtDiversion2.length()
        + rtDiversion3.length();

} else {
    throw new RuntimeException(
        "Vehicle reached destination without a recorded route."
    );
}

double totalDistance =
    rtApproach.length()
    + comparisonDistance
    + rtExit.length();

excelResults.setCellValue(
    runId, "Vehicles", excelRow, 1);

excelResults.setCellValue(
    vehicle.vehicleId, "Vehicles", excelRow, 2);

excelResults.setCellValue(
    vehicle.exportRoute, "Vehicles", excelRow, 3);

excelResults.setCellValue(
    vehicle.entryTime, "Vehicles", excelRow, 4);

excelResults.setCellValue(
    vehicle.exitTime, "Vehicles", excelRow, 5);

if (vehicle.diversionTime >= 0) {
    excelResults.setCellValue(
        vehicle.diversionTime, "Vehicles", excelRow, 6);
}

if (vehicle.rejoinTime >= 0) {
    excelResults.setCellValue(
        vehicle.rejoinTime, "Vehicles", excelRow, 7);
}

excelResults.setCellValue(
    totalDistance, "Vehicles", excelRow, 8);

excelResults.setCellValue(
    comparisonDistance, "Vehicles", excelRow, 9);

excelRow++;
/*ALCODEEND*/}

double writeScenarioSummary()
{/*ALCODESTART::1794300000020*/
// Scenario description
double informedPct = 0;
String scenario = scenarioName.replace(",", ";");

double normalTT  = trafficIncident.getNormalNetworkTT();
double recovery  = trafficIncident.recoveryRecorded ? trafficIncident.recoveryTime : -1;
double tsttHours = totalSystemTravelTime / 60.0;
int    enRoute   = (int) (vehiclesOnRouteA + vehiclesOnRouteB);
java.util.Locale us = java.util.Locale.US;

// ---- Console report
traceln("");
traceln("==================================================");
traceln(" SCENARIO SUMMARY   run " + runId);
traceln(" " + scenario);
traceln("==================================================");
traceln(String.format(us, " Completed trips               : %d", (int) completedTrips));
traceln(String.format(us, " Vehicles still en route       : %d", enRoute));
traceln(String.format(us, " Pre-incident network TT (min) : %.2f", normalTT));
traceln(String.format(us, " Avg travel time, all (min)    : %.2f", tripTimeAll.mean()));
traceln(String.format(us, " Avg travel time, Route A      : %.2f", tripTimeA.mean()));
traceln(String.format(us, " Avg travel time, Route B      : %.2f", tripTimeB.mean()));
traceln(String.format(us, " Avg delay (min)               : %.2f", delayAll.mean()));
traceln(String.format(us, " Max delay (min)               : %.2f", delayAll.max()));
traceln(String.format(us, " Max queue length (veh)        : %d", maxQueueLength));
traceln(String.format(us, " Avg wait at incident (min)    : %.2f", incidentWait.mean()));
traceln(String.format(us, " Max wait at incident (min)    : %.2f", incidentWait.max()));
traceln(String.format(us, " Total system travel time (h)  : %.1f", tsttHours));
if (recovery >= 0) {
    traceln(String.format(us, " Recovery time (min)           : %.2f", recovery));
} else {
    traceln(" Recovery time (min)           : not reached before the run ended");
}
traceln("==================================================");

// ---- CSV log (one row per run)
String header = "run_id,scenario,informed_pct,demand_vph,routeA_share,capacity_reduction,"
    + "incident_start_min,incident_duration_min,simulated_min,completed_trips,vehicles_en_route,"
    + "avg_travel_time_all_min,avg_travel_time_A_min,avg_travel_time_B_min,pre_incident_network_tt_min,"
    + "avg_delay_min,max_delay_min,max_queue_veh,avg_incident_wait_min,max_incident_wait_min,"
    + "total_system_travel_time_veh_h,recovery_time_min";

String row = String.join(",",
    String.valueOf(runId),
    scenario,
    String.format(us, "%.0f", informedPct),
    String.format(us, "%.0f", trafficDemand),
    String.format(us, "%.3f", routeA_share),
    String.format(us, "%.3f", trafficIncident.capacityReduction),
    String.format(us, "%.1f", trafficIncident.incidentStartTime),
    String.format(us, "%.1f", trafficIncident.incidentDuration),
    String.format(us, "%.1f", time(MINUTE)),
    String.valueOf((int) completedTrips),
    String.valueOf(enRoute),
    String.format(us, "%.4f", tripTimeAll.mean()),
    String.format(us, "%.4f", tripTimeA.mean()),
    String.format(us, "%.4f", tripTimeB.mean()),
    String.format(us, "%.4f", normalTT),
    String.format(us, "%.4f", delayAll.mean()),
    String.format(us, "%.4f", delayAll.max()),
    String.valueOf(maxQueueLength),
    String.format(us, "%.4f", incidentWait.mean()),
    String.format(us, "%.4f", incidentWait.max()),
    String.format(us, "%.2f", tsttHours),
    recovery >= 0 ? String.format(us, "%.2f", recovery) : "NA"
);

java.io.File file = new java.io.File("scenarioResults.csv");
boolean writeHeader = !file.exists() || file.length() == 0;

try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file, true))) {
    if (writeHeader) {
        writer.println(header);
    }
    writer.println(row);
    traceln(" Results appended to " + file.getAbsolutePath());
} catch (java.io.IOException e) {
    traceln(" WARNING: could not write scenarioResults.csv (" + e.getMessage()
        + "). Close the file if it is open in Excel.");
}
/*ALCODEEND*/}

double updateIncidentMarker()
{/*ALCODESTART::1794400000001*/
//Live incident marker on the GIS map.
  
if (trafficIncident.incidentActive) {
    ptIncident.setFillColor(java.awt.Color.RED);
    ptIncident.setRadius(Math.min(6.0 + currentQueueLength / 25.0, 30.0));
} else {
    ptIncident.setFillColor(new java.awt.Color(2144175423, true));
    ptIncident.setRadius(3);
}
/*ALCODEEND*/}

double sampleTimeSeries()
{/*ALCODESTART::1794500000100*/

double now = time(MINUTE);
if (now < tsNextSample) {
    return;
}

double t = tsNextSample;

// Averages of the window that just ended. Keep the previous value when nothing happened.
if (winDoneN > 0) {
    lastAvgDelay = winDelaySum / winDoneN;
    lastAvgTT    = winTTSum / winDoneN;
}
if (winWaitN > 0) {
    lastAvgWait = winWaitSum / winWaitN;
}
lastThroughput = winDoneN;

java.util.Locale us = java.util.Locale.US;
String header = "run_id,scenario,time_min,incident_active,route_a_capacity_vph,queue_length_veh,"
    + "avg_wait_at_incident_min,avg_delay_min,avg_trip_time_min,trips_completed_per_min,"
    + "vehicles_on_route_a,vehicles_on_route_b";
String row = String.join(",",
    String.valueOf(runId),
    scenarioName.replace(",", ";"),
    String.format(us, "%.1f", t),
    trafficIncident.incidentActive ? "1" : "0",
    String.format(us, "%.0f", trafficIncident.routeA_currentCapacity),
    String.valueOf(currentQueueLength),
    String.format(us, "%.4f", lastAvgWait),
    String.format(us, "%.4f", lastAvgDelay),
    String.format(us, "%.4f", lastAvgTT),
    String.format(us, "%.0f", lastThroughput),
    String.valueOf(vehiclesOnRouteA),
    String.valueOf(vehiclesOnRouteB)
);

java.io.File file = new java.io.File("timeSeries.csv");
boolean writeHeader = !file.exists() || file.length() == 0;
try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file, true))) {
    if (writeHeader) {
        writer.println(header);
    }
    writer.println(row);
} catch (java.io.IOException e) {
    traceln(" WARNING: could not write timeSeries.csv (" + e.getMessage() + ")");
}

// Open the next one minute window
winDelaySum = 0;
winTTSum    = 0;
winWaitSum  = 0;
winDoneN    = 0;
winWaitN    = 0;
tsNextSample = tsNextSample + 1;
if (tsNextSample <= now) {
    tsNextSample = Math.floor(now) + 1;
}
/*ALCODEEND*/}

