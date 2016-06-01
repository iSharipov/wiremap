jQuery(document).ready(function () {
    /*
     Fullscreen background
     */
    $.backstretch("/resources/assets/img/backgrounds/map1.jpg");
    /*
     Login form validation
     */
    $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function () {
        $(this).removeClass('input-error');
    });

    $('.login-form').on('submit', function (e) {

        $(this).find('input[type="text"], input[type="password"], textarea').each(function () {
            if ($(this).val() == "") {
                e.preventDefault();
                $(this).addClass('input-error');
            }
            else {
                $(this).removeClass('input-error');
            }
        });
    });

    /*
     Registration form validation
     */
    $('.registration-form input[type="text"], .registration-form textarea').on('focus', function () {
        $(this).removeClass('input-error');
    });

    $('.registration-form').on('submit', function (e) {

        $(this).find('input[type="text"], textarea').each(function () {
            if ($(this).val() == "") {
                e.preventDefault();
                $(this).addClass('input-error');
            }
            else {
                $(this).removeClass('input-error');
            }
        });
    });

    ymaps.ready(init);
    var myMap;

    function init() {
        myMap = new ymaps.Map("map", {
            center: [55.76, 37.64],
            zoom: 7
        });
    }

    var circleGeometry;
    var circleGeoObject;
    $("#getButtonWifi").click(function () {
        var wifiArr = $('#TextBoxesGroupBssid').children().find('input').toArray();
        var values = [];
        wifiArr.forEach(function (item, i, arr) {
            values.push(item.value);
        });

        $.ajax({
            url: "http://isharipov.com/rest/mac?bssid=" + values[0] + "&ssw=" + values[1] + "&bssid="
            + values[2] + "&ssw=" + values[3] + "&bssid=" + values[4] + "&ssw=" + values[5],
            crossDomain: true,
            type: 'GET',
            dataType: 'jsonp',
            success: function (data) {
                //data = callback(data);
                console.log(circleGeometry === undefined);
                if (circleGeometry === undefined) {
                    circleGeometry = new ymaps.geometry.Circle([data.lat, data.lng], data.accuracy);
                    circleGeoObject = new ymaps.GeoObject({
                        geometry: circleGeometry,
                        properties: {
                            hintContent: data.provider,
                            balloonContentHeader: data.provider,
                            balloonContentBody: data.accuracy
                        }
                    });
                } else {
                    circleGeometry.setCoordinates([data.lat, data.lng]);
                    circleGeometry.setRadius(data.accuracy);
                    circleGeoObject.properties.set("hintContent", data.provider);
                    circleGeoObject.properties.set("balloonContentHeader", data.provider);
                    circleGeoObject.properties.set("balloonContentBody", data.accuracy);
                }

                myMap.geoObjects.add(circleGeoObject);
                myMap.setCenter([data.lat, data.lng], 15, {
                    checkZoomRange: true
                });
            },
            error: function (e) {
                console.log(e);
            }
        });
    });
});


