package app.com.HungryEnglish.Interface;

import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;

public interface OnItemClick {
        public void onItemClick(TeacherListResponse response, int pos);
    }