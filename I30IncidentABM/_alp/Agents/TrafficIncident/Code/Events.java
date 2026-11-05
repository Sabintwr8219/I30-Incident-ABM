void IncidentStart()
{/*ALCODESTART::1793556016345*/

incidentActive = true;

routeA_currentCapacity =
    routeA_capacity * (1.0 - capacityReduction);

traceln("=== INCIDENT STARTED ===");
traceln("Time: " + time(MINUTE) + " minutes");
traceln("Route A capacity: " + routeA_currentCapacity + " veh/hr");

/*ALCODEEND*/}

void IncidentClear()
{/*ALCODESTART::1793556248487*/

incidentActive = false;

routeA_currentCapacity = routeA_capacity;

traceln("Incident cleared at time: " + time());
traceln("Route A capacity: " + routeA_currentCapacity);

/*ALCODEEND*/}

