package com.malalaoshi.android.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.malalaoshi.android.MalaApplication;
import com.malalaoshi.android.R;
import com.malalaoshi.android.api.TagListApi;
import com.malalaoshi.android.core.network.api.ApiExecutor;
import com.malalaoshi.android.core.network.api.BaseApiContext;
import com.malalaoshi.android.entity.Tag;
import com.malalaoshi.android.result.TagListResult;
import com.malalaoshi.android.view.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kang on 16/1/21.
 */
public class FilterTagFragment extends Fragment implements View.OnClickListener {
    public static String ARGMENTS_TAGS_ID = "tagsId";
    private  long[] extraTagIds;

    private static final String API_TAGS_URL = "/api/v1/tags";
    private List<Map<String, Object>> mTags = new ArrayList<>();

    @Bind(R.id.tags_loading)
    protected View mTagsLoading;

    @Bind(R.id.iv_load_anim)
    protected ImageView ivLoadAnim;

    private AnimationDrawable loadAnim;

    @Bind(R.id.tags_load_again)
    protected View mTagsLoadAgain;

    @Bind(R.id.tags_load_again_msg)
    protected TextView mTagsLoadAgainMsg;

    @Bind(R.id.tags_container)
    protected View mTagsContainer;

    @Bind(R.id.flowlayout_tags)
    protected FlowLayout mTagFlow;

    private List<TextView> mTagViews = new ArrayList<>();

    //tag样式
    private int mTagStyles[] = {
            R.drawable.tag1_textview_bg,
            R.drawable.tag2_textview_bg,
            R.drawable.tag3_textview_bg,
            R.drawable.tag4_textview_bg,
            R.drawable.tag5_textview_bg,
            R.drawable.tag6_textview_bg,
            R.drawable.tag7_textview_bg,
            R.drawable.tag8_textview_bg,
            R.drawable.tag9_textview_bg};

    //"不限"tag
    private View mAllTag;

    private OnTagClickListener tagsClickListener;


    public FilterTagFragment() {
    }

    public void setOnTagClickListener(OnTagClickListener tagsClickListener){
        this.tagsClickListener = tagsClickListener;
    }

    public static FilterTagFragment newInstance() {
        FilterTagFragment filterTagFragment = new FilterTagFragment();
        return filterTagFragment;
    }

    public static FilterTagFragment newInstance(long[] tags) {
        FilterTagFragment filterTagFragment = new FilterTagFragment();
        Bundle bundle = new Bundle();
        bundle.putLongArray(ARGMENTS_TAGS_ID,tags);
        filterTagFragment.setArguments(bundle);
        return filterTagFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_filter, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle!=null){
            extraTagIds = bundle.getLongArray(ARGMENTS_TAGS_ID);
        }
        loadAnim = (AnimationDrawable) ivLoadAnim.getDrawable();

