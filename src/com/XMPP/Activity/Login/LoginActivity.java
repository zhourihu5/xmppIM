package com.XMPP.Activity.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.XMPP.R;
import com.XMPP.Activity.Mainview.MainviewActivity;
import com.XMPP.Service.FriendService;
import com.XMPP.smack.Smack;
import com.XMPP.smack.SmackImpl;
import com.XMPP.util.Constants;

public class LoginActivity extends Activity implements OnClickListener {

	private String username;
	private String password;
	private TextView submitLogin;
	private TextView forget;
	private Smack smack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		init();
	}

	public void init() {
		smack = new SmackImpl();
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				smack.connect(Constants.SERVER_IP, Constants.SERVER_PORT);
			}
			
		}).start();
		
		submitLogin = (TextView) findViewById(R.id.submitLogin);
		forget = (TextView) findViewById(R.id.forget);
		submitLogin.setOnClickListener(this);
	}

	public void startAllServices() {
		Intent firend_intent = new Intent(this,FriendService.class);
		this.startService(firend_intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitLogin:
			username = ((EditText) findViewById(R.id.username)).getText()
					.toString();
			password = ((EditText) findViewById(R.id.password)).getText()
					.toString();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final int login_result = smack.login(username, password);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							switch (login_result) {
							case Constants.LOGIN_SUCCESS:
								startAllServices();
								Intent intent = new Intent(LoginActivity.this,
										MainviewActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("GroupList",
										smack.getGroupList());
								intent.putExtras(bundle);
								LoginActivity.this.startActivity(intent);
								break;
							case Constants.LOGIN_CONNECT_FAIL:
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.connect_fail),
										Toast.LENGTH_SHORT).show();
								break;
							case Constants.LOGIN_USERNAME_PSW_ERROR:
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.name_psw_wrong),
										Toast.LENGTH_SHORT).show();
							}

						}
					});

				}
			}).start();
			break;

		case R.id.forget:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

}
