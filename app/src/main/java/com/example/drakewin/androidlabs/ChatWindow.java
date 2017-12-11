package com.example.drakewin.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.drakewin.androidlabs.ChatDatabaseHelper.COLUMNS;
import static com.example.drakewin.androidlabs.ChatDatabaseHelper.KEY_MESSAGES;
import static com.example.drakewin.androidlabs.ChatDatabaseHelper.TABLE_NAME;
import static com.example.drakewin.androidlabs.ListItemsActivity.ACTIVITY_NAME;

public class ChatWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
       final ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
       final SQLiteDatabase db = dbHelper.getWritableDatabase();
       final SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        //Widgets varriables
        final ListView listView = (ListView)findViewById(R.id.listView);
        final EditText chatbox = (EditText)findViewById(R.id.chatBox);
        Button btnSend = (Button)findViewById(R.id.send);

        final ArrayList<String> chats = new ArrayList<String>();

        // get the previous records
        Cursor cursor = db2.query(dbHelper.TABLE_NAME,dbHelper.COLUMNS,null,null,null,null,null);


        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount();i++){
                cursor.moveToNext();
                chats.add(cursor.getString(1));
                Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );
            }
        }



        class ChatAdapter extends ArrayAdapter<String> {
                public ChatAdapter(Context ctx) {
                    super(ctx,0);
                }

                public int getCount(){
                    return chats.size();
                }

                public String getItem(int position){
                    return chats.get(position);
                }

                public View getView(int position, View convertView, ViewGroup partent){
                    LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
                    View result= null;
                    if (position%2==0){
                        result = inflater.inflate(R.layout.chat_row_incoming,null);
                    } else {
                        result = inflater.inflate(R.layout.chat_row_outgoing,null);
                    }
                    TextView message = (TextView)result.findViewById(R.id.message_text);
                    message.setText(getItem(position));
                    return result;
                }

        }

        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);
        // event listener when someone hits send
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chats.add(chatbox.getText().toString());
                messageAdapter.notifyDataSetChanged();

                ContentValues values = new ContentValues();
                values.put(dbHelper.KEY_MESSAGES,chatbox.getText().toString());

                db.insert(dbHelper.TABLE_NAME,null,values);

                chatbox.setText("");

            }

        });


      /*
        Cursor cursor =
                db.query(TABLE_NAME, COLUMNS," id = ?",new String[] {String.valueOf(0)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        while(!cursor.isAfterLast() )
            Log.i(ACTIVITY_NAME,cursor.getString( cursor.getColumnIndex( dbHelper.KEY_MESSAGES) ) );

        Log.i(ACTIVITY_NAME, "Cursor  column count =" + cursor.getColumnCount() );

        chats.add(cursor.getString(0));
                     */


    }

}
