package com.crux.qxm.utils;

import android.content.Context;

import com.crux.qxm.R;

import java.util.ArrayList;

public class QxmCategoryHelper {
    public static ArrayList<String> getQxmCategoryList(Context context){

        ArrayList<String> qxmCategoryList = new ArrayList<>();
        qxmCategoryList.add(context.getString(R.string.category_arts_and_culture));
        qxmCategoryList.add(context.getString(R.string.category_books));
        qxmCategoryList.add(context.getString(R.string.category_business));
        qxmCategoryList.add(context.getString(R.string.category_class_related_exam));
        qxmCategoryList.add(context.getString(R.string.category_design_and_prototyping));
        qxmCategoryList.add(context.getString(R.string.category_education));
        qxmCategoryList.add(context.getString(R.string.category_engineering));
        qxmCategoryList.add(context.getString(R.string.category_electrical_and_electronics));
        qxmCategoryList.add(context.getString(R.string.category_environment));
        qxmCategoryList.add(context.getString(R.string.category_entertainment));
        qxmCategoryList.add(context.getString(R.string.category_film_and_animation));
        qxmCategoryList.add(context.getString(R.string.category_food));
        qxmCategoryList.add(context.getString(R.string.category_fun));
        qxmCategoryList.add(context.getString(R.string.category_gadget_and_product));
        qxmCategoryList.add(context.getString(R.string.category_game));
        qxmCategoryList.add(context.getString(R.string.category_general_knowledge));
        qxmCategoryList.add(context.getString(R.string.category_globalization));
        qxmCategoryList.add(context.getString(R.string.category_health_and_medicine));
        qxmCategoryList.add(context.getString(R.string.category_history));
        qxmCategoryList.add(context.getString(R.string.category_music));
        qxmCategoryList.add(context.getString(R.string.category_news_and_politics));
        qxmCategoryList.add(context.getString(R.string.category_opinion));
        qxmCategoryList.add(context.getString(R.string.category_professional_or_training_exam));
        qxmCategoryList.add(context.getString(R.string.category_programming));
        qxmCategoryList.add(context.getString(R.string.category_quiz));
        qxmCategoryList.add(context.getString(R.string.category_science_and_technology));
        qxmCategoryList.add(context.getString(R.string.category_society));
        qxmCategoryList.add(context.getString(R.string.category_sport));
        qxmCategoryList.add(context.getString(R.string.category_travel_and_exam));
        qxmCategoryList.add(context.getString(R.string.category_tutorial));
        qxmCategoryList.add(context.getString(R.string.category_biology_and_bio_technology));

        return qxmCategoryList;
    }
}
