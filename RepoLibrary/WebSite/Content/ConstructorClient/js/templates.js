var templates = {
    screenView: function(id) {
        return "<div screen-view='" + id + "'><div class='screen-name'></div><div class='screen-controls'></div></div>";
    },
    screen: function(id, name) {
        return "<div screen='" + id + "'>" + name + "</div>";
    },
    screenEditor: function(id) {
        return "<div screen-editor='" + id + "'></div>";
    },
    mainButton: function(id) {
        return "<div class='main-button' screen='" + id + "'></div>";
    },
    confirmDataSend: "Подтвердите отправку данных",
    successDataSend: "Данные успешно отправлены",
    waitDataSend: "Отправка данных...",
    errorDataSend: "Ошибка отпрапвки данных"
}