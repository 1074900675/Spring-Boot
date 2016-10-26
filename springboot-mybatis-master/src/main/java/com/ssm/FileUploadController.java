package com.ssm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.groovy.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import net.minidev.json.JSONObject;

@Controller
public class FileUploadController {
	@ResponseBody
	@RequestMapping(value = "/batch/upload", method = RequestMethod.POST)

	public String batchFileUpload(HttpServletRequest request) throws IllegalStateException, IOException {
		JSONObject filename = new JSONObject();
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		String contextPath = request.getSession().getServletContext().getRealPath("/") + "\\uploads\\";
		System.out.println(contextPath);
		File tempFile = new File(contextPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		for (int i = 0; i < files.size(); ++i) {
			UUID uuid = UUID.randomUUID();
			MultipartFile file = files.get(i);
			System.out.println(file.getContentType());
			System.out.println(file.isEmpty());
			UUID name = uuid;
			if (!file.isEmpty()) {
				try {
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(new File("F:/cp/" + name + i)));
					filename.put("Name" + i, name.toString());
					stream.write(file.getBytes());
					stream.close();

				} catch (Exception e) {
					System.out.println("You failed to upload " + filename + " => " + e.getMessage());
					return "You failed to upload " + filename + " => " + e.getMessage();

				}
			} else {
				return "You failed to upload " + filename + " because the file was empty.";
			}
		}
		return "upload successful" + filename;
	}

	@RequestMapping("/uploads")
	@ResponseBody
	public List<String> handleFileUploads(
			/* @RequestParam("file") MultipartFile file */@RequestParam("files") MultipartFile[] files) {
		System.out.println(files.length);/* 文件的长度 */
		JSONObject filename = new JSONObject();
		/* JSONArray filenames = new JSONArray(); */
		List<String> filenames = new ArrayList<String>();

		String[] prefixs = new String[] { "raw", "RAW", "jpg", "JPG", "png", "PNG", "gif", "GIF", "bmp", "BMP", "jpeg",
				"JPEG" };

		System.out.println(prefixs[0]);

		String position = null;
		if (files != null && files.length > 0) {
			// 循环获取file数组中得文件
			for (int i = 0; i < files.length; i++) {
				System.out.println("File" + files[i].getContentType());/* 获取文件后缀名字 */
				MultipartFile file = files[i]; /* 获取数组中的第几个文件 */
				/*
				 * String suffix =
				 * file.getOriginalFilename().substring(file.getOriginalFilename
				 * ().lastIndexOf("."));
				 * 
				 * System.out.println(suffix);
				 */

				String fileName = file.getOriginalFilename();
				String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
				System.out.println(prefix);

				position = "E:/myjava/springboot-mybatis-master/src/main/resources/static/data/doc/";
				for (String pre : prefixs) {
					if (StringUtils.equals(pre, prefix)) {
						position = "E:/myjava/springboot-mybatis-master/src/main/resources/static/data/img/";
						break;
					}

				}

				/*
				 * 
				 * System.out.print("File:::" + file.getOriginalFilename());
				 * 
				 * System.out.print("Filesssssssss" + file.getName());
				 */
				UUID uuid = UUID.randomUUID();
				UUID name = uuid;
				if (!file.isEmpty()) {
					try {
						filename.put("Name" + i, name.toString());/* 把名字记录到一个对象里面 */
						filenames.add(name.toString());

						System.out.print("FilesssssssssnNewName:" + name);/* 获取源文件的名字 */
						BufferedOutputStream stream = new BufferedOutputStream(

								new FileOutputStream(new File(position + name + "." + prefix)));

						/*
						 * new FileOutputStream(new File("F:/ssmimg/" + name +
						 * i)));
						 */
						/* 一看就知道这是路径啦，不加路径会存到Temp */

						/* http://10.3.13.212:8080/ssmimg/a.jpg */

						stream.write(file.getBytes());
						stream.close();
					} catch (Exception e) {
						System.out.println("You failed to upload " + filenames + " => " + e.getMessage());
						return filenames;
					}
				} else {
					return filenames;
				}
			}
		}
		return filenames;
	}

	/* 单文件上传 */
	@RequestMapping("/upload")
	@ResponseBody
	public String handleFileUpload(

			/* @RequestParam("file") MultipartFile file */

			@RequestParam(value = "selectallmodel", required = true) MultipartFile file

	) {

		JSONObject filename = new JSONObject();

		System.out.print(file.getOriginalFilename());

		UUID uuid = UUID.randomUUID();
		UUID name = uuid;
		if (!file.isEmpty()) {
			try {

				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File("F:/ssmimg/" + name)));
				filename.put("Name", name.toString());
				out.write(file.getBytes());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "上传失败," + e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				return "上传失败," + e.getMessage();
			}
			return "上传成功";
		} else {
			return "上传失败，因为文件是空的.";
		}
	}

}
