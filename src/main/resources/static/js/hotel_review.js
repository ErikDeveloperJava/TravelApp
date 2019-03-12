$(document).ready(function () {
    $("#reviewForm").on("submit",function (event) {
        event.preventDefault();
        var rating = $("#review_rating").val();
        var message = $("#review_message").val();
        var hotelId = $("#hotelId").val();
        if(message.length < 10){
            $("#review-message-error").show();
        }else {
            $("#review-message-error").hide();
            var reviewModel = {
                "rating":rating,
                "message":message,
                "hotelId": hotelId
            };
            $.ajax({
                type: "POST",
                url: "/hotel/review",
                data: JSON.stringify(reviewModel),
                contentType: "application/json",
                success: function (response) {
                    var messageError = response.messageError;
                    if(messageError){
                        $("#review-message-error").show();
                    }else {
                        var reviewSum = $("#reviewSum").text();
                        $("#reviewSum")
                            .text(parseInt(reviewSum) + 1);
                        var rating = response.rating;
                        $("#reviewRating").text(rating);
                        var ratingPercentList = response.ratingPercentList;
                        $.each(ratingPercentList,function (i, ratingPercent) {
                            var ratingNumber = ratingPercent.ratingNumber;
                            $("#rating-percent-" + ratingNumber)
                                .attr("style","width: " + ratingPercent.percent + "%;")
                            $("#rating-percent-" + ratingNumber)
                                .attr("aria-valuenow",ratingPercent.percent + "%")
                        });
                        var reviewDiv = '<div class="review-box clearfix">\n' +
                            '<figure class="rev-thumb">\n';
                        if(response.review.user.imgUrl != null){
                            reviewDiv+='<img style="width: 80px;height: 80px"\n' +
                                ' src="/resources/' + response.review.user.imgUrl + '" alt="">\n';

                        }else {
                            reviewDiv+='<img style="width: 80px;height: 80px"\n' +
                                ' src="/resources/default/user.png" alt="">\n';
                        }
                        reviewDiv+=    '</figure>\n' +
                            '<div class="rev-content">\n' +
                            '<h4>' + response.review.user.name + ' ' + response.review.user.surname + '</h4>\n'+
                            '<div class="rating">\n';
                        var reviewRating = response.review.rating;
                        for (var i = 1; i < 6; i++) {
                            if(i <= reviewRating){
                                reviewDiv+='<i class="icon_star voted"></i>\n';
                            }else {
                                reviewDiv+='<i class="icon_star"></i>\n';
                            }
                        }
                        reviewDiv+= '</div>\n' +
                            '<div class="rev-info" style="font-size: 13px;">' + response.review.sendDate + '</div>\n' +
                            '<div class="rev-text">\n' +
                            '<p>' + response.review.message +'</p>\n' +
                            '</div>\n' +
                            '</div>\n' +
                            '</div>';
                        $("#review-blog").append(reviewDiv);
                        $("#review_message").val("");
                        $("#review_rating").selected("1");
                    }
                },
                error: function () {
                    window.location = '/500';
                }
            })
        }
    })
});