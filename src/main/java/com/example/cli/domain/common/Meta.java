package com.example.cli.domain.common;

import com.example.cli.entity.Menu;
import lombok.Data;

/**
 * @author wjw
 */
@Data
public class Meta {
    private String title;

    private Boolean cache;

    private Boolean show;

    private Boolean hiddenHeaderContent;

    private String target;

    private String icon;

    private Boolean keepAlive;

    public Meta(Menu menu) {
        this.title = menu.getTitle();
        this.show = menu.getShowMenu();
        this.hiddenHeaderContent = menu.getHiddenHeaderContent();
        this.target = menu.getTarget();
        this.icon = menu.getIcon();
        this.keepAlive = menu.getKeepAlive();
    }
}
