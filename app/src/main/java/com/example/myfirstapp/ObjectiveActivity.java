package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectiveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText weight,height,age;
    private RadioGroup rgpurpose ,rgdiabetes,rgtypefood, rggender;
    private static final String TAG = "ObjectiveActivity";


    private ArrayList<String> selectedAllergies =new ArrayList<>();
    final private users user_confirm = ServiceDataBaseHolder.getUser_confirm();
    private CheckBox cbLactose ,cbGluten,cbSesame,cbSoy,cbSugar,cbNuts,cbEgg,cbFish;
    private static String level_activity="1"; //explain  in objective.php
    private static  String URL_OBJECT="http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/objective.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objective);

        weight =(EditText) findViewById(R.id.weight);
        height=(EditText) findViewById(R.id.height);
        age=(EditText) findViewById(R.id.age);

        rggender = (RadioGroup)findViewById(R.id.rgGender);
        rgpurpose=(RadioGroup)findViewById(R.id.rgPurpose);
        rgdiabetes=(RadioGroup)findViewById(R.id.rgDiabetes);
        rgtypefood=(RadioGroup)findViewById(R.id.rgTypeFood);


        //spinner its the level activity
        Spinner spinner = (Spinner) findViewById(R.id.leveActivity);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.level_activity,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

//check box allergies
        cbLactose =(CheckBox)findViewById(R.id.cbLactose);
        cbGluten =(CheckBox)findViewById(R.id.cbGluten);
        cbSesame =(CheckBox)findViewById(R.id.cbSesame);
        cbSoy =(CheckBox)findViewById(R.id.cbSoy);
        cbSugar =(CheckBox)findViewById(R.id.cbSugar);
        cbNuts =(CheckBox)findViewById(R.id.cbNuts);
        cbEgg =(CheckBox)findViewById(R.id.cbEgg);
        cbFish =(CheckBox)findViewById(R.id.cbFish);

        Button start = (Button) findViewById(R.id.btn_start);
        Button cleanAll = (Button) findViewById(R.id.btn_Clear);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mWeight =weight.getText().toString().trim();
                String mHeight =height.getText().toString().trim();
                String mAge =age.getText().toString().trim();
