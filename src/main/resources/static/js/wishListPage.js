$(document).ready(function () {
    var isClick = false;

    $(document).on("click",".wishListChange",function () {
        if(!isClick){
            isClick = true;
            var array = $(this).attr("id").split("-");
            var modelId = array[3];
            var type = array[2];
            sendWishList(modelId,type);
        }
    });

    function sendWishList(modelId,type) {
        $.ajax({
            type: "POST",
            url: "/user/wish_list",
            contentType: "application/json",
            data: JSON.stringify({"modelId": modelId,"modelType": type}),
            success: function () {
                pageNumber = 0;
                var wishListCount = parseInt($("#wishListCount").text());
                $("#wishListCount").text(wishListCount - 1)
                loadHotels(false,false,true);
                isClick = false;
            },
            error: function () {
                window.location = "/500";
            }
        })
    }
})