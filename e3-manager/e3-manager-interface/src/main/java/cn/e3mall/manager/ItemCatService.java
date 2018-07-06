package cn.e3mall.manager;

import cn.e3mall.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    public List<EasyUITreeNode> getCatList(long parentId);
}
