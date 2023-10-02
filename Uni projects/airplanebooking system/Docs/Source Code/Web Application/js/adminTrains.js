// Initialises list values
async function initialise()
{
	populateDropDown();
	populateParameterList();
}

// Populates drop-down lists for delete and manage functions
async function populateDropDown() {
    var trains = await getRequest("trains");
    var dropdowns = document.getElementsByClassName("dropdown");

    //for each dropdown
    for (i = 0; i < dropdowns.length; i++) {

		dropdowns[i].innerHTML = "<option>Choose...</option>";

        //add each station
        for (j = 0; j < trains.length; j++) {
            dropdowns[i].innerHTML += "<option value ='" + trains[j].TRAIN_ID + "'>" + "TRAIN_ID: " + trains[j].TRAIN_ID + " TRAIN_TYPE: " + trains[j].TRAIN_TYPE + " CARRIAGE_COUNT: " + trains[j].CARRIAGE_COUNT + " TRAIN_STATUS: " + trains[j].TRAIN_STATUS + "</option>";
        }
    }

    console.log(trains)
};

// Populates the parameter list depending on the selected parameter to search by.
async function populateParameterList()
{
	// Get page drop-down list elements.
	var dropdown = document.getElementById("searchParam");
	var searchByElement = document.getElementById("searchBy");
	dropdown.disabled = true;
	// When a different value on the search by drop-down list is selected.
	searchByElement.onchange = function()
	{
		// Get the current value of the search by drop-down list.
		searchBy = searchByElement.options[searchByElement.selectedIndex].value;
		if (searchBy == "TRAIN_STATUS")
		{
			dropdown.disabled = false;
			dropdown.innerHTML = "<option>Choose...</option>";

			// Fill the parameter dropdown list.
			dropdown.innerHTML += "<option>Operational</option>";
			dropdown.innerHTML += "<option>Faulty</option>";
			dropdown.innerHTML += "<option>Decommissioned</option>";
		}
		if (searchBy == "TRAIN_TYPE")
		{
			dropdown.disabled = false;
			dropdown.innerHTML = "<option>Choose...</option>";

			// Fill the parameter dropdown list.
			dropdown.innerHTML += "<option>Steam</option>";
			dropdown.innerHTML += "<option>Diesel</option>";
			dropdown.innerHTML += "<option>Electric</option>";
			dropdown.innerHTML += "<option>Transforming</option>";
		}

		else if (searchBy == "displayAll")
        {
            dropdown.disabled = true;
            populateDropDown();

        }
	}
};

//get values from selections and append to a single string
async function submitTrain()
{
    var JSONString;
    var TRAIN_TYPE = document.getElementById("trainType");
    TRAIN_TYPE = TRAIN_TYPE.options[TRAIN_TYPE.selectedIndex].value;

    var CARRIAGE_COUNT = document.getElementById("carriageCount");
    CARRIAGE_COUNT = CARRIAGE_COUNT.value;

    if (TRAIN_TYPE == "")
	{
		showErrorModal("Please select a train type!");
    }

    var TRAIN_STATUS = "";

    JSONString = { "TRAIN_TYPE": TRAIN_TYPE, "CARRIAGE_COUNT": CARRIAGE_COUNT, "TRAIN_STATUS": TRAIN_STATUS};

    console.log(JSONString);
    var result = await postRequest("trains", JSONString);
    if (result)
	  {
		Swal.fire({
            text: 'The train has been added!',
            type: 'success',
            confirmButtonText: 'Ok'
        });
    }
    else{
    	error();
    }
}

// Submit a delete request with the selected ID
async function removeTrain() {
    var trainID = document.getElementById("trainToRemove");
    trainID = trainID.options[trainID.selectedIndex].value;
    var result = await deleteRequest("trains", trainID);
		if (result) {
		Swal.fire({
            text: 'The train has been deleted!',
            type: 'success',
            confirmButtonText: 'Ok'
        });
		populateDropDown();
    } else {
		error();
    }
}

async function updateTrain(){

    var trainID = document.getElementById("trainToUpdate");
    trainID = trainID.options[trainID.selectedIndex].value;

		// Gets the status SearchBy drop-down list element from the page.
		var trainStatus = document.getElementById("statusUpdate");
		trainStatus = trainStatus.options[trainStatus.selectedIndex].value;

		// Gets the status SearchBy drop-down list element from the page.
		var trainType = document.getElementById("trainTypeUpdate");
		trainType = trainType.options[trainType.selectedIndex].value;
		// Gets the status SearchBy drop-down list element from the page.
		var carriageCount = document.getElementById("carriageCountUpdate");
		carriageCount = carriageCount.value;

    JSONString = {"TRAIN_ID": trainID, "TRAIN_TYPE": trainType, "CARRIAGE_COUNT": carriageCount, "TRAIN_STATUS": trainStatus};
		var result = await putRequest("trains/"+trainID, JSONString);
    if (result) {
		Swal.fire({
            text: 'The train has been updated!',
            type: 'success',
            confirmButtonText: 'Ok'
        });
		populateDropDown();
    } else {
		error();
    }
}

