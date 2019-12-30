package com.github.boot.service.core;

import com.github.boot.beans.sys.TreeNodeResponse;

public interface CoreTreeNodeService {

    /**
     *  构建树节点基本信息
     * @param id
     * @param name
     * @param code
     * @return
     */
    TreeNodeResponse buildTreeNode(Long id, String name, String code);

}
