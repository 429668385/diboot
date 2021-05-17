/*
 * Copyright (c) 2015-2020, www.dibo.ltd (service@dibo.ltd).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.diboot.iam.annotation.process;

import com.diboot.iam.cache.IamCacheManager;

import java.util.List;

/**
 * 注解相关缓存
 * {@link IamCacheManager}
 * @author mazc@dibo.ltd
 * @version v2.0
 * @date 2019/12/30
 */
@Deprecated
public class ApiPermissionCache {

    /**
     * 读取缓存permission
     * {@link IamCacheManager#getPermissionCode(String)}
     * @param requestMethodAndUrl
     * @return
     */
    @Deprecated
    public static String getPermissionCode(String requestMethodAndUrl){
        return IamCacheManager.getPermissionCode(requestMethodAndUrl);
    }

    /**
     * 读取缓存permission
     * {@link IamCacheManager#getPermissionCode(String, String)}
     * @param requestMethod
     * @param url
     * @return
     */
    @Deprecated
    public static String getPermissionCode(String requestMethod, String url){
        return IamCacheManager.getPermissionCode(requestMethod, url);
    }

    /**
     * 返回全部ApiPermissionVO
     * {@link IamCacheManager#getApiPermissionVoList()}
     * @return
     */
    @Deprecated
    public static List<ApiPermissionWrapper> getApiPermissionVoList(){
        return IamCacheManager.getApiPermissionVoList();
    }

}