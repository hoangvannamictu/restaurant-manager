package test1.com.quanlyquanlautrungkhanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import test1.com.quanlyquanlautrungkhanh.Model.Admin;

public class AdminActivity extends AppCompatActivity {
    private LinearLayout layoutEditEmail_admin, layoutEditPassword_admin, layoutDelete_admin;
    private CardView cvAddAdmin;
    private TextView tvEmailAdmin, tvLogoutAdmin;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userCurrent = firebaseAuth.getCurrentUser();
                if (userCurrent == null) {
                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        anhXa();
        addEvents();
    }

    private void addEvents() {
        layoutEditEmail_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AdminActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_edit_email_admin);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                EditText edtNewEmailAdmin = dialog.findViewById(R.id.edtNewEmailAdmin);
                Button btnChangeEmailAdmin = dialog.findViewById(R.id.btnChangeEmailAdmin);
                Button btnQuitDiaLogEditEmailAdmin = dialog.findViewById(R.id.btnQuitDiaLogEditEmailAdmin);

                btnChangeEmailAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strNewEmail = edtNewEmailAdmin.getText().toString().trim();
                        editEmailAdmin(strNewEmail);
                    }
                });

                btnQuitDiaLogEditEmailAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        layoutEditPassword_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AdminActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_edit_password_admin);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                EditText edtNewPasswordAdmin = dialog.findViewById(R.id.edtNewPasswordAdmin);
                Button btnChangePasswordAdmin = dialog.findViewById(R.id.btnChangePasswordAdmin);
                Button btnQuitDiaLogEditPasswordAdmin = dialog.findViewById(R.id.btnQuitDiaLogEditPasswordAdmin);

                btnChangePasswordAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strNewPassword = edtNewPasswordAdmin.getText().toString().trim();
                        editPasswordlAdmin(strNewPassword);
                    }
                });

                btnQuitDiaLogEditPasswordAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        layoutDelete_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminActivity.this, R.style.AlertDialog);
                dialog.setTitle(getResources().getString(R.string.xoa_taikhoan));
                dialog.setMessage(getResources().getString(R.string.ban_muon_xoa_taikhoan));

                dialog.setPositiveButton(getResources().getString(R.string.co), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccountAdmin();
                    }
                });

                dialog.setNegativeButton(getResources().getString(R.string.khong), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        cvAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AdminActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_add_admin);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                EditText edtAddEmailAdmin = dialog.findViewById(R.id.edtAddEmailAdmin);
                EditText edtAddPassword = dialog.findViewById(R.id.edtAddPassword);
                EditText edtConfirmPasswordAdmin = dialog.findViewById(R.id.edtConfirmPasswordAdmin);
                EditText edtAddNameAdmin = dialog.findViewById(R.id.edtAddNameAdmin);
                EditText edtAddPhonetAdmin = dialog.findViewById(R.id.edtAddPhonetAdmin);
                Button btnAddAdmin = dialog.findViewById(R.id.btnAddAdmin);
                Button btnQuitDiaLogAddAdmin = dialog.findViewById(R.id.btnQuitDiaLogAddAdmin);

                btnAddAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strEmailAdmin = edtAddEmailAdmin.getText().toString().trim();
                        String strAddPassword = edtAddPassword.getText().toString().trim();
                        String strConfirmPasswordAdmin = edtConfirmPasswordAdmin.getText().toString().trim();
                        String strNameAdmin = edtAddNameAdmin.getText().toString().trim();
                        String strPhoneAdmin = edtAddPhonetAdmin.getText().toString().trim();

                        if(strEmailAdmin.isEmpty() || strAddPassword.isEmpty() || strNameAdmin.isEmpty() || strPhoneAdmin.isEmpty()){
                            Toast.makeText(AdminActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                        }else {
                            if(strAddPassword.length() >= 6){
                                if (strAddPassword.equals(strConfirmPasswordAdmin)){
                                    Admin admin = new Admin(strEmailAdmin, strAddPassword, strNameAdmin, strPhoneAdmin);
                                    addAccountAdmin(admin);
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(AdminActivity.this, getResources().getString(R.string.xacnhan_matkhau_lai_khongdung), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(AdminActivity.this, getResources().getString(R.string.dodai_matkhau_lonhon_5), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                btnQuitDiaLogAddAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        tvLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    private void addAccountAdmin(Admin admin) {
        mAuth.createUserWithEmailAndPassword(admin.getEmailAdmin(), admin.getPasswordAdmin())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String strUID = task.getResult().getUser().getUid();
                            ref.child("admin").child(strUID).setValue(admin, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null){
                                        Toast.makeText(AdminActivity.this, getResources().getString(R.string.tao_taikhoan_thanhcong)+"\n"+ admin.getEmailAdmin(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(AdminActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(AdminActivity.this, getResources().getString(R.string.tao_taikhoan_khong_thanhcong)+"\n"+ admin.getEmailAdmin(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void deleteAccountAdmin() {

        if (mUser != null) {
            mUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminActivity.this, getResources().getString(R.string.xoa_thanhcong), Toast.LENGTH_SHORT).show();
                                ref.child("admin").child(mUser.getUid()).removeValue();
                                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(AdminActivity.this, getResources().getString(R.string.xoa_thatbai), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void editPasswordlAdmin(String strNewPassword) {
        if(strNewPassword.isEmpty()){
            Toast.makeText(AdminActivity.this, getResources().getString(R.string.ban_chuanhap_matkhau_moi), Toast.LENGTH_SHORT).show();
        }else if(strNewPassword.length() < 6){
            Toast.makeText(this, getResources().getString(R.string.dodai_matkhau_lonhon_5), Toast.LENGTH_SHORT).show();
        }else {
            mUser.updatePassword(strNewPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ref.child("admin").child(mUser.getUid()).child("passwordAdmin").setValue(strNewPassword, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null){
                                            Toast.makeText(AdminActivity.this, getResources().getString(R.string.matkhau_da_đuoc_doi_vuilong_dangnhap_lai), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                                            finish();
                                        }else {
                                            Toast.makeText(AdminActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(AdminActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }



    }

    private void editEmailAdmin(String strNewEmail) {
        if(strNewEmail.isEmpty()){
            Toast.makeText(AdminActivity.this, getResources().getString(R.string.ban_chuanhap_email_moi), Toast.LENGTH_SHORT).show();
        }else{
            mUser.updateEmail(strNewEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ref.child("admin").child(mUser.getUid()).child("emailAdmin").setValue(strNewEmail, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null){
                                            Toast.makeText(AdminActivity.this, getResources().getString(R.string.email_da_đuoc_doi_vuilong_dangnhap_lai), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(AdminActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(AdminActivity.this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void anhXa() {
        layoutEditEmail_admin = findViewById(R.id.layoutEditEmail_admin);
        layoutEditPassword_admin = findViewById(R.id.layoutEditPassword_admin);
        layoutDelete_admin = findViewById(R.id.layoutDelete_admin);
        cvAddAdmin = findViewById(R.id.cvAddAdmin);
        tvEmailAdmin = findViewById(R.id.tvEmailAdmin);
        tvLogoutAdmin = findViewById(R.id.tvLogoutAdmin);

        tvEmailAdmin.setText(mUser.getEmail());
    }
}