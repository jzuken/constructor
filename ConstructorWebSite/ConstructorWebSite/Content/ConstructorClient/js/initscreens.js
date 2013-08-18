var initScreens = function() {
    screens.addScreen('main', new Screen({
            name: "Главный экран",
            params: {
                items: [],
                getScreenButtonText: function(id) {
                    return screens.items[id].buttonText;
                },
                setScreenButtonText: function(id, value) {
                    screens.items[id].buttonText = value;
                },
                getScreenButtonStyle: function(id) {
                    return screens.items[id].buttonStyle;
                },
                setScreenButtonStyle: function(id, value) {
                    screens.items[id].buttonStyle = value;
                },
                getScreenDisabled: function(id) {
                    return screens.items[id].disabled;
                },
                setScreenDisabled: function(id, value) {
                    var $screenButton = $("#screens [screen=" + id + "]");
                    $screenButton.css({
                        display: value ? "none" : "block"
                    });

                    return screens.items[id].disabled;
                },
                loadMainButton: function(id) {
                    var $el = this.$view.find(".main-button[screen=" + id + "]");

                    if (this.getScreenDisabled(id)) {
                        $el.hide();
                    } else {
                        $el.show();
                        $el.text(this.getScreenButtonText(id));
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
                        var screenId = this.items[i];
                
                        switch (screenId) {
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
                        var screenId = this.items[i];

                        if (screenId == this.selectedButton) {
                            finded = true;
                        } else {
                            if (!this.getScreenDisabled(screenId)) {
                                idToSwap = screenId;
                            }
                        }
                    }

                    this.swapMainButtons(this.selectedButton, idToSwap);
                },
                mainButtonToDown: function() {
                    var idToSwap = null;

                    var findedSelected = false;
                    for (var i = 0, l = this.items.length; idToSwap == null && i < l; ++i) {
                        var screenId = this.items[i];

                        if (findedSelected) {
                            if (!this.getScreenDisabled(screenId)) {
                                idToSwap = screenId;
                            }
                        } else {
                            if (screenId == this.selectedButton) {
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

                    var $button = this.$view.find(".main-button[screen=" + this.selectedButton + "]");

                    this.$view.find(".main-button").removeClass("selected");
                    $button.addClass("selected");

                    this.$editor.find("#main-button-text").val(this.getScreenButtonText(this.selectedButton));

                    var me = this;
                    this.stylesPanel.setEl($button, function(style) {
                        me.setScreenButtonStyle(me.selectedButton, style);
                    });
                },
                changeSelectedButtonText: function(text) {
                    this.setScreenButtonText(this.selectedButton, text);
                    this.$view.find(".main-button[screen=" + this.selectedButton + "]").text(text);
                },
                removeSelectedButton: function() {
                    this.setScreenDisabled(this.selectedButton, true);

                    var $el = this.$view.find(".main-button[screen=" + this.selectedButton + "]");
                    $el.fadeOut();
                    this.$editor.find("#removed-buttons").append("<div class='removed-button' screen='" + this.selectedButton + "'>" + this.getScreenButtonText(this.selectedButton) + "</div>");

                    var me = this;

                    this.$editor.find(".removed-button[screen=" + this.selectedButton + "]").button();
                    this.$editor.find(".removed-button[screen=" + this.selectedButton + "]").click(function() {
                        me.setScreenDisabled(me.selectedButton, false);
                        $el.fadeIn();

                        $(this).remove();
                    });

                    this.loadSelectedButton();
                }
            },
            loadScreen: function() {
                this.$view.find(".main-button").remove();
                this.$editor.find(".removed-button").remove();

                var me = this;

                for (var i = 0, l = this.params.items.length; i < l; ++i) {
                    var screenId = this.params.items[i];

                    var style = this.params.getScreenButtonStyle(screenId);
                    
                    this.$view.append("<div class='main-button main-button-style-" + style + "' screen='" + screenId + "' button-style='" + style + "'></div>");

                    if (this.params.getScreenDisabled(screenId)) {
                        this.$editor.find("#removed-buttons").append("<div class=\"removed-button\" screen=\"" + screenId + "\">" + this.getScreenButtonText() + "</div>");

                        this.$editor.find(".removed-button[screen=" + screenId + "]").button();
                        this.$editor.find(".removed-button[screen=" + screenId + "]").click(function() {
                            me.params.setScreenDisabled(screenId, false);
                            me.$view.find(".main-button[screen=" + screenId + "]").fadeIn();

                            $(this).remove();
                        });
                    }
                }

                this.$view.find(".main-button").each(function() {
                    me.params.loadMainButton($(this).attr("screen"));
                });

                this.$view.find(".main-button").click(function() {
                    me.params.loadSelectedButton($(this).attr("screen"));
                });

                if (this.params.items.length > 0) {
                    this.params.loadSelectedButton(this.params.items[0]);
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
                               '<input id="main-button-text" maxlength="10"></input>' +
                               '<div id="main-button-styles-panel"></div>' +
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

                params.stylesPanel = new MainButtonStylesPanel(this.$editor.find("#main-button-styles-panel"));

                this.$editor.find("#main-button-text").keyup(function() {
                    params.changeSelectedButtonText($(this).val());
                });

                this.$editor.find("#main-button-text").change(function() {
                    params.changeSelectedButtonText($(this).val());
                });
            }
        })
    );

    screens.addScreen('stats', new Screen({
            name: "Статистика", 
            params: {
            },
            loadScreen: function() {
            },
            initView: function($el) {
            },
            initEditor: function($el) {
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
            }
        })
    );
}