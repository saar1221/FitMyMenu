package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText name,email,password,c_password;
    private Button btn_regist;
    private ProgressBar loading;
    private  static  String URL_REGISTER ="http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/register.php";
    private static final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        loading= findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        c_password= findViewById(R.id.c_password);
        btn_regist=findViewById(R.id.btn_regist);
        TextView link_login = findViewById(R.id.link_login);

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkifinputfull()) {
                    Regist();
                }
            }
        });
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));//LoginActivity
                //nice anim slide left to right the Login page
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
    }

    public void Regist(){
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);
        final String name=this.name.getText().toString().trim();
        final String email=this.email.getText().toString().trim();
        final String password =this.password.getText().toString().trim();


        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL_REGISTER,
          new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try{
                    Log.d(TAG, "indexxxxx        response        xxxxxx    "+response);
                    JSONObject jsonObject =new JSONObject(response);
                    String success=jsonObject.getString( "success");

                    String id=jsonObject.getString( "id");
                    Log.d(TAG, "indexxxxx        id        xxxxxx    "+id);
                   Integer id_int= Integer.parseInt(id);
                    ServiceDataBaseHolder.getUser_confirm().setId(id_int);

                    if(success.equals("1")){
                        Toast.makeText(RegisterActivity.this,"Register Success!",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this,ObjectiveActivity.class);
                        intent.putExtra("user_mode", "new");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                        btn_regist.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this,"Register Error! only use by this email " ,Toast.LENGTH_SHORT).show();//+e.toString()
                    loading.setVisibility(View.GONE);
                    btn_regist.setVisibility(View.VISIBLE);
                }
            }
          },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this," Register Error! " +error.toString(),Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_regist.setVisibility(View.VISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",password);

                return params;
           }
        };


        RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);


    }
    private boolean checkifinputfull( ){

        final String name1=this.name.getText().toString().trim();
        final String email1=this.email.getText().toString().trim();
        final String password1 =this.password.getText().toString().trim();
        final String c_password1 =this.c_password.getText().toString().trim();

        if( name1.isEmpty()|| email1.isEmpty()|| password1.isEmpty()|| c_password1.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Fill all the input ",Toast.LENGTH_SHORT).show();
            return  false;
        }

      else   if(!password1.equals(c_password1)){
            Toast.makeText(RegisterActivity.this,"No The Same Password ",Toast.LENGTH_SHORT).show();
            return false;
        }
       else  if(!isEmailValid(email1)){
             Toast.makeText(RegisterActivity.this,"Incorrect Email",Toast.LENGTH_SHORT).show();
             return false;
         }

        else
            return true;
    }

    public static boolean isEmailValid(String email) {
        return !(email == null || TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}