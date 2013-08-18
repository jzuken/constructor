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

                    screens.items[id].disabled = value;
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
                moveInto: function(id, before) {
                    var newItems = [];

                    var findedId = false;
                    for (var i = 0, l = this.items.length; i < l; ++i) {
                        switch(this.items[i]) {
                        case id:
                            findedId = true;
                            break;
                        case before:
                            if (findedId) {
                                newItems.push(before);
                                newItems.push(id);
                            } else {
                                newItems.push(id);
                                newItems.push(before);
                            }
                            break;
                        default:
                            newItems.push(this.items[i]);
                            break;
                        }
                    }

                    this.items = newItems;

                    this.renderButtons();
                },
                renderButtons: function() {
                    this.$view.find(".main-button").remove();
                    this.$editor.find(".removed-button").remove();

                    var me = this;

                    for (var i = 0, l = this.items.length; i < l; ++i) {
                        var screenId = this.items[i];

                        if (this.getScreenDisabled(screenId)) {
                            this.$editor.find("#removed-buttons").append("<div class=\"removed-button\" screen=\"" + screenId + "\">" + this.getScreenButtonText(screenId) + "</div>");

                            this.$editor.find(".removed-button[screen=" + screenId + "]").button();
                            this.$editor.find(".removed-button[screen=" + screenId + "]").click(function() {
                                me.setScreenDisabled(screenId, false);
                                me.renderButtons();

                                $(this).remove();
                            });
                        } else {
                            var style = this.getScreenButtonStyle(screenId);

                            this.$view.append("<div class='main-button main-button-style-" + style + "' screen='" + screenId + "' button-style='" + style + "'></div>");
                        }
                    }

                    this.$view.find(".main-button").each(function() {
                        me.loadMainButton($(this).attr("screen"));
                    });

                    this.$view.find(".main-button").mousedown(function() {
                        me.loadSelectedButton($(this).attr("screen"));
                    });

                    if (!this.selectedButton || this.getScreenDisabled(this.selectedButton)) {
                        if (this.items.length > 0) {
                            this.loadSelectedButton(this.items[0]);
                        }
                    } else {
                        this.loadSelectedButton(this.selectedButton);
                    }

                    this.$view.find(".main-button").draggable({
                        start: function() {
                            $(this).addClass("drag");
                        },
                        stop: function() {
                            $(this).removeClass("drag");
                            
                            var dragOffset = $(this).offset();
                            var centerX = dragOffset.left + 60;
                            var centerY = dragOffset.top + 60;
                            
                            var screenToReplace;
                            me.$view.find(".main-button").each(function() {
                                var screenId = $(this).attr("screen");
                                if (screenId !== me.selectedButton) {
                                    var offset = $(this).offset();

                                    // 120 = width(height) + padding
                                    if (offset.left < centerX && offset.left + 120 > centerX &&
                                        offset.top < centerY && offset.top + 120 > centerY) {
                                        screenToReplace = screenId;
                                    }
                                }
                            })

                            if (screenToReplace) {
                                me.moveInto(me.selectedButton, screenToReplace);
                            } else {
                                $(this).css({
                                    top: "0px",
                                    left: "0px"
                                });
                            }
                        }
                    });
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
                    $el.remove();
                    this.$editor.find("#removed-buttons").append("<div class='removed-button' screen='" + this.selectedButton + "'>" + this.getScreenButtonText(this.selectedButton) + "</div>");

                    var me = this;

                    this.$editor.find(".removed-button[screen=" + this.selectedButton + "]").button();
                    this.$editor.find(".removed-button[screen=" + this.selectedButton + "]").click(function() {
                        me.setScreenDisabled(me.selectedButton, false);
                        me.renderButtons();

                        $(this).remove();
                    });

                    this.loadSelectedButton();
                }
            },
            loadScreen: function() {
                this.params.renderButtons();
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
                                   '<div id="main-remove-button">Удалить</div>' +
                               '</div>' +
                           '</div>' +
                           '<div id="removed-buttons" class="controls properties-block">' +
                               '<h2>Удалённые кнопки</h2>' +
                           '</div>');

                this.$editor.find("#main-remove-button").button();

                var params = this.params;

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