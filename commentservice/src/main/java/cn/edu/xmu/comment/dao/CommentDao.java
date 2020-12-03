package cn.edu.xmu.comment.dao;

import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.comment.mapper.CommentPoMapper;
import cn.edu.xmu.comment.model.bo.Comment;
import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.comment.model.po.CommentPoExample;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentDao implements InitializingBean {

    @Autowired
    private CommentPoMapper commentPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    public ReturnObject<PageInfo<VoObject>> showCommentBySkuid(Integer page, Integer pageSize, Long id) {
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();

        criteria.andGoodsSkuIdEqualTo(id);
        PageHelper.startPage(page, pageSize);
        List<CommentPo> commentPos = null;
        try {
            commentPos = commentPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(commentPos.size());
            for (CommentPo po : commentPos) {
                Comment com = new Comment(po);
                if (po.getState() == 1)
                    ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<CommentPo> commentPoPage = PageInfo.of(commentPos);
            PageInfo<VoObject> commentPage = new PageInfo<>(ret);
            commentPage.setPages(commentPoPage.getPages());
            commentPage.setPageNum(commentPoPage.getPageNum());
            commentPage.setPageSize(commentPoPage.getPageSize());
            commentPage.setTotal(commentPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showCommentBySkuid: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}