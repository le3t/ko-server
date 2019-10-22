package org.ko.sigma.rest.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.ko.sigma.data.entity.Dict;
import org.ko.sigma.rest.dict.condition.QueryDictCondition;

import java.util.List;

/**
 * 字典表
 */
public interface DictService extends IService<Dict> {

    /**
     * <p>查询字典表列表</p>
     * @param condition
     * @return
     */
    List<Dict> queryDictList(QueryDictCondition condition);

    /**
     * <p>通过主键查询字典表详细信息</p>
     * @param id
     * @return
     */
    Dict queryDictInfo(Long id);

    /**
     * <p>新增字典表一条数据</p>
     * @param dict
     */
    Long createDict(Dict dict);

    /**
     * <p>通过ID更新字典表</p>
     * @param id Dict Id
     * @param dict
     * @return
     */
    Dict updateDict(Long id, Dict dict);

    /**
     * <p>删除字典表数据</p>
     * @param id Dict 主键id
     * @return
     */
    Long deleteDict(Long id);

}