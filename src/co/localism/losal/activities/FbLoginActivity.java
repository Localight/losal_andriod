package co.localism.losal.activities;

import java.util.Arrays;

import co.localism.losal.R;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.internal.SessionTracker;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FbLoginActivity extends Activity {

	
	  private Session mCurrentSession;
	private SessionTracker mSessionTracker;




	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    signInWithFacebook();
/*
	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
//	                TextView welcome = (TextView) findViewById(R.id.welcome);
//	                welcome.setText
	                Log.d("FB","Hello " + user.getName() + "!");
	              }
	            }
	          });
	        }
	      }
	    });
	    
	    */
	  }

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }

	

	  
	  private void signInWithFacebook() {

		    mSessionTracker = new SessionTracker(getBaseContext(), new StatusCallback() {

		        @Override
		        public void call(Session session, SessionState state, Exception exception) {
		        }
		    }, null, false);

		    String applicationId = Utility.getMetadataApplicationId(getBaseContext());
		    mCurrentSession = mSessionTracker.getSession();

		    if (mCurrentSession == null || mCurrentSession.getState().isClosed()) {
		        mSessionTracker.setSession(null);
		        Session session = new Session.Builder(getBaseContext()).setApplicationId(applicationId).build();
		        Session.setActiveSession(session);
		        mCurrentSession = session;
		    }

		    if (!mCurrentSession.isOpened()) {
		        Session.OpenRequest openRequest = null;
		        openRequest = new Session.OpenRequest(FbLoginActivity.this);

		        if (openRequest != null) {
		            openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
		            openRequest.setPermissions(Arrays.asList("user_birthday", "email", "user_location"));
		            openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);

		            mCurrentSession.openForRead(openRequest);
		        }
		    }else {
		        Request.executeMeRequestAsync(mCurrentSession, new Request.GraphUserCallback() {
		              @Override
		              public void onCompleted(GraphUser user, Response response) {
		                  Log.w("myConsultant", user.getId() + " " + user.getName() + " " + user.getInnerJSONObject());
		              }
		            });
		    }
		}

	  
}
