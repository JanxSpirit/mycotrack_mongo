function randomKeyToggle() {
    if ($('#keyCheckbox').is(':checked')) {
        $('#keyText').attr('disabled', true);
    } else {
        $('#keyText').removeAttr('disabled');
    }
}