//take the input from edit text and check if empty
                ChecksThatTheTextViewIsNotEmpty(mWeight,mHeight,mAge);


            }
        });
        //clear all the options are selected in the page
        cleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         cleanAllThePage();
            }
        });

    }



    private void Objective() {
        RadioButton selectedrbgender1 = (RadioButton) findViewById(rggender.getCheckedRadioButtonId());
        RadioButton selectedrbpurpose1 = (RadioButton) findViewById(rgpurpose.getCheckedRadioButtonId());
        RadioButton selectedrbdiabetes1 = (RadioButton) findViewById(rgdiabetes.getCheckedRadioButtonId());
        RadioButton selectedrgtypefood = (RadioButton) findViewById(rgtypefood.getCheckedRadioButtonId());



        final  String user_mode=(String) getIntent().getSerializableExtra("user_mode");



        final String weight=this.weight.getText().toString().trim();
        final String height=this.height.getText().toString().trim();
        final String age=this.age.getText().toString().trim();
        final String activity=level_activity;

        //selected radio button
        final   String selectedrbgender= selectedrbgender1.getText().toString().trim();
        final   String selectedrbpurpose= selectedrbpurpose1.getText().toString().trim();
        final  String selectedrbdiabetes= selectedrbdiabetes1.getText().toString().trim();
        final String selectedrbtypefood= selectedrgtypefood.getText().toString().trim();

            // function that take all the selected allergies and insert to arrayList
                  SelectedAllergies();


        final String finalUser_mode = user_mode;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OBJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "indexxxxx        response        xxxxxx    "+response);
                            JSONObject jsonObject =new JSONObject(response);
                            String success=jsonObject.getString( "success");

                            Log.d(TAG, "indexxxxx        success1        xxxxxx    ");

                            JSONArray array_user =jsonObject.getJSONArray("user");
                            JSONObject object = array_user.getJSONObject(0);

                            Log.d(TAG, "indexxxxx        success 2       xxxxxx    ");


                            user_confirm.setId(object.getInt("id"));
                            user_confirm.setName(object.getString("name"));
                            user_confirm.setEmail(object.getString("email"));
                            user_confirm.setWeight(object.getDouble("weight"));
                            user_confirm.setHeight(object.getDouble("height"));
                            user_confirm.setAge(object.getInt("age"));
                            user_confirm.setPurpose(object.getString("purpose"));
                            user_confirm.setLevel_activity(object.getInt("level_activity"));
                            user_confirm.setGender(object.getString("gender"));
                              //  user_confirm.setCsv_file(false);  // צריל לבדוק את זה כי אם אני מעדכן את הנתונים זה יעדכן שהוא לא השתמש באלגוריתם

                            //update the user data into the constractor
                            ServiceDataBaseHolder.setUser_confirm(user_confirm);


                            Log.d(TAG, "indexxxxx        success 3       xxxxxx    ");

                            if(success.equals("1")){
                                Toast.makeText(ObjectiveActivity.this,"Objective Success!",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ObjectiveActivity.this,LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ObjectiveActivity.this,"Objective Error! " +e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ObjectiveActivity.this,"Objective Error! " +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();

                params.put("weight",weight);
                params.put("height",height);
                params.put("age",age);
                params.put("purpose",selectedrbpurpose);
                params.put("activity",activity);
                params.put("diabetes",selectedrbdiabetes);
                params.put("typefood",selectedrbtypefood);
                params.put("id",ServiceDataBaseHolder.getUser_confirm().getId().toString());
                params.put("gender",selectedrbgender);
                params.put("user_mode",user_mode);
                params.put("selectedAllergies",selectedAllergies.toString());

                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(ObjectiveActivity.this);
        requestQueue.add(stringRequest);

    }

    //selcted checkbox allergies and insert to array
    private void SelectedAllergies() {
        final String selectedcblactose =this.cbLactose.getText().toString().trim();
        final String selectedcbgluten =this.cbGluten.getText().toString().trim();
        final String selectedcbsesame =this.cbSesame.getText().toString().trim();
        final String selectedcbsoy =this.cbSoy.getText().toString().trim();
        final String selectedcbcbsugar =this.cbSugar.getText().toString().trim();
        final String selectedcbnuts =this.cbNuts.getText().toString().trim();
        final String selectedcbegg =this.cbEgg.getText().toString().trim();
        final String selectedcbfish =this.cbFish.getText().toString().trim();

        if(cbLactose.isChecked())
            selectedAllergies.add(selectedcblactose);

        if(cbGluten.isChecked())
            selectedAllergies.add(selectedcbgluten);

        if(cbSesame.isChecked())
            selectedAllergies.add(selectedcbsesame);

        if(cbSoy.isChecked())
            selectedAllergies.add(selectedcbsoy);

        if(cbSugar.isChecked())
            selectedAllergies.add(selectedcbcbsugar);

        if(cbNuts.isChecked())
            selectedAllergies.add(selectedcbnuts);

        if(cbEgg.isChecked())
            selectedAllergies.add(selectedcbegg);

        if(cbFish.isChecked())
            selectedAllergies.add(selectedcbfish);

    }


    //Item Selected on level_activity its take only the first word from the selected String so its 1 to 5
    /*
		###	activity can be 1 to 5
		###	this explain the value in the data base on table users_table the colom level_activity
        1-Little or no activity - office work at a desk
        2-Little activity - 1-3 times a week
        3-Average activity - 3-5 times a week
        4-Intensive activity - daily
        5-Intensive activity is combined with physical work
*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text =parent.getItemAtPosition(position).toString();
        level_activity =text.substring(0,1);
        Log.d(TAG, "indexxxxx        text        xxxxxx    "+ text + "    "   );
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void ChecksThatTheTextViewIsNotEmpty (String mWeight,String mHeight,String mAge){
        //if any of the options unselected send error message
        double decimalmWeight = Double.parseDouble(mWeight);
        double decimalmHeightt = Double.parseDouble(mHeight);
        int  decimalmAge = Integer.parseInt(mAge);
        if(mWeight.isEmpty() )
        {
            weight.setError("Please Insert Weight");
        }
        else if( (decimalmAge>=97 || decimalmAge<=16) || (decimalmHeightt>=240 || decimalmHeightt<=80) || (decimalmWeight>=170 ||decimalmWeight<=30  ) )
        {
            height.setError("Please check Height < 270");
            weight.setError("Please check Weight < 200");
            age.setError("Please check Age < 120 and > 16");
        }
        else if(mHeight.isEmpty())
        {
            height.setError("Please Insert Height");
        }
        else if(mAge.isEmpty())
        {
            age.setError("Please Insert Age");
        }
        else if(rggender.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(ObjectiveActivity.this,"Select Gender!",Toast.LENGTH_SHORT).show();
        }
        else if (rgpurpose.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(ObjectiveActivity.this,"Select Purpose!",Toast.LENGTH_SHORT).show();
        }
        else if (rgdiabetes.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(ObjectiveActivity.this,"Select Diabetes!",Toast.LENGTH_SHORT).show();
        }
        else if (rgtypefood.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(ObjectiveActivity.this,"Select Type Food!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Objective();
        }

    }
    private void cleanAllThePage() {
        weight.setText("");
        height.setText("");
        age.setText("");
        rggender.clearCheck();
        rgpurpose.clearCheck();
        rgdiabetes.clearCheck();
        rgtypefood.clearCheck();
        cbLactose.setChecked(false);
        cbGluten.setChecked(false);
        cbSesame.setChecked(false);
        cbSoy.setChecked(false);
        cbSugar.setChecked(false);
        cbNuts.setChecked(false);
        cbEgg.setChecked(false);
        cbFish.setChecked(false);
    }
}

