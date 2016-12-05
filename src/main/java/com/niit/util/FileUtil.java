package com.niit.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil
{
	public static Logger log = LoggerFactory.getLogger(FileUtil.class);

	public static void saveImage(MultipartFile file, String path)
	{
		log.debug("MethodStart: saveImage");
		try
		{
			File f = new File(path);
			f.createNewFile();
			BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(f));
			byte[] bytes = file.getBytes();
			bs.write(bytes);
			bs.close();
			log.info("Image uploaded");
		}catch(Exception e)
		{
			log.error("Exception occured while uploading image: " + e);
		}
		log.debug("MethodEnd: saveImage");
	}
}