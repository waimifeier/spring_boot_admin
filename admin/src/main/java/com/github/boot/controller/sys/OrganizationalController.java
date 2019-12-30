package com.github.boot.controller.sys;

import cn.hutool.core.map.MapUtil;
import com.github.boot.annotation.LoginUser;
import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.common.PlantException;
import com.github.boot.beans.request.sys.DepartmentRequestParams;
import com.github.boot.service.sys.OrganizationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class OrganizationalController {

    @Autowired
    private OrganizationalService organizationalService;


    /**
     * 2.添加公司部门
     * @param params
     * @return
     */
    @RequestMapping("/add")
    public JSONReturn addDepartment(@RequestBody @Validated DepartmentRequestParams params, @LoginUser Long userId){
        organizationalService.addDepartment(params, userId);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 3. 修改公司部门
     * @param params
     * @return
     */
    @RequestMapping("/modify")
    public JSONReturn modifyDepartment(@RequestBody @Validated DepartmentRequestParams params, @LoginUser Long userId){
        organizationalService.modifyDepartment(params, userId);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /***
     * 4. 查看详情
     * @return
     */
    @RequestMapping("/detail")
    public JSONReturn detail(@RequestBody DepartmentRequestParams params){
        Map<String, Object> response = organizationalService.detail(params);
        return JSONReturn.buildSuccess(response);
    }


    /**
     * 5. 获取组织架构树结构
     * @return
     */
    @RequestMapping("/list")
    public JSONReturn list(){
        return JSONReturn.buildSuccess(
                organizationalService.nodeList()
        );
    }

    /**
     * 6. 获取公司
     */
    @RequestMapping("/companyOrDepartmentList")
    public JSONReturn companyOrDepartmentList(@RequestBody HashMap<String,Object> params){

        String code = MapUtil.getStr(params, "code");
        if(StringUtils.isEmpty(code)) throw new PlantException("缺省code参数值");

        return JSONReturn.buildSuccess(
                organizationalService.companyOrDepartmentList(code)
        );
    }

    @RequestMapping("/agent_list")
    public JSONReturn agentList(){
        return JSONReturn.buildSuccess(
                organizationalService.agentList()
        );
    }


}
