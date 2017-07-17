package cn.ngame.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.QuestionResult;

/**
 * 显示总评分的控件
 * Created by zeng on 2016/6/13.
 */
public class ReviewScoreView extends LinearLayout {

    private TextView tv_score, tv_total;
    private RatingBar ratingBar;
    private ProgressBar pb1, pb2, pb3, pb4, pb5;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5;

    private List<QuestionResult> resultList;


    public ReviewScoreView(Context context) {
        this(context, null);
    }

    public ReviewScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ReviewScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_review_score, this);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_total = (TextView) findViewById(R.id.tv_total);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
        pb3 = (ProgressBar) findViewById(R.id.progressBar3);
        pb4 = (ProgressBar) findViewById(R.id.progressBar4);
        pb5 = (ProgressBar) findViewById(R.id.progressBar5);

        tv_1 = (TextView) findViewById(R.id.text1);
        tv_2 = (TextView) findViewById(R.id.text2);
        tv_3 = (TextView) findViewById(R.id.text3);
        tv_4 = (TextView) findViewById(R.id.text4);
        tv_5 = (TextView) findViewById(R.id.text5);


    }

    public void setData(GameInfo gameInfo) {
        this.resultList = gameInfo.questionResults;
        int totalPeople = gameInfo.commentPeople;
        if (null == resultList) {
            return;
        }
        for (QuestionResult qr : resultList) {

            double commentPeople = qr.commentPeople;
            double tempProcess = commentPeople / totalPeople * 100;
            int process = (int) tempProcess;

            switch (qr.itemId) {
                case 5:
                    if (qr.commentPeople != 0) {
                        if (qr.commentPeople > 0) {
                            tv_5.setText(qr.commentPeople + "");
                        }
                        pb5.setProgress(process);
                    } else {
                        tv_5.setText(0 + "");
                        pb5.setProgress(0);
                    }

                    break;
                case 4:
                    if (qr.commentPeople != 0) {
                        tv_4.setText(qr.commentPeople + "");
                        pb4.setProgress(process);
                    } else {
                        tv_4.setText(0 + "");
                        pb4.setProgress(0);
                    }
                    break;
                case 3:
                    if (qr.commentPeople != 0) {
                        tv_3.setText(qr.commentPeople + "");
                        pb3.setProgress(process);
                    } else {
                        tv_3.setText(0 + "");
                        pb3.setProgress(0);
                    }
                    break;
                case 2:
                    if (qr.commentPeople != 0) {
                        tv_2.setText(qr.commentPeople + "");
                        pb2.setProgress(process);
                    } else {
                        tv_2.setText(0 + "");
                        pb2.setProgress(0);
                    }
                    break;
                case 1:
                    if (qr.commentPeople != 0) {
                        tv_1.setText(qr.commentPeople + "");
                        pb1.setProgress(process);
                    } else {
                        tv_1.setText(0 + "");
                        pb1.setProgress(0);
                    }
                    break;
            }


            tv_total.setText(String.valueOf(totalPeople));

            /*double tempTotalScore = Math.floor(totalScore/totalPeople);
            ratingBar.setRating((int)tempTotalScore);
            tv_score.setText((int)tempTotalScore+"分");*/
        }

        ratingBar.setRating(gameInfo.percentage);
        tv_score.setText(gameInfo.percentage + "分");

    }


}
