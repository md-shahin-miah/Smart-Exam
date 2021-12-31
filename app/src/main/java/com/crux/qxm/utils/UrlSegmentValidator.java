package com.crux.qxm.utils;

import static com.crux.qxm.utils.StaticValues.URI_GROUP;
import static com.crux.qxm.utils.StaticValues.URI_POLL;
import static com.crux.qxm.utils.StaticValues.URI_QXM;
import static com.crux.qxm.utils.StaticValues.URI_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.URI_USER;

public class UrlSegmentValidator {

    public static boolean isValidSegmentCategory(String segment){

        return segment.equals(URI_USER) || segment.equals(URI_QXM) || segment.equals(URI_SINGLE_MCQ) ||
                segment.equals(URI_POLL) || segment.equals(URI_GROUP);
    }
}
