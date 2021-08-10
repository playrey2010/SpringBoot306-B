function validate() {
    // result boolean variable
    var formIsOkay = true;
    // Extra: boolean variable to place focus on username when multiple fields are missing
    var usernameMissing = false;
    // find the username field and its value, then repeat for password.
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    // store error span messages in variables
    var userError = document.getElementById("user-error-message");
    var passwordError = document.getElementById("password-error-message");
    // reset previous error messages
    var errorMessages= document.getElementsByTagName("span");
    for (let i = 0; i< errorMessages.length; i++){
        errorMessages[i].innerHTML = "";
    }

    // check if username field is empty
    if (!username) {
        userError.innerHTML = "Please enter username";
        document.getElementById("username").focus();
        usernameMissing = true;
        formIsOkay = false;
    }
        // check if username has only spaces. /.../ is a Regular expression literal (you could put a string here). Instead, I wrote \s which means "whitespace".The g after my expression signifies "global", meaning
        // the string query/search should pick up "all" characters that match the expression.
        // Final thing to remember: booleans can be expressed as true, false, but also 0 or 1. Here, I replace all characters matching whitespace with a '' (empty string)...
    // so that if the username only had whitespace, in will have a length of 0 now.
    else if (!username.replace(/\s/g, '').length){
        userError.innerHTML = "Only whitespace detected";
        document.getElementById("username").focus();
        document.getElementById("username").value = "";
        usernameMissing = true;
        formIsOkay = false;
    }
    if (!password){
        passwordError.innerHTML = "Please enter a password";
        if (!usernameMissing){
            document.getElementById("password").focus();
        }
        formIsOkay = false;
    }

    return formIsOkay;
}
