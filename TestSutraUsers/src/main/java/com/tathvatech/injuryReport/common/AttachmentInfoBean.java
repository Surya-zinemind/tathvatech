package com.tathvatech.injuryReport.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.user.entity.Attachment;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@XmlRootElement(name="attachment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentInfoBean
{
    private int pk;
    private int objectPk;
    private int objectType;
    private String fileName;
    private String fileDisplayName;
    private String fileDescription;
    private String attachContext;
    private String fileEncodedString; // is used when we need to pass an image from the tablet. in this case pk will be 0;

    public String getFileName() {
        return fileName;
    }

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

    public int getObjectType()
    {
        return objectType;
    }

    public void setObjectType(int objectType)
    {
        this.objectType = objectType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public void setFileDisplayName(String fileDisplayName) {
        this.fileDisplayName = fileDisplayName;
    }

    public String getFileDescription()
    {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription)
    {
        this.fileDescription = fileDescription;
    }

    public String getAttachContext()
    {
        return attachContext;
    }

    public void setAttachContext(String attachContext)
    {
        this.attachContext = attachContext;
    }

    public String getFileEncodedString() {
        return fileEncodedString;
    }

    public void setFileEncodedString(String fileEncodedString) {
        this.fileEncodedString = fileEncodedString;
    }

    /**
     *
     */
    public AttachmentInfoBean()
    {
    }

    public static AttachmentInfoBean getBean(Attachment attachment)
    {
        AttachmentInfoBean aAtt = new AttachmentInfoBean();
        aAtt.setPk((int) attachment.getPk());
        aAtt.setObjectPk(attachment.getObjectPk());
        aAtt.setObjectType(attachment.getObjectType());
        aAtt.setFileName(attachment.getFileName());
        aAtt.setFileDisplayName(attachment.getFileDisplayName());
        aAtt.setAttachContext(attachment.getAttachContext());

        return aAtt;
    }

    @Override
    public int hashCode()
    {
        // TODO Auto-generated method stub
        return pk;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof AttachmentInfoBean))
            return false;
        if(((AttachmentInfoBean)obj).getPk() == this.getPk())
            return true;

        return false;
    }

}

