$(document).ready(function () {
    var isCallOrderForm = false;
    var allRoomsBusied = $("#locale_allRoomsBusied").attr("content");
    var orderWrongData = $("#locale_orderWrongData").attr("content");
    var orderSucceed = $("#locale_orderSucceed").attr("content");
    $("#orderForm").on("submit", function (event) {
        event.preventDefault();
        if(!isCallOrderForm){
            isCallOrderForm = true;
            var whenDate = $("#whenDate").val();
            var daysCount = $("#daysCount").val();
            var adultCount = $("#adultCount").val();
            var childrenCount = $("#childrenCount").val();
            var hotelRoomId = $("#hotelRoomSelect").val();
            var hotelId = $("#hotelId").val();
            var rightValuesCount = 0;
            var userOrder = {"whenDate": null,
                "daysCount":"0",
                "adultCount": "0","childrenCount": "0",
                "hotelId": hotelId,
                "hotelRoomId": "-1"};

            if (whenDate == '' || whenDate.length < 10) {
                $("#whenDate").attr("style", "border: 1px solid red");
            } else {
                $("#whenDate").attr("style", "border: 1px solid grey");
                rightValuesCount++;
                var date = new Date(whenDate);
                // userOrder.whenDate = date.getFullYear() + '-' + (addNumberZero(parseInt(date.getMonth()) +1)) + '-'
                //     + addNumberZero(date.getDate()) + ' ' + addNumberZero(date.getHours()) + ':'
                //     + addNumberZero(date.getMinutes()) + ':' +addNumberZero(date.getSeconds());
                userOrder.whenDate = date;
            }
            if (daysCount <= 0 || daysCount.charAt(0) == "0") {
                $("#daysCount").attr("style", "border: 1px solid red");
            } else {
                $("#daysCount").attr("style", "border: 1px solid grey");
                rightValuesCount++;
                userOrder.daysCount = daysCount;
            }
            var rightAdAndChildValueCount =0;
            if (adultCount < 0 || adultCount.charAt(0) == "0") {
                $("#adultCount").attr("style", "border: 1px solid red");
            } else {
                $("#adultCount").attr("style", "border: 1px solid grey");
                rightAdAndChildValueCount++;
                userOrder.adultCount = adultCount;
            }
            if (childrenCount < 0 || childrenCount.charAt(0) == "0") {
                $("#childrenCount").attr("style", "border: 1px solid red");
            } else {
                $("#childrenCount").attr("style", "border: 1px solid grey");
                rightAdAndChildValueCount++;
                userOrder.childrenCount = childrenCount;
            }
            if(rightAdAndChildValueCount > 0){
                var adultAndChildSum = (parseInt(childrenCount) + parseInt(adultCount));
                if (adultAndChildSum <= 0) {
                    $("#childrenCount").attr("style", "border: 1px solid red");
                    $("#adultCount").attr("style", "border: 1px solid red");
                } else {
                    $("#childrenCount").attr("style", "border: 1px solid grey");
                    $("#adultCount").attr("style", "border: 1px solid grey");
                }
            }
            if (hotelRoomId == -1) {
                $(".nice-select").attr("style", "border: 1px solid red");
            } else {
                $(".nice-select").attr("style", "border: 1px solid grey");
                rightValuesCount++;
                userOrder.hotelRoomId = hotelRoomId;
            }
            if(rightValuesCount == 3 && rightAdAndChildValueCount > 0){
                $("#order-message").attr("style","display: none");
                $.ajax({
                    type: "POST",
                    url: "/order",
                    contentType: "application/json",
                    data: JSON.stringify(userOrder),
                    success: function (orderDto) {
                        if(orderDto.errorList.length > 0){
                            $.each(orderDto.errorList,function (i,error) {
                                if(error == 'allRoomsBusied'){
                                    $("#message-blog").text(allRoomsBusied);
                                }else {
                                    $("#message-blog").text(orderWrongData);
                                }
                            })
                        }else {
                            $("#message-blog").text(orderSucceed);
                            var busiedRoomsCount = parseInt($("#busied-rooms-count-text-" + hotelRoomId).text().split("  ")[1]) +1;
                            var allRoomsCount = parseInt($("#all-rooms-count-" + hotelRoomId).text().split("  ")[1]);
                            if(busiedRoomsCount < allRoomsCount){
                                $("#busied-rooms-count-text-" + hotelRoomId).text(":  " + busiedRoomsCount);
                                $("#free-rooms-count-" + hotelRoomId).text(":  " + (parseInt(allRoomsCount) - parseInt(busiedRoomsCount)));
                                $("#busied-rooms-count-blog-" + hotelRoomId).show();
                            }else {
                                $("#busied-rooms-count-blog-" + hotelRoomId).hide();
                                $("#all-rooms-busied-" + hotelRoomId).show();
                                $("#free-rooms-count-" + hotelRoomId).parent().hide();
                            }
                            $("#bookingCount").text(parseInt($("#bookingCount").text()) +1);
                            setEmptyValuesOrderForm();
                        }
                        $("#order-message").show();
                        isCallOrderForm = false;
                    },
                    error: function () {
                        window.location = "/500";
                    }
                })
            }else {
                isCallOrderForm = false;
            }
        }

    });

    $("#close-order-message").on("click",function () {
        $("#order-message").attr("style","display: none");
    })
});

function addNumberZero(number) {
    number+="";
    if(number.length == 1){
        return "0" + number;
    }else {
        return number;
    }
}

function setEmptyValuesOrderForm() {
    $("#whenDate").val(null);
    $("#daysCount").val("0");
    $("#adultCount").val("0");
    $("#childrenCount").val("0");
    var orderPriceStrArray = $("#orderPrice").text().split(" ");
    $("#orderPrice").text(orderPriceStrArray[0] + " 0 " + " $");
}