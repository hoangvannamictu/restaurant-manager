package test1.com.quanlyquanlautrungkhanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail_login, edtPassword_login;
    private RadioButton rbUser_login, rbAdmin_login;
    private Button btnLogin_login, btnQuit_login;
    private String strEmail, strPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhXa();
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
//        if (mAuth.getCurrentUser() != null && rbQuanLy.isChecked()) {
//            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
//            finish();
//        }else if(mAuth.getCurrentUser() != null && rbNhanVien.isChecked()){
//            startActivity(new Intent(LoginActivity.this, OrderMainActivity.class));
//            finish();
//        }
        addEvents();
    }

    private void addEvents() {
        btnQuit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetConnection(LoginActivity.this)){
                    if (rbUser_login.isChecked()){
                        CheckSignInEmailUser();
                    }else {
                        CheckSignInEmailAdmin();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.khong_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CheckSignInEmailUser() {
        strEmail = edtEmail_login.getText().toString().trim();
        strPassword = edtPassword_login.getText().toString().trim();

        if (strEmail.isEmpty() || strPassword.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.email_matkhau_trong), Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String strUID = task.getResult().getUser().getUid();
                                checkUserFirebase(strUID);
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_matkhau_sai), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void CheckSignInEmailAdmin() {
        strEmail = edtEmail_login.getText().toString().trim();
        strPassword = edtPassword_login.getText().toString().trim();

        if (strEmail.isEmpty() || strPassword.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.email_matkhau_trong), Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String strUID = task.getResult().getUser().getUid();
                                checkAdminFirebase(strUID);
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_matkhau_sai), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void checkUserFirebase(String strUID) {
        if (strUID != null) {
            Query query = ref.child("users").orderByKey().equalTo(strUID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String fetchedValue = "";
                        for (DataSnapshot snap : dataSnapshot.getChildren()){
                            fetchedValue = snap.getKey();
                        }
                        if (!strUID.equals(fetchedValue)){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.loi_dang_nhap), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.dangnhap_thanhcong), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, OrderMainActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.taikhoan_admin_khongthe_dangnhap_vao_nhanvien), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.loi)+" : "+databaseError, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkAdminFirebase(String strUID) {
        if (strUID != null) {
            Query query = ref.child("admin").orderByKey().equalTo(strUID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String fetchedValue = "";
                        for (DataSnapshot snap : dataSnapshot.getChildren()){
                            fetchedValue = snap.getKey();
                        }
                        if (!strUID.equals(fetchedValue)){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.loi_dang_nhap), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.dangnhap_thanhcong), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ban_chuaduoc_cap_quyen), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.loi)+" : "+databaseError, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //check internet connection :if connect return true or if not connect re turn false
    public static boolean isInternetConnection(Context mContext){
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    private void anhXa() {
        edtEmail_login = findViewById(R.id.edtEmail_Login);
        edtPassword_login = findViewById(R.id.edtPassword_login);
        rbUser_login = findViewById(R.id.rbUser_login);
        rbAdmin_login = findViewById(R.id.rbAdmin_login);
        btnLogin_login = findViewById(R.id.btnLogin_login);
        btnQuit_login = findViewById(R.id.btnQuit_login);
    }
}