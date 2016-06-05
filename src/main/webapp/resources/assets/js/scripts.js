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

    if ($("#map")[0]) {
        ymaps.ready(initDemo);
        var myMap;
    }

    function initDemo() {
        myMap = new ymaps.Map($("#map")[0], {
            center: [55.76, 37.64],
            zoom: 7
        });
    }

    var coverageMap;
    var myCircle;
    if ($("#coverageMap")[0]) {
        ymaps.ready(initCoverage);
    }

    function initCoverage() {
        coverageMap = new ymaps.Map($("#coverageMap")[0], {
                center: [55.76, 37.64],
                zoom: 11
            }
        );

        $.ajax({
                url: "http://isharipov.com/rest/map",
                crossDomain: true,
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    data.forEach(function (item, i, arr) {
                            var providerColor;
                            switch (item.provider) {
                                case 'yandex':
                                    providerColor = '#FFFF0077';
                                    break;
                                case 'google':
                                    providerColor = '#FF000077';
                                    break;
                                case 'mozilla' :
                                    providerColor = '#00FF0077';
                                    break;
                                case 'skyhook' :
                                    providerColor = '#0000FF77';
                                    break;
                            }
                            myCircle = new ymaps.Circle(
                                [[
                                    item.lat,
                                    item.lng
                                ], item.accuracy],
                                {
                                    hintContent: item.provider,
                                    balloonContentHeader: item.provider,
                                    balloonContentBody: item.accuracy
                                },
                                {
                                    fillColor: providerColor,
                                    strokeWidth: 0
                                }
                            );
                            coverageMap.geoObjects.add(myCircle);
                        }

                    );
                }
            }
        );
        $("#cover").css("display", "none")
    }

    var circleGeometry;
    var circleGeoObject;
    $("#getButtonWifi").click(function () {
        var wifiArr = $('#TextBoxesGroupBssid').children().find('input').toArray();
        var cellArr = $('#TextBoxesGroupSsid').children().find('input').toArray();
        var wifiValues = [];
        var cellValues = [];
        wifiArr.forEach(function (item, i, arr) {
            wifiValues.push(item.value);
        });

        cellArr.forEach(function (item, i, arr) {
            cellValues.push(item.value);
        });

        $.ajax({
            url: "http://isharipov.com/rest/all?bssid=" + wifiValues[0] + "&ssw=" + wifiValues[1] + "&bssid="
            + wifiValues[2] + "&ssw=" + wifiValues[3] + "&bssid=" + wifiValues[4] + "&ssw=" + wifiValues[5]
            + "&mcc=" + cellValues[0] + "&mnc=" + cellValues[1] + "&lac=" + cellValues[2] + "&cid=" + cellValues[3] + "&ssc=" + cellValues[4]
            + "&mcc=" + cellValues[5] + "&mnc=" + cellValues[6] + "&lac=" + cellValues[7] + "&cid=" + cellValues[8] + "&ssc=" + cellValues[9],
            crossDomain: true,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
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


