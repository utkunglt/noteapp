package com.example.noteapp;

import android.app.Application;

import com.example.noteapp.R.string;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

public class NoteApplication extends Application {

	
  @Override
  public void onCreate() {
    super.onCreate();
    Parse.initialize(this, ApplicationConstants.APPLICATION_ID,
    		ApplicationConstants.CLIENT_KEY);
  }
}
