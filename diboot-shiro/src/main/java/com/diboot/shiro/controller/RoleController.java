package com.diboot.shiro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.diboot.core.controller.BaseCrudRestController;
import com.diboot.core.service.BaseService;
import com.diboot.core.service.DictionaryService;
import com.diboot.core.util.V;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.KeyValue;
import com.diboot.core.vo.Pagination;
import com.diboot.core.vo.Status;
import com.diboot.shiro.authz.annotation.AuthorizationPrefix;
import com.diboot.shiro.authz.annotation.AuthorizationWrapper;
import com.diboot.shiro.dto.RoleDto;
import com.diboot.shiro.entity.Permission;
import com.diboot.shiro.entity.Role;
import com.diboot.shiro.enums.IUserType;
import com.diboot.shiro.service.PermissionService;
import com.diboot.shiro.service.RoleService;
import com.diboot.shiro.vo.RoleVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色管理
 *
 * @author Wangyongliang
 * @version v2.0
 * @date 2019/6/20
 */
@RestController
@RequestMapping("/role")
@AuthorizationPrefix(prefix = "role", code = "role", name = "角色管理")
public class RoleController extends BaseCrudRestController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    protected BaseService getService() {
        return roleService;
    }

    /***
     * 获取Entity列表（分页）
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/list")
    @AuthorizationWrapper(value = @RequiresPermissions("list"), name = "列表")
    public JsonResult getVOList(RoleDto roleDto, Pagination pagination, HttpServletRequest request) throws Exception{
        QueryWrapper<RoleDto> queryWrapper = super.buildQueryWrapper(roleDto);
        // 获取结果
        List<RoleVO> voList = roleService.getRoleList(queryWrapper, pagination);
        // 返回结果
        return new JsonResult(Status.OK, voList).bindPagination(pagination);
    }

    /***
     * 创建Entity
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    @AuthorizationWrapper(value = @RequiresPermissions("create"), name = "添加")
    public JsonResult createEntity(@RequestBody Role entity, BindingResult result, HttpServletRequest request)
            throws Exception{
        // 创建
        return roleService.createRole(entity) ? new JsonResult(Status.OK) : new JsonResult(Status.FAIL_OPERATION);
    }

    /***
     * 更新Entity
     * @param id ID
     * @return
     * @throws Exception
     */
    @PutMapping("/{id}")
    @AuthorizationWrapper(value = @RequiresPermissions("update"), name = "更新")
    public JsonResult updateModel(@PathVariable("id")Long id, @RequestBody Role entity, BindingResult result,
                                  HttpServletRequest request) throws Exception{
        entity.setId(id);
        roleService.updateRole(entity);
        return new JsonResult(Status.OK);
    }

    /***
     * 查询Entity
     * @param id ID
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    @AuthorizationWrapper(value = @RequiresPermissions("read"), name = "查看")
    public JsonResult getModel(@PathVariable("id")Long id, HttpServletRequest request)
            throws Exception{
        RoleVO roleVO = roleService.getRole(id);
        return new JsonResult(roleVO);
    }

    /***
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{id}")
    @AuthorizationWrapper(value = @RequiresPermissions("delete"), name = "删除")
    public JsonResult deleteModel(@PathVariable("id")Long id, HttpServletRequest request) throws Exception{
        boolean success = roleService.deleteRole(id);
        if(success){
            return new JsonResult(Status.OK);
        }else{
            return new JsonResult(Status.FAIL_OPERATION );
        }
    }


    /***
     * 显示创建页面
     * @return
     * @throws Exception
     */
    @GetMapping("/toCreatePage")
    public JsonResult toCreatePage(HttpServletRequest request, ModelMap modelMap)
            throws Exception{
        modelMap.put("menuList", permissionService.getApplicationAllPermissionList());
        return new JsonResult(modelMap);
    }


    /***
     * 显示更新页面
     * @return
     * @throws Exception
     */
    @GetMapping("/toUpdatePage/{id}")
    public JsonResult toUpdatePage(@PathVariable("id")Long id, HttpServletRequest request)
            throws Exception{
        RoleVO roleVO = roleService.toUpdatePage(id);
        return new JsonResult(roleVO);
    }


    /***
     * 获取所有菜单,以及每个菜单下的所有权限
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllMenu")
    public JsonResult getAllMenu(HttpServletRequest request)
            throws Exception{
        return new JsonResult(permissionService.getApplicationAllPermissionList());
    }

    /***
     * 加载更多数据
     * @return
     * @throws Exception
     */
    @GetMapping("/attachMore")
    public JsonResult attachMore(HttpServletRequest request, ModelMap modelMap)
            throws Exception{

        //获取角色状态KV
        List<KeyValue> roleStatusKvList = dictionaryService.getKeyValueList(Role.DICT_STATUS);
        modelMap.put("roleStatusKvList", roleStatusKvList);

        return new JsonResult(modelMap);
    }


    /**
     * 校验角色code是否重复
     * @param id
     * @param code
     * @param request
     * @return
     */
    @GetMapping("/checkCodeRepeat")
    public JsonResult checkCodeRepeat(@RequestParam(required = false) Long id, @RequestParam String code, HttpServletRequest request){
        if(V.notEmpty(code)){
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper();
            wrapper.eq(Role::getCode, code);
            if(V.notEmpty(id)){
                wrapper.ne(Role::getId, id);
            }
            List<Role> roleList = roleService.getEntityList(wrapper);
            if(V.isEmpty(roleList)){
                return new JsonResult(Status.OK, "code可用");
            }
            return new JsonResult(Status.FAIL_OPERATION, "code已存在");
        }

        return new JsonResult(Status.OK,"code可用");
    }


}
