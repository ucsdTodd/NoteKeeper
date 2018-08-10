package webb.todd.notekeeper;;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int RC_SIGN_IN = 101;

    public List<Object> notes = new ArrayList<>();
    {
        // Dummy data for now
        notes.add( "Need to finish report for class on Tuesday");
        notes.add( "Contact plumber\n\tleak in upstair bathtub\n\t" +
                "replace supply line");
        notes.add( new String[]{ "take out trash", "do laundry", "walk the dog", "Workout" } );
        notes.add( "This is my third note");

    }

    LinearLayout layout;

    private final static ViewGroup.MarginLayoutParams ITEM_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
    static{
        ITEM_LAYOUT_PARAMS.setMargins( 30, 15, 30, 15 );
    }


    GoogleSignInClient googleSignInClient;

    SignInButton signInButton;

    Object currentAccount = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout) findViewById( R.id.rootLayout );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // create the Google sign in client
        googleSignInClient = GoogleSignIn.getClient( this, gso );

        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE ); // SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check for existing Google sign in account.  If the user is already signed in,
        // the GoogleSignInAccount will be non-null
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if( gAccount != null ){
            currentAccount = gAccount;
            updateUI( gAccount );
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if( account != null ) {
                // Signed in successfully, show authenticated UI.
                currentAccount = account;
                updateUI(account);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w( "SignIn", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI( GoogleSignInAccount account ){
        if( false ){ //account == null ){
            layout.removeAllViews();
            layout.addView( signInButton );
        }
        else{
            layout.removeView( signInButton );
            restoreUserNotes( account );
        }
    }

    void restoreUserNotes( GoogleSignInAccount account ){
        for( Object note: notes ){
            if( note instanceof String[] ){
                addListView( (String[])note );
            }
            else{
                addNoteView( note.toString() );
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if( item.getItemId() == R.id.createList ) {
            // Log.i("Action", "Create List");
            addListView( null );
        }
        else if( item.getItemId() == R.id.createNote ){
            addNoteView( null );
        }
        return super.onOptionsItemSelected(item);
    }

    void addNoteView( final String noteText ){
        //if( currentAccount == null ){
        //    return;
       // }
        //TextView noteView = new TextView( this );
        TextView noteView = new EditText( this );

        if( noteText != null ) {
            noteView.setText(noteText);
        }
        noteView.setPadding( 24, 10, 30, 10 );
        noteView.setBackgroundColor(  getResources().getColor(R.color.textViewBackground) );
        noteView.setElevation( 8 );
        layout.addView( noteView, ITEM_LAYOUT_PARAMS );
    }

    void addListView( final String[] items ){
        //if( currentAccount == null ){
        //    return;
        // }
       final RecyclerView listView = new RecyclerView( this );
       listView.setLayoutManager( new LinearLayoutManager( this ) );
       listView.setAdapter( new ItemViewAdapter(this, items) );
       listView.setHasFixedSize( true );
//        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView textView = ((TextView)view);
//                if( textView.getText() == null || textView.getText().length() == 0 ) {
//                    textView.setText(" hi there ");
//                }
//                else {
//                    listView.setItemChecked(i, true);
//                }
//            }
//        });
        listView.setPadding( 24, 10, 30, 10 );
        listView.setBackgroundColor( getResources().getColor( R.color.listViewBackground ));
        listView.setElevation( 8 );
        layout.addView( listView, ITEM_LAYOUT_PARAMS );
    }

}
