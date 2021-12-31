package com.crux.qxm.adapter.questions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.QuestionSet;
import com.crux.qxm.db.models.questions.QuestionSettings;
import com.crux.qxm.db.models.questions.ShortAnswer;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.utils.ImageProcessor;
//import com.crux.qxm.utils.RepeatableAlertdialogBuilder;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.inputValidations.InputValidationHelper;
import com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment;
import com.crux.qxm.views.fragments.createQxmActivityFragments.QxmSettingsFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import mabbas007.tagsedittext.TagsEditText;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.MAX_LENGTH_OF_CHARACTER_LIMIT_OF_SHORT_ANSWER_IN_DIGIT;
import static com.crux.qxm.utils.StaticValues.MAX_LENGTH_OF_POINT_IN_DIGIT;
import static com.crux.qxm.utils.StaticValues.MAX_LENGTH_OF_QUESTION_DESCRIPTION;
import static com.crux.qxm.utils.StaticValues.MAX_LENGTH_OF_QUESTION_HIDDEN_DESCRIPTION;
import static com.crux.qxm.utils.StaticValues.MAX_LENGTH_OF_QUESTION_TITLE;
import static com.crux.qxm.utils.StaticValues.MAX_LENGTH_OF_TAG;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION;
import static com.crux.qxm.utils.StaticValues.PARCELABLE_QUESTION_SET;
import static com.crux.qxm.utils.StaticValues.PARCELABLE_QUESTION_SETTINGS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;
import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment.questionThumbnailImage;
import static com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment.youtubeThumbnailURL;

public class TemplateQuestionAdapter extends RecyclerView.Adapter<TemplateQuestionAdapter.ViewHolder> {

    private static final String TAG = "TemplateQuestionAdapter";
    private Fragment fragment;
    private Context context;
    private ArrayList<Question> questionArrayList;
    private RecyclerView recyclerView;
    private ImageProcessor imageProcessorForSingleQuestion;
    private OnPhotoUploadCLickListener onPhotoUploadCLickListener;
    private LinearLayoutCompat llcAdQuestion;
    private AppCompatImageView ivDoneCreatingQuestion;
    private AppCompatEditText etQuestionSetTitle;
    private AppCompatEditText etQuestionSetDescription;
    private AppCompatEditText etYouTubeLink;
    private AppCompatTextView tvYouTubeVideoTitle;
    private QuestionSettings questionSettings;
    private QxmDraft qxmDraft;
    private boolean qxmEdit = false;
    private InputFilter.LengthFilter inputFilterDescription;
    private InputFilter.LengthFilter inputFilterHiddenDescription;
    private InputFilter.LengthFilter inputFilterTag;
    private InputValidationHelper inputValidationHelperDescription;
    private InputValidationHelper inputValidationHelperHiddenDescription;
    private InputValidationHelper inputValidationHelperTag;
    private InputValidationHelper inputValidationHelperCharacterLimit;
    private int maxLengthOfPointMessageShownCount = 0;
    private int maxLengthOfCharacterLimitShownCount = 0;


    public TemplateQuestionAdapter(Context context, Fragment fragment, ArrayList<Question> questionArrayList, QuestionSettings questionSettings, RecyclerView recyclerView, ImageProcessor imageProcessorForSingleQuestion, LinearLayoutCompat llcAdQuestion, AppCompatImageView ivDoneCreatingQuestion, AppCompatEditText etQuestionSetTitle, AppCompatEditText etQuestionSetDescription, AppCompatEditText etYouTubeLink, AppCompatTextView tvYouTubeVideoTitle, QxmDraft qxmDraft) {
        this.context = context;
        this.fragment = fragment;
        this.questionArrayList = questionArrayList;
        this.questionSettings = questionSettings;
        this.recyclerView = recyclerView;
        this.imageProcessorForSingleQuestion = imageProcessorForSingleQuestion;
        this.llcAdQuestion = llcAdQuestion;
        this.ivDoneCreatingQuestion = ivDoneCreatingQuestion;
        this.etQuestionSetTitle = etQuestionSetTitle;
        this.etQuestionSetDescription = etQuestionSetDescription;
        this.etYouTubeLink = etYouTubeLink;
        this.qxmDraft = qxmDraft;
        this.tvYouTubeVideoTitle = tvYouTubeVideoTitle;

        inputFilterDescription = new InputFilter.LengthFilter(MAX_LENGTH_OF_QUESTION_DESCRIPTION);
        inputFilterHiddenDescription = new InputFilter.LengthFilter(MAX_LENGTH_OF_QUESTION_HIDDEN_DESCRIPTION);
        inputFilterTag = new InputFilter.LengthFilter(MAX_LENGTH_OF_TAG);

        inputValidationHelperDescription = new InputValidationHelper(context);
        inputValidationHelperHiddenDescription = new InputValidationHelper(context);
        inputValidationHelperTag = new InputValidationHelper(context);
        inputValidationHelperCharacterLimit = new InputValidationHelper(context);
    }

    public TemplateQuestionAdapter(Context context, Fragment fragment, ArrayList<Question> questionArrayList, QuestionSettings questionSettings, RecyclerView recyclerView, ImageProcessor imageProcessorForSingleQuestion, LinearLayoutCompat llcAdQuestion, AppCompatImageView ivDoneCreatingQuestion, AppCompatEditText etQuestionSetTitle, AppCompatEditText etQuestionSetDescription, AppCompatEditText etYouTubeLink, AppCompatTextView tvYouTubeVideoTitle, QxmDraft qxmDraft, boolean qxmEdit) {
        this.context = context;
        this.fragment = fragment;
        this.questionArrayList = questionArrayList;
        this.questionSettings = questionSettings;
        this.recyclerView = recyclerView;
        this.imageProcessorForSingleQuestion = imageProcessorForSingleQuestion;
        this.llcAdQuestion = llcAdQuestion;
        this.ivDoneCreatingQuestion = ivDoneCreatingQuestion;
        this.etQuestionSetTitle = etQuestionSetTitle;
        this.etQuestionSetDescription = etQuestionSetDescription;
        this.etYouTubeLink = etYouTubeLink;
        this.qxmDraft = qxmDraft;
        this.tvYouTubeVideoTitle = tvYouTubeVideoTitle;
        this.qxmEdit = qxmEdit;
    }


