void IncidentStart()
{/*ALCODESTART::1793556016345*/
// Activate the incident
incidentActive = true;

// Reduce Route A capacity
routeA_currentCapacity =
    routeA_capacity * (1.0 - capacityReduction);

// Display incident information
traceln("=== INCIDENT STARTED ===");
traceln("Time: " + time(MINUTE) + " minutes");
traceln("Route A capacity: " + routeA_currentCapacity + " veh/hr");

// Verify discharge interval
traceln("Discharge interval: "
    + getDischargeInterval() + " minutes");

// Update the marker on the map
get_Main().updateIncidentMarker();

/*ALCODEEND*/}

void IncidentClear()
{/*ALCODESTART::1793556248487*/
// Deactivate the incident
incidentActive = false;

// Stop incident information
informationAvailable = false;

// Restore normal Route A capacity
routeA_currentCapacity = routeA_capacity;

// Record incident clearance time in minutes
incidentClearTime = time(MINUTE);

// Display results
traceln("=== INCIDENT CLEARED ===");
traceln("Clearance time: " + incidentClearTime + " minutes");
traceln("Incident active: " + incidentActive);
traceln("Information available: " + informationAvailable);
traceln("Route A capacity: " + routeA_currentCapacity + " veh/hr");

// Update the marker on the map
get_Main().updateIncidentMarker();

/*ALCODEEND*/}

void InformationRelease()
{/*ALCODESTART::1793988701351*/

if (incidentActive && alertEnabled) {

    informationAvailable = true;

    traceln("=== INCIDENT INFORMATION RELEASED ===");
    traceln("Time: " + time(MINUTE) + " minutes");
    traceln("Information available: " + informationAvailable);

} else {

    informationAvailable = false;

    traceln("No incident information released.");
}

/*ALCODEEND*/}

void RunSummary()
{/*ALCODESTART::1794300000018*/
// write the scenario summary //
get_Main().writeScenarioSummary();
/*ALCODEEND*/}

