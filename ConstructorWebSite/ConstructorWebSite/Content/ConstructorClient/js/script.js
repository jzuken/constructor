var Screen = function(data) {
    this.bgColor = "FFFFFF";
    this.textColor = "000000";
    this.bgImage = null;
    this.bgImageSrc = null;
    this.name = data.name;

    this.disabled = false;

    this.params = data.params;
    this.loadScreen = data.loadScreen;
    this.initView = data.initView;
    this.initEditor = data.initEditor;

    this.serialize = data.serialize;
    this.loadSaved = data.loadSaved;
}

var screens = {
    keys: [],
    items: {},
    addScreen: function(id, screen) {
        this.items[id] = screen;
        this.keys.push(id);

        var me = this;

        if (id != 'main') {
            var mainScreen = this.items.main;
            var mainScreenItem = {
                id: id,
                disabled: false,
                hasButton: true,
                buttonText: screen.name,
                buttonImage: null,
                buttonImageSrc: null,
                buttonBgColor: "000000",
                buttonTextColor: "FFFFFF"
            };

            mainScreen.params.items.push(mainScreenItem);

            $("#chose-screens").append(templates.choseScreenTrigger(id, screen.name));

            $("#chose-screens [screen=" + id + "] input").change(function() {
                screens.changeScreenTitle(id, $(this).val());
            });

            $("#chose-screens [screen=" + id + "] input").keyup(function() {
                screens.changeScreenTitle(id, $(this).val());
            });
            
            $("#chose-screens [screen=" + id + "] a").click(function() {
                var screen = me.items[id];

                var isDisabled = screen.disabled;
                if (isDisabled) {
                    $(this).parent().removeClass("screen-disabled");
                    $(this).parent().addClass("screen-enabled");
                } else {
                    $(this).parent().removeClass("screen-enabled");
                    $(this).parent().addClass("screen-disabled");
                }

                screen.disabled = !isDisabled;
                mainScreen.params.getScreen(id).disabled = !isDisabled;
            });
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

        this.items.main.params.getScreen(id).buttonText = title;
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

        $("#screen-bg-color").ColorPickerSetColor("#" + screen.bgColor);
        $("#screen-text-color").ColorPickerSetColor("#" + screen.textColor);

        var bgImageProperty;
        if (screen.bgImage) {
            bgImageProperty = "url(" + screen.bgImageSrc + ")";
        } else {
            bgImageProperty = "none";
        }

        $("#phone-screen").css({
            'color': "#" + screen.textColor,
            'background-color': "#" + screen.bgColor,
            'background-image': bgImageProperty,
        });

        this.items[id].loadScreen();

        $("div[screen-editor]").css("display", "none");
        $("div[screen-editor=" + id + "]").css("display", "block");
    },
    changeSelectedScreenBgColor: function (hex) {
        var screen = this.items[this.selectedScreen];

        screen.bgColor = hex;
        $("#phone-screen").css({
            'background-color': "#" + hex
        });
    },
    changeSelectedScreenTextColor: function (hex) {
        var screen = this.items[this.selectedScreen];

        screen.textColor = hex;
        $("#phone-screen").css({
            'color': "#" + hex
        });
    },
    loadScreenBgImage: function(file) {
        if (!file.type.match('image.*')) {
            return;
        }

        var screen = this.items[this.selectedScreen];
        screen.bgImage = file;

        var reader = new FileReader();
        reader.onload = (function(theFile) {
            return function(e) {
                var src = e.target.result;
                screen.bgImageSrc = src;

                $("#phone-screen").css({
                    'background-image': "url(" + src + ")"
                });
            };
        })(file);

        reader.readAsDataURL(file);
    },
    serialize: function() {
        var res = [];

        for (var i = 0, l = this.keys.length; i < l; ++i) {
            var key = this.keys[i];
            var screen = this.items[key];

            if (!screen.disabled) {
                res.push({
                    id: key,
                    common: {
                        bgColor: screen.bgColor,
                        bgColorRGB: hexToRGB(screen.bgColor),
                        textColor: screen.textColor,
                        textColorRGB: hexToRGB(screen.textColor),
                        name: screen.name
                        // TO DO: service for send images
                    },
                    data: screen.serialize()
                });
            }
        }

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
        $(".chose-screen-trigger").each(function() {
            var screenId = $(this).attr("screen");

            if (screens.items[screenId].disabled) {
                $(this).addClass("screen-disabled");
                $(this).removeClass("screen-enabled");
            } else {
                $(this).removeClass("screen-disabled");
                $(this).addClass("screen-enabled");
            }
        });
        break;
    case 1:
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
    case 2:
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

var initStep1 = function() {
    $("#screen-bg-color").ColorPicker({
        onChange: function (hsb, hex, rgb) {
            screens.changeSelectedScreenBgColor(hex);
            $(this).css('backgroundColor', '#' + hex);
        }
    });

    $("#screen-text-color").ColorPicker({
        onChange: function (hsb, hex, rgb) {
            screens.changeSelectedScreenTextColor(hex);
            $(this).css('backgroundColor', '#' + hex);
        }
    });

    $("#screen-bg").change(function(e) {
        var file = e.target.files[0];

        screens.loadScreenBgImage(file);
    });
}

var initStep2 = function() {
    $("#send-button").button();
    $("#send-button").click(function() {
        $(".send-status").html(templates.waitDataSend);

        var data = screens.serialize();

        $.post("url", data, function(data) {
            if (data.success) {
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

    initStep1();
    initStep2();

    $("#next-step-button").click(function() {
        if (currentStep < 2) {
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
        $.post("url", "", function(data) {
            screens.loadData(data);

            loadStep(0);
        }, "json");
    });

    loadStep();
});
