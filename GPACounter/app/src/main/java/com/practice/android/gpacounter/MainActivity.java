package com.practice.android.gpacounter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    int lessonNumber = 0;
    EditText ed1;
    TextView credit, gpa;
    private String[] arrText;
    private String[] arrScore;
    private String[] arrCredit;
    private String[] arrTemp1, arrTemp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1= (EditText) findViewById(R.id.LessonNumber);
        ed1.setText("" + lessonNumber);
        credit =  (TextView) findViewById(R.id.credit);
        gpa =  (TextView) findViewById(R.id.GPA);
        credit.setText("0");
        gpa.setText("0");
    }

    public void submitLesson(View view){
        lessonNumber = Integer.parseInt(ed1.getText().toString());
        displayLesson(lessonNumber);
        arrTemp1 = new String[lessonNumber];
        arrTemp2 = new String[lessonNumber];
        arrText = new String[lessonNumber];
        arrScore = new String[lessonNumber];
        arrCredit = new String[lessonNumber];

        int i;
        String s = "Lesson ";
        String l = "score  : ";
        String c = "credit : ";
        for(i=1; i<=lessonNumber; i++){
            arrText[i-1] = s.concat(String.valueOf(i));
            arrScore[i-1] = l;
            arrCredit[i-1] = c;
        }

        MyListAdapter myListAdapter = new MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.listViewMain);
        listView.setAdapter(myListAdapter);
    }

    public void decrement(View view) {
        if(lessonNumber>0) lessonNumber--;
        displayLesson(lessonNumber);
    }

    public void increment(View view) {
        lessonNumber++;
        displayLesson(lessonNumber);
    }

    private void displayLesson(int num){
        ed1.setText("" + lessonNumber);
    }

    private double convertScore(String score){
        double x=0;
        if(score.equals("A"))
            x=4;
        else if(score.equals("AB"))
            x=3.5;
        else if(score.equals("B"))
            x=3;
        else if(score.equals("BC"))
            x=2.5;
        else if(score.equals("C"))
            x=2;
        else if(score.equals("D"))
            x=1;
        else if(score.equals("E"))
            x=0;
        return x;
    }

    public void countGPA(View view){
        int i, cred=0;
        double total=0;
        for(i=0; i<lessonNumber; i++){
            total += (convertScore(arrTemp1[i])*Integer.parseInt(arrTemp2[i]));
            cred += Integer.parseInt(arrTemp2[i]);
        }
        double gpas = total/cred;
        gpa.setText(String.valueOf(gpas));
        credit.setText(String.valueOf(cred));
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(arrText != null && arrText.length != 0){
                return arrText.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arrText[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //ViewHolder holder = null;
            final ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.listitem, null);
                holder.textLesson1 = (TextView) convertView.findViewById(R.id.textLesson1);
                holder.textScore1 = (TextView) convertView.findViewById(R.id.textScore1);
                holder.editScore1 = (EditText) convertView.findViewById(R.id.editScore1);
                holder.textCredit1 = (TextView) convertView.findViewById(R.id.textCredit1);
                holder.editCredit1 = (EditText) convertView.findViewById(R.id.editCredit1);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            holder.textLesson1.setText(arrText[position]);
            holder.textScore1.setText(arrScore[position]);
            holder.editScore1.setText(arrTemp1[position]);
            holder.textCredit1.setText(arrCredit[position]);
            holder.editCredit1.setText(arrTemp2[position]);
            holder.editScore1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    arrTemp1[holder.ref] = arg0.toString();
                }
            });
            holder.editCredit1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    arrTemp2[holder.ref] = arg0.toString();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView textLesson1;
            TextView textScore1;
            TextView textCredit1;
            EditText editScore1;
            EditText editCredit1;
            int ref;
        }
    }
}
