package com.meutkarsh.androidchatapp.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meutkarsh.androidchatapp.POJO.Question;
import com.meutkarsh.androidchatapp.R;
import java.util.ArrayList;

/**
 * Created by tanay on 12/11/17.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    ArrayList<Question> questionList;
    Context context;
    public QuestionAdapter(ArrayList<Question> qlist, Context context){
        this.questionList = qlist;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        if(this.questionList.get(position).getSuccess()) return 1;
        return 0;
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate( ( viewType==1) ? R.layout.solved_problem : R.layout.unsolved_problem
                , parent,false);


        return new QuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuestionHolder holder, int position) {
        Question question = questionList.get(position);

        holder.question.setText(question.getTitle());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder{
        public TextView question;

        public QuestionHolder(View itemView){
            super(itemView);
            this.question = (TextView) itemView.findViewById(R.id.questionid);
        }
    }
}
