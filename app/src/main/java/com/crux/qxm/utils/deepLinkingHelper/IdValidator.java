package com.crux.qxm.utils.deepLinkingHelper;

public class IdValidator {
    public static boolean isValidMongoDBObjectId(String _id) {
        String regex = "[a-fA-F0-9]{24}";
        return _id.matches(regex);
    }
}
