double getDischargeInterval()
{/*ALCODESTART::1793990015869*/
return 60.0 / routeA_currentCapacity;
/*ALCODEEND*/}

double checkNetworkRecovery(double currentNetworkTT,double normalNetworkTT)
{/*ALCODESTART::1793991453421*/

// Check if the incident has cleared
if (!incidentActive &&
    incidentClearTime >= 0 &&
    !recoveryRecorded &&
    normalNetworkTT > 0 &&
    currentNetworkTT > 0) {

    // Calculate acceptable travel time
    double acceptableTT =
        normalNetworkTT * (1.0 + recoveryThreshold);

    // Check recovery condition
    if (currentNetworkTT <= acceptableTT) {

        recoveryTime = time(MINUTE) - incidentClearTime;

        recoveryRecorded = true;

        traceln("=== NETWORK RECOVERED ===");
        traceln("Recovery time: " + recoveryTime + " minutes");
        traceln("Current network travel time: " + currentNetworkTT);
        traceln("Normal network travel time: " + normalNetworkTT);
    }
}

/*ALCODEEND*/}

double getNormalNetworkTT()
{/*ALCODESTART::1794300000016*/
//Average trip time of the vehicles that finished in the window just before the incident. //
return normalTTCount > 0 ? normalTTSum / normalTTCount : -1;
/*ALCODEEND*/}

double trackTripForRecovery(double tripTime)
{/*ALCODESTART::1794300000017*/
//Called from the Sink every time a vehicle finishes its trip. */

double now = time(MINUTE);

// 1. Learn the pre-incident benchmark (normal network travel time)
if (now >= incidentStartTime - preIncidentWindow && now < incidentStartTime) {
    normalTTSum = normalTTSum + tripTime;
    normalTTCount = normalTTCount + 1;
}

// 2. Collect trips finished in the current window
windowTTSum = windowTTSum + tripTime;
windowTTCount = windowTTCount + 1;

// 3. At the end of each window, compare with the benchmark
if (now >= nextRecoveryCheck) {

    currentNetworkTT = windowTTSum / windowTTCount;

    if (incidentClearTime >= 0) {
        checkNetworkRecovery(currentNetworkTT, getNormalNetworkTT());
    }

    windowTTSum = 0;
    windowTTCount = 0;
    nextRecoveryCheck = now + recoveryCheckInterval;
}
/*ALCODEEND*/}

