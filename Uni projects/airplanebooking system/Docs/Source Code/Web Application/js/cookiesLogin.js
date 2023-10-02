// Creates a cookie in minutes
function setCookie(cname,cvalue,exdays) {
  var d = new Date();
  d.setTime(d.getTime() + (exdays*60*1000));
  var expires = "expires=" + d.toGMTString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
// Gets the cookie
function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}
// Checks if the admin cookie is there otherwise sent to homepage
function checkCookieAdmin() {
  var user=getCookie("loginAdmin");
  if (user != "") {
    return true;
  } else {
      location.href = "homePage.html";
     }
  }

// Checks if the controller cookie is there otherwise sent to homepage
function checkCookieController() {
  var user=getCookie("loginController");
  if (user != "") {
    return true;
  } else {
      location.href = "homePage.html";
     }
  }
