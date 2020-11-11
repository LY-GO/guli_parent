package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @author: zxl
 * @Data:2020/9/4
 */
@Data
public class CourseQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String teacherId;
    private String subjectParentId;
    private String subjectId;
}
