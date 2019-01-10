var register = document.getElementById("register");
var cancel = document.getElementById("cancel");
var container = document.getElementById("container");
var login_box = document.getElementById("login_box");
var register_box = document.getElementById("register_box");

// go to register page when it's click
function register_button() {
    // alert("register");
    container.style.height = 150 + "px";
    login_box.style.display = "none";
    document.getElementsByName("username")[0].value = "";
    document.getElementsByName("password")[0].value = "";
    register_box.style.display = "block";
};

// go back to login page when cancel
function login_button() {
    // alert("cancel");
    container.style.height = 130 + "px";
    register_box.style.display = "none";
    document.getElementsByName("username")[1].value = "";
    document.getElementsByName("password")[1].value = "";
    document.getElementsByName("re-password")[0].value = "";
    login_box.style.display = "block";
};

// test username and password without using database
function test_login() {
    $("#login_form").submit((event) => {
        // alert($(this)[0]); // window
        // console.log($("input")[0].value);
        event.preventDefault();
        if ($("input[name=username]").val() === "iamusername" && $("input[name=password]").val() === "iampassword") {
            window.location.replace("main.html");
        } else {
            $("#login_span").text("You enter wrong account info!").show().fadeOut(3000);
            return false; // don't submit
        }
    });
};

// test register function
function test_register() {
    $("#register_form").submit(function(event) {
        // alert("register");
        event.preventDefault();
        var reg_un = /^\w{6,12}$/; // username must be 6-12 Eng or Num
        var reg_pw = /^.{6,20}$/; // password must be length of 6-20

        // alert($("#register_form input[name=username]").val());
        // if the username and password does not follow the rule, end submit
        if (!reg_un.test($("#register_form input[name=username]").val())) {
            $("#register_span").text("Invalid Username!").show().fadeOut(3000);
            return false;
        }
        if (!reg_pw.test($("#register_form input[name=password]").val())) {
            $("#register_span").text("Invalid Password!").show().fadeOut(3000);
            return false;
        }
        if ($("#register_form input[name=password]").val() !== $("#register_form input[name=re-password]").val()) {
            $("#register_span").text("Different Password!").show().fadeOut(3000);
            return false;
        }
        if ($("#register_form input[name=password]").val() === $("#register_form input[name=re-password]").val()) {
            $("#register_span").text("Success!").show().fadeOut(3000);
            return true;
        }
    });
}

// submit login form
$("#login_form").submit((event) => {
    // alert("im login");
    event.preventDefault();
    $.post('api/login', $("#login_form").serialize(), function(data) {
        var resultDataJson = JSON.parse(data);

        console.log("handle login response");
        console.log(resultDataJson);
        console.log(resultDataJson["status"]);

        // If login success, redirect to index.html page
        if (resultDataJson["status"] === "success") {
            window.location.replace("main.html");
        }
        // If login fail, display error message on <span> with id "login_span"
        else {
            console.log("show error message");
            console.log(resultDataJson["message"]);
            $("span").text(resultDataJson["message"]).show().fadeOut(3000);
        }
    });
});

// submit register form
$("#register_form").submit((event) => {
    event.preventDefault();
    $.post('api/register', $("#register_form").serialize(), function(data) {
        var resultDataJson = JSON.parse(data);

         // If register success, redirect to login page
        if (resultDataJson["status"] === "success") {
            $("#register_span").text(resultDataJson["message"]).show().fadeOut(3000);
            setTimeout(login_button, 2500);
        }
        // If login fail, display error message on <span> with id "register_span"
        else {
            console.log("show error message");
            console.log(resultDataJson["message"]);
            $("span").text(resultDataJson["message"]).show().fadeOut(3000);
        }
    });    
});