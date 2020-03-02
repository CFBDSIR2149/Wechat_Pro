package com.cwd.Mapper;

import com.cwd.Entity.AddedApplications;
import com.cwd.Entity.Comment;
import com.cwd.Entity.Topic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TopicMapper {
    @Insert({"insert into topic(uid,openid,article,content,nickName,avatarUrl,type,image,isCheck,viewCount,peopleSize,publicDatetime)" +
            " values(#{uid},#{openid},#{article},#{content},#{nickName},#{avatarUrl}," +
            "#{type},#{image},#{isCheck},#{viewCount},#{peopleSize},#{publicDatetime})"})
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addTopic(Topic topic);//添加一条话题记录

    @Select("select * from topic where isCheck=#{isCheck} and openid=#{openid}")
    @ResultType(value = Topic.class)
    List<Topic> getApplyingTopic(@Param("openid") String openid, @Param("isCheck") int isCheck);//查询指定申请人申请中的记录

    @Select(("select * from topic where isCheck=#{isCheck}"))
    @ResultType(value = Topic.class)
    List<Topic> getAllTopics(@Param("isCheck") int isCheck);//所有的话题

    @Insert("insert into AddedApplications(uid,openid,addedOpenid,addedUid) values(#{uid},#{openid}," +
            "#{addedOpenid},#{addedUid})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void postAddedApplication(AddedApplications addedApplications);//添加一条已参加的话题

    @Select("select * from AddedApplications where openid=#{openid}")
    @ResultType(value = AddedApplications.class)
    List<AddedApplications> getAddedApplications(String openid);//获取已参加话题

    /*获取加入的用户对应的openid通过uid指定的话题信息
    @Param:addedOpenid:已加入的话题的拥有者的openid
    @Param:addedUid:已加入的话题拥有者addedOpenid对应的话题uid
    * */
    @Select("select * from topic where openid=#{addedOpenid} " +
            " and uid=#{addedUid} and isCheck=1")
    List<Topic> getAddedApplicationTopics(String addedOpenid, String addedUid);

    /*获取话题*/
    @Select("select * from topic where openid=#{openid} and uid=#{uid} and isCheck=#{isCheck}")
    @ResultType(Topic.class)
    Topic getTopic(String openid, String uid, int isCheck);

    /*热门话题
     * */
    @Select("SELECT * FROM topic where isCheck=1 ORDER BY peopleSize DESC LIMIT 1")
    @ResultType(value = Topic.class)
    Topic hotTopic();

    /*话题榜*/
    @Select("SELECT * FROM topic WHERE isCheck=1 ORDER BY peopleSize DESC")
    @ResultType(value = Topic.class)
    List<Topic> topicListOrderByPeopleSize();

    @Insert("insert into Comment(uid,openid,targetOpenid,targetUid,content,nickName,avatarUrl,commentDatetime) " +
            "values(#{uid},#{openid},#{targetOpenid},#{targetUid},#{content},#{nickName},#{avatarUrl},#{commentDatetime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addComment(Comment comment);//添加用户评论

    /*查询所有评论*/
    @Select("select * from Comment where targetOpenid=#{openid} and targetUid=#{uid}")
    @ResultType(Comment.class)
    List<Comment> getAllComment(String openid, String uid);

    /*增加评论人数*/
    @Update("update topic set peopleSize=#{peopleSize} where openid=#{openid} and uid=#{uid}")
    void addCommentPeopleSize(int peopleSize, String openid, String uid);

    /*查询评论人数*/
    @Select("select peopleSize from topic where openid=#{openid} and uid=#{uid}")
    @ResultType(Integer.class)
    int getCommentPeopleSize(String openid, String uid);

    /*增加查看人数*/
    @Update("update topic set viewCount=#{viewCount} where openid=#{openid} and uid=#{uid} and isCheck=1")
    void addCommentViewCount(int viewCount, String openid, String uid);

    /*查询查看人数*/
    @Select("select viewCount from topic where openid=#{openid} and uid=#{uid}")
    @ResultType(Integer.class)
    int getCommentViewCount(String openid, String uid);

    /*添加点赞数*/
    @Update("update topic set thumbUp=#{thumbUp} where openid=#{openid} and uid=#{uid} and isCheck=1")
    void plusThumbUp(int thumbUp, String openid, String uid);

    /*查询点赞数*/
    @Select("select thumbUp from topic where openid=#{openid} and uid=#{uid} and isCheck=1")
    @ResultType(Integer.class)
    int getThumbUp(String openid,String uid );
}
