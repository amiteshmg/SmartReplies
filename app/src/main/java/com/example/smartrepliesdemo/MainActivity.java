package com.example.smartrepliesdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeechService;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//import com.applozic.mobicomkit.api.account.user.User;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.util.ChatBot;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.threeten.bp.chrono.MinguoChronology;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
   // RecyclerView recyclerView;
    ImageView send_imageView;
    private EditText queryEditText;
    TextView textView;
    LinearLayout linearLayout;
    private LinearLayout chatLayout;
    private FirebaseAnalytics mFirebaseAnalytics;
    AIDataService aiDataService;
    AIRequest aiRequest;
    //String uuid="33473";
    private ChatView mChatView;
    private String uuid = UUID.randomUUID().toString();
    AIServiceContext aIServiceContext;
    SessionsClient sessionsClient;
    private SessionName session;



    private static final int USER = 10001;
    private static final int BOT = 10002;

    //ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        String id=new Random().toString();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "DEMO");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        final ScrollView scrollview = findViewById(R.id.chatScrollView);

        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        //scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);

        final ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(/*this::sendMessage*/new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

        queryEditText = findViewById(R.id.queryEditText);
//        queryEditText.setOnKeyListener((view, keyCode, event) -> {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_DPAD_CENTER:
//                    case KeyEvent.KEYCODE_ENTER:
//                        sendMessage(sendBtn);
//                        return true;
//                    default:
//                        break;
//                }
//            }
//            return false;
//        });

        queryEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    sendMessage(sendBtn);
                    return true;
                default:
                    break;
            }
        }
        return false;
        });

        //recyclerView= findViewById(R.id.conversationRecyclerView);

        // Android client
//        initChatbot();

        // Java V2
        initV2Chatbot();


/*
        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        //final User me = new User(myId, myName, myIcon);
        //final User me = new User(m);
        //final User you = new User(yourId, yourName, yourIcon);





        //mChatView = (ChatView)findViewById(R.id.chatLayout);

        //Set UI parameters if you need
      *//*  mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
*//*



        //Click Send Button
     *//*   mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRight(true)
                        .setText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");

                //Receive message
                final Message receivedMessage = new Message.Builder()
                        .setUser(you)
                        .setRight(false)
                        .setText(ChatBot.talk(me.getName(), message.getText()))
                        .build();

                // This is a demo bot
                // Return within 3 seconds
                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.receive(receivedMessage);
                    }
                }, sendDelay);
            }

        });*//*

        //constraintLayout=findViewById(R.id.main_layout);
       *//* send_imageView=findViewById(R.id.send_imageview);
        editText=findViewById(R.id.message_edit_text);
        textView=findViewById(R.id.message_log);
        linearLayout=findViewById(R.id.suggestions_layout);
*//*
        //TextToSpeechService

    *//*    send_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty())
                {
                    String message = editText.getText().toString();
                    editText.setText("");
                    textView.append("\n"+message);
                    ArrayList<String> suggestionList=getSmartSuggestions(message);
                    //ChatRecyclerAdapter chatRecyclerAdapter=new ChatRecyclerAdapter(message);
                    //recyclerView.setAdapter(new ChatRecyclerAdapter(message));
                }
            }
        });*/
    }

    private void initChatbot(){
        /*final ai.api.AIConfiguration config = new AIConfiguration("3c698cb682ba496f91b4d74cca12e6ff"), AIConfiguration.
        ai.api.AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
*/
     /*   AIConfiguration config=new AIConfiguration("3c698cb682ba496f91b4d74cca12e6ff");
        aiDataService = new AIDataService(config);
        aIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);
        aiRequest = new AIRequest();*/

        final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration("3c698cb682ba496f91b4d74cca12e6ff",
                ai.api.android.AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new ai.api.android.AIDataService(this, config);
        aIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);// helps to create new session whenever app restarts
        aiRequest = new AIRequest();
    }




    private void initV2Chatbot(){
        try{
            //getResources().openRawResource(R.raw.)
            InputStream stream = getResources().openRawResource(R.raw.smartrepliesdemo);
            //test_agent_credentials
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
            SessionsSettings.Builder settingsBuilder =SessionsSettings.newBuilder();
            //SessionsSettings sessionsSettings=settingsBuilder
            SessionsSettings sessionSettings=settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionSettings);
            session = SessionName.of(projectId,uuid);

        } catch(Exception e){

        }

    }



    private void sendMessage(View view) {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter your query!", Toast.LENGTH_LONG).show();
        } else {
            showTextView(msg, USER);
            queryEditText.setText("");
            // Android client
//            aiRequest.setQuery(msg);
//            RequestTask requestTask = new RequestTask(MainActivity.this, aiDataService, customAIServiceContext);
//            requestTask.execute(aiRequest);

            // Java V2
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("en-US")).build();
            new RequestJavaV2Task(MainActivity.this,session, sessionsClient, queryInput).execute();
        }
    }


    /* public class MyAsyncTask extends AsyncTask {


        *//*MyAsyncTask(AIDataService aiDataService, AIServiceContext customAIServiceContext) {


        }
*//*
        @Override
        protected AIResponse doInBackground(AIRequest...aiRequests) {
            final AIRequest aiRequest=aiRequests[0];
            try{
                return aiDataService.request(aiRequest,aIServiceContext);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/

    private void showTextView(String message, int type) {
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            default:
                layout = getBotLayout();
                break;
        }
        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
        TextView tv = layout.findViewById(R.id.chatMsg);
        tv.setText(message);
        layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            // process aiResponse here
            String botReply = response.getQueryResult().getFulfillmentText();
            Log.d(TAG, "V2 Bot Reply: " + botReply);
            showTextView(botReply, BOT);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Please Try again!", BOT);
        }
    }


    
    public void callback(AIResponse aiResponse) {
        if (aiResponse != null) {
            // process aiResponse here
            String botReply = aiResponse.getResult().getFulfillment().getSpeech();
            Log.d(TAG, "Bot Reply: " + botReply);
            showTextView(botReply, BOT);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Please Try again!", BOT);
        }
    }




    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }


    private ArrayList<String> getSmartSuggestions(String message) {

        String[] arr = message.split(" ");

        if (message.equalsIgnoreCase("how are you?")) {
            String[] answer = {"I am fine.How about you?", "Totally fine. And you?"};

            //TextView[] suggestions =new TextView();
            final TextView[] suggestions = new TextView[answer.length];

            for (int i = 0; i < answer.length; i++) {
                suggestions[i] = new TextView(getApplicationContext());
                suggestions[i].setBackground(getResources().getDrawable(R.drawable.blue_rectangle));
                suggestions[i].setPadding(10, 10, 10, 10);
                suggestions[i].setText(answer[i]);
                suggestions[i].setTextColor(getResources().getColor(R.color.aidialog_background));
                linearLayout.addView(suggestions[i]); //  <==========  ERROR IN THIS LINE DURING 2ND RUN

                final int finalI = i;
                suggestions[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.append("\n" + suggestions[finalI].getText().toString());
                        //editText.setText();
                        linearLayout.removeAllViews();
                    }
                });
            }
        }


        for (int i = 0; i < arr.length; i++)
        {

        }


        for ( String ss : arr) {
            if(isStopWords(ss))
            {

            }
        }

        return null;
    }

    public boolean isStopWords(String word)
    {
        return true;
    }
}
