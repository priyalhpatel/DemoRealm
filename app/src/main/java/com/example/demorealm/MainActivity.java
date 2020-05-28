package com.example.demorealm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demorealm.Model.Person;

import java.util.jar.Attributes;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

  private   EditText txtname,txtage;
    private Button btnadd,btndelete,btnview;
     private TextView txtview;
     private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         realm = Realm.getDefaultInstance();

        txtname = (EditText) findViewById(R.id.txtname);
        txtage= (EditText)findViewById(R.id.txtage);
        btnadd=(Button) findViewById(R.id.btnadd);
        btndelete=(Button) findViewById(R.id.btndelete);
        btnview=(Button) findViewById(R.id.btnview);
        txtview=(TextView) findViewById(R.id.txtview);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_to_database(txtname.getText().toString().trim(),Integer.parseInt(txtage.getText().toString().trim()));

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = txtname.getText().toString();
                delete_from_database(Name);

            }
        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_database();

            }
        });
    }

    private void delete_from_database(String Name) {
        final  RealmResults<Person> Person = realm.where (Person.class).equalTo("Name",Name).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person.deleteFromRealm(0);
            }
        });

    }

    private void refresh_database() {
        RealmResults<Person> result = realm.where(Person.class).findAllAsync();
        result.load();
        String output="";
        for(Person Person:result){
            output+=Person.toString();
        }
        txtview.setText(output);
    }

    private void save_to_database(final String Name, final int Age) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person Person = realm.createObject(Person.class);
                Person.setName(Name);
                Person.setAge(Age);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.v("Success","Ok");
            }


        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Failed",error.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
         super.onDestroy();
    }
}
