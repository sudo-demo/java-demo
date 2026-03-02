package com.cobazaar.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.cobazaar.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 导出工具类
 * 用于导出 Excel、CSV 等格式的数据
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
public final class ExportUtils {

    private ExportUtils() {
        throw new UnsupportedOperationException("ExportUtils is a utility class and cannot be instantiated");
    }

    // ==================== Excel 导出 ====================

    /**
     * 导出 Excel 文件
     * 
     * @param response  HttpServletResponse
     * @param fileName  文件名
     * @param sheetName 工作表名
     * @param dataList  数据列表
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName, List<?> dataList) {
        if (CollUtil.isEmpty(dataList)) {
            log.warn("导出数据为空");
            return;
        }

        try {
            // 设置响应头
            setExcelResponseHeader(response, fileName);

            // 创建 Excel 写入器
            BigExcelWriter writer = ExcelUtil.getBigWriter(sheetName);

            // 写入数据
            writer.write(dataList, true);

            // 输出到浏览器
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            writer.close();
            IoUtil.close(out);

            log.info("Excel 导出成功，文件名：{}", fileName);
        } catch (Exception e) {
            log.error("Excel 导出失败", e);
            throw new ServiceException("Excel 导出失败", e);
        }
    }

    /**
     * 导出 Excel 文件（指定列头）
     * 
     * @param response  HttpServletResponse
     * @param fileName  文件名
     * @param sheetName 工作表名
     * @param dataList  数据列表
     * @param headerMap 列头映射
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName, 
                                 List<Map<String, Object>> dataList, Map<String, String> headerMap) {
        if (CollUtil.isEmpty(dataList)) {
            log.warn("导出数据为空");
            return;
        }

        try {
            // 设置响应头
            setExcelResponseHeader(response, fileName);

            // 创建 Excel 写入器
            BigExcelWriter writer = ExcelUtil.getBigWriter(sheetName);

            // 设置列头
            if (CollUtil.isNotEmpty(headerMap)) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    writer.addHeaderAlias(entry.getKey(), entry.getValue());
                }
                writer.setOnlyAlias(true);
            }

            // 写入数据
            writer.write(dataList, true);

            // 输出到浏览器
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            writer.close();
            IoUtil.close(out);

            log.info("Excel 导出成功，文件名：{}", fileName);
        } catch (Exception e) {
            log.error("Excel 导出失败", e);
            throw new ServiceException("Excel 导出失败", e);
        }
    }

    /**
     * 设置 Excel 响应头
     * 
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @throws IOException IO异常
     */
    private static void setExcelResponseHeader(HttpServletResponse response, String fileName) throws IOException {
        // 编码文件名
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setHeader("Content-Disposition", 
                "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
    }

    // ==================== CSV 导出 ====================

    /**
     * 导出 CSV 文件
     * 
     * @param response  HttpServletResponse
     * @param fileName  文件名
     * @param dataList  数据列表
     */
    public static void exportCsv(HttpServletResponse response, String fileName, List<?> dataList) {
        throw new UnsupportedOperationException("CSV 导出功能暂未实现");
    }

    /**
     * 设置 CSV 响应头
     * 
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @throws IOException IO异常
     */
    private static void setCsvResponseHeader(HttpServletResponse response, String fileName) throws IOException {
        // 编码文件名
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        
        // 设置响应头
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setHeader("Content-Disposition", 
                "attachment; filename*=UTF-8''" + encodedFileName + ".csv");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
    }

    // ==================== 工具方法 ====================

    /**
     * 生成导出文件名（带时间戳）
     * 
     * @param prefix 文件名前缀
     * @return 带时间戳的文件名
     */
    public static String generateFileName(String prefix) {
        if (StrUtil.isBlank(prefix)) {
            prefix = "export";
        }
        String timestamp = DateUtils.format(new java.util.Date(), "yyyyMMddHHmmss");
        return prefix + "_" + timestamp;
    }

    /**
     * 检查导出数据量是否超过限制
     * 
     * @param dataList 数据列表
     * @param maxSize  最大数据量
     * @return 是否超过限制
     */
    public static boolean isOverLimit(List<?> dataList, int maxSize) {
        return CollUtil.isNotEmpty(dataList) && dataList.size() > maxSize;
    }

    /**
     * 检查导出数据量是否超过限制（默认限制 10000 条）
     * 
     * @param dataList 数据列表
     * @return 是否超过限制
     */
    public static boolean isOverLimit(List<?> dataList) {
        return isOverLimit(dataList, 10000);
    }
}
