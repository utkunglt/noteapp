package com.example.noteapp;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends Activity {

	private Note note;
	private String postTitle;
	private String postContent;
	private EditText titleEditText;
	private EditText contentEditText;
	private Button saveNoteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_edit_note);

		Intent intent = this.getIntent();

		titleEditText = (EditText) findViewById(R.id.noteTitle);
		contentEditText = (EditText) findViewById(R.id.noteContent);

		if (intent.getExtras() != null) {
			note = new Note(intent.getStringExtra("noteId"),
					intent.getStringExtra("noteTitle"),
					intent.getStringExtra("noteContent"));
			titleEditText.setText(note.getTitle());
			contentEditText.setText(note.getContent());
		}

		saveNoteButton = (Button) findViewById(R.id.saveNote);
		saveNoteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveNote();

			}
		});
	}

	private void saveNote() {
		postTitle = titleEditText.getText().toString();
		postContent = contentEditText.getText().toString();

		postTitle.trim();
		postContent.trim();

		if (!postTitle.isEmpty()) {
			if (note == null) {
				final ParseObject post = new ParseObject("Post");
				post.put("title", postTitle);
				post.put("content", postContent);
				setProgressBarIndeterminateVisibility(true);
				post.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						setProgressBarIndeterminateVisibility(false);
						if (e == null) {
							note = new Note(post.getObjectId(), postTitle, postContent);
							Toast.makeText(getApplicationContext(), "Saved",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Failed to Save", Toast.LENGTH_SHORT)
									.show();
							Log.d(getClass().getSimpleName(), "User update error: " + e);
						}
					}
				});
			} else {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

				query.getInBackground(note.getId(),
						new GetCallback<ParseObject>() {
							public void done(ParseObject object,
									ParseException e) {
								if (e == null) {
									object.put("title", postTitle);
									object.put("content", postContent);
									setProgressBarIndeterminateVisibility(true);
									object.saveInBackground(new SaveCallback() {

										@Override
										public void done(ParseException e) {
											setProgressBarIndeterminateVisibility(false);
											if (e == null) {
												Toast.makeText(
														getApplicationContext(),
														"Saved",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Log.d(getClass().getSimpleName(), ">>>postTitle.isEmpty()ParseException: "+ e);
												Toast.makeText(
														getApplicationContext(),
														"Failed to Save",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
								}
							}
						});
			}
		}
		else if (postTitle.isEmpty() && !postContent.isEmpty()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
			builder.setMessage(R.string.edit_error_message)
				.setTitle(R.string.edit_error_title)
				.setPositiveButton(android.R.string.ok, null);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
