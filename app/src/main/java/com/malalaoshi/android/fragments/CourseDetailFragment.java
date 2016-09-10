package com.malalaoshi.android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malalaoshi.android.R;
import com.malalaoshi.android.adapter.SimpleMonthAdapter;
import com.malalaoshi.android.core.network.api.ApiExecutor;
import com.malalaoshi.android.core.network.api.BaseApiContext;
import com.malalaoshi.android.course.api.CourseInfoApi;
import com.malalaoshi.android.entity.Course;
import com.malalaoshi.android.util.CalendarUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kang on 16/3/15.
 */
public class CourseDetailFragment extends Fragment implements View.OnClickListener {

    public static String ARGS_FRAGEMENT_COURSE_ID = "course id";

    @Bind(R.id.ll_root)
    protected LinearLayout llRoot;

    @Bind(R.id.ll_container)
    protected LinearLayout llContainer;

    @Bind(R.id.tv_course_date)
    protected TextView tvCourseDate;

    @Bind(R.id.tv_subject)
    protected TextView tvSubject;

    @Bind(R.id.tv_address)
    protected TextView tvAddress;

    @Bind(R.id.tv_time)
    protected TextView tvTime;

    @Bind(R.id.tv_residual_class)
    protected TextView tvResidualClass;

    protected View loadingView;
    protected View loadFailedView;


    //本节课ID
    private String courceSubId;

    private Course cource = null;

    public Course getCource() {
        return cource;
    }

    public CourseDetailFragment() {
    }

    public static CourseDetailFragment newInstance(String courseSubId) {
        CourseDetailFragment f = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_FRAGEMENT_COURSE_ID, courseSubId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courceSubId = getArguments().getString(ARGS_FRAGEMENT_COURSE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coursedetail, container, false);
        ButterKnife.bind(this, view);
        initData();
        initViews();
        setEvent();
        return view;
    }


    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        loadFailedView = inflater.inflate(R.layout.view_load_failed, null);
        loadingView = inflater.inflate(R.layout.view_loading, null);
        setLoadingUi();
    }

    private void setEvent() {
        loadFailedView.setOnClickListener(this);
    }

    private void setLoadingUi() {
        llRoot.removeAllViews();
        llRoot.addView(loadingView);
    }

    private void setLoadFailedUi() {
        llRoot.removeAllViews();
        llRoot.addView(loadFailedView);
    }

    private void setLoadSucceedUi() {
        llRoot.removeAllViews();
        llRoot.addView(llContainer);
    }

    private void initData() {
        loadData();
    }

    private void loadData() {
        ApiExecutor.exec(new FetchCourseInfoRequest(this, courceSubId));
    }

    private static final class FetchCourseInfoRequest extends BaseApiContext<CourseDetailFragment, Course> {
        private String courseId;

        public FetchCourseInfoRequest(CourseDetailFragment courseDetailFragment, String courseId) {
            super(courseDetailFragment);
            this.courseId = courseId;
        }

        @Override
        public Course request() throws Exception {
            return new CourseInfoApi().getCourseInfo(courseId);
        }

        @Override
        public void onApiSuccess(@NonNull Course response) {
            get().onFetchCourseSuccess(response);
        }

        @Override
        public void onApiFailure(Exception exception) {
            get().onFetchCourseFailed();
        }
    }

    private void onFetchCourseFailed() {
        setLoadFailedUi();
    }

    private void onFetchCourseSuccess(@NonNull Course course) {
        this.cource = course;
        updateUI();
    }

    private void updateUI() {
        setLoadSucceedUi();
        //设置数据
        SimpleMonthAdapter.CalendarDay calendarDay = CalendarUtils.timestampToCalendarDay(cource.getEnd());
        if (calendarDay != null) {
            tvCourseDate.setText(String.format("%04d", calendarDay.getYear()) + "." + String.format("%02d", calendarDay.getMonth()) + "." + String.format("%02d", calendarDay.getDay()));
        }
        tvSubject.setText(cource.getSubject());

        tvAddress.setText(cource.getSchool());

        Calendar start = CalendarUtils.timestampToCalendar(cource.getStart());
        Calendar end = CalendarUtils.timestampToCalendar(cource.getEnd());
        if (start != null && end != null) {
            String strStart = String.format("%02d", start.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", start.get(Calendar.MINUTE));
            String strEnd = String.format("%02d", end.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", end.get(Calendar.MINUTE));
            tvTime.setText(strStart + "-" + strEnd);
        } else {
            tvTime.setText("时间异常");
        }
        //tvResidualClass;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loadFailedView.getId()) {
            reloadData();
        }
    }

    private void reloadData() {
        setLoadingUi();
        loadData();
    }
}
