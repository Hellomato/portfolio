// This function sets the cookies to be out of date
// and then sends the users to the signoutAnimation
function Logout(){
setCookie("loginAdmin", "username", -60);
setCookie("loginController","username",-60)
location.href = "signoutAnimation.html";
}
