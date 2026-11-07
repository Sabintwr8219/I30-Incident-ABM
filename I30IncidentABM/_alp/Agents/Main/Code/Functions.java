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

