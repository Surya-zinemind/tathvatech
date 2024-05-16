package com.tathvatech.injuryReport.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.site.service.SiteServiceImpl;
import com.tathvatech.user.entity.UserQuery;
import com.tathvatech.user.repository.UserRepository;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@XmlRootElement(name = "comment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentInfoBean extends BaseResponseBean
{
    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    private int pk;
    private int objectPk;
    private String objectType;
    private String commentContext; // something which can tell why it is
    // attached. managed by the object
    private String commentText;
    private int createdBy;
    private String createdByName;
    private Date createdDate;
    private String label; // a label against a comment for some identification
    private Date lastUpdated;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public int getObjectPk()
    {
        return objectPk;
    }

    public void setObjectPk(int objectPk)
    {
        this.objectPk = objectPk;
    }

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
    }

    public String getCommentContext()
    {
        return commentContext;
    }

    public void setCommentContext(String commentContext)
    {
        this.commentContext = commentContext;
    }

    public String getCommentText()
    {
        return commentText;
    }

    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(int createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedByName()
    {
        return createdByName;
    }

    public void setCreatedByName(String createdByName)
    {
        this.createdByName = createdByName;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public static CommentInfoBean getCommentInfoBean(Comment comment)
    {
        CommentInfoBean ibeam = new CommentInfoBean();

        ibeam.setPk(comment.getPk());
        ibeam.setObjectPk(comment.getObjectPk());
        ibeam.setObjectType(EntityTypeEnum.fromValue(comment.getObjectType()).name());
        ibeam.setCommentText(comment.getCommentText());
        ibeam.setCommentContext(comment.getCommentContext());
        ibeam.setCreatedDate(comment.getCreatedDate());
        ibeam.setCreatedBy(comment.getCreatedBy());
        try
        {
            UserQuery user = UserRepository.getInstance().getUser(comment.getCreatedBy());
            if (user != null)
                ibeam.setCreatedByName(user.getDisplayString());
        }
        catch (Exception ex)
        {
            logger.error("Error getting user for pk:" + comment.getCreatedBy(), ex);
        }

        return ibeam;
    }

    public static List<CommentInfoBean> getCommentInfoBeanList(List<Comment> comments)
    {
        List<CommentInfoBean> returnList = new ArrayList();
        for (Iterator iterator = comments.iterator(); iterator.hasNext();)
        {
            Comment comment = (Comment) iterator.next();
            returnList.add(getCommentInfoBean(comment));
        }
        return returnList;
    }

    @Override
    public String toString()
    {
        return "pk:" + ListStringUtil.showString(pk) + "; objectPk:" + ListStringUtil.showString(objectPk)
                + "; objectType:" + ListStringUtil.showString(objectType) + "; commentContext:"
                + ListStringUtil.showString(commentContext) + "; commentText:" + ListStringUtil.showString(commentText)
                + "; createdBy:" + ListStringUtil.showString(createdBy) + "; createdByName:"
                + ListStringUtil.showString(createdByName) + "; createdDate:" + ListStringUtil.showString(createdDate)
                + "; label:" + ListStringUtil.showString(label) + "; lastUpdated:"
                + ListStringUtil.showString(lastUpdated);
    }

}

