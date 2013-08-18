var StylesPanelGenerator = function(generatorData) {
    return function($el) {
        $el.addClass("stylesPanel");

        this.$currentEl = null;
        this.setStyleCb = null;

        var me = this;

        var addStyle = function(id, options) {
            var cssCode = '.' + generatorData.elClassPrefix + id + ' {' +
                              'background-color: ' + options.bgColor + '!important ;' +
                              'color: ' + options.textColor + ';' +
                              (options.image ? ('background-image: ' + 'url(' + options.image + ');background-repeat: no-repeat;') : '') +
                          '}';
            $("head").append('<style type="text/css">' + cssCode + '</style>');

            $el.append('<div class="' + generatorData.elClass + ' ' + generatorData.elClassPrefix + id + '" ' + generatorData.styleAttr + '"' + id + '">' + options.name + '</div>');

            $el.find("." + generatorData.elClassPrefix + id).click(function() {
                me.setStyle(id);
            });
        }
        
    /*    $.post("url",
            "",
            function(data) {
                for (var i = 0, l = data.length; i < l; ++i) {
                    addStyle(data[i].id, data[i].options);
                }
            },
            "json");*/
            
        // !!!!!!!!!!!!!!!!!!!!!!!!

        var data;
        
        switch (generatorData.url) {
        case "buttons":
            data = [{
                id: "style1",
                options: {
                    name: "Стиль 1",
                    bgColor: "#FFFFFF",
                    textColor: "#000000",
                    image: "images/buttons/style1.png"
                }
            }, {
                id: "style2",
                options: {
                    name: "Стиль 2",
                    bgColor: "#000000",
                    textColor: "#FFFFFF",
                    image: "images/buttons/style2.png"
                }
            }, {
                id: "style3",
                options: {
                    name: "Стиль 3",
                    bgColor: "#333333",
                    textColor: "#AA0000",
                    image: "images/buttons/style3.png"
                }
            }];
            break;
        case "screens":
            data = [{
                id: "style1",
                options: {
                    name: "Стиль 1",
                    bgColor: "#FFFFFF",
                    textColor: "#000000",
                    image: "images/screens/style1.png"
                }
            }, {
                id: "style2",
                options: {
                    name: "Стиль 2",
                    bgColor: "#000000",
                    textColor: "#FFFFFF",
                    image: "images/screens/style2.png"
                }
            }, {
                id: "style3",
                options: {
                    name: "Стиль 3",
                    bgColor: "#333333",
                    textColor: "#FFAAAA",
                    image: "images/screens/style3.png"
                }
            }];
            break;
        default:
            break;
        }

        for (var i = 0, l = data.length; i < l; ++i) {
            addStyle(data[i].id, data[i].options);
        }
        
        // !!!!!!!!!!!!!!!!!!!!!!!!
        
        this.setEl = function($mainButton, setStyleCb) {
            this.$currentEl = $mainButton;
            this.setStyleCb = setStyleCb;

            var style = this.$currentEl.attr(generatorData.styleAttr);

            if (style) {
                $el.find("[" + generatorData.styleAttr + "=" + style + "]").addClass("selected");
            }
        }

        this.setStyle = function(style) {
            if (this.$currentEl) {
                var oldStyle = this.$currentEl.attr(generatorData.styleAttr);

                if (oldStyle) {
                    this.$currentEl.removeClass(generatorData.elClassPrefix + oldStyle);
                }

                this.$currentEl.attr(generatorData.styleAttr, style);
                this.$currentEl.addClass(generatorData.elClassPrefix + style);

                this.setStyleCb(style);
            }
        }
    }
}

var MainButtonStylesPanel = StylesPanelGenerator({
    elClass: "main-button",
    elClassPrefix: "main-button-style-",
    styleAttr: "button-style",
    url: "buttons"
});

var ScreenStylesPanel = StylesPanelGenerator({
    elClass: "screen-mini",
    elClassPrefix: "screen-style-",
    styleAttr: "screen-style",
    url: "screens"
});