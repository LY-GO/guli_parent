package com.atguigu.guli.service.edu.fallback;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: zxl
 * @Data:2020/9/2
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
