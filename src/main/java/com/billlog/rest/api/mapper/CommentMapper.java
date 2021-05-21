package com.billlog.rest.api.mapper;

import com.billlog.rest.api.model.comment.FoodComment;
import com.billlog.rest.api.model.comment.FoodCommentManage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    //게시글의 댓글 가져오기
    @Select("SELECT " +
            " comment.* " +
            ", user.user_name " +
            ", IFNULL(( SELECT file_download_uri FROM food_file WHERE file_idx = user.att_file_id AND use_yn = 'Y' ) ,'') AS image_url " +
            " FROM " +
            " food_comment comment" +
            " LEFT JOIN " +
            " food_user user " +
            " on comment.writer_user_idx = user.user_idx " +
            " WHERE comment_idx = #{comment_manage_idx} " +
            " AND use_yn = 'Y' " +
            " ORDER BY comment_sn DESC " )
    List<FoodComment> getCommentsByCommentIdx(@Param("comment_manage_idx") int comment_manage_idx);
//    List<FoodComment> getCommentsByCommentIdx(@Param("comment_manage_idx") int comment_manage_idx,@Param("page") int page, @Param("count") int count);

    //댓글 매니지 먼트 글 생성
    int createCommentManage(FoodCommentManage foodCommentManage);

    //댓글 작성
    int createComment(FoodComment foodComment);

    //댓글 수정
    int modifyCommentByIdxSn(FoodComment foodComment);

    //댓글 삭제
    @Delete("UPDATE food_comment " +
            " SET use_yn = 'N' " +
            " WHERE" +
            " comment_idx = #{comment_idx} " +
            " AND comment_sn = #{comment_sn} " +
            " AND writer_user_idx = #{writer_user_idx} ")
    boolean deleteCommentByIdxSn(@Param("comment_idx") int comment_idx, @Param("comment_sn") int comment_sn, @Param("writer_user_idx") int writer_user_idx);

}
