package com.example.cli.domain.common;

import com.example.cli.entity.Menu;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/9 11:53
 */
@Data
public class RouterMenu {

    private Integer id;

    private Integer parentId;

    private String path;

    private String permissionId;

    private String permissionName;

    private String component;

    private String componentPath;

    private Integer sort;

    private String redirect;

    private Meta meta;

    public RouterMenu(Menu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.path = menu.getPath();
        this.permissionId = menu.getPermissionId();
        this.permissionName = menu.getPermissionName();
        this.component = menu.getComponent();
        this.componentPath = menu.getComponentPath();
        this.sort = menu.getSort();
        this.redirect = menu.getRedirect();

        Meta meta = new Meta(menu);
        this.setMeta(meta);
    }
}
