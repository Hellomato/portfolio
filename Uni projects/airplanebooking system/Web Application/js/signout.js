(function() {

	var preload = document.getElementById("preload");
	var loading = 0; //the timer
	var id = setInterval(frame, 64);

	//when the loading timer gets to 100, the page is replaced by the secure page
	function frame(){
		if(loading == 100){
			clearInterval(id); // this stops the time
			window.location.replace("homePage.html", "_self")
		} else{
			loading = loading +1;
			if(loading == 90){
				preload.style.animation = "fadeout 2s ease";
			}
		}
	}
})();