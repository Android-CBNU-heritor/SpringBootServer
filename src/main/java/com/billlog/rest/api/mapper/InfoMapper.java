package com.billlog.rest.api.mapper;

import com.billlog.rest.api.model.FoodInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InfoMapper {

    //정보제공성 게시글의 모든 목록 가져오기
    @Select("SELECT info.* " +
            " , ( SELECT user_name FROM food_user WHERE user_idx = info.writer_user_idx ) AS writer_user_name " +
            " , ( SELECT city_name FROM food_city WHERE city_idx = info.city ) as city_nm " +
            " , ( SELECT file_download_uri FROM food_file WHERE file_idx = info.att_file_id AND file_sn = '1' ) AS poster_image_url " +
            " , ( SELECT file_download_uri FROM food_file WHERE file_idx = (SELECT att_file_id FROM food_user WHERE user_idx = info.writer_user_idx) ) AS writer_image_url " +
            " FROM food_info info" +
            " WHERE type = #{type}" +
            " AND use_yn = 'Y' " +
            " ORDER BY info_idx DESC" +
            " LIMIT #{page} , #{count}")
    List<FoodInfo> getInfoArticleAll(@Param("type") String type, @Param("page") int page, @Param("count") int count);

    //인포 인덱스로 인포정보 특정 인포 글 가져오기
    @Select("SELECT info.* \n" +
            " , ( SELECT city_name FROM food_city WHERE city_idx = info.city ) as city_nm \n" +
            " , ( SELECT user_name FROM food_user WHERE user_idx = info.writer_user_idx ) AS writer_user_name " +
            " , ( SELECT file_download_uri FROM food_file WHERE file_idx = (SELECT att_file_id FROM food_user WHERE user_idx = info.writer_user_idx) ) AS writer_image_url " +
            "        FROM food_info info \n" +
            "        WHERE info_idx = #{info_idx}" +
            "        AND type = #{type}")
    FoodInfo getInfoArticleByIdx(@Param("info_idx") int info_idx, @Param("type") String type);

    //정보제공성 게시글의 총 글의 수
    @Select("SELECT COUNT(info_idx) FROM food_info")
    int getInfoTotalCount();

    //정보제공성 게시글 등록
    //동적 쿼리를 사용하기 위해 InfoMapper.xml 에서 쿼리를 작성하였고,
    //해당 메소드 명 'createInfoArticle'이 id 값과 매칭이 되어 호출이 될 수 있다.
    int createInfoArticle(FoodInfo foodInfo);

    //정보제공성 게시글의 수정
    int modifyInfoArticleByIdx(FoodInfo foodInfo);

    //정보제공성 게시글의 사용 여부 변경 ( 삭제 )
    @Delete("UPDATE food_info SET use_yn='N' WHERE info_idx = #{info_idx}")
    boolean deleteInfoArticleByIdx(@Param("info_idx") int info_idx);
}
