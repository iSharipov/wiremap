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
        console.log(1);
        var wifiArr = $('#TextBoxesGroupBssid').children().find('input').toArray();
        var cellArr = $('#TextBoxesGroupSsid').children().find('input').toArray();
        var wifiValues = [];
        var cellValues = [];
        wifiArr.forEach(function (item, i, arr) {
            wifiValues.push(item.value);
        });

        cellArr.forEach(function(item, i, arr){
            cellValues.push(item.value);
        });

        $.ajax({
            url: "http://isharipov/rest/all?bssid=" + wifiValues[0] + "&ssw=" + wifiValues[1] + "&bssid="
            + wifiValues[2] + "&ssw=" + wifiValues[3] + "&bssid=" + wifiValues[4] + "&ssw=" + wifiValues[5]
            + "&mcc="+cellValues[0]+ "&mnc="+cellValues[1]+ "&lac="+cellValues[2]+ "&cid="+cellValues[3]+ "&ssc="+cellValues[4]
            + "&mcc="+cellValues[6]+ "&mnc="+cellValues[7]+ "&lac="+cellValues[8]+ "&cid="+cellValues[9]+ "&ssc="+cellValues[10],
            crossDomain: true,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
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


