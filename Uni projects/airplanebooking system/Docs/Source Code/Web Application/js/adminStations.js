// Initialise list values
async function initialise()
{
	populateDropDown();
	populateParameterList();
}

// Populate dropdown lists
async function populateDropDown() {
    //get station data from API
    var stations = await getRequest("stations");
    //get dropdown elements
    var dropdowns = document.getElementsByClassName("dropdown");

    //for each dropdown
    for (i = 0; i < dropdowns.length; i++) {
        //clear dropdown
        dropdowns[i].innerHTML = "";
        //add each station
        for (j = 0; j < stations.length; j++) {
            dropdowns[i].innerHTML += "<option value ='" + stations[j].STATION_ID + "'>" + stations[j].STATION_NAME + " ("+ stations[j].ADDRESS +")" + "</option>";
        }
    }

    console.log(stations)
};

// Populate parameter lists
async function populateParameterList()
{   
    //get dropdown elements
	var dropdown = document.getElementById("searchParam");
    dropdown.disabled = true;
    //get parameter types
	var searchByElement = document.getElementById("searchBy");
    //get name from search box
	var manageName = document.getElementById("manageName");
    //change dropdown content or input type according to thr value selected in the "searchBy" dropdown menu
	searchByElement.onchange = function()
	{
		searchBy = searchByElement.options[searchByElement.selectedIndex].value;

		if (searchBy == "STATION_NAME")
		{
			manageName.style.display= 'block';
			dropdown.style.display= 'none';
		}

		else if (searchBy == "STATION_STATUS")
		{
			manageName.style.display= 'none';
			dropdown.style.display= 'block';
            dropdown.disabled = false;
			dropdown.innerHTML = "<option>Choose...</option>";


			// Fill the parameter dropdown list.
			dropdown.innerHTML += "<option>Active</option>";
			dropdown.innerHTML += "<option>Inactive</option>";
		}

		else if (searchBy == "PLATFORM_COUNT")
		{
			manageName.style.display= 'none';
			dropdown.style.display= 'block';
            dropdown.disabled = false;
			dropdown.innerHTML = "<option>Choose...</option>";

			// Fill the parameter dropdown list.
			dropdown.innerHTML += "<option>1</option>";
			dropdown.innerHTML += "<option>2</option>";
			dropdown.innerHTML += "<option>3</option>";
			dropdown.innerHTML += "<option>4</option>";
			dropdown.innerHTML += "<option>5</option>";
			dropdown.innerHTML += "<option>6</option>";
			dropdown.innerHTML += "<option>7</option>";
			dropdown.innerHTML += "<option>8</option>";
		}

        else if (searchBy == "displayAll")
        {
            manageName.style.display= 'none';
            dropdown.style.display = 'block';
            dropdown.disabled = true;

            populateDropDown();

        }
	}
};

// Filter the stations available in the database according to the search parameters
async function searchStation() {

    var stations = await getRequest("stations");

    if (stations) {
        Swal.fire({
            text: 'The list of stations has been updated',
            type: 'success',
            confirmButtonText: 'Ok'
        });
    } else {
        error();
    }

	var searchParam = document.getElementById("searchParam");
    searchParam = searchParam.options[searchParam.selectedIndex].value;

    var stationName = document.getElementById("manageName");

    if (stationName.style.display == "block") {
    	searchParam = stationName.value;
    }

    var searchBy = document.getElementById("searchBy");
    searchBy = searchBy.options[searchBy.selectedIndex].value;

    var output = document.getElementById("stationToManage");

    //reset the dropdown
    output.innerHTML = "<option value=''>Choose...</option>";

    //for each station
    for (index in stations) {

        //for each header
        for (data in stations[index]) {

            //check if we are searching by header and if search param matches
            if (data == searchBy && stations[index][data] == searchParam) {
                //add the data from stations
                output.innerHTML += "<option value ='" + stations[index].STATION_ID + "'>" + stations[index].STATION_NAME + " ("+ stations[index].ADDRESS +")" + "</option>";
            }
        }
    }
}

// Submit a delete request with the selected ID
async function removeStation() {
    var dropdown = document.getElementById("stationToManage");
    
    var stationID = dropdown.options[dropdown.selectedIndex].value;

    var result = await deleteRequest("stations", stationID);

    if (result) {
		populateDropDown();
        Swal.fire({
            text: 'The station has been removed',
            type: 'success',
            confirmButtonText: 'Ok'
        });

    } else {
		error();
    }
}


//get values from selections and append to a single string 
async function addNewStation() 
{

    var STATION_NAME = document.getElementById("stationName");
    STATION_NAME = STATION_NAME.value;

    var POST_CODE = document.getElementById("stationPostcode");
    POST_CODE = POST_CODE.value;

    var ADDRESS = document.getElementById("stationAddress");
    ADDRESS = ADDRESS.value;
    
    var PLATFORM_COUNT = document.getElementById("noPlatforms");
    PLATFORM_COUNT = PLATFORM_COUNT.value;

    var STATION_STATUS = document.getElementById("stationStatus");
    STATION_STATUS = STATION_STATUS.options[STATION_STATUS.selectedIndex].value;

    var JSONString;
    JSONString = {"STATION_NAME": STATION_NAME, "POST_CODE": POST_CODE, "ADDRESS": ADDRESS, "PLATFORM_COUNT": PLATFORM_COUNT, "STATION_STATUS": STATION_STATUS};

    console.log(JSONString);
    var result = await postRequest("stations", JSONString);
    if (result) 
    {
        Swal.fire({
            text: 'The station has been added!',
            type: 'success',
            confirmButtonText: 'Ok'
        });

        populateDropDown();
    }
    else
    {
        error();
    }
} 

function error()
{        
    Swal.fire({
        text: 'Oops, something went wrong! Please try again later.',
        type: 'error',
        confirmButtonText: 'Ok'
    });        
}