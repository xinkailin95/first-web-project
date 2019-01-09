// test username and password without using database
function test() {
     $("#login_form").submit((event) => {
         // alert($(this)[0]); // window
         // console.log($("input")[0].value);
         event.preventDefault();
         if ($("input[name=username]").val() === "username" && $("input[name=password]").val() === "password") {
             window.location.replace("main.html");
         } else {
             $("span").text("You enter wrong account info!").show().fadeOut(3000);
         }
     });
};

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login success, redirect to index.html page
    if (resultDataJson["status"] === "success") {
        window.location.replace("main.html");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {

        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("span").text(resultDataJson["message"]).show().fadeOut(3000);
    }
}
/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    $.post(
        "api/login",
        // Serialize the login form to the data sent by POST request
        $("#login_form").serialize(),
         (resultDataString) => handleLoginResult(resultDataString));

}

// Bind the submit action of the form to a handler function
$("#login_form").submit((event) => submitLoginForm(event));

