package com.muxi.workbench.ui.progress.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.muxi.workbench.R;

public class ProgressTitleBar extends ConstraintLayout {

    private AppCompatSpinner OptionSp;
    private ImageButton AddIb, OptionIb;
    public SpinnerAdapter adapter;

    public ProgressTitleBar(Context context) {
        super(context);
        initView(context);
    }

    public ProgressTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProgressTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.title_bar_progress, this, true);
        OptionSp = findViewById(R.id.spn_progress_title_bar_option);
        AddIb = findViewById(R.id.ib_progress_title_bar_add);
        OptionIb = findViewById(R.id.ib_progress_title_bar_option);

    }

    public void setOptionSp(Context context) {
        adapter = SpinnerAdapter.createFromResource(context, R.array.progress_titlebar_screening,R.layout.spinner_progress_layout);
        adapter.setDropDownViewResource(R.layout.spinner_progress_dropdown);
        OptionSp.setBackgroundColor(0x0);
        OptionSp.setAdapter(adapter);
        OptionSp.setDropDownVerticalOffset(Math.round(getResources().getDisplayMetrics().density * 25));
    }

    public void setAddListener(OnClickListener onClickListener) {
        AddIb.setOnClickListener(onClickListener);
    }

    public void setOptionIbListener(OnClickListener onClickListener) {
        OptionIb.setOnClickListener(onClickListener);
    }

    public void showSpinner(){
        OptionSp.performClick();
    }

    public void setSpinnerLabel(int position) {
        adapter.setSelectedPosition(position);
    }

    public void setOptionSelectListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        OptionSp.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static class SpinnerAdapter<T> extends ArrayAdapter<T> {

        private int selectedPosition;

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public SpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView textView = (TextView)view;
            if ( selectedPosition == position ) {
                textView.setTextColor(0xff373741);
                textView.getPaint().setFakeBoldText(true);
            } else{
                textView.setTextColor(0xff6d6d6d);
                textView.getPaint().setFakeBoldText(false);
            }
            return view;
        }

        public static @NonNull SpinnerAdapter<CharSequence> createFromResource(
                @NonNull Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {
            final CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
            return new SpinnerAdapter<>(context, textViewResId, strings);
        }
    }
}
