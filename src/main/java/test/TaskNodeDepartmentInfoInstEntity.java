package test;

/**
 * @author wusd
 * @description 空
 * @create 2020/03/18 18:01
 */
public class TaskNodeDepartmentInfoInstEntity {

    /**
     * departmentId : 100100
     * parentDepartmentId : -1
     */

    private String departmentId;
    private String parentDepartmentId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(String parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }
}
