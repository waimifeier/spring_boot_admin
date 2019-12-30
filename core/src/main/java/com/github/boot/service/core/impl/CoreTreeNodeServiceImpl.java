package com.github.boot.service.core.impl;

import com.github.boot.beans.sys.TreeNodeResponse;
import com.github.boot.service.core.CoreTreeNodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CoreTreeNodeServiceImpl implements CoreTreeNodeService {


    @Override
    public TreeNodeResponse buildTreeNode(Long id, String name, String code){
        TreeNodeResponse response = new TreeNodeResponse();
        response.setValue(id);
        response.setLabel(name);
        response.setCode(code);
        return response;
    }
}
