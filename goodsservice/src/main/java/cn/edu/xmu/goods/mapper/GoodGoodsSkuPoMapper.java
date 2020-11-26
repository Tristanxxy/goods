package cn.edu.xmu.goods.mapper;

import cn.edu.xmu.goods.model.po.GoodGoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodGoodsSkuPoExample;
import java.util.List;

public interface GoodGoodsSkuPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    int insert(GoodGoodsSkuPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    int insertSelective(GoodGoodsSkuPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    List<GoodGoodsSkuPo> selectByExample(GoodGoodsSkuPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    GoodGoodsSkuPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(GoodGoodsSkuPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table good_goods_sku
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(GoodGoodsSkuPo record);
}