package webb.todd.notekeeper;;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<String> notes = new ArrayList<>();

    {
        // Dummy data for now
        notes.add( "This is my first note");
        notes.add( "This is my second note");
    }

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById( R.id.rootLayout );
        restoreAllNotes();
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
            Log.i("Action", "Create List");
        }
        else if( item.getItemId() == R.id.createNote ){
            addNoteView( null );
        }
        return super.onOptionsItemSelected(item);
    }

    void restoreAllNotes(){
        for( String note: notes ){
            addNoteView( note );
        }
    }

    void addNoteView( final String noteText ){
        //TextView noteView = new TextView( this );
        TextView noteView = new EditText( this );

        if( noteText != null ) {
            noteView.setText(noteText);
        }
        //((ViewGroup.MarginLayoutParams)noteView.getLayoutParams()).setMargins( 5, 5, 5, 5);
        ViewGroup.MarginLayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        params.setMargins( 30, 15, 30, 15 );
        noteView.setLayoutParams( params );
        noteView.setBackgroundColor(  getResources().getColor(R.color.textViewBackground) );
        layout.addView( noteView );
    }
}
