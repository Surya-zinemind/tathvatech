package com.tathvatech.injuryReport.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.sarvasutra.etest.api.model.AttachmentInfoBean;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.Attachment;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.FileStoreManager;
import com.tathvatech.ts.core.common.service.CommonServicesDelegate;

/**
 * @author Hari
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Attachments
{
    private static final Logger logger = Logger.getLogger(Attachments.class);

    public static void saveResponseImage(UserContext context, int Pk, EntityTypeEnum type,
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

                CommonServicesDelegate.saveAttachments(context, Pk, type.getValue(), attachedFiles, true);

            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new Exception("Image could not be saved, Please try again later");
            }
        }
    }

    public static void addResponseImage(UserContext context, int Pk, EntityTypeEnum type,
                                        List<AttachmentInfoBean> attachments) throws Exception
    {
        if (attachments != null && attachments.size() > 0)
        {
            Connection con = null;
            try
            {
                con = ServiceLocator.locate().getConnection();
                con.setAutoCommit(false);

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
                        int pk = PersistWrapper.createEntity(att);
                    } else
                    {
                        PersistWrapper.update(att);
                    }
                }
                con.commit();
            }
            catch (IOException e)
            {
                con.rollback();
                e.printStackTrace();
                throw new Exception("Image could not be saved, Please try again later");
            }
            finally
            {
            }

        }
    }
}

