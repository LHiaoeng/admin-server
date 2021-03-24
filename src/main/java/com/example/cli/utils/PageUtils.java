package com.example.cli.utils;

import com.example.cli.domain.common.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Collections;

/**
 * @author wjw
 */
public class PageUtils {

    public static <T> PageInfo<T> getPageInfo(Page<T> page,Class<T> tClass){
        PageInfo<T> pageInfo=new PageInfo<>();
        pageInfo.setPageNo(page.getNumber() + 1);
        pageInfo.setTotalCount(page.getTotalElements());
        pageInfo.setData(page.getContent());
        pageInfo.setColumns(ColumnUtils.getColumns(tClass));
        return pageInfo;
    }

    /**
     * 返回空分组对象
     * @return
     */
    public static PageInfo empty() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setData(Collections.EMPTY_LIST);
        pageInfo.setPageNo(1);
        pageInfo.setTotalCount(0);
        return pageInfo;
    }
}
