package com.crux.qxm.db.sampleModels;

import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.group.GroupData;

import java.util.ArrayList;
import java.util.List;

public class GroupDataSampleModel {

    public static GroupData getSampleGroupData(){
        GroupData groupData = new GroupData();

        groupData.setGroupImageUrl("");
        groupData.setGroupName("Demo Group");
        groupData.setMyStatus("Admin");
        groupData.setFollowerCount("5");
        groupData.setMemberCount("10");
        groupData.setQxmCount("3");
        groupData.setGroupDescription("Demo Group Sample Description...");
        List<FeedData> feedDataList = new ArrayList<>();
        groupData.setGroupQxmList(feedDataList);

        return groupData;

    }

}
