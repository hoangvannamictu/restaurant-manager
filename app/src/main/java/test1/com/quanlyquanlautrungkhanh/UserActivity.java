package test1.com.quanlyquanlautrungkhanh;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import test1.com.quanlyquanlautrungkhanh.Adapter.UserAdapter;
import test1.com.quanlyquanlautrungkhanh.Fragment.BottomSheetDetailUserFragment;
import test1.com.quanlyquanlautrungkhanh.Model.User;

public class UserActivity extends AppCompatActivity {
    private RecyclerView rcv_User;
    private CardView cvAdd_User;
    private ImageView imgAddAvt_user;
    private ImageView  imgEditAvt_user;

    private UserAdapter userAdapter;

    private static final int PERMISSION_RESULT_CODE_ADD_AVT = 101;
    private static final int PERMISSION_RESULT_CODE_EDIT_AVT = 102;
    private static final int KEY_ADD_USER = 11;
    private static final int KEY_EDIT_USER = 12;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    private ActivityResultLauncher<Intent> mActivityResultLauncher_addUser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if(data == null){
                            return;
                        }

                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        imgAddAvt_user.setImageBitmap(bitmap);
                    }
                }
            });

    private ActivityResultLauncher<Intent> mActivityResultLauncher_editUser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if(data == null){
                            return;
                        }

                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        imgEditAvt_user.setImageBitmap(bitmap);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance("gs://quan-ly-quan-lau-trung-khanh.appspot.com");

        anhXa();
        readListUserFromFirebase();
        addEvents();
    }

    private void anhXa() {
        rcv_User = findViewById(R.id.rcv_user);
        cvAdd_User = findViewById(R.id.cvAdd_user);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv_User.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    private void readListUserFromFirebase() {
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), User.class)
                .setLifecycleOwner(this)
                .build();

        userAdapter = new UserAdapter(options, new UserAdapter.IClickItemUserAdapter() {
            @Override
            public void clickDeleteAccountUser(User model, int position) {
                deleteImageUserFromStorage(position);
            }

            @Override
            public void clicDetailAccountUser(User model, int position) {
                detailInfoUser(model);
            }

            @Override
            public void clickEditGeneralInfoUser(User model, int position) {
                editGeneralInfoUser(model, position);
            }

            @Override
            public void clickEditAvtUser(User model, int position) {
                editAvtUser(model, position);
            }
        });

        rcv_User.setAdapter(userAdapter);
    }

    private void addEvents() {
        cvAdd_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(UserActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_add_user);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                imgAddAvt_user = dialog.findViewById(R.id.imgAddAvt_user);
                Button btnAdd_user = dialog.findViewById(R.id.btnAdd_user);
                Button btnTakeAPhoto_user = dialog.findViewById(R.id.btnTakeAPhoto_user);
                Button btnQuitDiaLogAdd_user = dialog.findViewById(R.id.btnQuitDiaLogAdd_user);
                EditText edtAddEmail_user = dialog.findViewById(R.id.edtAddEmail_user);
                EditText edtAddPassword_user = dialog.findViewById(R.id.edtAddPassword_user);
                EditText edtConfirmPassword_user = dialog.findViewById(R.id.edtConfirmPassword_user);
                EditText edtAddName_user = dialog.findViewById(R.id.edtAddName_user);
                EditText edtAddAge_user = dialog.findViewById(R.id.edtAddAge_user);
                EditText edtAddPhone_user = dialog.findViewById(R.id.edtAddPhone_user);
                EditText edtAddIdentityCard_user = dialog.findViewById(R.id.edtAddIdentityCard_user);
                EditText edtAddAddress_user = dialog.findViewById(R.id.edtAddAddress_user);

                imgAddAvt_user.setImageBitmap(conVertAvtUserDefault(R.drawable.ic_account_avt_128));

                btnTakeAPhoto_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickCheckPermission(KEY_ADD_USER);
                    }
                });

                btnAdd_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strAddEmail_user = edtAddEmail_user.getText().toString().trim();
                        String strAddPassword_user = edtAddPassword_user.getText().toString().trim();
                        String strConfirmPassword_user = edtConfirmPassword_user.getText().toString().trim();
                        String strAddName_user = edtAddName_user.getText().toString().trim();
                        String strAddAge_user = edtAddAge_user.getText().toString().trim();
                        String strAddPhone_user = edtAddPhone_user.getText().toString().trim();
                        String strAddIdentityCard_user = edtAddIdentityCard_user.getText().toString().trim();
                        String strAddAddress_user = edtAddAddress_user.getText().toString().trim();
                        String strDownloadUri = "";

                        if(strAddEmail_user.isEmpty() || strAddPassword_user.isEmpty() || strAddName_user.isEmpty() || strAddPhone_user.isEmpty() || strAddAge_user.isEmpty() || strAddIdentityCard_user.isEmpty() || strAddAddress_user.isEmpty()){
                            Toast.makeText(UserActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                        }else {
                            if (strAddPassword_user.length() >= 6) {
                                if (strAddPassword_user.equals(strConfirmPassword_user)) {
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                                    String strDateCreatedUser = sdf.format(calendar.getTime());
                                    User user = new User(strAddEmail_user, strAddPassword_user, strDownloadUri, strAddName_user, strDateCreatedUser, strAddAddress_user,strAddPhone_user, strAddAge_user, strAddIdentityCard_user, 1);

                                    if(LoginActivity.isInternetConnection(UserActivity.this)){
                                        uploadImgUserMemory(user, KEY_ADD_USER);
                                    }else {
                                        Toast.makeText(UserActivity.this, getResources().getString(R.string.khong_internet), Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(UserActivity.this, getResources().getString(R.string.xacnhan_matkhau_lai_khongdung), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UserActivity.this, getResources().getString(R.string.dodai_matkhau_lonhon_5), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                btnQuitDiaLogAdd_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    //convert avt default to bitmap and then set avt default
    private Bitmap conVertAvtUserDefault(int ic_account_avt_128) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_account_avt_128);
        Canvas canvas = new Canvas();
        Bitmap mBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(mBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return mBitmap;
    }

    private void editAvtUser(User model, int position) {
        Dialog dialog = new Dialog(UserActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_avt_user);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        imgEditAvt_user = dialog.findViewById(R.id.imgEditAvt_user);
        Button btnEditTakeAPhoto_user = dialog.findViewById(R.id.btnEditTakeAPhoto_user);
        Button btnEditAvt_user = dialog.findViewById(R.id.btnEditAvt_user);
        Button btnQuitDiaLogEditAvt_user = dialog.findViewById(R.id.btnQuitDiaLogEditAvt_user);

        Picasso.get().load(model.getImgAvtUser()).into(imgEditAvt_user);

        if (imgEditAvt_user.getDrawable() == null){
            imgEditAvt_user.setImageBitmap(conVertAvtUserDefault(R.drawable.ic_account_avt_128));
        }


        btnEditTakeAPhoto_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCheckPermission(KEY_EDIT_USER);
            }
        });

        btnEditAvt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImgUserMemory(model, KEY_EDIT_USER);
                dialog.dismiss();
            }
        });

        btnQuitDiaLogEditAvt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void editGeneralInfoUser(User model, int position) {
        Dialog dialog = new Dialog(UserActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_general_info_user);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnEdit_user = dialog.findViewById(R.id.btnEdit_user);
        Button btnEditTakeAPhoto_user = dialog.findViewById(R.id.btnEditTakeAPhoto_user);
        Button btnQuitDiaLogEdit_user = dialog.findViewById(R.id.btnQuitDiaLogEdit_user);
        EditText edtEditName_user = dialog.findViewById(R.id.edtEditName_user);
        EditText edtEditAge_user = dialog.findViewById(R.id.edtEditAge_user);
        EditText edtEditPhone_user = dialog.findViewById(R.id.edtEditPhone_user);
        EditText edtEditIdentityCard_user = dialog.findViewById(R.id.edtEditIdentityCard_user);
        EditText edtEditAddress_user = dialog.findViewById(R.id.edtEditAddress_user);
        RadioButton rbStatusOn_editUser = dialog.findViewById(R.id.rbStatusOn_editUser);
        RadioButton rbStatusPause_editUser = dialog.findViewById(R.id.rbStatusPause_editUser);

        edtEditName_user.setText(model.getNameUser());
        edtEditAge_user.setText(model.getAgeUser());
        edtEditIdentityCard_user.setText(model.getIdentityCardUser());
        edtEditPhone_user.setText(model.getPhoneUser());
        edtEditAddress_user.setText(model.getAddressUser());

        if(model.getStatusUser() != 1){
            rbStatusPause_editUser.setChecked(true);
        }

        btnEdit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEditName_user = edtEditName_user.getText().toString().trim();
                String strEditAge_user = edtEditAge_user.getText().toString().trim();
                String strtEditIdentityCard_user = edtEditIdentityCard_user.getText().toString().trim();
                String strtEditPhone_user = edtEditPhone_user.getText().toString().trim();
                String strEditAddress_user = edtEditAddress_user.getText().toString().trim();
                if (rbStatusOn_editUser.isChecked()){
                    model.setStatusUser(1);
                }else {
                    model.setStatusUser(0);
                }
                model.setNameUser(strEditName_user);
                model.setAgeUser(strEditAge_user);
                model.setIdentityCardUser(strtEditIdentityCard_user);
                model.setPhoneUser(strtEditPhone_user);
                model.setAddressUser(strEditAddress_user);

                if(strEditName_user.isEmpty() || strEditAge_user.isEmpty() || strtEditIdentityCard_user.isEmpty() || strtEditPhone_user.isEmpty() || strEditAddress_user.isEmpty()){
                    Toast.makeText(UserActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                }else {
                    ref.child("users").child(model.getKeyUser()).setValue(model, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null){
                                Toast.makeText(UserActivity.this, getResources().getString(R.string.sua_thanhcong), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        btnQuitDiaLogEdit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void detailInfoUser(User model) {
        BottomSheetDetailUserFragment mBottomSheetDetailUserFragment = BottomSheetDetailUserFragment.myInstance(model);
        mBottomSheetDetailUserFragment.show(getSupportFragmentManager(), mBottomSheetDetailUserFragment.getTag());
    }

    private void deleteImageUserFromStorage(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity.this, R.style.AlertDialog);
        dialog.setTitle(getResources().getString(R.string.xoa_taikhoan));
        dialog.setMessage(getResources().getString(R.string.ban_muon_xoa_taikhoan));

        dialog.setPositiveButton(getResources().getString(R.string.co), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strImgAvtUser = userAdapter.getItem(position).getImgAvtUser();
                StorageReference photoRef = mStorage.getReferenceFromUrl(strImgAvtUser);

                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteInfoUserFromFirebaseDatabase(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+exception, Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void deleteInfoUserFromFirebaseDatabase(int position) {
        String strKeyUser = userAdapter.getItem(position).getKeyUser();
        ref.child("users").child(strKeyUser).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(UserActivity.this, getResources().getString(R.string.xoa_thanhcong) , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addAccountUser(User user) {
        if (user.getImgAvtUser().isEmpty()){
            return;
        }else {
            mAuth.createUserWithEmailAndPassword(user.getEmailUser(), user.getPasswordUser())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String strUID = task.getResult().getUser().getUid();
                                user.setKeyUser(strUID);
                                ref.child("users").child(strUID).setValue(user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null){
                                            Toast.makeText(UserActivity.this, getResources().getString(R.string.tao_taikhoan_thanhcong)+"\n"+ user.getEmailUser(), Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(UserActivity.this, getResources().getString(R.string.tao_taikhoan_khong_thanhcong)+"\n"+ user.getEmailUser(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void uploadImgUserMemory(User user, int KEY) {
        String oldUrl = user.getImgAvtUser();
        Calendar calendar = Calendar.getInstance();
        String nameImgAvtUser = "avatar_user"+calendar.getTimeInMillis()+".png";
        StorageReference storageRef = mStorage.getReference();
        StorageReference avtUserRef = storageRef.child("user").child(nameImgAvtUser);
        // Get the data from an ImageView as bytes
        Bitmap bitmap = null;
        if (KEY == KEY_ADD_USER){
            imgAddAvt_user.setDrawingCacheEnabled(true);
            imgAddAvt_user.buildDrawingCache();
            bitmap = ((BitmapDrawable) imgAddAvt_user.getDrawable()).getBitmap();
        }else if(KEY == KEY_EDIT_USER){
            imgEditAvt_user.setDrawingCacheEnabled(true);
            imgEditAvt_user.buildDrawingCache();
            bitmap = ((BitmapDrawable) imgEditAvt_user.getDrawable()).getBitmap();
        }
        
        if (bitmap == null){
            Toast.makeText(this, getResources().getString(R.string.loi_anh), Toast.LENGTH_SHORT).show();
        }else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = avtUserRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+exception, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    avtUserRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String strDownloadUri = uri.toString();
                            user.setImgAvtUser(strDownloadUri);
                            user.setNameImgAvtUser(nameImgAvtUser);

                            if (KEY == KEY_ADD_USER){
                                addAccountUser(user);
                            }else if (KEY == KEY_EDIT_USER){
                                ref.child("users").child(user.getKeyUser()).setValue(user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null){

                                            StorageReference photoRef = mStorage.getReferenceFromUrl(oldUrl);
                                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(UserActivity.this, getResources().getString(R.string.sua_thanhcong), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+exception, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else {
                                            Toast.makeText(UserActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    private void onClickCheckPermission(int KEY_CAMERA){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (KEY_CAMERA == KEY_ADD_USER){
                openCamera(PERMISSION_RESULT_CODE_ADD_AVT);
            }else if (KEY_CAMERA == KEY_EDIT_USER){
                openCamera(PERMISSION_RESULT_CODE_EDIT_AVT);
            }
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            if (KEY_CAMERA == KEY_ADD_USER){
                openCamera(PERMISSION_RESULT_CODE_ADD_AVT);
            }else if (KEY_CAMERA == KEY_EDIT_USER){
                openCamera(PERMISSION_RESULT_CODE_EDIT_AVT);
            }
        }else {
            if (KEY_CAMERA == KEY_ADD_USER){
                String [] permission = {Manifest.permission.CAMERA};
                requestPermissions(permission, PERMISSION_RESULT_CODE_ADD_AVT);
            }else if (KEY_CAMERA == KEY_EDIT_USER){
                String [] permission = {Manifest.permission.CAMERA};
                requestPermissions(permission, PERMISSION_RESULT_CODE_EDIT_AVT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_RESULT_CODE_ADD_AVT){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera(PERMISSION_RESULT_CODE_ADD_AVT);
            }else {
                Toast.makeText(this, getResources().getString(R.string.ban_chua_cap_quyen_camera), Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == PERMISSION_RESULT_CODE_EDIT_AVT){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera(PERMISSION_RESULT_CODE_EDIT_AVT);
            }else {
                Toast.makeText(this, getResources().getString(R.string.ban_chua_cap_quyen_camera), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera(int PERMISSION_RESULT_CODE) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (PERMISSION_RESULT_CODE == PERMISSION_RESULT_CODE_ADD_AVT){
            mActivityResultLauncher_addUser.launch(Intent.createChooser(intent, "take a photo"));
        }else if(PERMISSION_RESULT_CODE == PERMISSION_RESULT_CODE_EDIT_AVT){
            mActivityResultLauncher_editUser.launch(Intent.createChooser(intent, "take a photo"));
        }
    }
}