package com.atguigu.guli.service.edu.entity.vo;

import java.io.Serializable;

/**
 * @Description:
 * @author: zxl
 * @Data:2020/10/13
 */
public class CoursePublishVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectParentTitle;
    private String subjectTitle;
    private String teacherName;
    private String price;//只用于显示
}
