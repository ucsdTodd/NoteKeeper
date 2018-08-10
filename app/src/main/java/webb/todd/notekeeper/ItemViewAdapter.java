package webb.todd.notekeeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {

    private List<String> items = new ArrayList<>();

    private Context context;

    ItemViewAdapter(Context context, final String[] noteItems ){
        this.context = context;
        if( noteItems != null ) {
            items.addAll(Arrays.asList(noteItems));
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( android.R.layout.activity_list_item, parent, false);
        //.inflate(R.layout.fragment_main_list, parent, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position ) {
        if( position == items.size() ){
            holder.textView.setText( "+" );
        }
        else if( position < items.size() ){
           holder.textView.setText( items.get(position) );
        }
    }

    @Override
    public int getItemCount() {
        return items.size() +1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView = new EditText( context );

        public final CheckBox checkBox;

        private long rowId;

        public ViewHolder( View itemView ) {
            super( new EditText( context ) );
            textView = (TextView) this.itemView;
            checkBox = itemView.findViewById( android.R.id.checkbox );
            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.i("Debug:", "ViewHolder.onClick" );
                }
            });
        }

        public void setRowID( long rowID ){
            this.rowId = rowID;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText().toString() + "'";
        }
    }
}
