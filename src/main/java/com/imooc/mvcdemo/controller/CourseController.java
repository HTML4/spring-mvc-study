package com.imooc.mvcdemo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.mvcdemo.model.Course;
import com.imooc.mvcdemo.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	private static Logger log = LoggerFactory.getLogger(CourseController.class);
	
	private CourseService courseService;
	
	@Autowired
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}
	
	@RequestMapping(value = "/view", method=RequestMethod.GET)
	public String viewCourse(@RequestParam("courseId") Integer courseId, Model model) {
		log.debug("In viewCourse, courseId = {}", courseId);
		return "index";
	}
	@RequestMapping(value = "/view2/{courseId}", method=RequestMethod.GET)
	public String viewCourse2(@PathVariable("courseId") Integer courseId, Map<String,Object> model) {
		log.debug("In viewCourse, courseId = {}", courseId);
		return "index";
	}
	@RequestMapping(value = "/view3", method=RequestMethod.GET)
	public String viewCourse3(HttpServletRequest request) {
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		log.debug("In viewCourse, courseId = {}", courseId);
		return "index";
	}
//	提交表单
	@RequestMapping(value = "/admin", method=RequestMethod.GET, params = "add")
	public String createCourse() {
		return "course_admin/edit";
	}
	
	@RequestMapping(value = "/save", method=RequestMethod.POST)
	public String doSave(@ModelAttribute Course course) {
		log.debug("Info of Course:");
		log.debug(ReflectionToStringBuilder.toString(course));
		course.setCourseId(course.getLevel());
		return "redirect:view2/" + course.getCourseId();
	}
//	上传组件
	@RequestMapping(value = "/file", method=RequestMethod.GET)
	public String createUpload() {
		return "course_admin/file";
	}
	@RequestMapping(value = "/doUpload", method=RequestMethod.POST)
	public String doUpload(@RequestParam("file") MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			log.debug("p file: {}", file.getOriginalFilename());
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File("D:\\eclipseWorkspace\\spring-mvc-study\\src\\main\\webapp\\resources\\upload", System.currentTimeMillis() + file.getOriginalFilename()));
		}
		return "index";
	}
	
	@RequestMapping(value="/{courseId}", method=RequestMethod.GET)
	public @ResponseBody Course getCourseInJson(@PathVariable Integer courseId) {
		return courseService.getCoursebyId(courseId);
	}
	
	@RequestMapping(value="/jsontype/{courseId}", method=RequestMethod.GET)
	public ResponseEntity<Course> getCourseInJson2(@PathVariable Integer courseId) {
		Course course = courseService.getCoursebyId(courseId);
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
}
