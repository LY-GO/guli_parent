package com.atguigu.guli.service.edu.controller.admin;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: zxl
 * @Data:2020/9/4
 */

@Api(description = "课程管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/course")
public class CourseControlle {
    @Resource
    CourseService courseService;

    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId).message("保存成功");
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id) {

        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        if (courseInfoForm != null) {
            return R.ok().data("item", courseInfoForm);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("更新课程")
    @PutMapping("update-course-info")
    public R updateCourseInfoById(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {

        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("分页课程列表")
    @GetMapping("list/{page}/{limit}")
    public R index(@ApiParam(value = "当前页码", required = true) @PathVariable Long page, @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit, @ApiParam(value = "查询对象", required = true) CourseQueryVo courseQueryVo) {
        IPage<CourseVo> pageModel = courseService.selectPage(page, limit, courseQueryVo);
        List<CourseVo> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total", total).data("rows", records);

    }

    @ApiOperation("根据id删除课程")
    @GetMapping("remove/{id}")
    public R remove(@ApiParam(value = "课程id", required = true) @PathVariable int id) {
        //删除vod
        //删除课程封面
        courseService.removeCoverById(id);
        //删除课程
        boolean result = courseService.removeCourseById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }


    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(@PathVariable(value = "id", required = true) String id) {
        CourseInfoForm course = courseService.getCourseInfoById(id);
        CoursePublishVo coursePublishVo = new CoursePublishVo();
        BeanUtils.copyProperties(course, coursePublishVo);
        if (coursePublishVo != null) {
            return R.ok().data("course", coursePublishVo);
        } else {
            return R.error().message("课程发布失败");
        }
    }
    @ApiOperation("我是测试")
    @GetMapping("course-publish/{id}")
    public R get人人CoursePublishVoById(@PathVariable(value = "id", required = true) String id) {
        CourseInfoForm course = courseService.getCourseInfoById(id);
        CoursePublishVo coursePublishVo = new CoursePublishVo();
        BeanUtils.copyProperties(course, coursePublishVo);
        if (coursePublishVo != null) {
            return R.ok().data("course", coursePublishVo);
        } else {
            return R.error().message("课程发布失败");
        }
    }
}
