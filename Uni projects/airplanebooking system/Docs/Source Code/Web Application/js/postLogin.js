function postlogin(){
  document.getElementById("error").innerHTML = "";
  var username = document.forms["LoginForm"]["username"].value;
  var password = document.forms["LoginForm"]["password"].value;
  suffix = "login/";
  data =  {"USERNAME":username,"PASSWORD":password};
  var xhr = new XMLHttpRequest();
  proxyuri = "https://cors-anywhere.herokuapp.com/http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/";
  xhr.open("POST", proxyuri + suffix, true);
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.send(JSON.stringify(data));
  xhr.onreadystatechange = function (storage) {
      if (this.readyState == 4) {
          if (this.status == 200) {
              if(username.charAt(0) == 'A'){
                setCookie("loginAdmin", username, 10);
                document.getElementById("error").innerHTML ="";
                location.href = "adminLoginAnimation.html";

              }
              else if (username.substring(0,2) == 'CO') {
                setCookie("loginController",username,10)
                document.getElementById("error").innerHTML ="";
                location.href = "controllerLoginAnimation.html";
              }
          }
      }
      else {
              document.getElementById("error").innerHTML = "Username or Password is incorrect";
            };

  }
  return false;
}
