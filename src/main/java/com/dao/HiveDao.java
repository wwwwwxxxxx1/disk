package com.dao;

import com.beans.LogGroupInfo;
import com.beans.LogInfo;

import java.util.List;

/**
 * @Author wuxin
 * @Date 2023/6/30 16:48
 * @Description
 * @Version
 */
public interface HiveDao {
    List<LogInfo> getLogList(String userName);

    List<LogGroupInfo> getLogGroupList(String userName);
    List<LogGroupInfo> get_ershi(String userName);
}