    @NonNull
    @Override
    public TemplateQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.create_qxm_single_question, parent, false);
        return new TemplateQuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateQuestionAdapter.ViewHolder holder, int position) {


        File mediaStorageDir;
        File imageFile;

        Question question = questionArrayList.get(holder.getAdapterPosition());
        switch (question.getQuestionType()) {

            case QUESTION_TYPE_MULTIPLE_CHOICE:

                holder.spinnerQuestionType.setSelection(0);
                Log.d(TAG, "onBindViewHolder: Multiple Choice");

                // region setting basic element of multiple choice if exists


                holder.etMultipleChoiceQuestionName.setText(question.getMultipleChoice().getQuestionTitle());
                holder.etPoints.setText(question.getMultipleChoice().getPoints());
                holder.switchRequired.setChecked(question.getMultipleChoice().getRequired());


                if (question.getMultipleChoice().getLocalImage() != null) {

                    Log.d(TAG, "LocalImage: " + question.getMultipleChoice().getLocalImage());

                    if (!question.getMultipleChoice().getLocalImage().isEmpty()) {
                        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.qxm_local_resource_storage_directory_name));
                        imageFile = new File(mediaStorageDir, question.getMultipleChoice().getLocalImage());
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        holder.ivQuestionImage.setVisibility(View.VISIBLE);
                        //holder.ivQuestionImage.setImageBitmap(myBitmap);

                        String imageUrl = IMAGE_SERVER_ROOT + question.getMultipleChoice().getLocalImage();
                        Log.d(TAG, "onBindViewHolder imageUrl: " + imageUrl);
                        Picasso.get().load(imageUrl).into(holder.ivQuestionImage);
                    }

                }

                //endregion

                holder.llcMultipleQuestionContainer.setVisibility(View.VISIBLE);
                holder.llcFillTheBlanksContainer.setVisibility(View.GONE);
                holder.llcShortAnswerContainer.setVisibility(View.GONE);

                Log.d(TAG, "onPollSelected: MultipleChoice Container Activated");
                holder.etWordLimit.setVisibility(View.GONE);
                holder.viewDivision.setVisibility(View.GONE);
                break;


            case QUESTION_TYPE_FILL_IN_THE_BLANKS:

                holder.spinnerQuestionType.setSelection(1);
                Log.d(TAG, "onBindViewHolder: Fill In the blanks");

                // region setting basic element of fill in the blanks if exists

                holder.etFillBlankQuestionName.setText(question.getFillInTheBlanks().getQuestionTitle());
                holder.etPoints.setText(question.getFillInTheBlanks().getPoints());
                holder.switchRequired.setChecked(question.getFillInTheBlanks().getRequired());

                if (question.getFillInTheBlanks().getLocalImage() != null) {

                    if (!question.getFillInTheBlanks().getLocalImage().isEmpty()) {

                        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.qxm_local_resource_storage_directory_name));
                        imageFile = new File(mediaStorageDir, question.getFillInTheBlanks().getLocalImage());
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        holder.ivQuestionImage.setVisibility(View.VISIBLE);
                        // holder.ivQuestionImage.setImageBitmap(myBitmap);
                        String imageUrl = IMAGE_SERVER_ROOT + question.getFillInTheBlanks().getLocalImage();
                        Log.d(TAG, "onBindViewHolder imageUrl: " + imageUrl);
                        Picasso.get().load(imageUrl).into(holder.ivQuestionImage);
                    }

                }

                //endregion

                holder.llcMultipleQuestionContainer.setVisibility(View.GONE);
                holder.llcFillTheBlanksContainer.setVisibility(View.VISIBLE);
                holder.llcShortAnswerContainer.setVisibility(View.GONE);

                holder.etWordLimit.setVisibility(View.GONE);
                holder.viewDivision.setVisibility(View.GONE);
                Log.d(TAG, "onPollSelected: FillInTheBlanks Container Activated");


                break;

            case QUESTION_TYPE_SHORT_ANSWER:

                holder.spinnerQuestionType.setSelection(2);
                Log.d(TAG, "onBindViewHolder: Short Answer");

                // region setting basic element of short answer if exists

                holder.etShortAnswerQuestionName.setText(question.getShortAnswer().getQuestionTitle());
                holder.etPoints.setText(question.getShortAnswer().getPoints());
                holder.switchRequired.setChecked(question.getShortAnswer().getRequired());
                holder.etWordLimit.setText(question.getShortAnswer().getWordLimits());

                if (question.getShortAnswer().getLocalImage() != null) {

                    if (!question.getShortAnswer().getLocalImage().isEmpty()) {

                        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.qxm_local_resource_storage_directory_name));
                        imageFile = new File(mediaStorageDir, question.getShortAnswer().getLocalImage());
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        holder.ivQuestionImage.setVisibility(View.VISIBLE);
                        // holder.ivQuestionImage.setImageBitmap(myBitmap);
                        String imageUrl = IMAGE_SERVER_ROOT + question.getMultipleChoice().getLocalImage();
                        Log.d(TAG, "onBindViewHolder imageUrl: " + imageUrl);
                        Picasso.get().load(imageUrl).into(holder.ivQuestionImage);
                    }

                }

                //endregion

                holder.llcMultipleQuestionContainer.setVisibility(View.GONE);
                holder.llcFillTheBlanksContainer.setVisibility(View.GONE);
                holder.llcShortAnswerContainer.setVisibility(View.VISIBLE);

                holder.etWordLimit.setVisibility(View.VISIBLE);
                Log.d(TAG, "onPollSelected: ShortAnswer Container Activated");
                break;
        }


        // region question type spinner
        holder.spinnerQuestionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d(TAG, "onPollSelected: Called");

                switch (i) {
                    case 0:

                        question.setQuestionType(QUESTION_TYPE_MULTIPLE_CHOICE);

                        if (question.getQuestionType().equals(QUESTION_TYPE_FILL_IN_THE_BLANKS)) {
                            question.setFillInTheBlanks(getEmptyFillInTheBlanks());
                        }
                        if (question.getQuestionType().equals(QUESTION_TYPE_SHORT_ANSWER)) {
                            question.setShortAnswer(getEmptyShortAnswer());
                        }

                        holder.llcMultipleQuestionContainer.setVisibility(View.VISIBLE);
                        holder.llcFillTheBlanksContainer.setVisibility(View.GONE);
                        holder.llcShortAnswerContainer.setVisibility(View.GONE);

                        Log.d(TAG, "onPollSelected: MultipleChoice Container Activated");
                        holder.etWordLimit.setVisibility(View.GONE);
                        holder.viewDivision.setVisibility(View.GONE);

                        break;
                    case 1:

                        showFillBlanksEvaluationDialog(view.getContext());

                        question.setQuestionType(QUESTION_TYPE_FILL_IN_THE_BLANKS);

                        if (question.getQuestionType().equals(QUESTION_TYPE_MULTIPLE_CHOICE)) {
                            question.setMultipleChoice(getEmptyMultipleChoice());
                        }
                        if (question.getQuestionType().equals(QUESTION_TYPE_SHORT_ANSWER)) {
                            question.setShortAnswer(getEmptyShortAnswer());
                        }

                        holder.llcMultipleQuestionContainer.setVisibility(View.GONE);
                        holder.llcFillTheBlanksContainer.setVisibility(View.VISIBLE);
                        holder.llcShortAnswerContainer.setVisibility(View.GONE);

                        holder.etWordLimit.setVisibility(View.GONE);
                        holder.viewDivision.setVisibility(View.GONE);
                        Log.d(TAG, "onPollSelected: FillInTheBlanks Container Activated");


                        break;
                    case 2:

                        question.setQuestionType(QUESTION_TYPE_SHORT_ANSWER);

                        if (question.getQuestionType().equals(QUESTION_TYPE_MULTIPLE_CHOICE)) {
                            question.setMultipleChoice(getEmptyMultipleChoice());
                        }
                        if (question.getQuestionType().equals(QUESTION_TYPE_FILL_IN_THE_BLANKS)) {
                            question.setFillInTheBlanks(getEmptyFillInTheBlanks());
                        }
                        holder.llcMultipleQuestionContainer.setVisibility(View.GONE);
                        holder.llcFillTheBlanksContainer.setVisibility(View.GONE);
                        holder.llcShortAnswerContainer.setVisibility(View.VISIBLE);

                        holder.etWordLimit.setVisibility(View.VISIBLE);
                        holder.viewDivision.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onPollSelected: ShortAnswer Container Activated");

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //endregion

        // region multiple choice

        // updating multiple question title
        holder.etMultipleChoiceQuestionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: maxLengthOfQuestion: " + MAX_LENGTH_OF_QUESTION_TITLE);

                if (!TextUtils.isEmpty(editable) && !qxmEdit) {
                    if (editable.length() >= MAX_LENGTH_OF_QUESTION_TITLE) {
                        Toast.makeText(context, String.format(context.getString(R.string.toast_message_question_reached_maximum_length) + " %s.", MAX_LENGTH_OF_QUESTION_TITLE), Toast.LENGTH_SHORT).show();
                    }
                }

                Log.d(TAG, "afterTextChanged: QuestionTitle_MultipleChoice" + editable);
                question.getMultipleChoice().setQuestionTitle(editable.toString());
            }
        });


        // region multiple option adding


        MultipleChoiceAdapter multipleChoiceAdapter = new MultipleChoiceAdapter(context, question.getMultipleChoice().getOptions(), question.getMultipleChoice().getCorrectAnswers(), qxmEdit);
        holder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
        holder.rvMultipleOptions.setAdapter(multipleChoiceAdapter);
        holder.rvMultipleOptions.setNestedScrollingEnabled(false);

        holder.llcAddMultipleChoiceOption.setOnClickListener(v -> {

            question.getMultipleChoice().getOptions().add("");
            multipleChoiceAdapter.notifyItemInserted(question.getMultipleChoice().getOptions().size() - 1);
            holder.rvMultipleOptions.setNestedScrollingEnabled(false);
        });

        // endregion

        // endregion

        //region fill in the blank answer and blank adding


        // updating fill in the blanks questionArrayList.get(position) tile
        holder.etFillBlankQuestionName.addTextChangedListener(new TextWatcher() {

            // String beforeText = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.d(TAG, "beforeTextChanged QuestionTitle_FillInTheBlanks : " + charSequence.toString());
                // beforeText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.d(TAG, "onTextChanged QuestionTitle_FillInTheBlanks : " + charSequence.toString());
                Log.d(TAG, "onTextChanged i: " + i);
                Log.d(TAG, "onTextChanged i1: " + i1);
                Log.d(TAG, "onTextChanged i2: " + i2);

                // // blocking fill in the blanks editing
                // if (i1 > i2) {
                //     holder.etFillBlankQuestionName.setText(beforeText);
                //     holder.etFillBlankQuestionName
                //             .setSelection(Objects.requireNonNull(holder.etFillBlankQuestionName.getText()).length());
                //     Toast.makeText(context, context.getString(R.string.fill_in_the_blanks_editing_not_allowed_till_now),
                //             Toast.LENGTH_LONG).show();
                // }

            }

            @Override
            public void afterTextChanged(Editable editable) {


                // if (!TextUtils.isEmpty(editable) && !qxmEdit) {
                //     if (editable.length() >= MAX_LENGTH_OF_FILL_IN_THE_BLANKS_QUESTION_TITLE) {
                //         Toast.makeText(context, String.format(context.getString(R.string.toast_message_fill_in_the_blanks_question_reached_maximum_length) + " %s.", MAX_LENGTH_OF_FILL_IN_THE_BLANKS_QUESTION_TITLE), Toast.LENGTH_SHORT).show();
                //     }
                // }
                // Log.d(TAG, "afterTextChanged: QuestionTitle_FillInTheBlanks: " + editable);
                question.getFillInTheBlanks().setQuestionTitle(editable.toString());
            }
        });


        FillInTheBlanksAdapter fillInTheBlanksAdapter = new FillInTheBlanksAdapter(context, question.getFillInTheBlanks().getCorrectAnswers());
        holder.rvFillBlankAnswers.setLayoutManager(new LinearLayoutManager(context));
        holder.rvFillBlankAnswers.setAdapter(fillInTheBlanksAdapter);


        holder.btInsertBlank.setOnClickListener(v -> {

            String[] optionSerial = {

                    "(a)",
                    "(b)",
                    "(c)",
                    "(d)",
                    "(e)"
            };

            String text = holder.etFillBlankQuestionName.getText().toString().trim();

            // unicode for underline
            String underLine = "\uff3f";

            // checking last number of blank to give next number to the new blank
            int number;
            if (text.contains("(e)"))
                number = 5;
            else if (text.contains("(d)"))
                number = 4;
            else if (text.contains("(c)"))
                number = 3;
            else if (text.contains("(b)"))
                number = 2;
            else if (text.contains("(a)"))
                number = 1;
            else
                number = 0;

            if (number < 5) {

                // blank with a serial number
                String blank = " " + optionSerial[number] + underLine + underLine + underLine + " ";

                String blankWithText = text + blank;

                // more thank 5 blank is not allowed
                holder.etFillBlankQuestionName.setText(blankWithText);
                holder.etFillBlankQuestionName.setSelection(blankWithText.length());

                question.getFillInTheBlanks().getCorrectAnswers().add("");
                fillInTheBlanksAdapter.notifyItemInserted(question.getFillInTheBlanks().getCorrectAnswers().size() - 1);
                holder.rvFillBlankAnswers.setNestedScrollingEnabled(false);

            } else {

                Toast.makeText(context, "More Than 5 blank is not allowed", Toast.LENGTH_SHORT).show();

            }

        });


        //endregion

        //region short answer

        // updating short answer question title
        holder.etShortAnswerQuestionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d(TAG, "afterTextChanged: maxLengthOfQuestion: " + MAX_LENGTH_OF_QUESTION_TITLE);

                if (!TextUtils.isEmpty(editable) && !qxmEdit) {
                    if (editable.length() >= MAX_LENGTH_OF_QUESTION_TITLE)
                        Toast.makeText(context, String.format(context.getString(R.string.toast_message_question_reached_maximum_length) + " %s.", MAX_LENGTH_OF_QUESTION_TITLE), Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, "afterTextChanged: QuestionTitle_ShortAnswer" + editable);
                question.getShortAnswer().setQuestionTitle(editable.toString());

            }
        });


        //updating short answer character limit
        holder.etWordLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!TextUtils.isEmpty(editable.toString()) && !qxmEdit) {
                    if (editable.length() >= MAX_LENGTH_OF_CHARACTER_LIMIT_OF_SHORT_ANSWER_IN_DIGIT && maxLengthOfCharacterLimitShownCount < 2) {
                        maxLengthOfCharacterLimitShownCount++;
                        Toast.makeText(context, context.getString(R.string.toast_message_character_input_reached_maximum_limit), Toast.LENGTH_SHORT).show();
                    }
                }

                Log.d(TAG, "afterTextChanged: ShortQuestion Word");
                question.getShortAnswer().setWordLimits(editable.toString());
            }
        });

        //endregion

        // region common for all

        holder.ivQuestionImagePicker.setOnClickListener(view -> {


            onPhotoUploadCLickListener.photoUploadedClicked(questionArrayList, holder.getAdapterPosition(), holder.spinnerQuestionType.getSelectedItemPosition());
            // imageProcessorForSingleQuestion = new ImageProcessor(context, fragment, TAG, apiService, user, holder
            //         .ivQuestionImage);

            StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION;
            imageProcessorForSingleQuestion.pickImageFromGallery();
            imageProcessorForSingleQuestion.setQuestionPosition(holder.getAdapterPosition());
            imageProcessorForSingleQuestion.setImageView(holder.ivQuestionImage);
            imageProcessorForSingleQuestion.setRemovePhotoImageView(holder.ivRemoveSingleQuestionImage);
            Log.d(TAG, "onBindViewHolder: " + questionThumbnailImage);

            switch ((int) holder.spinnerQuestionType.getSelectedItemId()) {

                case 0:
                    question.getMultipleChoice().setImage(questionThumbnailImage);
                    break;
                case 1:
                    question.getFillInTheBlanks().setImage(questionThumbnailImage);
                    break;
                case 2:
                    question.getShortAnswer().setImage(questionThumbnailImage);
                    break;

            }
        });

        //region remove single question image

        holder.ivRemoveSingleQuestionImage.setOnClickListener(v -> {

            switch ((int) holder.spinnerQuestionType.getSelectedItemId()) {

                case 0:
                    question.getMultipleChoice().setImage(" ");
                    break;
                case 1:
                    question.getFillInTheBlanks().setImage(" ");
                    break;
                case 2:
                    question.getShortAnswer().setImage(" ");
                    break;

            }

            holder.ivQuestionImage.setVisibility(View.GONE);
            holder.ivRemoveSingleQuestionImage.setVisibility(View.GONE);

        });


        // updating question point
        holder.etPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!TextUtils.isEmpty(editable.toString()) && !qxmEdit) {
                    if (editable.length() >= MAX_LENGTH_OF_POINT_IN_DIGIT && maxLengthOfPointMessageShownCount < 2) {
                        maxLengthOfPointMessageShownCount++;
                        Toast.makeText(context, context.getString(R.string.toast_message_point_reached_maximum_limit), Toast.LENGTH_SHORT).show();
                    }
                }

                if (holder.spinnerQuestionType.getSelectedItemId() == 0) {

                    Log.d(TAG, "afterTextChanged: Point_MultipleChoice: " + editable);
                    question.getMultipleChoice().setPoints(editable.toString());

                } else if (holder.spinnerQuestionType.getSelectedItemId() == 1) {

                    Log.d(TAG, "afterTextChanged: Point_FillInTheBlanks: " + editable);
                    question.getFillInTheBlanks().setPoints(editable.toString());
                } else if (holder.spinnerQuestionType.getSelectedItemId() == 2) {

                    Log.d(TAG, "afterTextChanged: Point_ShortAnswer: " + editable);
                    question.getShortAnswer().setPoints(editable.toString());

                }

            }
        });

        // updating if question is required or not
        holder.switchRequired.setOnCheckedChangeListener((compoundButton, b) -> {

            if (compoundButton.isChecked()) {

                switch ((int) holder.spinnerQuestionType.getSelectedItemId()) {

                    case 0:
                        Log.d(TAG, "Required_MultipleChoice: " + b);
                        question.getMultipleChoice().setRequired(b);
                        break;
                    case 1:
                        Log.d(TAG, "Required_FillInTheBlanks: " + b);
                        question.getFillInTheBlanks().setRequired(b);
                        break;
                    case 2:
                        Log.d(TAG, "Required_ShortAnswer: " + b);
                        question.getShortAnswer().setRequired(b);
                        break;
                }
            } else {

                switch ((int) holder.spinnerQuestionType.getSelectedItemId()) {

                    case 0:
                        Log.d(TAG, "Required_MultipleChoice: " + b);
                        question.getMultipleChoice().setRequired(b);
                        break;
                    case 1:
                        Log.d(TAG, "Required_FillInTheBlanks: " + b);
                        question.getFillInTheBlanks().setRequired(b);
                        break;
                    case 2:
                        Log.d(TAG, "Required_ShortAnswer: " + b);
                        question.getShortAnswer().setRequired(b);
                        break;
                }
            }
        });


        //question option onclick

        holder.ivQuestionOptions.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(context, v);
            popup.setGravity(Gravity.END);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_signle_question_options, popup.getMenu());

            // checking if already question has a description or hidden description
            // if so, then show 'Description added' instead of 'Add description' and
            // 'Hidden Description Added' instead of 'Add Hidden Description' and at last
            // if tags are added show "Tags Added" instead of 'Add Tags'

            // region checking if description,hidden description and tags are already given or not

            if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                if (!question.getMultipleChoice().getDescription().trim().isEmpty())
                    popup.getMenu().getItem(2).setTitle("Description Added");

                else popup.getMenu().getItem(2).setTitle("Add Description");

                if (!question.getMultipleChoice().getHiddenDescription().trim().isEmpty())
                    popup.getMenu().getItem(3).setTitle("Hidden Description Added");

                else popup.getMenu().getItem(3).setTitle("Add Hidden Description");

                if (question.getMultipleChoice().getTags() != null) {

                    if (question.getMultipleChoice().getTags().size() != 0)
                        popup.getMenu().getItem(4).setTitle("Tags Added");

                    else popup.getMenu().getItem(4).setTitle("Add Tags");
                }


            } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                if (!question.getFillInTheBlanks().getDescription().trim().isEmpty())
                    popup.getMenu().getItem(2).setTitle("Description Added");

                else popup.getMenu().getItem(2).setTitle("Add Description");

                if (!question.getFillInTheBlanks().getHiddenDescription().trim().isEmpty())
                    popup.getMenu().getItem(3).setTitle("Hidden Description Added");

                else popup.getMenu().getItem(3).setTitle("Add Hidden Description");

                if (question.getFillInTheBlanks().getTags() != null) {

                    if (question.getFillInTheBlanks().getTags().size() != 0)
                        popup.getMenu().getItem(4).setTitle("Tags Added");

                    else popup.getMenu().getItem(4).setTitle("Add Tags");
                }


            } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                if (!question.getShortAnswer().getDescription().trim().isEmpty())
                    popup.getMenu().getItem(2).setTitle("Description Added");

                else popup.getMenu().getItem(2).setTitle("Add Description");

                if (!question.getShortAnswer().getHiddenDescription().trim().isEmpty())
                    popup.getMenu().getItem(3).setTitle("Hidden Description Added");

                else popup.getMenu().getItem(3).setTitle("Add Hidden Description");

                if (question.getShortAnswer().getTags() != null) {

                    if (question.getShortAnswer().getTags().size() != 0)
                        popup.getMenu().getItem(4).setTitle("Tags Added");

                    else popup.getMenu().getItem(4).setTitle("Add Tags");

                }


            }

            // endregion

            popup.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {

                    case R.id.action_duplicate_item:

                        //region action duplicate

                        if (isSingleQuestionValid(question, holder)) {

                            // adding current question at the last position
                            Gson gson = new Gson();
                            Question copiedQuestion = gson.fromJson(gson.toJson(question), Question.class);
                            questionArrayList.add(copiedQuestion);
                            notifyItemInserted(questionArrayList.size() - 1);
                            //scrolling recycler view to the last position
                            recyclerView.smoothScrollToPosition(questionArrayList.size() - 1);

                        } else {
                            Toast.makeText(context, "The question you are duplicating is not valid!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    //endregion

                    case R.id.action_remove_question:

                        // region action remove question

                        questionArrayList.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        // notifyItemRemoved(holder.getAdapterPosition());
                        break;

                    //endregion

                    case R.id.action_add_description:

                        //region action add description
                        AppCompatDialog dialogDescription;
                        AlertDialog.Builder builderDescription = new AlertDialog.Builder(context);
                        LayoutInflater descriptionLayoutInflater = LayoutInflater.from(context);
                        View descriptionDialogView = descriptionLayoutInflater.inflate(R.layout.layout_edit_text_dialog, null);
                        builderDescription.setView(descriptionDialogView);
                        AppCompatEditText descriptionEditText = descriptionDialogView.findViewById(R.id.etDialog);

                        descriptionEditText.setFilters(new InputFilter[]{inputFilterDescription});

                        descriptionEditText.setTextColor(Color.parseColor("#000000"));
                        descriptionEditText.setHint("Describe question briefly");
                        builderDescription.setTitle("Description");
                        builderDescription.setCancelable(false);

                        // if already user put description, show it

                        if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                            if (!question.getMultipleChoice().getDescription().trim().isEmpty()) {

                                descriptionEditText.setText(question.getMultipleChoice().getDescription());
                            }
                        } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                            if (!question.getFillInTheBlanks().getDescription().trim().isEmpty()) {

                                descriptionEditText.setText(question.getFillInTheBlanks().getDescription());
                            }
                        } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                            if (!question.getShortAnswer().getDescription().trim().isEmpty()) {

                                descriptionEditText.setText(question.getShortAnswer().getDescription());
                            }
                        }

                        // The Allowed Description Character length is R.integer.maxLengthQuestionDescription = 200
                        // Add Text Watcher for showing proper Toast message for showing the description limit

                        inputValidationHelperDescription.showEditTextMaximumCharacterReachedMessage(
                                descriptionEditText,
                                MAX_LENGTH_OF_QUESTION_DESCRIPTION,
                                String.format(context.getString(R.string.toast_message_description_reached_maximum_length) + " %s.", MAX_LENGTH_OF_QUESTION_DESCRIPTION)
                        );

                        builderDescription.setPositiveButton("OK", (dialogInterface, i) -> {

                            // checking user if user left the edit text blank or not
                            if (!descriptionEditText.getText().toString().isEmpty()) {

                                if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                                    question.getMultipleChoice().setDescription(descriptionEditText.getText().toString());

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                                    question.getFillInTheBlanks().setDescription(descriptionEditText.getText().toString());

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                                    question.getShortAnswer().setDescription(descriptionEditText.getText().toString());
                                }


                            } else {

                                if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                                    question.getMultipleChoice().setDescription(" ");

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                                    question.getFillInTheBlanks().setDescription(" ");

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                                    question.getShortAnswer().setDescription(" ");
                                }
                            }

                            // hiding dialog after pressing ok
                            dialogInterface.dismiss();
                        });

                        dialogDescription = builderDescription.create();
                        dialogDescription.show();
                        break;

                    // endregion

                    case R.id.action_add_hidden_description:

                        //region action add hidden description

                        AppCompatDialog dialogHiddenDescription;
                        AlertDialog.Builder builderHiddenDescription = new AlertDialog.Builder(context);
                        LayoutInflater hiddenDescriptionLayoutInflater = LayoutInflater.from(context);
                        View hiddenDescriptionDialogView = hiddenDescriptionLayoutInflater.inflate(R.layout.layout_edit_text_dialog, null);
                        builderHiddenDescription.setView(hiddenDescriptionDialogView);
                        AppCompatEditText hiddenDescriptionEditText = hiddenDescriptionDialogView.findViewById(R.id.etDialog);

                        hiddenDescriptionEditText.setFilters(new InputFilter[]{inputFilterHiddenDescription});

                        hiddenDescriptionEditText.setTextColor(Color.parseColor("#000000"));
                        hiddenDescriptionEditText.setHint("Add hidden description");
                        builderHiddenDescription.setTitle("Hidden Description");
                        builderHiddenDescription.setCancelable(false);


                        // if already user put hidden description, show it

                        if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                            if (!question.getMultipleChoice().getHiddenDescription().trim().isEmpty()) {

                                hiddenDescriptionEditText.setText(question.getMultipleChoice().getHiddenDescription());
                            }
                        } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                            if (!question.getFillInTheBlanks().getHiddenDescription().trim().isEmpty()) {

                                hiddenDescriptionEditText.setText(question.getFillInTheBlanks().getHiddenDescription());
                            }
                        } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                            if (!question.getShortAnswer().getHiddenDescription().trim().isEmpty()) {

                                hiddenDescriptionEditText.setText(question.getShortAnswer().getHiddenDescription());
                            }
                        }

                        // The Allowed Hidden Description Character length is R.integer.maxLengthQuestionHiddenDescription = 200
                        // Add Text Watcher for showing proper Toast message for showing the description limit

                        inputValidationHelperHiddenDescription.showEditTextMaximumCharacterReachedMessage(
                                hiddenDescriptionEditText,
                                MAX_LENGTH_OF_QUESTION_HIDDEN_DESCRIPTION,
                                String.format(context.getString(R.string.toast_message_hidden_description_reached_maximum_length) + " %s.", MAX_LENGTH_OF_QUESTION_HIDDEN_DESCRIPTION)
                        );

                        builderHiddenDescription.setPositiveButton("OK", (dialogInterface, i) -> {

                            // checking user if user left the edit text blank or not
                            if (!hiddenDescriptionEditText.getText().toString().isEmpty()) {

                                if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                                    question.getMultipleChoice().setHiddenDescription(hiddenDescriptionEditText.getText().toString());

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                                    question.getFillInTheBlanks().setHiddenDescription(hiddenDescriptionEditText.getText().toString());

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                                    question.getShortAnswer().setHiddenDescription(hiddenDescriptionEditText.getText().toString());
                                }
                            } else {

                                if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                                    question.getMultipleChoice().setHiddenDescription(" ");

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                                    question.getFillInTheBlanks().setHiddenDescription(" ");

                                } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                                    question.getShortAnswer().setHiddenDescription(" ");
                                }

                            }

                            // hiding dialog after pressing ok
                            dialogInterface.dismiss();

                        });

                        dialogHiddenDescription = builderHiddenDescription.create();
                        dialogHiddenDescription.show();
                        break;

                    //endregion

                    case R.id.action_add_tags:

                        //region add tags

                        AppCompatDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Add tag and press enter");
                        LayoutInflater tagLayoutInflater = LayoutInflater.from(context);
                        View tagDialogView = tagLayoutInflater.inflate(R.layout.layout_tag_edit_text_dialog, null);
                        builder.setView(tagDialogView);
                        TagsEditText tagsEditText = tagDialogView.findViewById(R.id.etTag);

                        tagsEditText.setFilters(new InputFilter[]{inputFilterTag});
                        inputValidationHelperTag.showEditTextMaximumCharacterReachedMessage(
                                tagsEditText,
                                MAX_LENGTH_OF_TAG,
                                String.format(context.getString(R.string.toast_message_tag_reached_maximum_length) + " %s.", MAX_LENGTH_OF_TAG)
                        );


                        tagsEditText.setSeparator(" ");
                        tagsEditText.setTagsTextColor(R.color.white);
                        tagsEditText.setTagsBackground(R.drawable.tag_background);
                        tagsEditText.setTextColor(Color.parseColor("#000000"));
                        builder.setCancelable(false);


                        // if already user put hidden description, show it

                        if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                            if (question.getMultipleChoice().getTags() != null) {

                                if (question.getMultipleChoice().getTags().size() != 0) {

                                    // appending all tags
                                    StringBuilder allTags = new StringBuilder();
                                    for (String tag : question.getMultipleChoice().getTags()) {
                                        allTags.append(tag).append(" ");
                                    }
                                    // setting in tagEditText
                                    String[] separateTags = question.getMultipleChoice().getTags().toArray(new String[question.getMultipleChoice().getTags().size()]);
                                    tagsEditText.setTags(separateTags);
                                }

                            } else tagsEditText.setText("");

                        } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                            if (question.getFillInTheBlanks().getTags() != null) {

                                if (question.getFillInTheBlanks().getTags().size() != 0) {

                                    // appending all tags
                                    StringBuilder allTags = new StringBuilder();
                                    for (String tag : question.getFillInTheBlanks().getTags()) {
                                        allTags.append(tag).append(" ");
                                    }
                                    // setting in tagEditText
                                    String[] separateTags = question.getFillInTheBlanks().getTags().toArray(new String[question.getFillInTheBlanks().getTags().size()]);
                                    tagsEditText.setTags(separateTags);
                                }
                            } else {
                                tagsEditText.setText("");
                            }

                        } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                            if (question.getShortAnswer().getTags() != null) {

                                if (question.getShortAnswer().getTags().size() != 0) {

                                    // appending all tags
                                    StringBuilder allTags = new StringBuilder();
                                    for (String tag : question.getShortAnswer().getTags()) {
                                        allTags.append(tag).append(" ");
                                    }
                                    // setting in tagEditText
                                    String[] separateTags = question.getShortAnswer().getTags().toArray(new String[question.getShortAnswer().getTags().size()]);
                                    tagsEditText.setTags(separateTags);
                                }
                            } else {
                                tagsEditText.setText("");
                            }
                        }

                        builder.setPositiveButton("OK", (dialogInterface, i) -> {


                            if (holder.spinnerQuestionType.getSelectedItemPosition() == 0) {

                                if (!tagsEditText.getTags().isEmpty()) {
                                    question.getMultipleChoice().setTags(tagsEditText.getTags());
                                    String tagsAdded = "Tags added, click again to edit";

                                } else {

                                    question.getMultipleChoice().setTags(null);
                                }

                            } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 1) {

                                if (!tagsEditText.getTags().isEmpty()) {
                                    question.getFillInTheBlanks().setTags(tagsEditText.getTags());
                                    String tagsAdded = "Tags added, click again to edit";

                                } else {

                                    question.getFillInTheBlanks().setTags(null);
                                }
                            } else if (holder.spinnerQuestionType.getSelectedItemPosition() == 2) {

                                if (!tagsEditText.getTags().isEmpty()) {
                                    question.getShortAnswer().setTags(tagsEditText.getTags());
                                    String tagsAdded = "Tags added, click again to edit";

                                } else {

                                    question.getShortAnswer().setTags(null);
                                }
                            }

                            Log.d("TagEditText", tagsEditText.getTags().toString());
                            dialogInterface.dismiss();

                        });

                        dialog = builder.create();
                        dialog.show();
                        break;

                    // endregion
                }

                return false;
            });

            popup.show();
        });


        //endregion

        //region add new question button listener

        //this button is passed in adapter during adapter creation in fragment class

        llcAdQuestion.setOnClickListener(v -> {

            Log.d(TAG, "questionArrayList: " + questionArrayList);

            Log.d(TAG, "isSingleQuestionValid: " + isSingleQuestionValid(question, holder));

            if (questionArrayList.isEmpty()) {

                questionArrayList.add(initQuestionSetWithDefaultParams());
                notifyItemInserted(questionArrayList.size() - 1);
                //scrolling recycler view to the last position
                recyclerView.smoothScrollToPosition(questionArrayList.size() - 1);

            } else if (isSingleQuestionValid(question, holder)) {

                questionArrayList.add(initQuestionSetWithDefaultParams());
                notifyItemInserted(questionArrayList.size() - 1);
                //scrolling recycler view to the last position
                recyclerView.smoothScrollToPosition(questionArrayList.size() - 1);
            }

        });

        ivDoneCreatingQuestion.setOnClickListener(v -> {

            Log.d(TAG, "onBindViewHolder questionArrayList: " + questionArrayList);

            if (questionArrayList.isEmpty()) {

                Toast.makeText(context, "Question set is empty!", Toast.LENGTH_SHORT).show();

            } else if (isQuestionSetValid(holder)) {

                doneAndGotoSettings(fragment);
            } else {

                Toast.makeText(context, "One or more questions may not be completed!", Toast.LENGTH_SHORT).show();
            }

        });


        //endregion

        // region EditQxm - Disable specific UIs for restricting the user from changing major changes in existing question set.

        if (qxmEdit) {
            holder.spinnerQuestionType.setEnabled(false); // disable Question Type changing
            holder.ivQuestionOptions.setEnabled(false);   // disable Option Menu
            holder.ivQuestionOptions.setAlpha(0.4f);
            holder.etPoints.setEnabled(false);            // disable Marks edit
            holder.switchRequired.setEnabled(false);      // disable Required Switch
            holder.switchRequired.setAlpha(0.4f);

            // disable Add Option button of MultipleChoice, so that user can not add new option in existing MCQ
            holder.llcAddMultipleChoiceOption.setEnabled(false);
            holder.llcAddMultipleChoiceOption.setAlpha(0.4f);

            // disable Insert Blank Button of FillInTheBlanks, so that user can not add new option in existing FillInTheBlanks
            holder.btInsertBlank.setEnabled(false);
            holder.btInsertBlank.setAlpha(0.4f);

        }


        // endregion

    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public ArrayList<Question> getQuestionArrayList() {
        return questionArrayList;
    }

    //region creating on PhotoUploadClick Listener

    public void setOnPhotoUploadCLickListener(OnPhotoUploadCLickListener onPhotoUploadCLickListener) {
        this.onPhotoUploadCLickListener = onPhotoUploadCLickListener;
    }

    //endregion

    //region method for initializing every type of question with empty value

    // it will be passed in adapter
    private Question initQuestionSetWithDefaultParams() {

        Question question = new Question();
        question.setQuestionType(QUESTION_TYPE_MULTIPLE_CHOICE);

        // passing three types of question in question object
        question.setMultipleChoice(new MultipleChoice());
        question.setFillInTheBlanks(new FillInTheBlanks());
        question.setShortAnswer(new ShortAnswer());


        // initial empty value for multiple choice
        question.getMultipleChoice().setQuestionTitle("");
        question.getMultipleChoice().setImage("");
        question.getMultipleChoice().setLocalImage("");
        question.getMultipleChoice().setPoints("");
        question.getMultipleChoice().setRequired(false);
        question.getMultipleChoice().setDescription("");
        question.getMultipleChoice().setHiddenDescription("");
        question.getMultipleChoice().setHiddenDescription("");
        ArrayList<String> multipleChoiceTagsList = new ArrayList<>();
        question.getMultipleChoice().setTags(multipleChoiceTagsList);
        ArrayList<String> optionArrayList = new ArrayList<>();
        optionArrayList.add("");
        question.getMultipleChoice().setOptions(optionArrayList);
        ArrayList<String> correctAnswerList = new ArrayList<>();
        question.getMultipleChoice().setCorrectAnswers(correctAnswerList);


        // initial empty value for fill in the blanks
        question.getFillInTheBlanks().setQuestionTitle("");
        question.getFillInTheBlanks().setImage("");
        question.getFillInTheBlanks().setLocalImage("");
        question.getFillInTheBlanks().setPoints("");
        question.getFillInTheBlanks().setAchievedPoints("");
        question.getFillInTheBlanks().setRequired(false);
        question.getFillInTheBlanks().setDescription("");
        question.getFillInTheBlanks().setHiddenDescription("");
        question.getFillInTheBlanks().setHiddenDescription("");
        ArrayList<String> fillInTheTagsList = new ArrayList<>();
        question.getFillInTheBlanks().setTags(fillInTheTagsList);
        ArrayList<String> answerArrayList = new ArrayList<>();
        question.getFillInTheBlanks().setCorrectAnswers(answerArrayList);


        // initial empty value for short answer
        question.getShortAnswer().setQuestionTitle("");
        question.getShortAnswer().setImage("");
        question.getShortAnswer().setLocalImage("");
        question.getShortAnswer().setPoints("");
        question.getShortAnswer().setWordLimits("");
        question.getShortAnswer().setRequired(false);
        question.getShortAnswer().setDescription("");
        question.getShortAnswer().setHiddenDescription("");
        question.getShortAnswer().setHiddenDescription("");
        ArrayList<String> shortAnswerTagsList = new ArrayList<>();
        question.getShortAnswer().setTags(shortAnswerTagsList);

        return question;

    }

    //endregion

    // region making every types of initial question

    private MultipleChoice getEmptyMultipleChoice() {


        MultipleChoice multipleChoice = new MultipleChoice();

        // initial empty value for multiple choice
        multipleChoice.setQuestionTitle("");
        multipleChoice.setImage("");
        multipleChoice.setLocalImage("");
        multipleChoice.setPoints("");
        multipleChoice.setRequired(false);
        multipleChoice.setDescription("");
        multipleChoice.setHiddenDescription("");
        multipleChoice.setHiddenDescription("");
        ArrayList<String> multipleChoiceTagsList = new ArrayList<>();
        multipleChoice.setTags(multipleChoiceTagsList);
        ArrayList<String> optionArrayList = new ArrayList<>();
        multipleChoice.setOptions(optionArrayList);
        ArrayList<String> correctAnswerList = new ArrayList<>();
        multipleChoice.setCorrectAnswers(correctAnswerList);

        return multipleChoice;
    }

    private FillInTheBlanks getEmptyFillInTheBlanks() {

        FillInTheBlanks fillInTheBlanks = new FillInTheBlanks();

        // initial empty value for multiple choice
        fillInTheBlanks.setQuestionTitle("");
        fillInTheBlanks.setImage("");
        fillInTheBlanks.setLocalImage("");
        fillInTheBlanks.setPoints("");
        fillInTheBlanks.setRequired(false);
        fillInTheBlanks.setDescription("");
        fillInTheBlanks.setHiddenDescription("");
        fillInTheBlanks.setHiddenDescription("");
        ArrayList<String> fillInTheTagsList = new ArrayList<>();
        fillInTheBlanks.setTags(fillInTheTagsList);
        ArrayList<String> answerArrayList = new ArrayList<>();
        fillInTheBlanks.setCorrectAnswers(answerArrayList);

        return fillInTheBlanks;
    }

    private ShortAnswer getEmptyShortAnswer() {

        ShortAnswer shortAnswer = new ShortAnswer();

        shortAnswer.setQuestionTitle("");
        shortAnswer.setImage("");
        shortAnswer.setLocalImage("");
        shortAnswer.setWordLimits("");
        shortAnswer.setPoints("");
        shortAnswer.setRequired(false);
        shortAnswer.setDescription("");
        shortAnswer.setHiddenDescription("");
        shortAnswer.setHiddenDescription("");
        ArrayList<String> shortAnswerTagsList = new ArrayList<>();
        shortAnswer.setTags(shortAnswerTagsList);

        return shortAnswer;

    }

    //endregion

    //region doneAndGotoSettings

    private void doneAndGotoSettings(Fragment frag) {

        Log.d(TAG, "doneAndGotoSettings: Called");

        String questionSetTitle = etQuestionSetTitle.getText().toString().trim();
        String questionSetDescription = etQuestionSetDescription.getText().toString().trim();
        String youtubeLink = etYouTubeLink.getText().toString().trim();

        Log.d(TAG, "doneAndGotoSettings youtubeThumbnailURL : " + youtubeThumbnailURL);
        Log.d(TAG, "doneAndGotoSettings questionThumbnailImage : " + questionThumbnailImage);
        Log.d(TAG, "doneAndGotoSettings youtubeLink : " + youtubeLink);


        // checking if user gives question set title or not
        if (questionSetTitle.isEmpty()) {

            new SimpleTooltip.Builder(context)
                    .anchorView(etQuestionSetTitle)
                    .text("Write question set title")
                    .gravity(Gravity.BOTTOM)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();
        }
        // checking if user gives question set description or not
        /*else if (questionSetDescription.isEmpty()) {

            new SimpleTooltip.Builder(context)
                    .anchorView(etQuestionSetDescription)
                    .text("Write question set description")
                    .gravity(Gravity.BOTTOM)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();
        }*/
        // if youtube link editText field is visible user must give link
        else if (isViewVisible(etYouTubeLink) && youtubeLink.isEmpty()) {

            new SimpleTooltip.Builder(context)
                    .anchorView(etYouTubeLink)
                    .text("Give Youtube link")
                    .gravity(Gravity.BOTTOM)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();

        }
        // checking if question set has any question or not
        else if (questionArrayList.isEmpty()) {

            Toast.makeText(context, "Empty Question Body!", Toast.LENGTH_SHORT).show();
        }

        // if everything is ok,go to next fragmentapi
        else {


            QuestionSet questionSet = new QuestionSet();

            questionSet.setQuestionSetTitle(questionSetTitle);
            questionSet.setQuestionSetDescription(questionSetDescription);

            if (!TextUtils.isEmpty(questionThumbnailImage))
                questionSet.setQuestionSetThumbnail(questionThumbnailImage);

            else if (!TextUtils.isEmpty(youtubeThumbnailURL))
                questionSet.setQuestionSetThumbnail(youtubeThumbnailURL);

            // if youtube link editText is visible and user gives the link
            // then set youtube link as well
            if (isViewVisible(etYouTubeLink) && !youtubeLink.isEmpty()) {
                if (CreateQxmFragment.isYoutubeURLValid)
                    questionSet.setQuestionSetYoutubeLink(youtubeLink);
                else
                    questionSet.setQuestionSetYoutubeLink("");
            }

            questionSet.setQuestions(questionArrayList);

            Bundle args = new Bundle();
            args.putParcelable(PARCELABLE_QUESTION_SET, questionSet);
            if (questionSettings != null) {
                Log.d(TAG, "PARCELABLE_QUESTION_SETTINGS: " + questionSettings.toString());
                args.putParcelable(PARCELABLE_QUESTION_SETTINGS, questionSettings);
                args.putParcelable(QXM_DRAFT_KEY, qxmDraft);
            } else {
                Log.d(TAG, "QuestionSettings is empty");
            }
            QxmSettingsFragment qxmSettingsFragment = new QxmSettingsFragment();
            qxmSettingsFragment.setArguments(args);

            FragmentManager fragmentManager = frag.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, qxmSettingsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            Log.d(TAG, "onViewCreated: ivDoneClick: " + questionSet);
        }

    }

    //endregion

    // region method for making initial question

    private boolean isViewVisible(View view) {

        return view.getVisibility() == View.VISIBLE;

    }
    //endregion

    //region isSingleQuestionValid

    private boolean isSingleQuestionValid(Question question, ViewHolder holder) {

        // if question set is empty, it means nothing to check,
        // so let user add a question first (see else block)

        switch (question.getQuestionType()) {

            //region checking all the fields of multiple choice are given or not
            case QUESTION_TYPE_MULTIPLE_CHOICE:

                if (TextUtils.isEmpty(question.getMultipleChoice().getQuestionTitle().trim())) {

                    Log.d(TAG, "isSingleQuestionValid: multiple choice title is empty");
                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etMultipleChoiceQuestionName)
                            .text("Write question title")
                            .gravity(Gravity.TOP)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;

                } else if (question.getMultipleChoice().getOptions().isEmpty()) {

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.llcMultipleQuestionContainer)
                            .text("Give one or more option")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;

                } else if (question.getMultipleChoice().getOptions().size() < 2) {


                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.llcMultipleQuestionContainer)
                            .text("Minimum 2 options are required")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;

                } else if (question.getMultipleChoice().getCorrectAnswers().isEmpty()) {

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.llcMultipleQuestionContainer)
                            .text("Select correct answer")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;

                } else if (holder.etPoints.getText().toString().isEmpty()) {

                    // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                    // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etPoints)
                            .text("Give point")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;


                } else if (!question.getMultipleChoice().getOptions().isEmpty()) {

                    for (String singleMultiOption : question.getMultipleChoice().getOptions()) {

                        if (singleMultiOption.isEmpty()) {

                            new SimpleTooltip.Builder(context)
                                    .anchorView(holder.llcMultipleQuestionContainer)
                                    .text("Option can not be empty")
                                    .gravity(Gravity.END)
                                    .animated(true)
                                    .transparentOverlay(true)
                                    .build()
                                    .show();


                            return false;


                        }
                    }
                }

                break;
            //endregion

            //region checking all the fields of fillInTheBlanks are given or not
            case QUESTION_TYPE_FILL_IN_THE_BLANKS:

                if (TextUtils.isEmpty(question.getFillInTheBlanks().getQuestionTitle().trim())) {

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etFillBlankQuestionName)
                            .text("Write question title")
                            .gravity(Gravity.TOP)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;

                } else if (question.getFillInTheBlanks().getCorrectAnswers().isEmpty()) {

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.llcFillTheBlanksContainer)
                            .text("Give answer for the blank")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;

                } else if (holder.etPoints.getText().toString().isEmpty()) {

                    // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                    // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etPoints)
                            .text("Give point")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;


                } else if (!question.getFillInTheBlanks().getCorrectAnswers().isEmpty()) {

                    for (String fillBlankAnswer : question.getFillInTheBlanks().getCorrectAnswers()) {

                        if (fillBlankAnswer.isEmpty()) {

                            new SimpleTooltip.Builder(context)
                                    .anchorView(holder.llcFillTheBlanksContainer)
                                    .text("Answer can to be empty")
                                    .gravity(Gravity.END)
                                    .animated(true)
                                    .transparentOverlay(true)
                                    .build()
                                    .show();

                            return false;

                        }

                    }

                }

                break;
            //endregion

            //region all the fields of short answer are given or not
            case QUESTION_TYPE_SHORT_ANSWER:

                if (TextUtils.isEmpty(question.getShortAnswer().getQuestionTitle().trim())) {

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etShortAnswerQuestionName)
                            .text("Write question title")
                            .gravity(Gravity.TOP)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();


                    return false;

                } else if (holder.etPoints.getText().toString().isEmpty()) {

                    // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                    // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etPoints)
                            .text("Give point")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;


                } else if (holder.etWordLimit.getText().toString().isEmpty()) {

                    // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                    // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                    new SimpleTooltip.Builder(context)
                            .anchorView(holder.etWordLimit)
                            .text(R.string.give_character_limit)
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();

                    return false;


                }

                break;
            //endregion
        }

        return true;
    }
    //endregion

    //region is youtube editTextVisible

    public interface OnPhotoUploadCLickListener {

        void photoUploadedClicked(ArrayList<Question> questionArrayList, int position, int questionTypeIndex);
    }

    //endregion

    //region ViewHolder

    class ViewHolder extends RecyclerView.ViewHolder {

        // region common object
        CardView cvQuestionContainer;
        LinearLayoutCompat llcQuestionTypeSpinnerContainer;
        AppCompatSpinner spinnerQuestionType;
        AppCompatEditText etPoints;
        AppCompatImageView ivQuestionImagePicker;
        AppCompatImageView ivRemoveSingleQuestionImage;
        AppCompatImageView ivQuestionImage;
        SwitchCompat switchRequired;
        AppCompatImageView ivQuestionOptions;
        View viewDivision;

        //endregion

        // region multiple choice object

        AppCompatEditText etMultipleChoiceQuestionName;
        LinearLayoutCompat llcMultipleQuestionContainer;
        RecyclerView rvMultipleOptions;
        LinearLayoutCompat llcAddMultipleChoiceOption;

        //endregion

        //region fill in the blanks object

        LinearLayoutCompat llcFillTheBlanksContainer;
        AppCompatEditText etFillBlankQuestionName;
        RecyclerView rvFillBlankAnswers;
        AppCompatButton btInsertBlank;

        //endregion

        // region short answer object

        LinearLayoutCompat llcShortAnswerContainer;
        AppCompatEditText etShortAnswerQuestionName;
        AppCompatEditText etWordLimit;

        //endregion

        public ViewHolder(View itemView) {
            super(itemView);

            cvQuestionContainer = itemView.findViewById(R.id.cvQuestionContainer);
            llcQuestionTypeSpinnerContainer = itemView.findViewById(R.id.llcQuestionTypeSpinnerContainer);
            spinnerQuestionType = itemView.findViewById(R.id.spinnerQuestionType);
            ivQuestionImagePicker = itemView.findViewById(R.id.ivQuestionImagePicker);
            ivRemoveSingleQuestionImage = itemView.findViewById(R.id.ivRemoveSingleQuestionImage);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
            switchRequired = itemView.findViewById(R.id.switchRequired);
            ivQuestionOptions = itemView.findViewById(R.id.ivQuestionOptions);
            viewDivision = itemView.findViewById(R.id.viewDivision);

            llcMultipleQuestionContainer = itemView.findViewById(R.id.llcMultipleQuestionContainer);
            llcFillTheBlanksContainer = itemView.findViewById(R.id.llcFillTheBlanksContainer);
            llcShortAnswerContainer = itemView.findViewById(R.id.llcShortAnswerContainer);

            etPoints = itemView.findViewById(R.id.etPoints);
            etWordLimit = itemView.findViewById(R.id.etWordLimit);
            viewDivision = itemView.findViewById(R.id.viewDivision);


            etMultipleChoiceQuestionName = itemView.findViewById(R.id.etMultipleChoiceQuestionName);
            rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
            llcAddMultipleChoiceOption = itemView.findViewById(R.id.llcAddMultipleChoiceOption);

            etFillBlankQuestionName = itemView.findViewById(R.id.etFillBlankQuestionName);
            rvFillBlankAnswers = itemView.findViewById(R.id.rvFillBlankAnswers);
            btInsertBlank = itemView.findViewById(R.id.btInsertBlank);

            etShortAnswerQuestionName = itemView.findViewById(R.id.etShortAnswerQuestionName);
            etWordLimit = itemView.findViewById(R.id.etWordLimit);
        }
    }

    //endregion

    //region is question set valid
    private boolean isQuestionSetValid(ViewHolder holder) {

        for (int i = 0; i < questionArrayList.size(); i++) {

            Question ques = questionArrayList.get(i);

            switch (ques.getQuestionType()) {

                //region checking all the fields of multiple choice is given or not
                case QUESTION_TYPE_MULTIPLE_CHOICE:

                    if (ques.getMultipleChoice().getQuestionTitle().isEmpty()) {

                        recyclerView.smoothScrollToPosition(i);


                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etMultipleChoiceQuestionName)
                                .text("Write question title")
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;


                    } else if (ques.getMultipleChoice().getOptions().isEmpty()) {

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.llcMultipleQuestionContainer)
                                .text("Give one or more option")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();
                        return false;

                    } else if (ques.getMultipleChoice().getOptions().size() < 2) {

                        Log.d(TAG, "onBindViewHolder optionSize :" + ques.getMultipleChoice().getOptions().size());
                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.llcMultipleQuestionContainer)
                                .text("Minimum 2 options are required")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();
                        return false;

                    } else if (ques.getMultipleChoice().getCorrectAnswers().isEmpty()) {

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.llcMultipleQuestionContainer)
                                .text("Select correct answer")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;

                    } else if (ques.getMultipleChoice().getPoints().isEmpty()) {

                        // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                        // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etPoints)
                                .text("Give point")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;


                    }
                    break;
                //endregion

                //region checking all the fields of fillInTheBlanks is given or not
                case QUESTION_TYPE_FILL_IN_THE_BLANKS:

                    if (ques.getFillInTheBlanks().getQuestionTitle().isEmpty()) {

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etFillBlankQuestionName)
                                .text("Write question title")
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;

                    } else if (ques.getFillInTheBlanks().getCorrectAnswers().isEmpty()) {

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.llcFillTheBlanksContainer)
                                .text("Give answer for the blank")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;

                    } else if (ques.getFillInTheBlanks().getPoints().isEmpty()) {

                        // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                        // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etPoints)
                                .text("Give point")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;


                    }
                    break;
                //endregion

                //region all the fields of short answer is given or not
                case QUESTION_TYPE_SHORT_ANSWER:

                    if (ques.getShortAnswer().getQuestionTitle().isEmpty()) {

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etShortAnswerQuestionName)
                                .text("Write question title")
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;

                    } else if (ques.getShortAnswer().getPoints().isEmpty()) {

                        // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                        // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etPoints)
                                .text("Give point")
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;

                    } else if (ques.getShortAnswer().getWordLimits().isEmpty()) {

                        // holder.etPoints.setBackgroundResource(R.drawable.background_light_warning);
                        // holder.llcAddMultipleChoiceOption.setBackgroundResource(R.drawable.background_light_warning);

                        recyclerView.smoothScrollToPosition(i);

                        new SimpleTooltip.Builder(context)
                                .anchorView(holder.etWordLimit)
                                .text(R.string.give_character_limit)
                                .gravity(Gravity.END)
                                .animated(true)
                                .transparentOverlay(true)
                                .build()
                                .show();

                        return false;

                    }
                    break;
                //endregion
            }


        }

        return true;
    }
    //endregion

    //region fill in the blanks dialog
    private void showFillBlanksEvaluationDialog(Context context) {

//        if (!RepeatableAlertdialogBuilder.neverShowAgain) {
//            RepeatableAlertdialogBuilder repeatableAlertdialogBuilder =
//                    new RepeatableAlertdialogBuilder(context);
//            repeatableAlertdialogBuilder.setTitle("Remember !");
//            repeatableAlertdialogBuilder.setMessage("If you add fill in the blanks or short answer question," +
//                    "automatic evaluation is not possible.");
//            repeatableAlertdialogBuilder.setPosotiveButton("OK");
//            repeatableAlertdialogBuilder.setNeverShowAgainButton("Don't show again");
//            repeatableAlertdialogBuilder.show();
//        }
    }
    //endregion

}
