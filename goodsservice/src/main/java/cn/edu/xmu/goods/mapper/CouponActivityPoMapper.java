package cn.edu.xmu.goods.mapper;

import cn.edu.xmu.goods.model.po.CouponActivityPo;
import cn.edu.xmu.goods.model.po.CouponActivityPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CouponActivityPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int deleteByExample(CouponActivityPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int insert(CouponActivityPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int insertSelective(CouponActivityPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    List<CouponActivityPo> selectByExample(CouponActivityPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    CouponActivityPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CouponActivityPo record, @Param("example") CouponActivityPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CouponActivityPo record, @Param("example") CouponActivityPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CouponActivityPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CouponActivityPo record);
}