        initDatas();
        return view;
    }

    @OnClick(R.id.tags_load_again)
    public void onClickLoadAgain() {
        if (MalaApplication.getInstance().isNetworkOk()) {
            mTagsLoading.setVisibility(View.VISIBLE);
            mTagsLoadAgain.setVisibility(View.GONE);
            mTagsContainer.setVisibility(View.GONE);
            loadDatas();
        }
    }

    public void updateUI(){
        List<Map<String, Object>> smallTags = new ArrayList<>();
        List<Map<String, Object>> bigTags = new ArrayList<>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", -1L);
        item.put("name", "不限");
        smallTags.add(item);
        for (int i=0;i<mTags.size();i++){
            Map<String, Object> tag = mTags.get(i);
            String name = (String) tag.get("name");
            if (name.length()<=4){
                smallTags.add(tag);
            }else{
                bigTags.add(tag);
            }
        }

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        int index = 0;
        //三列
        int len = smallTags.size()/3;
        int remainderSmall = smallTags.size()%3;
        for (int i=0;i<len;i++){
            View view = layoutInflater.inflate(R.layout.tag_three_item, mTagFlow,false);

            TextView textView1 = (TextView)view.findViewById(R.id.text1);
            if(i==0){  //第一个标签:"不限"
                mAllTag = textView1;
                textView1.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }
            TextView textView2 = (TextView)view.findViewById(R.id.text2);
            TextView textView3 = (TextView)view.findViewById(R.id.text3);

            if (index>=mTagStyles.length) index = 1;
            textView1.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
            index++;
            if (index>=mTagStyles.length) index = 1;
            textView2.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
            index++;
            if (index>=mTagStyles.length) index = 1;
            textView3.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
            index++;

            textView1.setText((String) smallTags.get(3 * i).get("name"));
            textView2.setText((String) smallTags.get(3 * i + 1).get("name"));
            textView3.setText((String) smallTags.get(3 * i + 2).get("name"));

            textView1.setTag(smallTags.get(3 * i));
            textView2.setTag(smallTags.get(3 * i + 1));
            textView3.setTag(smallTags.get(3 * i + 2));

            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
            textView3.setOnClickListener(this);


            Object selectedObj = smallTags.get(3 * i).get("selected");
            if (selectedObj!=null&&(boolean)selectedObj==true){
                textView1.setSelected(true);
                textView1.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }else{
                textView1.setSelected(false);
            }
            selectedObj = smallTags.get(3 * i + 1).get("selected");
            if (selectedObj!=null&&(boolean)selectedObj==true){
                textView2.setSelected(true);
                textView2.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }else{
                textView2.setSelected(false);
            }
            selectedObj = smallTags.get(3 * i + 2).get("selected");
            if (selectedObj!=null&&(boolean)selectedObj==true){
                textView3.setSelected(true);
                textView3.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }else{
                textView3.setSelected(false);
            }

            mTagViews.add(textView1);
            mTagViews.add(textView2);
            mTagViews.add(textView3);
            mTagFlow.addView(view);
        }
        len = bigTags.size()/2;
        int remainderBig = bigTags.size()%2;
        for (int i=0;i<len;i++){
            View view = layoutInflater.inflate(R.layout.tag_two_items, mTagFlow,false);

            TextView textView1 = (TextView)view.findViewById(R.id.text1);
            TextView textView2 = (TextView)view.findViewById(R.id.text2);

            if (index>=mTagStyles.length) index = 1;
            textView1.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
            index++;
            if (index>=mTagStyles.length) index = 1;
            textView2.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
            index++;

            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);

            textView1.setText((String) bigTags.get(2 * i).get("name"));
            textView2.setText((String) bigTags.get(2 * i + 1).get("name"));

            textView1.setTag(bigTags.get(2 * i));
            textView2.setTag(bigTags.get(2 * i + 1));

            Object selectedObj = bigTags.get(2 * i).get("selected");
            if (selectedObj!=null&&(boolean)selectedObj==true){
                textView1.setSelected(true);
                textView1.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }else{
                textView1.setSelected(false);
            }
            selectedObj = bigTags.get(2 * i + 1).get("selected");
            if (selectedObj!=null&&(boolean)selectedObj==true){
                textView2.setSelected(true);
                textView2.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }else{
                textView2.setSelected(false);
            }

            mTagViews.add(textView1);
            mTagViews.add(textView2);
            mTagFlow.addView(view);
        }

        if (remainderSmall>0){
            for (int i=0;i<remainderSmall;i++){
                TextView textView1 = (TextView)layoutInflater.inflate(R.layout.tag_textview, mTagFlow,false);
                ViewGroup.LayoutParams layoutParams = textView1.getLayoutParams();
                layoutParams.width = (mTagFlow.getMeasuredWidth()-mTagFlow.getPaddingLeft()*4)/3;
                textView1.setLayoutParams(layoutParams);
                textView1.setText((String) smallTags.get(smallTags.size() - remainderSmall + i - 1).get("name"));

                if (index>=mTagStyles.length) index = 1;
                textView1.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
                index++;

                textView1.setOnClickListener(this);
                textView1.setTag(smallTags.get(smallTags.size() - remainderSmall + i - 1));
                Object selectedObj = smallTags.get(smallTags.size() - remainderSmall + i - 1).get("selected");
                if (selectedObj!=null&&(boolean)selectedObj==true){
                    textView1.setSelected(true);
                    textView1.setTextColor(getResources().getColor(R.color.color_white_ffffff));
                }else{
                    textView1.setSelected(false);
                }

                mTagFlow.addView(textView1);
                mTagViews.add(textView1);
            }
        }
        if (remainderBig>0){
            TextView textView1 = (TextView)layoutInflater.inflate(R.layout.tag_textview, mTagFlow,false);
            ViewGroup.LayoutParams layoutParams = textView1.getLayoutParams();
            layoutParams.width = (mTagFlow.getMeasuredWidth()-mTagFlow.getPaddingLeft()*3)/2;

            textView1.setLayoutParams(layoutParams);
            textView1.setText((String) bigTags.get(bigTags.size() - 1).get("name"));

            if (index>=mTagStyles.length) index = 1;
            textView1.setBackground(getActivity().getResources().getDrawable(mTagStyles[index]));
            index++;

            textView1.setTag(bigTags.get(bigTags.size() - 1));
            Object selectedObj = bigTags.get(bigTags.size() - 1).get("selected");

            if (selectedObj!=null&&(boolean)selectedObj==true){
                textView1.setSelected(true);
                textView1.setTextColor(getResources().getColor(R.color.color_white_ffffff));
            }else{
                textView1.setSelected(false);
            }
            textView1.setOnClickListener(this);
            mTagFlow.addView(textView1);
            mTagViews.add(textView1);
        }
    }



    private void initDatas() {
        loadDatas();
    }

    private void loadDatas() {
        loadAnim.start();
        ApiExecutor.exec(new FetchTagsRequest(this));
    }
    private void setDatas(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        mTags.clear();
        for (int i = 0; i < tags.size(); i++) {
            Tag obj = tags.get(i);
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", obj.getId());
            item.put("name", obj.getName());
            item.put("selected", false);
            for (int j=0;extraTagIds!=null&&j<extraTagIds.length;j++){
                Long tagId = extraTagIds[j];
                if (tagId.equals(obj.getId())){
                    item.put("selected", true);
                    break;
                }
            }
            mTags.add(item);
        }
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView)v;
        if (mAllTag==textView){
            if (!mAllTag.isSelected()){
                for (int i=0;i<mTagViews.size();i++){
                    if (mTagViews.get(i).isSelected()){
                        mTagViews.get(i).setSelected(false);
                        mTagViews.get(i).setTextColor(getResources().getColor(R.color.color_black_939393));
                    }
                }
            }
        }else{
            if (textView.isSelected()){
                textView.setTextColor(getResources().getColor(R.color.color_black_939393));
            }else{
                textView.setTextColor(getResources().getColor(R.color.color_white_ffffff));
                if (mAllTag.isSelected()){
                    mAllTag.setSelected(false);
                }
            }
        }
        v.setSelected(!v.isSelected());
        //所有
        ArrayList<Tag> mSelectedTags = new ArrayList<>();
        if (!mAllTag.isSelected()){
            for (int i = 0; i < mTagViews.size(); i++) {
                View view = mTagViews.get(i);
                if (view.isSelected()) {
                    Map<String, Object> tag = (Map<String, Object>) view.getTag();
                    Tag tag1 = new Tag((Long)tag.get("id"),(String)tag.get("name"));
                    mSelectedTags.add(tag1);
                }
            }
        }
        if (tagsClickListener!=null){
            tagsClickListener.onTagClick(mSelectedTags);
        }
    }

    public interface OnTagClickListener{
        void onTagClick(ArrayList<Tag> tags);
    }

    private static final class FetchTagsRequest extends BaseApiContext<FilterTagFragment, TagListResult> {

        public FetchTagsRequest(FilterTagFragment filterTagFragment) {
            super(filterTagFragment);
        }

        @Override
        public TagListResult request() throws Exception {
            return new TagListApi().get();
        }

        @Override
        public void onApiSuccess(@NonNull TagListResult response) {
            if (response!=null&&response.getResults()!=null){
                get().onLoadTagsSuccess(response);
            }else{
                get().onLoadTagsFailed();
            }

        }

        @Override
        public void onApiFailure(Exception exception) {
            get().onLoadTagsFailed();
        }

        @Override
        public void onApiFinished() {
            super.onApiFinished();
            get().onLoadFinish();
        }
    }

    private void onLoadFinish() {
        loadAnim.stop();

    }

    private void onLoadTagsFailed() {
        if (!MalaApplication.getInstance().isNetworkOk()) {
            mTagsLoadAgainMsg.setText("网络已断开，请更改网络配置后加载");
        }
        mTagsLoading.setVisibility(View.GONE);
        mTagsLoadAgain.setVisibility(View.VISIBLE);
        mTagsContainer.setVisibility(View.GONE);
    }

    private void onLoadTagsSuccess(TagListResult response) {
        List<Tag> tags = response.getResults();
        setDatas(tags);
        updateUI();
        mTagsLoading.setVisibility(View.GONE);
        mTagsLoadAgain.setVisibility(View.GONE);
        mTagsContainer.setVisibility(View.VISIBLE);

    }
}
