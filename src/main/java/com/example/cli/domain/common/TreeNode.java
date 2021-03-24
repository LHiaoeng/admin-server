package com.example.cli.domain.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wjw
 */
@Data
public class TreeNode extends BaseTree<TreeNode> {

    private Integer key;

    private String title;

    private String icon;

    private List<TreeNode> children;

    private Boolean group;

    private Map scopedSlots;
}