// Searches for a set of train records that have data matching the input search parameters
async function searchTrain() {

	// Get train data from the API
    var trains = await getRequest("trains");

    if (trains) {
	    Swal.fire({
	        text: 'The list of trains has been updated',
	        type: 'success',
	        confirmButtonText: 'Ok'
	    });
	}else {
        error();
    }

	// Retrieves the searchParam drop-down list element from the page.
	var searchParam = document.getElementById("searchParam");
    searchParam = searchParam.options[searchParam.selectedIndex].value;

	// Gets the searchBy drop-down list element from the page.
    var searchBy = document.getElementById("searchBy");
    searchBy = searchBy.options[searchBy.selectedIndex].value;

    var output = document.getElementById("trainToRemove")

    //reset the dropdown
    output.innerHTML = "<option value=''>Choose...</option>";

    //for each journey
    for (index in trains) {

        //for each header
        for (data in trains[index]) {

            //check if we are searching by header and if search param matches
            if (data == searchBy && trains[index][data] == searchParam) {
                var outputString;

                //add the data from journey
                outputString = "<option value ='" + trains[index].TRAIN_ID + "'>";
                for (i in trains[index]) {

                    outputString += " " + i + ": " + trains[index][i];
                }

                outputString += "</option>";

                output.innerHTML += outputString;
            }
        }
    }

}


async function searchTrainManage() {
	// Get train data from the API
  var trains = await getRequest("trains");

	// Gets the status SearchBy drop-down list element from the page.
  var statusSearchBy = document.getElementById("statusSearchBy");
  statusSearchBy = statusSearchBy.options[statusSearchBy.selectedIndex].value;

	// Gets the status SearchBy drop-down list element from the page.
  var trainTypeSearchBy = document.getElementById("trainTypeSearchBy");
  trainTypeSearchBy = trainTypeSearchBy.options[trainTypeSearchBy.selectedIndex].value;
	// Gets the status SearchBy drop-down list element from the page.
	var carriageCount = document.getElementById("carriageCountManage");
  carriageCount = carriageCount.value;

	// Gets the searchBy drop-down list element from the page.
	var output = document.getElementById("trainToUpdate")

  //reset the dropdown
  output.innerHTML = "<option value=''>Choose...</option>";

	for (j = 0; j < trains.length; j++) {
			if (trains[j].TRAIN_STATUS == statusSearchBy && trains[j].TRAIN_TYPE == trainTypeSearchBy &&
				trains[j].CARRIAGE_COUNT == carriageCount){
			var outputString;
			//add the data from journey
			outputString = "<option value ='" + trains[j].TRAIN_ID + "'>";
			for (i in trains[j]) {
				outputString += " " + i + ": " + trains[j][i];
			}
			outputString += "</option>";
			output.innerHTML += outputString;
		}
	}
}



function showErrorModal(errorMessage)
{
			// Get the submitTrainModal element
		var modal = document.getElementById('unsuccessfulTrainModal');
		modal.style.display = "block";

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("unsuccessfulModalClose")[0];

		var text = document.getElementById('errorMessage');
		text.innerHTML = errorMessage;

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event)
		{
			if (event.target == modal)
			{
				modal.style.display = "none";
			}
		}

		// When the user clicks on <span> (x), close the modal
		span.onclick = function()
		{
			modal.style.display = "none";
		}
        return;
}

function showSubmitModal()
{
	    // Get the submitTrainModal element
		var modal = document.getElementById('submitTrainModal');
		modal.style.display = "block";

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("submitModalClose")[0];

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event)
		{
			if (event.target == modal)
			{
				modal.style.display = "none";
			}
		}

		// When the user clicks on <span> (x), close the modal
		span.onclick = function()
		{
			modal.style.display = "none";
		}
}

function showDeleteModal()
{
		// Get the submitTrainModal element
		var modal = document.getElementById('deleteModal');
		modal.style.display = "block";

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("deleteModalClose")[0];

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event)
		{
			if (event.target == modal)
			{
				modal.style.display = "none";
			}
		}

		// When the user clicks on <span> (x), close the modal
		span.onclick = function()
		{
			modal.style.display = "none";
		}
        return;
}

function showUpdateModal()
{
		// Get the submitTrainModal element
		var modal = document.getElementById('updateModal');
		modal.style.display = "block";

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("updateModalClose")[0];

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event)
		{
			if (event.target == modal)
			{
				modal.style.display = "none";
			}
		}

		// When the user clicks on <span> (x), close the modal
		span.onclick = function()
		{
			modal.style.display = "none";
		}
        return;
}
function error()
{
    Swal.fire({
        text: 'Oops, something went wrong! Please try again later.',
        type: 'error',
        confirmButtonText: 'Ok'
    });
}
