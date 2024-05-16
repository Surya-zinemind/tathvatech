package com.tathvatech.injuryReport.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.common.UserContext;
import java.util.List;
import javax.imageio.ImageIO;
import com.tathvatech.user.entity.Attachment;
import com.tathvatech.user.service.CommonServiceManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@RequiredArgsConstructor
public class Attachments
{
    private static final Logger logger = LoggerFactory.getLogger(Attachments.class);
    private final CommonServiceManager commonServiceManager;
    private final PersistWrapper persistWrapper;

    public  void saveResponseImage(UserContext context, int Pk, EntityTypeEnum type,
                                         List<AttachmentInfoBean> attachments) throws Exception
    {
        if (attachments != null && attachments.size() > 0)
        {
            try
            {
                List attachedFiles = new ArrayList();
                for (int i = 0; i < attachments.size(); i++)
                {
                    String fileName = context.getUser().getUserName() + "_" + new Date().getTime() + ".jpg";
                    File file = FileStoreManager.getFile(fileName);
                    byte[] bytes = Base64.decodeBase64(attachments.get(i).getFileEncodedString().getBytes());
                    final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
                    ImageIO.write(bufferedImage, "jpg", file);
                    Attachment att = new Attachment();
                    att.setFileName(fileName);
                    att.setFileDisplayName(fileName);
                    att.setFileDescription("Additional images for oil items");
                    attachedFiles.add(att);
                }

                commonServiceManager.saveAttachments(context, Pk, type.getValue(), attachedFiles, true);

            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new Exception("Image could not be saved, Please try again later");
            }
        }
    }

    public  void addResponseImage(UserContext context, int Pk, EntityTypeEnum type,
                                        List<AttachmentInfoBean> attachments) throws Exception
    {
        if (attachments != null && attachments.size() > 0)
        {


                for (int i = 0; i < attachments.size(); i++)
                {
                    String fileName = context.getUser().getUserName() + "_" + new Date().getTime() + ".jpg";

                    Attachment att = new Attachment();
                    att.setFileName(fileName);
                    att.setFileDisplayName(fileName);
                    att.setFileDescription("Additional images for oil items");
                    if (attachments.get(i).getPk() == 0)
                    {
                        File file = FileStoreManager.getFile(fileName);
                        byte[] bytes = Base64.decodeBase64(attachments.get(i).getFileEncodedString().getBytes());
                        final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
                        ImageIO.write(bufferedImage, "jpg", file);
                        att.setCreatedDate(new Date());
                        att.setCreatedBy(context.getUser().getPk());
                        att.setObjectType(type.getValue());
                        att.setObjectPk(Pk);
                        int pk = (int) persistWrapper.createEntity(att);
                    } else
                    {
                        persistWrapper.update(att);
                    }
                }
                throw new Exception("Image could not be saved, Please try again later");


        }
    }
}

