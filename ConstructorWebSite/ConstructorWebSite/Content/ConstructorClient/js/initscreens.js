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
                    var $el = this.$view.find(".main-button[screen=" + id + "]");

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

                    var $el1 = this.$view.find(".main-button[screen=" + id1 + "]");
                    var $el2 = this.$view.find(".main-button[screen=" + id2 + "]");

                    $el1.attr('screen', id2);
                    $el2.attr('screen', id1);

                    this.loadMainButton(id1);
                    this.loadMainButton(id2);

                    this.loadSelectedButton();
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

                    this.$view.find(".main-button").removeClass("selected");
                    this.$view.find(".main-button[screen=" + this.selectedButton + "]").addClass("selected");

                    var screen = this.getScreen(this.selectedButton);

                    this.$editor.find("#main-button-text").val(screen.buttonText);
                    this.$editor.find("#main-button-bg-color").ColorPickerSetColor("#" + screen.buttonBgColor);
                    this.$editor.find("#main-button-text-color").ColorPickerSetColor("#" + screen.buttonTextColor);
                },
                changeSelectedButtonText: function(text) {
                    this.getScreen(this.selectedButton).buttonText = text;
                    this.$view.find(".main-button[screen=" + this.selectedButton + "] .main-button-text").text(text);
                },
                changeSelectedButtonBgColor: function(hex) {
                    this.getScreen(this.selectedButton).buttonBgColor = hex;

                    this.$view.find(".main-button[screen=" + this.selectedButton + "]").css({
                        'background-color': "#" + hex
                    });
                },
                changeSelectedButtonTextColor: function(hex) {
                    this.getScreen(this.selectedButton).buttonTextColor = hex;

                    this.$view.find(".main-button[screen=" + this.selectedButton + "]").css({
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
                            me.$view.find(".main-button[screen=" + me.selectedButton + "] img").attr('src', src);
                        };
                    })(file);

                    reader.readAsDataURL(file);
                },
                removeSelectedButton: function() {
                    var screen = this.getScreen(this.selectedButton);
                    screen.hasButton = false;

                    var $el = this.$view.find(".main-button[screen=" + this.selectedButton + "]");
                    $el.fadeOut();
                    this.$editor.find("#removed-buttons").append("<div class='removed-button' screen='" + this.selectedButton + "'>" + screen.buttonText + "</div>");

                    this.$editor.find(".removed-button[screen=" + this.selectedButton + "]").button();
                    this.$editor.find(".removed-button[screen=" + this.selectedButton + "]").click(function() {
                        screen.hasButton = true;
                        $el.fadeIn();

                        $(this).remove();
                    });

                    this.loadSelectedButton();
                }
            },
            loadScreen: function() {
                this.$view.find(".main-button").remove();

                for (var i = 0, l = this.params.items.length; i < l; ++i) {
                    var screen = this.params.items[i];

                    if (!screen.disabled) {
                        this.$view.append("<div class='main-button' screen='" + screen.id + "'></div>");
                    }
                }

                var me = this;
                this.$view.find(".main-button").each(function() {
                    me.params.loadMainButton($(this).attr("screen"));
                });

                this.$view.find(".main-button").click(function() {
                    me.params.loadSelectedButton($(this).attr("screen"));
                });

                if (this.params.items.length > 0) {
                    this.params.loadSelectedButton(this.params.items[0].id);
                }
            },
            initView: function($el) {
                this.$view = $el;
                this.params.$view = $el;
            },
            initEditor: function($el) {
                this.$editor = $el;
                this.params.$editor = $el;

                this.$editor.append('<div class="controls properties-block">' +
                               '<h2>Свойства кнопки</h2>' +
                               '<label for="main-button-text">Текст кнопки</label>' +
                               '<input id="main-button-text"></input>' +
                               '<div class="colors">' +
                                   '<div>' +
                                       'Фоновый рисунок кнопки' +
                                       '<input type="file" id="main-button-bg"></input>' +
                                   '</div>' +
                                   '<div>' +
                                       'Цвет фона кнопки' +
                                       '<div id="main-button-bg-color" class="color-selector"></div>' +
                                   '</div>' +
                                   '<div>' +
                                       'Цвет текста кнопки' +
                                       '<div id="main-button-text-color" class="color-selector"></div>' +
                                   '</div>' +
                               '</div>' +
                               '<div>' +
                                   '<h3>Положение конпки</h3>' +
                                   '<div id="main-up-button">Выше</div>' +
                                   '<div id="main-down-button">Ниже</div>' +
                                   '<div id="main-remove-button">Удалить</div>' +
                               '</div>' +
                           '</div>' +
                           '<div id="removed-buttons" class="controls properties-block">' +
                               '<h2>Удалённые кнопки</h2>' +
                           '</div>');

                this.$editor.find("#main-up-button").button();
                this.$editor.find("#main-down-button").button();
                this.$editor.find("#main-remove-button").button();

                var params = this.params;

                this.$editor.find("#main-up-button").click(function() {
                    params.mainButtonToUp();
                });

                this.$editor.find("#main-down-button").click(function() {
                    params.mainButtonToDown();
                });

                this.$editor.find("#main-remove-button").click(function() {
                    params.removeSelectedButton();
                });

                this.$editor.find("#main-button-bg-color").ColorPicker({
                    onChange: function (hsb, hex, rgb) {
                        params.changeSelectedButtonBgColor(hex);
                    }
                });

                this.$editor.find("#main-button-text-color").ColorPicker({
                    onChange: function (hsb, hex, rgb) {
                        params.changeSelectedButtonTextColor(hex);
                    }
                });

                this.$editor.find("#main-button-bg").change(function(e) {
                    var file = e.target.files[0];

                    params.loadButtonBgImage(file);
                });

                this.$editor.find("#main-button-text").keyup(function() {
                    params.changeSelectedButtonText($(this).val());
                });


                this.$editor.find("#main-button-text").change(function() {
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
            loadScreen: function() {
                var params = this.params;
                this.$editor.find("input").each(function() {
                    $(this).prop("checked", params[$(this).attr('param')]);
                });

                this.$view.find(".stats-block").each(function() {
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
                this.$view = $el;

                this.$view.html('<div class="stats-block" param="aboutOrders">' +
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

                this.$view.find(".stats-block").height(126);
            },
            initEditor: function($el) {
                this.$editor = $el;

                this.$editor.html('<div class="properties-block">' +
                               '<h2>Выбор свойств для отображения</h2>' +
                               '<div>' +
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
                               '</div>' +
                           '</div>');

                var params = this.params;
                this.$editor.find("input").change(function() {
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
                this.$view = $el;

                this.$view.css({
                    "height": "510px",
                    "background-image": "url(images/users/background.png)"
                });
            },
            initEditor: function($el) {
                this.$editor = $el;
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );

    screens.addScreen('discounts', new Screen({
            name: "Акции",
            params: {
            },
            loadScreen: function($el) {
            },
            initView: function($el) {
                this.$view = $el;
                
                this.$view.css({
                    "height": "510px",
                    "background-image": "url(images/discounts/background.png)"
                });
            },
            initEditor: function($el) {
                this.$editor = $el;
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );

    screens.addScreen('products', new Screen({
            name: "Товары",
            params: {
            },
            loadScreen: function() {
            },
            initView: function($el) {
                this.$view = $el;

                this.$view.css({
                    "height": "510px",
                    "background-image": "url(images/products/background.png)"
                });
            },
            initEditor: function($el) {
                this.$editor = $el;
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
            loadScreen: function() {
            },
            initView: function($el) {
                this.$view = $el;

                this.$view.css({
                    "height": "510px",
                    "background-image": "url(images/reviews/background.png)"
                });
            },
            initEditor: function($el) {
                this.$editor = $el;
            },
            serialize: function() {
                // !!! To do !!!
            }
        })
    );
}