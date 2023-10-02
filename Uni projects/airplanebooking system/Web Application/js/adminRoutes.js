// JavaScript source code
async function populateDropDown() {
    var stations = await getRequest("stations");
    var dropdowns = document.getElementsByClassName("dropdown");

    //for each dropdown
    for (i = 0; i < dropdowns.length; i++) {
        //add each station
        for (j = 0; j < stations.length; j++) {
            dropdowns[i].innerHTML += "<option value ='" + stations[j].STATION_ID + "'>" + stations[j].STATION_NAME + "</option>"
        }
    }

    console.log(stations)
};

async function removeRoute() {
    var routeID = document.getElementById("RouteToRemove");
    routeID = routeID.options[routeID.selectedIndex].value;

    var result = await deleteRequest("journeys", routeID);

    if (result) {
        alert("Route deleted");
    } else {
        alert("Delete failed");
    }
}
// Allows for searching through routes on the manage section
async function searchRoute() {

    var parameter = document.getElementById("searchParam").value;
    var journeys = await getRequest("journeys");

    var searchBy = document.getElementById("searchBy");


    searchBy = searchBy.options[searchBy.selectedIndex].value;

    var output = document.getElementById("RouteToRemove")
    //reset the dropdown
    output.innerHTML = "<option value=''>Choose...</option>";


    //for each journey
    for (index in journeys) {

        //for each header
        for (data in journeys[index]) {

            //check if we are searching by header and if search param matches
            if (data == searchBy && journeys[index][data] == parameter) {
                var outputString;

                //add the data from journey
                outputString = "<option value ='" + journeys[index].JOURNEY_ID + "'>";
                for (i in journeys[index]) {

                    outputString += " " + i + ": " + journeys[index][i];
                }


                outputString += "</option>";

                output.innerHTML += outputString;
            }
        }
    }

}


async function searchRouteManage() {

    var parameter = document.getElementById("searchParamManage").value;
    var journeys = await getRequest("journeys");

    var searchBy = document.getElementById("searchByManage");


    searchBy = searchBy.options[searchBy.selectedIndex].value;

    var output = document.getElementById("RouteToUpdate")
    //reset the dropdown
    output.innerHTML = "<option value=''>Choose...</option>";


    //for each journey
    for (index in journeys) {

        //for each header
        for (data in journeys[index]) {

            //check if we are searching by header and if search param matches
            if (data == searchBy && journeys[index][data] == parameter) {
                var outputString;

                //add the data from journey
                outputString = "<option value ='" + journeys[index].JOURNEY_ID + "'>";
                for (i in journeys[index]) {

                    outputString += " " + i + ": " + journeys[index][i];
                }


                outputString += "</option>";

                output.innerHTML += outputString;
            }
        }
    }

}

//get values from selections and append to a single string
async function submitJourney() {
    var JSONString;
    var START_LOCATION_ID = document.getElementById("departure");
    START_LOCATION_ID = START_LOCATION_ID.options[START_LOCATION_ID.selectedIndex].value;

    var END_LOCATION_ID = document.getElementById("destination");
    END_LOCATION_ID = END_LOCATION_ID.options[END_LOCATION_ID.selectedIndex].value;

    if (START_LOCATION_ID == "" || END_LOCATION_ID == "") {
        Swal.fire({
                text: 'Please choose stations',
                type: 'error',
                confirmButtonText: 'Ok'
            });
        return;
    }

    if (START_LOCATION_ID == END_LOCATION_ID) {
        Swal.fire({
                text: 'You cannot go nowhere',
                type: 'error',
                confirmButtonText: 'Ok'
            });
        return;
    }

    var DEPARTURE_DATE_TIME = new Date(document.getElementById("depDatepicker").value);

    var ARRIVAL_DATE_TIME = new Date(document.getElementById("arrDatepicker").value);

    var JOURNEY_COMMENTS = document.getElementById("comments").value;

    var JOURNEY_PRICE = document.getElementById("price").value;

    JSONString = { "START_LOCATION_ID": START_LOCATION_ID, "END_LOCATION_ID": END_LOCATION_ID, "DEPARTURE_DATE_TIME": DEPARTURE_DATE_TIME, "ARRIVAL_DATE_TIME": ARRIVAL_DATE_TIME, "JOURNEY_COMMENTS": JOURNEY_COMMENTS,"JOURNEY_PRICE": JOURNEY_PRICE };

    console.log(JSONString);
    var result = await postRequest("journeys", JSONString);
    if (result) {
    Swal.fire({
            text: 'The train has been added!',
            type: 'success',
            confirmButtonText: 'Ok'
        });
    populateDropDown();
    } else {
      Swal.fire({
          text: 'Oops, something went wrong! Please try again later.',
          type: 'error',
          confirmButtonText: 'Ok'
      });
    }
}

async function updateRoute() {
  var JOURNEY_ID = document.getElementById("RouteToUpdate");
    JOURNEY_ID = JOURNEY_ID.options[JOURNEY_ID.selectedIndex].value;
    var START_LOCATION_ID = document.getElementById("departureManage");
    START_LOCATION_ID = START_LOCATION_ID.options[START_LOCATION_ID.selectedIndex].value;

    var END_LOCATION_ID = document.getElementById("destinationManage");
    END_LOCATION_ID = END_LOCATION_ID.options[END_LOCATION_ID.selectedIndex].value;

    if (START_LOCATION_ID == "" || END_LOCATION_ID == "") {
        Swal.fire({
                text: 'Please choose stations',
                type: 'error',
                confirmButtonText: 'Ok'
            });
        return;
    }

    if (START_LOCATION_ID == END_LOCATION_ID) {
        Swal.fire({
                text: 'You cannot go nowhere',
                type: 'error',
                confirmButtonText: 'Ok'
            });
        return;
    }
    var DEPARTURE_DATE_TIME = new Date(document.getElementById("depDatepickerManage").value);

    var ARRIVAL_DATE_TIME = new Date(document.getElementById("arrDatepickerManage").value);

    var JOURNEY_COMMENTS = document.getElementById("commentsManage").value;

    var JOURNEY_PRICE = document.getElementById("priceManage").value;

    JSONString = {"JOURNEY_ID":JOURNEY_ID, "START_LOCATION_ID": START_LOCATION_ID, "END_LOCATION_ID": END_LOCATION_ID, "DEPARTURE_DATE_TIME": DEPARTURE_DATE_TIME, "ARRIVAL_DATE_TIME": ARRIVAL_DATE_TIME, "JOURNEY_COMMENTS": JOURNEY_COMMENTS,"JOURNEY_PRICE": JOURNEY_PRICE };

    console.log(JSONString);
	var result = await putRequest("journeys/"+JOURNEY_ID, JSONString);
  if (result) {
  Swal.fire({
          text: 'The train has been updated!',
          type: 'success',
          confirmButtonText: 'Ok'
      });
  populateDropDown();
  } else {
    Swal.fire({
        text: 'Oops, something went wrong! Please try again later.',
        type: 'error',
        confirmButtonText: 'Ok'
    });
  }
}
