package com.work1.rimduhui.nowshaking;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by rimduhui on 2016. 8. 16..
 */
public class Member {
  LinearLayout linearLayout;
  CheckBox checkBox;
  Button deleteButton;
  String name;

  public Member(){};

  public Member(String name, Context view){
    this.name = name;
    addMemberView(view);
  }

  public void addMemberView(Context view){
    this.linearLayout = new LinearLayout(view);
    this.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    addCheckBox(view);
    addDeleteButton(view);
  }

  public void addCheckBox(Context view){
    this.checkBox = new CheckBox(view);
    setCheckBoxText(this.checkBox);
    this.linearLayout.addView(this.checkBox);
  }

  public void setCheckBoxText(CheckBox checkBox){
    checkBox.setWidth(700);
    checkBox.setTextSize(30);
    checkBox.setText(this.name);
  }

  public void addDeleteButton(Context view){
    this.deleteButton = new Button(view);
    setDeleteButtonText(this.deleteButton);
    this.linearLayout.addView(this.deleteButton);
  }

  public void setDeleteButtonText(Button button){
    button.setText("Delete");
    button.setRight(View.FOCUS_RIGHT);
    button.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        deleteMember(v);
      }
    });
  }

  public void deleteMember(View v){
    this.linearLayout.removeAllViews();
    this.checkBox.setChecked(false);
    this.name = null;
  //  File file = new File(getFilesDir(), this.fileName);
  //  file.delete();
  }
}
