$(document).ready(function () {
    var isClick = false;

    $(".searchSelect").on("change",function () {
        if(!isClick){
            isClick = true;
            var id = $(this).attr("id");
            if(id == 'region'){
                var value = $("#" + id).val();
                if(value != -1){
                    $.ajax({
                        type: "GET",
                        async: false,
                        url: "/city/by/regionId/" + value,
                        contentType: "application/json",
                        success: function (cities) {
                            $("#city").empty();
                            $.each(cities,function (i, city) {
                                var cityName = getCityOrRegionName(city);
                                var option = "<option  value='" + city.id + "'>" + cityName + "</option>";
                                $("#city").append(option);
                                isClick = false;
                            });
                            $("#cityDiv").show();
                        },error: function () {
                            window.location = "/500";
                        }
                    })
                }else {
                    $("#cityDiv").hide();
                    $("#city").empty();
                }
            }
            pageNumber = 0;
            loadHotels(false,false,false);
            isClick = false;
        }
    });

    $(".searchInput").on("blur",function () {
        if(!isClick){
            isClick = true;
            pageNumber = 0;
            loadHotels(false,false,false);
            isClick = false;
        }
    });

})