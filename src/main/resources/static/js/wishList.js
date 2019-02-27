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
            success: function (booleanDto) {
                var isLike = booleanDto.bool;
                var wishListCount = parseInt($("#wishListCount").text());
                if(isLike){
                    var fullId = "#heart-empty-" + type + "-" + modelId;
                    var emptyId = "#heart-full-" + type + "-" + modelId;
                    $(fullId).hide();
                    $(emptyId).show();
                    $("#wishListCount").text(wishListCount + 1)
                }else {
                    $("#heart-full-" + type + "-" + modelId).hide();
                    $("#heart-empty-" + type + "-" + modelId).show();
                    $("#wishListCount").text(wishListCount - 1)
                }
                isClick = false;
            },
            error: function () {
                window.location = "/500";
            }
        })
    }
})