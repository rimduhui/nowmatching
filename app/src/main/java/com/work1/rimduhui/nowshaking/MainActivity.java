package com.work1.rimduhui.nowshaking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  ArrayList<Member> group = new ArrayList<Member>();
  LinearLayout dynamicLayout;

  String fileName = "MemberData.txt";
  FileOutputStream outputStream;
  FileInputStream inputStream;

  String text;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    this.dynamicLayout = (LinearLayout) findViewById(R.id.dynamicArea);
    initializeMember();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add:
        addDialog();
        return true;
    }
    return true;
  }

  @Override
  public void onPause() {
    super.onPause();
    updateMember();
  }

  public void initializeMember() {
    File file = new File(getFilesDir(), this.fileName);
    try {
      this.inputStream = openFileInput(this.fileName);
      BufferedReader buffer = new BufferedReader
          (new InputStreamReader(this.inputStream));
      String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
      while (str != null) {
        addMember(str);
        str = buffer.readLine();
      }
      buffer.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updateMember() {
    try {
      this.outputStream = openFileOutput(this.fileName, Context.MODE_PRIVATE);
      writeFile();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void writeFile() {
    for (int i = 0; i < this.group.size(); i++) {
      try {
        if (this.group.get(i).name != null) {
          this.outputStream.write((this.group.get(i).name +"\n").getBytes());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void addDialog() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    alert.setTitle("Register Member");
    alert.setMessage("Input Member Name.");
    final EditText name = new EditText(this);
    alert.setView(name);

    alert.setPositiveButton("Register", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        addMember(name.getText().toString());
        updateMember();
      }
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

      }
    });
    alert.show();
  }

  public void addMember(String name) {
    this.group.add(new Member(name, this));
    this.dynamicLayout.addView(this.group.get(this.group.size() - 1).linearLayout);
  }

  public void checkAll(View v) {
    for (int i = 0; i < this.group.size(); i++) {
      if (this.group.get(i).name != null) {
        this.group.get(i).checkBox.setChecked(true);
      }
    }
  }

  public void uncheckAll(View v) {
    for (int i = 0; i < this.group.size(); i++) {
      this.group.get(i).checkBox.setChecked(false);
    }
  }

  public void nowShaking(View v) throws IOException {
    String data = makeData();
    postData(data);
  }

  public String makeData(){
    String data = new String();
    int dataCount = 0;
    for(int i = 0 ; i < this.group.size() ; i++){
      if(this.group.get(i).checkBox.isChecked()){
        data += this.group.get(i).name + "&";
        dataCount++;
      }
    }
    if(dataCount%2 != 0){
      data += "Yourself&";
    }
    return data;
  }

  public void postData(String data){
    Gson gson = new GsonBuilder().setLenient().create();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.1.34:5000/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    EatPostService service = retrofit.create(EatPostService.class);
    //System.out.println(data);
    Call<String> call = service.postData(data);
    call.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
       // System.out.println(response.body().toString());
        getData();
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {

      }
    });
  }

  public void getData(){
    Gson gson = new GsonBuilder().setLenient().create();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.1.34:5000/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    EatGetService service = retrofit.create(EatGetService.class);
    Call<String> call = service.getString();
    call.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        System.out.println(response.body().toString());
        text = response.body().toString();
        text = text.replace("&", "\n");
        transferSlack();
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        System.out.println(t.toString());
      }
    });
  }

  public void transferSlack(){
    String token = "xoxp-17538967440-64929514737-68313059171-0334325aac";
    String channel = "nowshakingtest";
    String username = "nowmatching";
    URL url = null;
    try {
      url = new URL("https://ushhost.com/images/matching-icon.png");
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Retrofit retrofit2 = new Retrofit.Builder()
        .baseUrl("https://slack.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    SlackGetService service2 = retrofit2.create(SlackGetService.class);
    Call<Objects> call = service2.sendQuery(token, channel, text, username, url);
    call.enqueue(new Callback<Objects>() {
      @Override
      public void onResponse(Call<Objects> call, Response<Objects> response) {
        System.out.println("success2");
      }

      @Override
      public void onFailure(Call<Objects> call, Throwable t) {
        System.out.println("fail2");
      }
    });
  }
}
