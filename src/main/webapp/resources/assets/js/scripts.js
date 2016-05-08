/*
 Fullscreen background
 */
$.backstretch("/resources/assets/img/backgrounds/map1.jpg");

jQuery(document).ready(function () {


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

    $(document).on('click', '.btn-add-wifi', function(e)
    {
        e.preventDefault();

        var controlForm = $('.controls-wifi form:first'),
            currentEntry = $(this).parents('.entry-wifi:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);

        newEntry.find('input').val('');
        controlForm.find('.entry-wifi:not(:last) .btn-add-wifi')
            .removeClass('btn-add-wifi').addClass('btn-remove')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="glyphicon glyphicon-minus"></span>');
    }).on('click', '.btn-remove', function(e)
    {
        $(this).parents('.entry-wifi:first').remove();

        e.preventDefault();
        return false;
    });

    $(document).on('click', '.btn-add-cell', function(e)
    {
        e.preventDefault();

        var controlForm = $('.controls-cell form:first'),
            currentEntry = $(this).parents('.entry-cell:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);

        newEntry.find('input').val('');
        controlForm.find('.entry-cell:not(:last) .btn-add-cell')
            .removeClass('btn-add-cell').addClass('btn-remove')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="glyphicon glyphicon-minus"></span>');
    }).on('click', '.btn-remove', function(e)
    {
        $(this).parents('.entry-cell:first').remove();

        e.preventDefault();
        return false;
    });
});
