var initScreens = function() {
    screens.addScreen('main', new Screen({
            name: "Главный экран",
            params: {
                items: [],
                getScreen: function(id) {
                    if (id == "main") {
                        return this.mainScreen;
                    }

                    for (var key in this.items) {
                        var screen = this.items[key];

                        if (screen.id == id) {
                            return screen;
                        }
                    }

                    return null;
                },
                loadMainButton: function(id) {
                    var screen = this.getScreen(id);
                    var $el = $("#phone-screen .main-button[screen=" + id + "]");

                    if (screen.disabled) {
                        $el.hide();
                    } else {
                        $el.show();

                        var imgSrc = "";
                        if (screen.buttonImage) {
                            imgSrc = ' src="' + screen.buttonImageSrc + '"';
                        }

                        var imgBlock = "<img" + imgSrc + "></img>";
                        var textBlock = '<div class="main-button-text">' + screen.buttonText + '</div>';

                        $el.html(imgBlock + textBlock);
                        $el.css({
                            'color': "#" + screen.buttonTextColor,
                            'background-color': "#" + screen.buttonBgColor
                        });
                    }
                },
                selectedButton: "",
                swapMainButtons: function(id1, id2) {
                    if (id1 == id2) {
                        return;
                    }

                    var number1 = null;
                    var number2 = null;

                    for (var i = 0, l = this.items.length; (number1 == null || number2 == null) && i < l; ++i) {
                        var screen = this.items[i];
                
                        switch (screen.id) {
                        case id1:
                            number1 = i;
                            break;
                        case id2:
                            number2 = i;
                            break;
                        default:
                            break;
                        }
                    }

                    if (number1 == null || number2 == null) {
                        return;
                    }

                    var t = this.items[number1];
                    this.items[number1] = this.items[number2];
                    this.items[number2] = t;

                    var $el1 = $("#phone-screen .main-button[screen=" + id1 + "]");
                    var $el2 = $("#phone-screen .main-button[screen=" + id2 + "]");

                    $el1.attr('screen', id2);
                    $el2.attr('screen', id1);

                    this.loadMainButton(id1);
                    this.loadMainButton(id2);
                },
                mainButtonToUp: function() {
                    var idToSwap = null;

                    var finded = false;
                    for (var i = 0, l = this.items.length; !finded && i < l; ++i) {
                        var screen = this.items[i];
                
                        if (screen.id == this.selectedButton) {
                            finded = true;
                        } else {
                            if (!screen.disabled && screen.hasButton) {
                                idToSwap = screen.id;
                            }
                        }
                    }

                    this.swapMainButtons(this.selectedButton, idToSwap);
                },
                mainButtonToDown: function() {
                    var idToSwap = null;

                    var findedSelected = false;
                    for (var i = 0, l = this.items.length; idToSwap == null && i < l; ++i) {
                        var screen = this.items[i];

                        if (findedSelected) {
                            if (!screen.disabled && screen.hasButton) {
                                idToSwap = screen.id;
                            }
                        } else {
                            if (screen.id == this.selectedButton) {
                                findedSelected = true;
                            }
                        }
                    }

                    this.swapMainButtons(this.selectedButton, idToSwap);
                },
                loadSelectedButton: function(id) {
                    if (typeof(id) != 'undefined') {
                        this.selectedButton = id;
                    }

                    var screen = this.getScreen(this.selectedButton);

                    $("#main-button-text").val(screen.buttonText);
                    $("#main-button-bg-color").ColorPickerSetColor("#" + screen.buttonBgColor);
                    $("#main-button-text-color").ColorPickerSetColor("#" + screen.buttonTextColor);
                },
                changeSelectedButtonText: function(text) {
                    this.getScreen(this.selectedButton).buttonText = text;
                    $("#phone-screen .main-button[screen=" + this.selectedButton + "] .main-button-text").text(text);
                },
                changeSelectedButtonBgColor: function(hex) {
                    this.getScreen(this.selectedButton).buttonBgColor = hex;

                    $("#phone-screen .main-button[screen=" + this.selectedButton + "]").css({
                        'background-color': "#" + hex
                    });
                },
                changeSelectedButtonTextColor: function(hex) {
                    this.getScreen(this.selectedButton).buttonTextColor = hex;

                    $("#phone-screen .main-button[screen=" + this.selectedButton + "]").css({
                        'color': "#" + hex
                    });
                },
                loadButtonBgImage: function(file) {
                    if (!file.type.match('image.*')) {
                        return;
                    }

                    var screen = this.getScreen(this.selectedButton);
                    screen.buttonImage = file;

                    var reader = new FileReader();
                    var me = this;
                    reader.onload = (function(theFile) {
                        return function(e) {
                            var src = e.target.result;

                            screen.buttonImageSrc = e.target.result;
                            $("#phone-screen .main-button[screen=" + me.selectedButton + "] img").attr('src', src);
                        };
                    })(file);

                    reader.readAsDataURL(file);
                },
                removeSelectedButton: function() {
                    var screen = this.getScreen(this.selectedButton);
                    screen.hasButton = false;

                    var $el = $("#phone-screen .main-button[screen=" + this.selectedButton + "]");
                    $el.fadeOut();
                    $("#removed-buttons").append("<div class='removed-button' screen='" + this.selectedButton + "'>" + screen.buttonText + "</div>");

                    $(".removed-button[screen=" + this.selectedButton + "]").button();
                    $(".removed-button[screen=" + this.selectedButton + "]").click(function() {
                        screen.hasButton = true;
                        $el.fadeIn();

                        $(this).remove();
                    });

                    this.loadSelectedButton();
                }
            },
            loadScreen: function($el) {
                $el.find(".main-button").remove();

                for (var i = 0, l = this.params.items.length; i < l; ++i) {
                    var screen = this.params.items[i];

                    if (!screen.disabled) {
                        $el.append("<div class='main-button' screen='" + screen.id + "'></div>");
                    }
                }

                var me = this;
                $el.find(".main-button").each(function() {
                    me.params.loadMainButton($(this).attr("screen"));
                });

                $el.find(".main-button").click(function() {
                    me.params.loadSelectedButton($(this).attr("screen"));
                });
            },
            initView: function($el) {
            },
            initEditor: function($el) {
                $el.append('<div class="controls">' +
                                '<input id="main-button-text"></input>' +
                                '<div id="main-up-button">up</div>' +
                                '<div id="main-down-button">down</div>' +
                                '<div id="main-remove-button">remove</div>' +
                                '<div class="colors">' +
                                    '<div>' +
                                        'Фоновый рисунок кнопки' +
                                        '<input type="file" id="main-button-bg"></input>' +
                                    '</div>' +
                                    '<div>' +
                                        'Цвет фона кнопки' +
                                        '<input id="main-button-bg-color"></input>' +
                                    '</div>' +
                                    '<div>' +
                                        'Цвет текста кнопки' +
                                        '<input id="main-button-text-color"></input>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                            '<div id="removed-buttons">' +
                                'Удалённые кнопки' +
                            '</div>');

                $("#main-up-button").button();
                $("#main-down-button").button();
                $("#main-remove-button").button();

                var params = this.params;

                $("#main-up-button").click(function() {
                    params.mainButtonToUp();
                });

                $("#main-down-button").click(function() {
                    params.mainButtonToDown();
                });

                $("#main-remove-button").click(function() {
                    params.removeSelectedButton();
                });

                $("#main-button-bg-color").ColorPicker({
                    onChange: function (hsb, hex, rgb) {
                        params.changeSelectedButtonBgColor(hex);
                    }
                });

                $("#main-button-text-color").ColorPicker({
                    onChange: function (hsb, hex, rgb) {
                        params.changeSelectedButtonTextColor(hex);
                    }
                });

                $("#main-button-bg").change(function(e) {
                    var file = e.target.files[0];

                    params.loadButtonBgImage(file);
                });

                $("#main-button-text").keyup(function() {
                    params.changeSelectedButtonText($(this).val());
                });


                $("#main-button-text").change(function() {
                    params.changeSelectedButtonText($(this).val());
                });
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );

    screens.addScreen('stats', new Screen({
            name: "Статистика", 
            params: {
                aboutOrders: true,
                lastOrder: true,
                leadersOfSales: true,
                technicalInfo: true,
            },
            loadScreen: function($el) {
                var params = this.params;
                $el.find("input").each(function() {
                    $(this).prop("checked", params[$(this).attr('param')]);
                });

                $(".stats-block").each(function() {
                    if (params[$(this).attr('param')]) {
                        $(this).css({
                            display: 'block'
                        });
                    } else {
                        $(this).css({
                            display: 'none'
                        });
                    }
                });
            },
            initView: function($el) {
                $el.append('<div class="stats-block" param="aboutOrders">' +
                               '<img src="images/stats/about-orders.png">' +
                           '</div>' +
                           '<div class="stats-block" param="lastOrder">' +
                               '<img src="images/stats/last-order.png">' +
                           '</div>' +
                           '<div class="stats-block" param="leadersOfSales">' +
                               '<img src="images/stats/leaders-of-sales.png">' +
                           '</div>' +
                           '<div class="stats-block" param="technicalInfo">' +
                               '<img src="images/stats/technical-info.png">' +
                           '</div>');
            },
            initEditor: function($el) {
                $el.append('<div>' +
                               '<input type="checkbox" param="aboutOrders" checked>Общая информация о заказах' +
                           '</div>' +
                           '<div>' +
                               '<input type="checkbox" param="lastOrder" checked>Последний заказ' +
                           '</div>' +
                           '<div>' +
                               '<input type="checkbox" param="leadersOfSales" checked>Лидеры продаж' +
                           '</div>' +
                           '<div>' +
                               '<input type="checkbox" param="technicalInfo" checked>Техническая информация о магазине' +
                           '</div>');

                var params = this.params;
                $el.find("input").change(function() {
                    var param = $(this).attr('param');
                    var value = $(this).prop("checked");
                    params[param] = value;

                    if (value) {
                        $(".stats-block[param=" + param + "]").fadeIn();
                    } else {
                        $(".stats-block[param=" + param + "]").fadeOut();
                    }
                });
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );

    screens.addScreen('users', new Screen({
            name: "Информация о пользователях",
            params: {
            },
            loadScreen: function($el) {
            },
            initView: function($el) {
            },
            initEditor: function($el) {
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );

    screens.addScreen('stock', new Screen({
            name: "Акции",
            params: {
            },
            loadScreen: function($el) {
            },
            initView: function($el) {
            },
            initEditor: function($el) {
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );

    screens.addScreen('goods', new Screen({
            name: "Товары",
            params: {
            },
            loadScreen: function($el) {
            },
            initView: function($el) {
            },
            initEditor: function($el) {
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );
    screens.addScreen('reviews', new Screen({
            name: "Отзывы",
            params: {
            },
            loadScreen: function($el) {
            },
            initView: function($el) {
            },
            initEditor: function($el) {
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );
}