package com.cobazaar.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * 日期时间工具类
 * 提供企业级的日期时间处理功能
 * 
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
public final class DateUtils extends DateUtil {

    private DateUtils() {
        throw new UnsupportedOperationException("DateUtils is a utility class and cannot be instantiated");
    }

    // ==================== 日期格式化 ====================

    /**
     * 格式化日期为字符串（默认格式）
     * 格式：yyyy-MM-dd HH:mm:ss
     * 
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String format(Date date) {
        return format(date, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 格式化日期为字符串（指定格式）
     * 
     * @param date   日期
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return DateUtil.format(date, format);
    }

    /**
     * 格式化LocalDateTime为字符串
     * 
     * @param dateTime LocalDateTime
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 格式化LocalDateTime为字符串（指定格式）
     * 
     * @param dateTime LocalDateTime
     * @param format   格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    // ==================== 日期解析 ====================

    /**
     * 解析字符串为日期
     * 
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date parse(String dateStr) {
        return DateUtil.parse(dateStr);
    }

    /**
     * 解析字符串为日期（指定格式）
     * 
     * @param dateStr 日期字符串
     * @param format  格式
     * @return 日期
     */
    public static Date parse(String dateStr, String format) {
        return DateUtil.parse(dateStr, format);
    }

    /**
     * 解析字符串为LocalDateTime
     * 
     * @param dateStr 日期字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        return parseLocalDateTime(dateStr, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 解析字符串为LocalDateTime（指定格式）
     * 
     * @param dateStr 日期字符串
     * @param format  格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateStr, formatter);
    }

    // ==================== 日期计算 ====================

    /**
     * 计算两个日期之间的天数差
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差
     */
    public static long daysBetween(Date startDate, Date endDate) {
        return DateUtil.between(startDate, endDate, cn.hutool.core.date.DateUnit.DAY);
    }

    /**
     * 计算两个LocalDate之间的天数差
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 给日期添加天数
     * 
     * @param date   日期
     * @param days   天数
     * @return 新日期
     */
    public static Date addDays(Date date, int days) {
        return DateUtil.offsetDay(date, days);
    }

    /**
     * 给LocalDate添加天数
     * 
     * @param date   日期
     * @param days   天数
     * @return 新日期
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    /**
     * 获取当月第一天
     * 
     * @return 当月第一天
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(new Date());
    }

    /**
     * 获取指定日期所在月的第一天
     * 
     * @param date 日期
     * @return 当月第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        return DateUtil.beginOfMonth(date);
    }

    /**
     * 获取当月最后一天
     * 
     * @return 当月最后一天
     */
    public static Date getMonthEnd() {
        return getMonthEnd(new Date());
    }

    /**
     * 获取指定日期所在月的最后一天
     * 
     * @param date 日期
     * @return 当月最后一天
     */
    public static Date getMonthEnd(Date date) {
        return DateUtil.endOfMonth(date);
    }

    // ==================== 工作日计算 ====================

    /**
     * 判断是否为工作日
     * 
     * @param date 日期
     * @return 是否为工作日
     */
    public static boolean isWorkday(Date date) {
        DateTime dateTime = new DateTime(date);
        int dayOfWeek = dateTime.dayOfWeek();
        return dayOfWeek != 6 && dayOfWeek != 7; // 周六、周日为非工作日
    }

    /**
     * 计算工作日天数
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 工作日天数
     */
    public static int workdaysBetween(Date startDate, Date endDate) {
        int workdays = 0;
        Date current = startDate;
        
        while (current.before(endDate) || current.equals(endDate)) {
            if (isWorkday(current)) {
                workdays++;
            }
            current = addDays(current, 1);
        }
        
        return workdays;
    }

    // ==================== 日期范围 ====================

    /**
     * 获取本周的日期范围
     * 
     * @return 日期范围 [开始日期, 结束日期]
     */
    public static Date[] getThisWeekRange() {
        Date now = new Date();
        Date start = DateUtil.beginOfWeek(now);
        Date end = DateUtil.endOfWeek(now);
        return new Date[]{start, end};
    }

    /**
     * 获取本月的日期范围
     * 
     * @return 日期范围 [开始日期, 结束日期]
     */
    public static Date[] getThisMonthRange() {
        Date now = new Date();
        Date start = getFirstDayOfMonth(now);
        Date end = getMonthEnd(now);
        return new Date[]{start, end};
    }

    /**
     * 获取本季度的日期范围
     * 
     * @return 日期范围 [开始日期, 结束日期]
     */
    public static Date[] getThisQuarterRange() {
        Date now = new Date();
        Date start = DateUtil.beginOfQuarter(now);
        Date end = DateUtil.endOfQuarter(now);
        return new Date[]{start, end};
    }

    /**
     * 获取本年的日期范围
     * 
     * @return 日期范围 [开始日期, 结束日期]
     */
    public static Date[] getThisYearRange() {
        Date now = new Date();
        Date start = DateUtil.beginOfYear(now);
        Date end = DateUtil.endOfYear(now);
        return new Date[]{start, end};
    }

    // ==================== 时间戳 ====================

    /**
     * 获取当前时间戳（秒）
     * 
     * @return 时间戳
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间戳（毫秒）
     * 
     * @return 时间戳
     */
    public static long currentTimestampMillis() {
        return System.currentTimeMillis();
    }

    // ==================== 类型转换 ====================

    /**
     * 将Date转换为LocalDateTime
     * 
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将LocalDateTime转换为Date
     * 
     * @param dateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
