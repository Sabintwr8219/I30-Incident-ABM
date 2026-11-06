double getDischargeInterval()
{/*ALCODESTART::1793990015869*/
return 60.0 / routeA_currentCapacity;
/*ALCODEEND*/}

double joinIncidentQueue()
{/*ALCODESTART::1793990299328*/

/* VEHICLE JOINS INCIDENT QUEUE */

queueLengthRouteA = queueLengthRouteA + 1;

if (queueLengthRouteA > maxQueueLengthRouteA) {

    maxQueueLengthRouteA = queueLengthRouteA;

}

/*ALCODEEND*/}

double leaveIncidentQueue()
{/*ALCODESTART::1793990635045*/

/* VEHICLE LEAVES INCIDENT QUEUE */

// Reduce current queue length
if (queueLengthRouteA > 0) {

    queueLengthRouteA = queueLengthRouteA - 1;

}

/*ALCODEEND*/}

double getIncidentWaitTime()
{/*ALCODESTART::1793990841957*/

/* CALCULATE INCIDENT WAITING TIME */

// Current simulation time in minutes
double currentTime = time(MINUTE);

// Calculate discharge interval
double dischargeInterval = getDischargeInterval();

// Determine next available release time
double releaseTime = currentTime;

if (nextRouteAReleaseTime > currentTime) {
    releaseTime = nextRouteAReleaseTime;
}

// Calculate waiting time
double waitingTime = releaseTime - currentTime;

// Reserve next release time
nextRouteAReleaseTime = releaseTime + dischargeInterval;

// Return waiting time in minutes
return waitingTime;

/*ALCODEEND*/}

double checkNetworkRecovery(double currentNetworkTT,double normalNetworkTT)
{/*ALCODESTART::1793991453421*/

/* CHECK NETWORK RECOVERY */

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

double recordIncidentWaitingTime(double waitingTime)
{/*ALCODESTART::1793991608774*/

/* RECORD INCIDENT WAITING TIME */

// Check that waiting time is valid
if (waitingTime >= 0) {

    // Update total waiting time
    totalIncidentWaitingTime =
        totalIncidentWaitingTime + waitingTime;

    // Count vehicles with positive waiting time
    if (waitingTime > 0) {

        numberDelayedAtIncident =
            numberDelayedAtIncident + 1;

    }

    // Update maximum incident waiting time
    if (waitingTime > maxIncidentWaitingTime) {

        maxIncidentWaitingTime = waitingTime;

    }
}

/*ALCODEEND*/}

double getAverageIncidentWaitingTime()
{/*ALCODESTART::1793991947016*/

/* CALCULATE AVERAGE INCIDENT WAITING TIME */

if (numberDelayedAtIncident > 0) {

    return totalIncidentWaitingTime /
           numberDelayedAtIncident;

} else {

    return 0;

}

/*ALCODEEND*/}

