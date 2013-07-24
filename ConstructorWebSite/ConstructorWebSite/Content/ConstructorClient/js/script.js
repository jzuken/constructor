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
}

var screens = {
    items: {},
    addScreen: function(id, screen) {
        this.items[id] = screen;

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

            $("#chose-screens").append('<div screen="' + id + '">' +
                                            '<input value="' + screen.name + '"></input>' +
                                            '<a href="javascript:void(0)" class="remove-screen">' +
                                                '<img class="remove-screen-image" src="images/remove-screen.png"></img>' +
                                                '<img class="add-screen-image" src="images/add-screen.png"></img>' +
                                            '</a>' +
                                        '</div>');

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
                    $(this).removeClass("add-screen");
                    $(this).addClass("remove-screen");
                } else {
                    $(this).removeClass("remove-screen");
                    $(this).addClass("add-screen");
                }

                screen.disabled = !isDisabled;
                mainScreen.params.getScreen(id).disabled = !isDisabled;
            });
        }

        $("#phone-screen").append("<div screen-view='" + id + "'><div class='screen-name'></div><div class='screen-controls'></div></div>");
        screen.initView($("#phone-screen [screen-view=" + id + "] .screen-controls"));

        $("#screens").append("<div screen='" + id + "'>" + screen.name + "</div>");
        $("#screens [screen=" + id + "]").button();
        $("#screens [screen=" + id + "]").click(function() {
            me.loadSelectedScreen(id);
        });

        $("#screen-editors").append("<div screen-editor='" + id + "'></div>");
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
                var newEl = "<div class='main-button' screen='" + screen.id + "'></div>";
                $el.append(newEl);
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

        this.items[id].loadScreen($el);
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
    }
};

var currentStep = 0;

var loadStep = function() {
    switch(currentStep) {
    case 0:
        $("#phone-screen [screen-view]").css("display", "none");
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
    default:
        break;
    }

    $("#editors [step]").css("display", "none");
    $("#editors [step=" + currentStep + "]").css("display", "block");
}

var initStep1 = function() {
    $("#editors [step=1] div[screen]").click(function() {
        var screenName = $(this).attr('screen');
        screens.loadSelectedScreen(screenName);

        $("div[screen-editor]").css("display", "none");
        $("div[screen-editor=" + screenName + "]").css("display", "block");
    });


    $("#screen-bg-color").ColorPicker({
        onChange: function (hsb, hex, rgb) {
            screens.changeSelectedScreenBgColor(hex);
        }
    });

    $("#screen-text-color").ColorPicker({
        onChange: function (hsb, hex, rgb) {
            screens.changeSelectedScreenTextColor(hex);
        }
    });


    $("#screen-bg").change(function(e) {
        var file = e.target.files[0];

        screens.loadScreenBgImage(file);
    });
}

$(document).ready(function() {
    $(".button").button();

    initScreens();

    initStep1();

    $("#next-step-button").click(function() {
        ++currentStep;
        loadStep();
    });

    $("#prev-step-button").click(function() {
        if (currentStep > 0) {
            --currentStep;
            loadStep();
        }
    });

    loadStep();
});
