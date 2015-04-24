package com.example.noteapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private List<Note> posts;
	private ProgressBar prgBar;
	private NoteAdapter adapter;
	private ListView lstView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getSimpleName(), ">>>onCreate");
		super.onCreate(savedInstanceState);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			loadLoginView();
		}
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		lstView = (ListView) findViewById(android.R.id.list);
		prgBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
		posts = new ArrayList<Note>();
		adapter = new NoteAdapter(this,
		        R.layout.row_layout, posts);
		
		lstView.setAdapter(adapter);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Note note = posts.get(position);
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("noteId", note.getId());
                intent.putExtra("noteTitle", note.getTitle());
                intent.putExtra("noteContent", note.getContent());
                startActivity(intent);
                
            }
        });
		refeshPostList();
	}
	private void loadLoginView() {
		Intent intent = new Intent(this, LoginActivity.class);
		//Clear the stack history and set LoginActivity as start 
		//of history stack
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/*@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Note note = posts.get(position);
		Intent intent = new Intent(this, EditNoteActivity.class);
		intent.putExtra("noteId", note.getId());
		intent.putExtra("noteTitle", note.getTitle());
		intent.putExtra("noteContent", note.getContent());
		startActivity(intent);
		
	}*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new:
	            Intent intent = new Intent(this, EditNoteActivity.class);
	            startActivity(intent);
	            break;
	        case R.id.action_refresh:
	            adapter.clear();
	        	refeshPostList();
	             break;
	        case R.id.action_logout:
	        	ParseUser.logOut();
	            loadLoginView();
	        	break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return super.onOptionsItemSelected(item);
	}
	private void refeshPostList(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
		setProgressBarIndeterminateVisibility(true);
		prgBar.setVisibility(View.VISIBLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
		        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
				setProgressBarIndeterminateVisibility(true);
				
				query.findInBackground(new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						setProgressBarIndeterminateVisibility(false);
						prgBar.setVisibility(View.GONE);
						getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
						if (e == null) {
							posts.clear();
							for (ParseObject post : objects) {
								Note note = new Note(post.getObjectId(),
										post.getString("title"),
										post.getString("content"));
								posts.add(note);
							}
							ArrayAdapter<Note> listAdapter = (ArrayAdapter<Note>)lstView.getAdapter();
							ArrayAdapter<Note> arrayAdapter = listAdapter;
							arrayAdapter.notifyDataSetChanged();
						} else {
							Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
						}
						
					}
				});
				
			}
		});
	}
}
