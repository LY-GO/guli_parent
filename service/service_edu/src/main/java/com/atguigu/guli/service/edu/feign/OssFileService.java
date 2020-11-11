package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description:
 * @author: zxl
 * @Data:2020/9/2
 */
@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {
    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}
