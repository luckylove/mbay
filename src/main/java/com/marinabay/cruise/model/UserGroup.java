package com.marinabay.cruise.model;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class UserGroup extends GenericModel{

    private String name;
    private String description;

    private Long userCount;

    public Long getUserCount() {
        return userCount == null ? 0: userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
