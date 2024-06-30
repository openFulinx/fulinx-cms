/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.file;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.FileHashCalculatorUtils;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.file.FileUploadPathService;
import com.fulinx.spring.service.file.IFileService;
import com.fulinx.spring.service.file.dto.FileListResultDto;
import com.fulinx.spring.service.file.dto.FileQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.file.vo.FileDeleteVo;
import com.fulinx.spring.web.controller.serverSide.file.vo.FilePaginationParameterVo;
import com.fulinx.spring.web.framework.base.BaseServerSideController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "系统端-文件")
@RestController
@Slf4j
@Validated
@RequestMapping("/system/files")
public class FileController extends BaseServerSideController {

    private final IFileService iFileService;

    private final FileUploadPathService uploadPathService;

    private final FileHashCalculatorUtils fileHashCalculatorUtils;

    public FileController(IFileService iFileService, FileUploadPathService uploadPathService, FileHashCalculatorUtils fileHashCalculatorUtils) {
        this.iFileService = iFileService;
        this.uploadPathService = uploadPathService;
        this.fileHashCalculatorUtils = fileHashCalculatorUtils;
    }


    /**
     * 新增文件
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "新增文件", method = "POST")
    @PostMapping
//    @PreAuthorize("hasAnyAuthority('sys:file','sys:file:create')")
    public ResultVo<Optional<TbFileEntity>> Create(@RequestParam("file") MultipartFile file) throws BusinessException {
        if (file.isEmpty()) {
            log.warn("请选择要上传的文件");
            throw new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
        }
        try {
            // 指定文件保存路径
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // 获取当前日期
            String filePath = uploadPathService.getUploadDir() + date + "/";

            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName); // 之前提到的获取文件扩展名的方法
            String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
            String fileUrl = "/" + date + "/" + newFileName;

            // 如果保存路径不存在，则创建
            File uploadDir = new File(filePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 构建上传文件的完整路径
            String destPath = filePath + newFileName;

            // 保存文件
            File dest = new File(destPath);
            file.transferTo(dest);
            String sha256 = fileHashCalculatorUtils.calculateSHA256(destPath);
            return ResultVo.build(iFileService.create(originalFileName, newFileName, file.getContentType(), getFileExtension(file.getOriginalFilename()), destPath, fileUrl, sha256));
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("文件上传失败: " + e.getMessage());
            throw new BusinessException(ErrorMessageEnum.FILE_UPLOAD_FAIL.getMessage(), ErrorMessageEnum.FILE_UPLOAD_FAIL.getIndex());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileDeleteVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "删除文件", method = "DELETE")
    @DeleteMapping()
//    @PreAuthorize("hasAnyAuthority('sys:file','sys:file:remove')")
    public ResultVo<Boolean> Remove(@RequestBody @Valid FileDeleteVo fileDeleteVo) throws BusinessException {
        return ResultVo.build(iFileService.remove(fileDeleteVo.getIds()));
    }

    /**
     * 查看文件
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看文件", method = "GET")
    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('sys:file','sys:file:show')")
    public ResultVo<Optional<FileListResultDto>> Show(@PathVariable(value = "id") @Valid @NonNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iFileService.getById(id));
    }

    /**
     * 列表-带分页
     *
     * @param userFilePaginationParameterVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "文件列表", method = "POST")
    @PostMapping("/pagination")
//    @PreAuthorize("hasAnyAuthority('sys:file','sys:file:pagination')")
    public ResultVo<ResultListVo<FileListResultDto>> Pagination(@RequestBody @Valid FilePaginationParameterVo userFilePaginationParameterVo) throws BusinessException {
        FileQueryConditionDto fileQueryConditionDto = MiscUtils.copyProperties(userFilePaginationParameterVo, FileQueryConditionDto.class);
        IPage<FileListResultDto> fileListResultDoIPage = iFileService.page(fileQueryConditionDto, userFilePaginationParameterVo.getPageNumber(), userFilePaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(fileListResultDoIPage.getRecords(), fileListResultDoIPage.getTotal()));
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return ""; // 如果找不到扩展名，则返回空字符串
    }
}
