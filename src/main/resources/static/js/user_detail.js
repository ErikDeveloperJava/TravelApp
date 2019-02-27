$(document).ready(function () {

    $("#userChangeForm").on("submit",function (event) {
        event.preventDefault();
        var name = $("#name").val();
        var surname = $("#surname").val();
        var email = $("#email").val();
        var password = $("#password").val();
        var rePassword = $("#rePassword").val();
        var regionId = $("#regionSelect").val();
        var cityId = $("#citySelect").val();
        var address = $("#address").val();
        var userArray =
            [name,surname,email,
            password,rePassword,regionId,
            cityId,address];
        var rightValuesCount = checkData(userArray);
        if(rightValuesCount == 8){
            
        }
    })
});

function checkData(userArray) {
    var rightValuesCount = 0;
    $.each(userArray,function (i, value) {
        if(isValidData(getName(i),value)){
            rightValuesCount++;
        }
    })
    return rightValuesCount;
}

function isValidData(name, value) {
    switch (name) {
        case "name":
            if(value.length < 2 || value.length > 40){
                $("#nameError").show();
                return false;
            }else {
                $("#nameError").hide();
                return true;
            }
        case "surname":
            if(value.length < 2 || value.length > 50){
                $("#surnameError").show();
                return false;
            }else {
                $("#surnameError").hide();
                return true;
            }
        case "email":
            if(value.length < 9 || value.length > 100){
                $("#emailError").show();
                return false;
            }else {
                $("#emailError").hide();
                return true;
            }
        case "password":
            if(value != '' && (value.length < 2 || value.length > 40)){
                $("#passwordError").show();
                return false;
            }else {
                $("#passwordError").hide();
                return true;
            }
        case "rePassword":
            if(value != '' && (value.length < 2 || value.length > 40)){
                $("#rePasswordError").show();
                return false;
            }else {
                $("#rePasswordError").hide();
                return true;
            }
        case "regionId":
            if(value == -1){
                $("#regionError").show();
                return false;
            }else {
                $("#regionError").hide();
                return true;
            }
        case "cityId":
            return $("#regionSelect").val() != -1;
        case "address":
            if(value.length < 10 || value.length > 255){
                $("#addressError").show();
                return false;
            }else {
                $("#addressError").hide();
                return true;
            }
    }
}

function getName(index) {
    switch (index) {
        case 0:
            return "name";
        case 1:
            return "surname";
        case 2:
            return "email";
        case 3:
            return "password";
        case 4:
            return "rePassword";
        case 5:
            return "regionId";
        case 6:
            return "cityId";
        default:
            return "address";
    }
}