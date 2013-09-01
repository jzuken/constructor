var Screen = function(data) {
    this.name = data.name;
    this.buttonText = data.name.substring(0, 10);

    this.disabled = false;

    this.params = data.params;
    this.loadScreen = data.loadScreen;
    this.initView = data.initView;
    this.initEditor = data.initEditor;

    this.serialize = data.serialize;
    this.loadSaved = data.loadSaved;
}

var screens = {
    items: {},
    addScreen: function(id, screen) {
        this.items[id] = screen;

        var me = this;

        if (id != 'main') {
            this.items.main.params.items.push(id);
        }

        $("#phone-screen").append(templates.screenView(id));
        screen.initView($("#phone-screen [screen-view=" + id + "] .screen-controls"));

        $("#screens").append(templates.screen(id, screen.name));
        $("#screens [screen=" + id + "]").button();
        $("#screens [screen=" + id + "]").click(function() {
            me.loadSelectedScreen(id);
        });

        $("#screen-editors").append(templates.screenEditor(id));
        screen.initEditor($("#screen-editors [screen-editor=" + id + "]"));
    },
    changeScreenTitle: function(id, title) {
        this.items[id].name = title;

        $("#phone-screen [screen-view=" + id + "] .screen-name").text(title);
    },
    changeSelectedScreenTitle: function(title) {
        this.changeScreenTitle(this.selectedScreen, title);
    },
    loadScreens: function() {
        var $el = $("#phone-screen [screen-view=main]");
        $el.find(".main-button").remove();

        for (var i = 0, l = this.items.length; i < l; ++i) {
            var screen = this.items[i];

            if (!screen.disabled) {
                $el.append(templates.mainButton(screen.id));
            }
        }

        var me = this;
        $el.find(".main-button").each(function() {
            me.loadMainButton($(this).attr("screen"));
        });

        $el.find(".main-button").click(function() {
            me.loadSelectedButton($(this).attr("screen"));
        });

        this.loadSelectedScreen("main");
    },
    selectedScreen: "",
    loadSelectedScreen: function(id) {
        if (typeof(id) != 'undefined') {
            this.selectedScreen = id;
        }

        var $el = $("#phone-screen div[screen-view=" + this.selectedScreen + "]");

        $("#phone-screen div[screen-view]").css("display", "none");
        $el.css("display", "block");

        var screen = this.items[this.selectedScreen];

        this.items[id].loadScreen();

        $("div[screen-editor]").css("display", "none");
        $("div[screen-editor=" + id + "]").css("display", "block");

        $("#screen-title").val(this.items[id].name);

        this.screenStylesPanel.setEl($("#phone-screen [screen-view=" + id + "]"), function(style) {
            screen.style = style;
        })
    },
    serialize: function() {
        var res = {
            main: {
                style: this.items.main.style,
                title: this.items.main.name
            }
        };

        var other = [];
        var order = this.items.main.params.items;
        for (var i = 0, l = order.length; i < l; ++i) {
            var id = order[i].id;
            var screen = this.items[id];

            if (!screen.disabled) {
                other.push({
                    id: id,
                    style: screen.style,
                    title: screen.name,
                    buttonStyle: screen.buttonStyle,
                    buttonText: screen.buttonText
                });
            }
        }

        res.other = other;

        return JSON.stringify(res);
    },
    loadSaved: function(data) {
        for (var i = 0, l = this.keys.length; i < l; ++i) {
            var key = this.keys[i];
            var screen = this.items[key];

            screen.disabled = true;

            var mainItem = this.items.main.params.getScreen(key);
            if (mainItem) {
                mainItem.disabled = true;
            }
        }

        for (var i = 0, l = data.length; i < l; ++i) {
            var item = data[i];
            var screen = this.items[item.id];
            screen.disabled = false;
            var mainItem = this.items.main.params.getScreen(item.id);
            if (mainItem) {
                mainItem.disabled = false;
            }

            var commonData = item.common;
            screen.bgColor = commonData.bgColor;
            screen.textColor = commonData.textColor;
            screen.name = commonData.name;

            screen.loadSaved(item.data);
        }
    }
};

var currentStep = 0;

var loadStep = function(step) {
    if (typeof(step) === "number") {
        currentStep = step;
    }

    switch(currentStep) {
    case 0:
        $("#editors [step=1] div[screen]").each(function() {
            var screen = screens.items[$(this).attr("screen")];

            if (screen) {
                if (screen.disabled) {
                    $(this).hide();
                } else {
                    $(this).show();
                    $(this).text(screen.name);
                }
            }
        });

        $("#phone-screen .screen-name").each(function() {
            var screen = screens.items[$(this).parent().attr("screen-view")];

            if (screen) {
                $(this).text(screen.name);
            }
        });

        screens.loadScreens();
        screens.loadSelectedScreen("main");
        break;
    case 1:
        $(".send-status").html(templates.confirmDataSend);
        break;
    default:
        break;
    }

    $("#editors [step]").css("display", "none");
    $("#editors [step=" + currentStep + "]").css("display", "block");

    $(".step").removeClass("active");
    $(".step[step=" + currentStep + "]").addClass("active");
}

var initStep0 = function() {
    $("#screen-title").change(function() {
        screens.changeSelectedScreenTitle($(this).val());
    });

    $("#screen-title").keyup(function() {
        screens.changeSelectedScreenTitle($(this).val());
    });

    screens.screenStylesPanel = new ScreenStylesPanel($("#screen-styles-panel"));
}

var initStep1 = function() {
    $("#send-button").button();
    $("#send-button").click(function() {
        $(".send-status").html(templates.waitDataSend);

        //var data = screens.serialize();

        $.post("../../Home/SaveShop", "test" /*data*/, function(data) {
            if (data.success) {
                alert(data);
                $(".send-status").html(templates.successDataSend);
            } else {
                $(".send-status").html(templates.failDataSend);
            }
        }, "json")
        .fail(function() {
            $(".send-status").html(templates.failDataSend);
        });
    });
}

$(document).ready(function() {
    $(".button").button();

    initScreens();

    initStep0();
    initStep1();

    $("#next-step-button").click(function() {
        if (currentStep < 1) {
            ++currentStep;
            loadStep();
        }
    });

    $("#prev-step-button").click(function() {
        if (currentStep > 0) {
            --currentStep;
            loadStep();
        }
    });

    $("#load-data-button").click(function() {
        $.post("../../Home/LoadShop", "", function (data) {
            screens.loadData(data);
            loadStep(0);
        }, "json");
    });

    loadStep();
});