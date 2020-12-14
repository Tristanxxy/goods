package cn.edu.xmu.groupon.dao;

import org.springframework.stereotype.Repository;
import cn.edu.xmu.groupon.mapper.*;
import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.po.*;
import cn.edu.xmu.groupon.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhai
 */
@Repository
public class GrouponDao {

    private static final Logger logger = LoggerFactory.getLogger(GrouponDao.class);

    @Autowired
    GrouponActivityPoMapper grouponActivityPoMapper;

    /**
     * 查看所有团购活动
     *
     * @param spuId
     * @param shopId
     * @param page
     * @param pageSize
     * @return ReturnObject<PageInfo < VoObject>>
     * @author zhai
     */
    public PageInfo<GrouponActivityPo> findgroupons(Integer timeLine, Long spuId, Long shopId, Integer page, Integer pageSize) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(1);
        if (spuId != null) {
            criteria.andGoodsSpuIdEqualTo(spuId);
        }
        if (shopId != null) {

            criteria.andShopIdEqualTo(shopId);
        }
        //时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的
        if (timeLine != null) {
            if (timeLine == 0) {
                //timeLine等于0还没开始的活动
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else {
                if (timeLine == 1) {
                    //timeLine等于1明天开始的活动
                    criteria.andBeginTimeGreaterThan(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth() + 1, 0, 0, 0));
                } else {
                    if (timeLine == 2) {
                        //timeLine等于2正在进行的活动
                        criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                        criteria.andEndTimeGreaterThan(LocalDateTime.now());
                    } else {
                        //timeLine等于3已经结束的活动
                        if (timeLine == 3) {
                            criteria.andEndTimeLessThan(LocalDateTime.now());
                        }
                    }

                }
            }
        }
        List<GrouponActivityPo> grouponActivityPos;
        grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(grouponActivityPos);
    }

    /**
     * 查询shop团购活动
     *
     * @param state
     * @param spuId
     * @param id
     * @param page
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     * @author zhai
     */
    public ReturnObject<PageInfo<VoObject>> findShopGroupon(Integer state, Long spuId, Long id, Integer page, Integer pageSize, String beginTime, String endTime) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(id);
        if (state != null) {
            if (!state.toString().isBlank()) {
                criteria.andStateEqualTo(state);
            }
        }
        if (spuId != null) {
            if (!spuId.toString().isBlank()) {
                criteria.andGoodsSpuIdEqualTo(spuId);
            }
        }

        if (beginTime != null) {
            if (!beginTime.isBlank()) {
                criteria.andBeginTimeEqualTo(beginTime);
            }
        }
        if (endTime != null) {
            if (!endTime.isBlank()) {
                criteria.andEndTimeEqualTo(endTime);
            }
        }
        List<GrouponActivityPo> grouponActivityPos;
        try {
            grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(grouponActivityPos.size());
            for (GrouponActivityPo po : grouponActivityPos) {
                GrouponActivity grouponActivity = new GrouponActivity(po);
                ret.add(grouponActivity);
            }
            PageInfo<VoObject> grouponPage = PageInfo.of(ret);
            PageInfo<GrouponActivityPo> grouponPoPage = PageInfo.of(grouponActivityPos);
            PageInfo<VoObject> grouponAcvtivityPage = new PageInfo<>(ret);
            grouponAcvtivityPage.setPages(grouponPoPage.getPages());
            grouponAcvtivityPage.setPageNum(grouponPoPage.getPageNum());
            grouponAcvtivityPage.setPageSize(grouponPoPage.getPageSize());
            grouponAcvtivityPage.setTotal(grouponPoPage.getTotal());
            return new ReturnObject<>(grouponPage);
        } catch (DataAccessException e) {
            logger.error("findgroupons: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 查询sku团购活动
     *
     * @param id
     * @param state
     * @param shopId
     * @return
     * @author zhai
     */
    public ReturnObject<List> findSkuGrouponById(Long id, Integer state, Long shopId) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);
        criteria.andShopIdEqualTo(shopId);
        List<GrouponActivityPo> grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
        List<GrouponSelectVo> grouponSelectVos = new ArrayList<>(grouponActivityPos.size());
        if (grouponActivityPos.isEmpty()) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        for (GrouponActivityPo po : grouponActivityPos) {
            grouponSelectVos.add(new GrouponSelectVo(po));
        }
        return new ReturnObject<>(grouponSelectVos);

    }

    /**
     * 新增团购活动
     *
     * @param id
     * @param grouponInputVo
     * @param shopId
     * @return
     * @author zhai
     */

    public GrouponActivityPo addGroupon(Long id, GrouponInputVo grouponInputVo, Long shopId) {
        GrouponActivity grouponActivity = new GrouponActivity();
        GrouponActivityPo grouponActivityPo = grouponActivity.createAddPo(id, grouponInputVo, shopId);
        grouponActivityPo.setState(0);
        int ret = grouponActivityPoMapper.insertSelective(grouponActivityPo);
        if (ret == 0) {
            //检查新增是否成功
            logger.info("grouponId = " + id + " 的信息新增失败");
            grouponActivityPo = null;
        } else {
            logger.info("grouponId = " + id + " 的信息已新增成功");
        }
        return grouponActivityPo;
    }

    /**
     * 修改团购活动
     *
     * @param id             团购活动 id
     * @param grouponInputVo 可修改的团购活动信息
     * @return 返回对象 ReturnObject<Object>
     * @author zhai
     */
    public ReturnObject<Object> modifyGrouponById(Long id, GrouponInputVo grouponInputVo, Long shopId) {
        ReturnObject<Object> returnObject;
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if(grouponInputVo.getBeginTime().isAfter(grouponInputVo.getEndTime())){
            logger.info("团购活动开始时间晚于结束时间");
            return  new ReturnObject<>(ResponseCode.Log_Bigger);
        }
        if(grouponInputVo.getBeginTime().isAfter(LocalDateTime.now())){
            logger.info("团购活动开始时间晚于当前时间");
            return  new ReturnObject<>(ResponseCode.Log_Bigger);
        }
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：Id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = po.getShopId() == null ? null : po.getShopId();
        if (shopId.equals(shopid)) {
            GrouponActivity grouponActivity = new GrouponActivity(po);
            GrouponActivityPo grouponActivityPo = grouponActivity.creatUpdatePo(grouponInputVo);
            if (po.getState() == 1) {
                logger.info("团购活动已上线，无法修改：grouponId = " + id);
                return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
            }
            if (po.getState() == 2) {
                logger.info("团购活动已删除，无法修改：grouponId = " + id);
                return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
            }
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(grouponActivityPo);
            if (ret == 0) {
                //检查更新是否成功
                logger.info("团购活动修改失败：grouponId = " + id);
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("grouponId = " + id + " 的信息已更新");
                returnObject = new ReturnObject<>();
                return returnObject;
            }

        } else {
            logger.error("无权限修改团购活动");
            returnObject = new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
            return returnObject;
        }
    }

    /**
     * 更改团购活动状态
     *
     * @param id 种类 id
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> deleteGrouponState(Long shopId, Long id) {

        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if (po.getShopId() == null) {
            logger.info("无法修改：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (po.getState() == 2) {
            logger.info("团购活动已删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        } else if (po.getState() == 1) {
            logger.info("团购活动已上线，无法修改：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        }

        if (po.getShopId().equals(shopId)) {

            po.setState(2);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动删除失败：GrouponId = " + id);
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("团购活动删除成功：GrouponId = " + id);
                returnObject = new ReturnObject<>();
                return returnObject;
            }
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
    }

    /**
     * 上线团购活动
     *
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject<Object> onGrouponState(Long shopId, Long id) {

        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if (po.getShopId() == null) {
            logger.info("无法修改：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (po.getState() == 2) {
            logger.info("团购活动已删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        } else if (po.getState() == 1) {
            logger.info("团购活动已上线：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        }

        if (po.getShopId().equals(shopId)) {

            po.setState(1);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动上线失败：GrouponId = " + id);
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("团购活动上线成功：GrouponId = " + id);
                returnObject = new ReturnObject<>();
                return returnObject;
            }
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
    }


    /**
     * 下线团购活动
     *
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject<Object> offGrouponState(Long shopId, Long id) {

        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if (po.getShopId() == null) {
            logger.info("无法修改：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (po.getState() == 2) {
            logger.info("团购活动已删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        } else if (po.getState() == 0) {
            logger.info("团购活动已下线：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        }

        if (po.getShopId().equals(shopId)) {

            po.setState(0);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动下线失败：GrouponId = " + id);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("团购活动下线成功：GrouponId = " + id);
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
    }

    public boolean disableActivity(Long shopId) {
        try{
            GrouponActivityPoExample example = new GrouponActivityPoExample();
            GrouponActivityPoExample.Criteria criteria = example.createCriteria();
            criteria.andShopIdEqualTo(shopId);
            List<Byte> states = new ArrayList<>();
            states.add((byte) 0);
            states.add((byte)1);
            criteria.andStateIn(states);
            List<GrouponActivityPo> pos = grouponActivityPoMapper.selectByExample(example);
            for(GrouponActivityPo po: pos){
                po.setGmtModified(LocalDateTime.now());
                po.setState(2);
                grouponActivityPoMapper.updateByPrimaryKey(po);
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
