package com.example.cli.domain.search;

import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;


/**
 * @author wjw
 */
@Data
public class BaseSearch {

    private Integer pageNo;

    private Integer pageSize;

    private String sortField;

    private String sortOrder;

    private Sort sort;

    public Sort getSort() {
        if (StringUtils.hasLength(sortOrder) && StringUtils.hasLength(sortField)) {
            sort = new Sort("descend".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        } else {
            sort = new Sort(Sort.Direction.DESC, "id");
        }
        return sort;
    }
}
