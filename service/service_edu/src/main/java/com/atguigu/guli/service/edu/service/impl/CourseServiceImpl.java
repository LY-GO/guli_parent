package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zxl
 * @since 2020-08-27
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    //注意：为了避免idea在这个位置报告找不到依赖的错误，
//我们可以在CourseDescriptionMapper接口上添加@Repository注解
    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        //保存课程基本信息
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.insert(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {

        //从course表中取数据
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }

        //从course_description表中取数据
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        //创建courseInfoForm对象
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    /**
     * 要判断前端传过来的数据是否有id,有才可以在这里使用id来更新数据
     *
     * @param courseInfoForm
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);
    }

    /**
     * 分页课程列表
     *
     * @param page
     * @param limit
     * @param courseQueryVo
     * @return
     */
    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> qw = new QueryWrapper<>();
        qw.orderByDesc("c.gmt_create");
        String title = courseQueryVo.getTitle();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String teacherId = courseQueryVo.getTeacherId();
        if (!StringUtils.isEmpty(title)) {
            qw.like("c.title", title);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            qw.like("c.subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            qw.like("c.subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            qw.like("c.teacher_id", teacherId);
        }
        Page<CourseVo> page1 = new Page<>(page, limit);
        List<CourseVo> records = baseMapper.selectPageByCourseQueryVo(page1, qw);
        page1.setRecords(records);
        return page1;
    }

    /**
     * 删除课程封面
     * 涉及到另外一个服务,就单独拿出来单独删除,一面涉及事务问题
     * 而且即使没删除也没关系,图片不重要
     *
     * @param id 课程id
     * @return
     */
    @Override
    public boolean removeCoverById(Integer id) {
        Course course = baseMapper.selectById(id);
        String cover = course.getCover();
        if (StringUtils.isEmpty(cover)) {
            R r = ossFileService.removeFile(cover);
            return r.getSuccess();
        }
        return false;
    }

    /**
     * 删除课程
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(Integer id) {
        //删除收藏信息
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);
        //删除评论信息
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);
        //删除章节信息
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);
        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);
        //删除课程详情
        courseDescriptionMapper.deleteById(id);
        return this.removeById(id);
    }
}
